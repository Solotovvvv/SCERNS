<?php
session_start();
include '../../includes/config.php';

$pdo = Database::connection();

if (isset($_POST['id_c'])) {
    $id = $_POST['id_c'];

    $name = $_POST['name'];
    $hotline = $_POST['hotline'];
    $dpcode = $_POST['dpcode'];
    $department = $_POST['department'];

    $update_login_query = "UPDATE `scerns_respondents` SET `Name` = :name, `Dispatcher_Code` = :dpcode, `HotlineNumber` = :hotline, `Category` =:department  WHERE `Id` = :id";
    $stmt = $pdo->prepare($update_login_query);
    $stmt->bindParam(':name', $name, PDO::PARAM_STR);
    $stmt->bindParam(':dpcode', $dpcode, PDO::PARAM_STR);
    $stmt->bindParam(':hotline', $hotline, PDO::PARAM_STR);
    $stmt->bindParam(':department', $department, PDO::PARAM_STR);
    $stmt->bindParam(':id', $id, PDO::PARAM_INT);
    $stmt->execute();



    echo json_encode(['status' => 'success']);
}
