<?php
session_start();
include '../../includes/config.php';
// Pusher configuration
require_once '../../vendor/autoload.php';
include '../../Controller/pusher.php';


$pdo = Database::connection();

if (isset($_POST['id'])) {
    $id = $_POST['id'];
    $status = $_POST['status'];
    $dispatcher= $_POST['dispatcher']; // Assuming you're getting dispatcher name from the frontend.

    // First, let's retrieve the dispatcher's ID from the `respondent` table.
    $select_dispatcher_query = "SELECT `Id` FROM `respondent` WHERE `Dispatcher_Code` = :dispatcher_name";
    $stmt = $pdo->prepare($select_dispatcher_query);
    $stmt->bindParam(':dispatcher_name', $dispatcher, PDO::PARAM_STR);
    $stmt->execute();
    $dispatcher_row = $stmt->fetch(PDO::FETCH_ASSOC);

    if (!$dispatcher_row) {
        // Handle case where dispatcher with given name does not exist.
        echo json_encode(['status' => 'error', 'message' => 'Dispatcher not found']);
        exit;
    }

    $dispatcher_id = $dispatcher_row['Id'];

    // Now, update the report with the retrieved dispatcher ID.
    $update_report_query = "UPDATE `reports` SET `Status` = :status, `Dispatcher_Id` = :dispatcher_id WHERE `Id` = :id";
    $stmt = $pdo->prepare($update_report_query);
    $stmt->bindParam(':status', $status, PDO::PARAM_STR);
    $stmt->bindParam(':dispatcher_id', $dispatcher_id, PDO::PARAM_INT);
    $stmt->bindParam(':id', $id, PDO::PARAM_INT);
    $stmt->execute();

    $pusher->trigger('Scerns', 'new-report-table', null);
    echo json_encode(['status' => 'success']);
}
?>
