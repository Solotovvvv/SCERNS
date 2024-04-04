<?php
session_start();
include '../../includes/config.php';
// Get the submitted OTP value
$password = $_POST['password'];
$email = $_POST['email'];
$id = $_SESSION['user_id'];

// OTPs match, proceed to newpass.php
$hash_password = md5($password);

$sql = "UPDATE scerns_v2.login SET password = '$hash_password' WHERE Id = '$id'";
$result = mysqli_query($conn, $sql);
header('Location: ../../index.php');



// if (!empty($errors)) {
//     // Redirect back to the login page with the error messages
//     $errorString = implode(',', $errors);
//     header('Location: ../newpass.php?errors=' . urlencode($errorString));
//     exit();
// }
