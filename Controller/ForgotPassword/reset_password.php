<?php
session_start();
include '../../includes/config.php';

if ($_POST['confirmpass'] !== $_POST['password']) {
    echo json_encode(['message' => "Invalid request or passwords do not match"], 400);
    exit();
}

$id = isset($_POST['user_id']) ? $_POST['user_id'] : $_SESSION['user_id'];
$password = $_POST['password'];
$hash_password = sha1($password);

$sql = "UPDATE login SET Password = ? WHERE Id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("si", $hash_password, $id);
$stmt->execute();

if ($stmt->affected_rows > 0) {
    session_destroy();
    echo json_encode(['success' => true], 200);
} else {
    echo json_encode(['message' => "Failed to update password"], 500);
}

$stmt->close();
$conn->close();