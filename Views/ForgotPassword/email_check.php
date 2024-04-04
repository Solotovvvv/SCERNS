<?php
include '../../includes/config.php';
session_start();

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

require '../../includes/phpmailer/src/PHPMailer.php';
require '../../includes/phpmailer/src/SMTP.php';
require '../../includes/phpmailer/src/Exception.php';

$email = trim($_POST['email']);

// Prepare and bind the query to avoid SQL injection
$query = "SELECT * FROM scerns_v2.user_details WHERE Email = ?";
$stmt = mysqli_prepare($conn, $query);
mysqli_stmt_bind_param($stmt, "s", $email);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);

if (mysqli_num_rows($result) > 0) {
    // Generate a random OTP
    $otp = mt_rand(100000, 999999);

    // The subject
    $sub = "This is your OTP for your password reset";

    // The message
    $msg = $otp;

    // Recipient email
    $rec = $email;

    // Send email
    mail($rec, $sub, $msg);

    // You should store the OTP in the database or session for validation on the OTP page.
    $_SESSION['otp'] = $otp;
    $_SESSION['email'] = $email;
    header('Location: ../ForgotPassword/otp.php');
    exit();
} else {
    // Email does not exist, display SweetAlert or handle errors as needed
    $errors[] = "No email found in our database!";
}

// Check for errors
if (!empty($errors)) {
    // Redirect back to the login page with the error messages
    $errorString = implode(',', $errors);
    header('Location: ../ForgotPassword/forgotpass.php?errors=' . urlencode($errorString));
    exit();
}
