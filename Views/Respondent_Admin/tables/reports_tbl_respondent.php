<?php
session_start();
include '../../../includes/config.php';

$pdo = Database::connection();

// Initialize DataTables request parameters
$start = $_POST['start'] ?? 0; // Start index of records to fetch
$length = $_POST['length'] ?? 10; // Number of records per page
$searchValue = $_POST['search']['value'] ?? ''; // Search keyword
$orderColumn = $_POST['order'][0]['column'] ?? 0; // Index of the column to order by
$orderDirection = $_POST['order'][0]['dir'] ?? 'asc'; // Order direction

// Define the base SQL query
$sql = "SELECT 
            r.*, 
            rd.Dispatcher_Code,
            rd.Name,
            rd.Category,
            ud.Fullname,
            ud.Phone,
            ud.Email
        FROM 
            reports AS r
        LEFT JOIN 
            respondent AS rd ON r.Dispatcher_Id = rd.Id
        INNER JOIN 
            user_details AS ud ON r.User_id = ud.User_id
        WHERE 
            r.Status != 'Arrived' AND r.TypeOfEmergency = :userType";

// Add search condition if search keyword is provided
if (!empty($searchValue)) {
    $sql .= " AND (r.TypeOfEmergency LIKE '%$searchValue%' OR r.Date LIKE '%$searchValue%' OR r.Address LIKE '%$searchValue%' OR r.Level LIKE '%$searchValue%' OR rd.Dispatcher_Code LIKE '%$searchValue%' OR r.Status LIKE '%$searchValue%')";
}

// Add order by clause
$orderColumnNames = ['r.TypeOfEmergency', 'r.Date', 'r.Address', 'r.Level', 'rd.Dispatcher_Code', 'r.Status'];
$orderColumnIndex = isset($orderColumnNames[$orderColumn]) ? $orderColumnNames[$orderColumn] : $orderColumnNames[0];
$sql .= " ORDER BY $orderColumnIndex $orderDirection";

// Add limit and offset for pagination
$sql .= " LIMIT $start, $length";

$stmt = $pdo->prepare($sql);
$stmt->bindParam(':userType', $_SESSION['type'], PDO::PARAM_STR);

$data = [];

if ($stmt->execute()) {
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        // Format date
        $formattedDateTime = date('F j, Y H:i:s', strtotime($row['Date']));

        $subarray = [
            $row['TypeOfEmergency'],
            $formattedDateTime,
            $row['Address'],
            $row['Level'],
            $row['Dispatcher_Code'],
            $row['Status'],
            '<button class="btn btn-primary" onclick="view_reports_respondent(' . $row['Id'] . ')"><i class="nav-icon fas fa-edit"></i></button>',
        ];
        $data[] = $subarray;
    }
}

// Get total records count for pagination
$totalRecordsCount = $pdo->query("SELECT COUNT(*) FROM reports WHERE Status != 'Arrived'")->fetchColumn();

// Construct output in DataTables format
$output = [
    'draw' => $_POST['draw'] ?? 1, // Add draw parameter
    'recordsTotal' => $totalRecordsCount, // Total records count
    'recordsFiltered' => $totalRecordsCount, // Total records count after filtering (in this case, same as total records count because we're not applying filtering in PHP)
    'data' => $data,
];

echo json_encode($output);
?>
