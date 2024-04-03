<?php
session_start();
include '../../../includes/config.php';

$pdo = Database::connection();

// Set default values for start and end dates
$startDate = isset($_GET['startDate']) ? $_GET['startDate'] : null;
$endDate = isset($_GET['endDate']) ? $_GET['endDate'] : null;

// Define the base SQL query
$sql = "SELECT 
            r.*, 
            rd.Dispatcher_Code,
            ud.Fullname
        FROM 
            reports AS r
        INNER JOIN 
            respondent AS rd ON r.Dispatcher_Id = rd.Id
        INNER JOIN 
            user_details AS ud ON r.User_id = ud.User_id
            WHERE r.Status = 'Arrived' ";


// Add WHERE clause for date filtering if start and end dates are provided
if ($startDate && $endDate) {
    $sql .= " AND  r.Date BETWEEN :startDate AND :endDate";
} elseif ($startDate) {
    $sql .= " AND r.Date = :startDate";
} elseif ($endDate) {
    $sql .= " AND r.Date <= :endDate";
}



$stmt = $pdo->prepare($sql);

$data = [];

if ($startDate) {
    $stmt->bindParam(':startDate', $startDate, PDO::PARAM_STR);
}
if ($endDate) {
    $stmt->bindParam(':endDate', $endDate, PDO::PARAM_STR);
}

if ($stmt->execute()) {
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        // Format date
        $formattedDate = date('F j, Y', strtotime($row['Date']));
      
        $subarray = [
            '<td>' . $row['Id'] . '</td>',
            '<td>' . $row['Fullname'] . '</td>',
            '<td>' . $row['Dispatcher_Code'] .'</td>',
            '<td>' . $row['TypeOfEmergency'] .'</td>',
            '<td>' . $row['Level'] .'</td>',
            '<td>' . $formattedDate . '</td>',
            '<td>' . $row['Status'] .'</td>',
        ];
        $data[] = $subarray;
    }
}

$output = [
    'data' => $data,
];

echo json_encode($output);
?>
