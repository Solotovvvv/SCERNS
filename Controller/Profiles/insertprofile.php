<?php
session_start();
include_once '../../includes/config.php'; // Using include_once to prevent multiple inclusions

$pdo = Database::connection();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Get the uploaded file
    $file = $_FILES['file'];

    // Set the upload directory
    $upload_dir = '../../Views/Respondent_Admin/storage/images/profiles'; // Adjusted the upload directory path
    $upload_file = $upload_dir . basename($file['name']);

    // Move the uploaded file to the upload directory
    if (move_uploaded_file($file['tmp_name'], $upload_file)) {
        // Update the user's image in the database
        $user_id = $_SESSION['user_id'];
        $image_path = $upload_file;

        $sql = "UPDATE user_details SET Images = :image_path WHERE User_id = :user_id";
        $stmt = $pdo->prepare($sql); // Changed $conn to $pdo
        $stmt->bindParam(':image_path', $image_path);
        $stmt->bindParam(':user_id', $user_id);

        if ($stmt->execute()) {
            echo $image_path;
        } else {
            echo "Error: " . $stmt->errorInfo()[2];
        }
    } else {
        echo "Error uploading file.";
    }
}
?>
