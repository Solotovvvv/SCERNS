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

$sql = "UPDATE login SET `Password` = ? WHERE Id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("si", $hash_password, $id);

// Execute the statement
if ($stmt->execute()) {
    // Check if any rows were affected
    if ($stmt->affected_rows > 0) {
        echo json_encode(['success' => true], 200);
        session_destroy();
        $stmt->close();
        $conn->close();
    } else {
        echo json_encode(['message' => "Failed to update password"], 500);
        exit();
    }
} else {
    // Error occurred during execution
    echo json_encode(['message' => "Failed to execute the statement"], 500);
    exit();
}
?>
