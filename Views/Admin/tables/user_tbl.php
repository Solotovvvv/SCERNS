<?php
session_start();
// if (!isset($_SESSION['fullname'])) {
//     header('Location:login.php');
//     exit;
// }
include '../../../includes/config.php';

$pdo = Database::connection();


$sql = "SELECT ud.*, lg.* 
FROM scerns_user_details ud
INNER JOIN scerns_login lg ON ud.User_id = lg.Id
WHERE lg.UserRole = 'Pending'";
$stmt = $pdo->prepare($sql);

$data = [];

if ($stmt->execute()) {
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {

        $subarray = [
            '<td>' . $row['Username'] . '</td>',
            '<td>
            <button class="btn btn-primary" onclick="view_user(' . $row['Id'] . ')"><i class="nav-icon fas fa-edit"></i></button>
            <button class="btn btn-danger" onclick="delete_user(' . $row['Id'] . ')"><i class="nav-icon fas fa-trash"></i></button>
            </td>',
        ];
        $data[] = $subarray;
    }
}

$output = [
    'data' => $data,
];

echo json_encode($output);
