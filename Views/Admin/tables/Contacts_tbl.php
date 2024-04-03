<?php
session_start();
include '../../../includes/config.php';

$pdo = Database::connection();


$stmt = $pdo->prepare("SELECT * FROM respondent");

$data = [];

if ($stmt->execute()) {
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $subarray = [
            '<td>' . $row['HotlineNumber'] . '</td>',
            '<td>' . $row['Dispatcher_Code'] . '</td>',
            '<td>' . $row['Name'] . '</td>',
            '<td>' . $row['Category'] . '</td>',
            '<td>
            <button class="btn btn-primary" onclick="view_contacts(' . $row['Id'] . ')"><i class="nav-icon fas fa-edit"></i></button>
            <button class="btn btn-danger" onclick="delete_admin(' . $row['Id'] . ')"><i class="nav-icon fas fa-trash"></i></button>
            </td>',
        ];
        $data[] = $subarray;
    }
}

$output = [
    'data' => $data,
];

echo json_encode($output);
?>
