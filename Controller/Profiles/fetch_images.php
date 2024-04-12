<?php
session_start();
include_once '../../includes/config.php';

$pdo = Database::connection();

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $user_id = $_SESSION['user_id'];

    $sql = "SELECT Images FROM user_details WHERE User_id = :user_id";
    $stmt = $pdo->prepare($sql);
    $stmt->bindParam(':user_id', $user_id);
    $stmt->execute();

    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    if ($result) {
        echo $result['Images'];
    } else {
        echo "No image found";
    }
}
?>
