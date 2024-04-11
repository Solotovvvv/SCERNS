<?php
include '../../includes/config.php';
// Pusher configuration
require_once '../../vendor/autoload.php';
include '../../Controller/pusher.php';


$pdo = Database::connection();

if (isset($_POST['id_s'])) {
    $id = $_POST['id_s'];
    $status = $_POST['status'];
   

    // Now, update the report with the retrieved dispatcher ID.
    $update_report_query = "UPDATE `login` SET `UserRole` = :status WHERE `Id` = :id";
    $stmt = $pdo->prepare($update_report_query);
    $stmt->bindParam(':status', $status, PDO::PARAM_STR);

    $stmt->bindParam(':id', $id, PDO::PARAM_INT);
    $stmt->execute();

    $pusher->trigger('Scerns', 'user-tbl', null);
    $pusher->trigger('Scerns', 'new-user', null);
    echo json_encode(['status' => 'success']);
}
?>
