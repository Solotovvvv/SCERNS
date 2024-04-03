<?php
session_start();
include '../../includes/config.php';

$pdo = Database::connection();

if (isset($_POST['id_r'])) {
    $id = $_POST['id_r'];
    $password = sha1($_POST['password']);
    $username = $_POST['username'];
    $fullname = $_POST['fullname'];
    $email = $_POST['email'];

    $update_login_query = "UPDATE `login` SET `Username` = :username, `Password` = :password WHERE `Id` = :id";
    $stmt = $pdo->prepare($update_login_query);
    $stmt->bindParam(':username', $username, PDO::PARAM_STR);
    $stmt->bindParam(':password', $password, PDO::PARAM_STR);
    $stmt->bindParam(':id', $id, PDO::PARAM_INT);
    $stmt->execute();

    $update_user_query = "UPDATE `user_details` SET `Fullname` = :fullname, `Email` = :email WHERE `User_id` = :id";
    $stmt = $pdo->prepare($update_user_query);
    $stmt->bindParam(':fullname', $fullname, PDO::PARAM_STR);
    $stmt->bindParam(':email', $email, PDO::PARAM_STR);
    $stmt->bindParam(':id', $id, PDO::PARAM_INT);
    $stmt->execute();

    echo json_encode(['status' => 'success']);
}
?>
