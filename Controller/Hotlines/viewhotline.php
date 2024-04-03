<?php
include '../../includes/config.php';

$pdo = Database::connection();

if (isset($_POST['id'])) {
    $id = $_POST['id'];

    $stmt = $pdo->prepare('SELECT * FROM respondent WHERE id = :id');
    $stmt->bindParam(':id', $id); // corrected parameter name
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    echo json_encode($result); // corrected echo statement
} else {
    echo json_encode(['message' => 'Hotline Not Found'], 404);
}
