<?php
session_start();
include '../../includes/config.php';

$pdo = Database::connection();

if (isset($_POST['hotline'], $_POST['name'], $_POST['DispatcherCode'], $_POST['department'])) {
    $hotline = $_POST['hotline'];
    $name = $_POST['name'];
    $dispatcherCode = $_POST['DispatcherCode']; // Assuming this is the correct variable name
    $department = $_POST['department'];

    // Check if the name and dispatcher code already exist
    $check_query = "SELECT * FROM respondent WHERE Name = :name AND Dispatcher_Code = :dispatcherCode";
    $stmt = $pdo->prepare($check_query);
    $stmt->bindParam(':name', $name, PDO::PARAM_STR);
    $stmt->bindParam(':dispatcherCode', $dispatcherCode, PDO::PARAM_STR);
    $stmt->execute();

    if ($stmt->rowCount() > 0) {
        // Data already exists
        echo json_encode(['status' => 'data_exist']);
    } else {
        // Insert new data
        $insert_query = "INSERT INTO respondent (HotlineNumber, Name, Dispatcher_Code, Category)
                         VALUES (:hotline, :name, :dispatcherCode, :department)";
        $stmt = $pdo->prepare($insert_query);
        $stmt->bindParam(':hotline', $hotline, PDO::PARAM_STR);
        $stmt->bindParam(':name', $name, PDO::PARAM_STR);
        $stmt->bindParam(':dispatcherCode', $dispatcherCode, PDO::PARAM_STR);
        $stmt->bindParam(':department', $department, PDO::PARAM_STR);
        
        if ($stmt->execute()) {
            // Success
            echo json_encode(['status' => 'success']);
        } else {
            // Failed to insert
            echo json_encode(['status' => 'insert_failed']);
        }
    }
}
else if(isset($_POST['id'])) {
    $id = $_POST['id'];

    
    $select_query = "SELECT * FROM respondent WHERE Id = :id";
    $stmt = $pdo->prepare($select_query);
    $stmt->bindParam(':id', $id, PDO::PARAM_INT);
    $stmt->execute();
    $data = $stmt->fetch(PDO::FETCH_ASSOC);

    echo json_encode($data);
}

else if(isset($_POST['removes'])) {
    $id = $_POST['removes'];

    try {
      
      
        $stmt = $pdo->prepare("DELETE FROM `respondent` WHERE Id = :id");
        $stmt->bindParam(':id', $id, PDO::PARAM_INT);
        $stmt->execute();

        $data = array(
            'status' => 'success',
        );
        echo json_encode($data);
    } catch (PDOException $e) {
        $data = array(
            'status' => 'failed',
            'error' => $e->getMessage(),
        );
        echo json_encode($data);
    }
}
?>
