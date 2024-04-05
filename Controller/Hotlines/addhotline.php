<?php
session_start();
include '../../includes/config.php';

$pdo = Database::connection();

if (isset($_POST['code'], $_POST['number'], $_POST['emergency'], $_POST['name'])) {
    $code = $_POST['code'];
    $name = $_POST['name'];
    $emergency = $_POST['emergency'];
    $number = $_POST['number'];

    $addHotline = "INSERT INTO `respondent`(`Dispatcher_Code`, `Name`, `HotlineNumber`, `Category`) VALUES (:code, :name, :number, :emergency)";
    $stmt = $pdo->prepare($addHotline);
    $stmt->bindParam(':code', $code, PDO::PARAM_STR);
    $stmt->bindParam(':name', $name, PDO::PARAM_STR);
    $stmt->bindParam(':emergency', $emergency, PDO::PARAM_STR);
    $stmt->bindParam(':number', $number, PDO::PARAM_STR);
    $addHotlineResult = $stmt->execute();

    if ($addHotlineResult) {
        echo json_encode(['message' => 'Hotline Added Successfully'], 200);
    } else {
        echo json_encode(['message' => 'Adding of Hotline Failed'], 404);
    }
}
