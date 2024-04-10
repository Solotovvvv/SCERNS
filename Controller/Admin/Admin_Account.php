<?php
session_start();
include '../../includes/config.php';

$pdo = Database::connection();
if (isset($_POST['email'], $_POST['username'], $_POST['password'])) {
    $email = $_POST['email'];
    $username = $_POST['username'];
    $password = sha1($_POST['password']); // Hash the password



    $check_username_query = "SELECT * FROM login WHERE Username = :username";
    $stmt = $pdo->prepare($check_username_query);
    $stmt->bindParam(':username', $username, PDO::PARAM_STR);
    $stmt->execute();

    if ($stmt->rowCount() > 0) {

        echo json_encode(['status' => 'data_exist']);
    } else {

        $insert_login_query = "INSERT INTO `login` (`Username`, `Password`, `UserRole`)
                               VALUES (:username, :password, :role)";
        $stmt = $pdo->prepare($insert_login_query);
        $role = 1; // Assuming this is the default role for new admins
        $stmt->bindParam(':username', $username, PDO::PARAM_STR);
        $stmt->bindParam(':password', $password, PDO::PARAM_STR);
        $stmt->bindParam(':role', $role, PDO::PARAM_INT);
        $insert_login_result = $stmt->execute();

        if ($insert_login_result) {

            $user_id = $pdo->lastInsertId(); // Get the last inserted user_id
            $insert_user_query = "INSERT INTO `user_details` (`Fullname`, `Email`, `User_id`)
                                  VALUES (:fullname, :email, :user_id)";
            $stmt = $pdo->prepare($insert_user_query);
            $stmt->bindParam(':fullname', $username, PDO::PARAM_STR);
            $stmt->bindParam(':email', $email, PDO::PARAM_STR);
            $stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
            $insert_user_result = $stmt->execute();

            if ($insert_user_result) {

                echo json_encode(['status' => 'success']);
            } else {

                echo json_encode(['status' => 'user_details_insert_failed']);
            }
        } else {

            echo json_encode(['status' => 'login_insert_failed']);
        }
    }
} else if (isset($_POST['id'])) {
    $id = $_POST['id'];

    $select_query = "SELECT login.*, user_details.* FROM login 
                     JOIN user_details ON login.Id = user_details.User_id
                     WHERE login.Id = :id";
    $stmt = $pdo->prepare($select_query);
    $stmt->bindParam(':id', $id, PDO::PARAM_INT);
    $stmt->execute();
    $data = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($data && isset($data['Requirement']) && !empty($data['Requirement'])) {
        // Convert the BLOB data to Base64
        $base64Requirement = base64_encode($data['Requirement']);
        // Replace the 'Requirement' field with the Base64 encoded value
        $data['RequirementBase64'] = $base64Requirement;
        // Remove the original 'Requirement' field
        unset($data['Requirement']);
    }

    // Return JSON response
    echo json_encode($data);
} else if (isset($_POST['remove'])) {
    $id = $_POST['remove'];

    try {
        $stmt = $pdo->prepare("DELETE FROM `login` WHERE id = :id");
        $stmt->bindParam(':id', $id, PDO::PARAM_INT);
        $stmt->execute();

        $stmt = $pdo->prepare("DELETE FROM `user_details` WHERE User_id = :id");
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
