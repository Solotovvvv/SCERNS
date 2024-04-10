<?php
session_start();
include '../includes/config.php';

if (isset($_POST['username'], $_POST['password'])) {
    $username = $_POST['username'];
    $password = sha1($_POST['password']);
    $cookiepass = $_POST['password'];

    try {
        // Establish PDO connection
        $pdo = Database::connection();

        // Prepare and execute the query to fetch user from login table
        $stmt = $pdo->prepare("SELECT * FROM login WHERE Username = :username AND Password = :password");
        $stmt->execute(array(':username' => $username, ':password' => $password));
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($user) {
            $_SESSION['username'] = $user['Username']; // Set the username in session

            if ($user['UserRole'] == 'pending') {
                // Display SweetAlert popup for pending account
                echo "<script>Swal.fire({
                    icon: 'warning',
                    title: 'Account Pending',
                    text: 'Your account is pending approval. Please wait for admin approval.',
                    showConfirmButton: true
                }).then(function() {
                    window.location = 'index.php'; // Redirect to login page
                });</script>";
                exit();
            }

            // Fetch user details from user_details table using Id from login table
            $user_details_stmt = $pdo->prepare("SELECT * FROM user_details WHERE User_id = :user_id");
            $user_details_stmt->execute(array(':user_id' => $user['Id']));
            $user_details = $user_details_stmt->fetch(PDO::FETCH_ASSOC);

            $cookie_time = 60 * 60 * 24 * 30;

            if (isset($_REQUEST['remember'])) {
                setcookie('fQnaIJDS', $username, time() + $cookie_time, "/");
                // setcookie('KnbxyTHW', $cookiepass, time() + $cookie_time, "/");
                setcookie('BJausdnK', 'checked', time() + $cookie_time, "/");
            } else {
                setcookie('fQnaIJDS', '', time() - 3600, "/");
                setcookie('KnbxyTHW', '', time() - 3600, "/");
                setcookie('BJausdnK', '', time() - 3600, "/");
            }

            // Store user details in session
            $_SESSION['user_details'] = $user_details;

            // Set session variables based on user's role
            $_SESSION['role'] = $user['UserRole'];
            $_SESSION['image'] = $user_details['Images'];
            $_SESSION['user_id'] = $user_details['User_id'];
            $_SESSION['type'] = $user_details['Type'];
            $_SESSION['fullname'] = $user_details['Fullname'];
            $_SESSION['images'] = $user_details['Images'];
            $_SESSION['phone'] = $user_details['Phone'];
            $_SESSION['address'] = $user_details['Address'];
            $_SESSION['email'] = $user_details['Email'];
            $_SESSION['username'] = $user['Username'];

            // Redirect based on user's role
            switch ($user['UserRole']) {
                case 1:
                    header("Location: ../../../Client3/Views/Admin/history.php");
                    exit();
                case 2:
                    header("Location: ../../../Client3/Views/Respondent_Admin/IncidentReport.php");
                    exit();
            }
        } else {
            // Handle invalid credentials
            header("Location: ../../../Client3/index.php?errors=Invalid+Username+or+Password");
       
            exit();
        }
    } catch (PDOException $e) {
        echo "Error: " . $e->getMessage(); // Output any potential errors
        die(); // Terminate script execution
    }
}
?>
