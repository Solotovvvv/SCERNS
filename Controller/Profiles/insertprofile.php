<?php
session_start();
include '../../includes/config.php';

$pdo = Database::connection();
// $uploadDirectory = __DIR__ . '/../../storage/images/profiles/';
$uploadDirectory = dirname(dirname(__DIR__)) . '/Views/Respondent_Admin/storage/images/profiles/';

if (!is_dir($uploadDirectory)) {
    mkdir($uploadDirectory, 0755, true);
}

if (isset($_FILES['file']) && $_FILES["file"]["error"] == 0) {
    $pfp = $_FILES['file'];
    $fileName = basename($_FILES["file"]["name"]);
    $newFileName = uniqid() . '-' . $fileName;

    $userId = $_SESSION['user_id'];

    if (move_uploaded_file($_FILES["file"]["tmp_name"], $uploadDirectory . $newFileName)) {
        $stmt = $pdo->prepare("UPDATE `scerns_user_details` SET `Images` = :Images WHERE `Id` = :Id");

        $imagePath = 'storage/images/profiles/' . $newFileName;

        $stmt->bindParam(':Images', $imagePath, PDO::PARAM_STR);
        $stmt->bindParam(':Id', $userId, PDO::PARAM_INT);

        if ($stmt->execute()) {
            echo json_encode(['message' => 'File uploaded and record updated successfully.'], 200);
        } else {
            echo json_encode(['message' => 'File uploaded but error updating record.'], 400);
        }
    } else {
        echo json_encode(['message' => 'Error uploading file.'], 400);
    }
} else {
    $error = isset($_FILES["file"]["error"]) ? $_FILES["file"]["error"] : "Unknown error";
    echo json_encode(['message' => 'Error: ' . $error], 404);
}
