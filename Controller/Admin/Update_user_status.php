<?php
include '../../includes/config.php';

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
    echo json_encode(['status' => 'success']);
}
?>
