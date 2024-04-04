<?php
include '../../includes/config.php';
$pdo = Database::connection();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $response = array();

    if (isset($_POST['category'])) {
        $category = $_POST['category'];
        $stmt = $pdo->prepare("SELECT `Id`, `Dispatcher_Code`, `Name` FROM `respondent` WHERE `Category` = :category");
        $stmt->bindParam(':category', $category, PDO::PARAM_STR);
        $stmt->execute();
        $respondents = $stmt->fetchAll(PDO::FETCH_ASSOC);
        $response = $respondents;
    } elseif (isset($_POST['dispatcherCodeId'])) {
        $dispatcherCodeId = $_POST['dispatcherCodeId'];
        $stmt = $pdo->prepare("SELECT `Name` FROM `respondent` WHERE `Id` = :dispatcherCodeId");
        $stmt->bindParam(':dispatcherCodeId', $dispatcherCodeId, PDO::PARAM_INT);
        $stmt->execute();
        $name = $stmt->fetchColumn();
        $response = $name;
    }

    echo json_encode($response);
}
?>