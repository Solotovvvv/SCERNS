<?php
include '../../includes/config.php';

$pdo = Database::connection();

if (isset($_POST['id'])) {
    $id = $_POST['id'];
    $code = $_POST['code'];
    $name = $_POST['name'];
    $number = $_POST['number'];

    $update = 'UPDATE `scerns_respondents` SET `Dispatcher_Code`=:code, `Name`=:name, `HotlineNumber`=:number WHERE `Id` = :id';

    $stmt = $pdo->prepare($update);
    $stmt->bindParam(':id', $id);
    $stmt->bindParam(':code', $code);
    $stmt->bindParam(':name', $name);
    $stmt->bindParam(':number', $number);
    $stmt->execute();

    $rowCount = $stmt->rowCount();
    if ($rowCount > 0) {
        echo json_encode(['message' => 'Hotline updated successfully']);
    } else {
        echo json_encode(['message' => 'Hotline not found or no changes made'], 404);
    }
} else {
    echo json_encode(['message' => 'Hotline Not Found'], 404);
}
