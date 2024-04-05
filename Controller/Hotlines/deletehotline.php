<?php
include '../../includes/config.php';

$pdo = Database::connection();

if (isset($_POST['id'])) {
    $id = $_POST['id'];

    // Prepare the SQL statement
    $delete = 'DELETE FROM `respondent` WHERE `Id` = :id';

    // Prepare and execute the statement
    $stmt = $pdo->prepare($delete);
    $stmt->bindParam(':id', $id);
    $stmt->execute();

    // Check if any rows were affected
    $rowCount = $stmt->rowCount();
    if ($rowCount > 0) {
        echo json_encode(['message' => 'Hotline deleted successfully']);
    } else {
        echo json_encode(['message' => 'Hotline not found'], 404);
    }
} else {
    echo json_encode(['message' => 'Hotline ID not provided'], 400);
}
