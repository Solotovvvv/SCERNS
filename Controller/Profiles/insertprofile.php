<?php
session_start();
include '../../includes/config.php';

$pdo = Database::connection();
$uploadDirectory = __DIR__ . '/../../storage/images/profiles/';

if (!is_dir($uploadDirectory)) {
    mkdir($uploadDirectory, 0755, true);
}

if (isset($_FILES['file']) && $_FILES["file"]["error"] == 0) {
    $pfp = $_FILES['file'];
    $fileName = basename($_FILES["file"]["name"]);
    $newFileName = uniqid() . '-' . $fileName;

    if (move_uploaded_file($_FILES["file"]["tmp_name"], $uploadDirectory . $newFileName)) {
        return json_encode(['message' => 'File uploaded successfully.'], 200);
    } else {
        return json_encode(['message' => 'Error uploading file.'], 400);
    }
} else {
    $error = isset($_FILES["file"]["error"]) ? $_FILES["file"]["error"] : "Unknown error";
    return json_encode(['message' => 'Error: ' . $error], 404);
}
