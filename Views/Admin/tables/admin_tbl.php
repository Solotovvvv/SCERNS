<?php
session_start();
// if (!isset($_SESSION['fullname'])) {
//     header('Location:login.php');
//     exit;
// }
include '../../../includes/config.php';

$pdo = Database::connection();

$role= 1;

$sql = "SELECT ud.*, lg.* 
FROM user_details ud
INNER JOIN login lg ON ud.User_id = lg.Id
WHERE lg.UserRole = $role";
$stmt = $pdo->prepare($sql);

$data = [];

if ($stmt->execute()) {
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {

        $subarray = [
            '<td>' . $row['Username'] . '</td>',
            '<td>' . $row['Fullname'] . '</td>',
            '<td>
            <button class="btn btn-primary" onclick="view_admin(' . $row['Id'] . ')"><i class="nav-icon fas fa-edit"></i></button>
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
