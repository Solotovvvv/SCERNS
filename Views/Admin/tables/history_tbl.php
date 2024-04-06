<?php
session_start();
include '../../../includes/config.php';

$pdo = Database::connection();

$startDate = isset($_GET['startDate']) ? $_GET['startDate'] : null;
$endDate = isset($_GET['endDate']) ? $_GET['endDate'] : null;

// Define the base SQL query
$sql = "SELECT 
            r.*, 
            rd.Dispatcher_Code,
            ud.Fullname
        FROM 
        scerns_reports AS r
        INNER JOIN 
        scerns_respondents AS rd ON r.Dispatcher_Id = rd.Id
        INNER JOIN 
        scerns_user_details AS ud ON r.User_id = ud.User_id
            WHERE r.Status = 'Arrived' ";



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
        // $formattedDate = date('F j, Y', strtotime($row['Date']));
        $formattedDateTime = date('F j, Y H:i:s', strtotime($row['Date']));


        $subarray = [
            '<td>' . $row['Id'] . '</td>',
            '<td>' . $row['Fullname'] . '</td>',
            '<td>' . $row['Dispatcher_Code'] . '</td>',
            '<td>' . $row['TypeOfEmergency'] . '</td>',
            '<td>' . $row['Level'] . '</td>',
            '<td>' . $formattedDateTime . '</td>',
            '<td>' . $row['Status'] . '</td>',
        ];
        $data[] = $subarray;
    }
}

$output = [
    'data' => $data,
];

echo json_encode($output);
