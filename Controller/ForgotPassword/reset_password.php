<?php
session_start();
include '../../includes/config.php';
// Get the submitted OTP value

if ($_POST['user_id'] || $_POST['confirmpass'] != $_POST['password']) {
    $cpassword = $_POST['confirmpass'];
    $password = $_POST['password'];
    $id = $_SESSION['user_id'];
} else {
    echo json_encode(['message' => "Something's wrong. Please Try Again Later."], 404);
}
$hash_password = sha1($password);

$sql = "UPDATE scerns_v2.login SET password = '$hash_password' WHERE Id = '$id'";
$result = mysqli_query($conn, $sql);

if ($result) {
    session_destroy();
} else {
    echo json_encode(['message' => "Something's wrong. Please Try Again Later."], 404);
}



// if (!empty($errors)) {
//     // Redirect back to the login page with the error messages
//     $errorString = implode(',', $errors);
//     header('Location: ../newpass.php?errors=' . urlencode($errorString));
//     exit();
// }
