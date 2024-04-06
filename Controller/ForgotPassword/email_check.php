<?php
include '../../includes/config.php';
session_start();

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

require '..\..\includes\phpmailer\vendor\autoload.php';

$email = trim($_POST['email']);

// Prepare and bind the query to avoid SQL injection
$query = "SELECT * FROM scerns_user_details WHERE Email = ?";
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

    // User ID
    $row = mysqli_fetch_assoc($result);
    $userID = $row['Id'];

    // Create a PHPMailer object
    $mail = new PHPMailer(true);

    try {
        //Server settings
        // $mail = new PHPMailer();
        // $mail->isSMTP();
        // $mail->Host = 'sandbox.smtp.mailtrap.io';
        // $mail->SMTPAuth = true;
        // $mail->Port = 2525;
        // $mail->Username = 'f23edba5aea8e5';
        // $mail->Password = '3a22fc3bd23d2e';

        $mail->isSMTP();
        $mail->Host = 'smtp.gmail.com';
        $mail->SMTPAuth = true;
        $mail->Username = 'ibiajiro@gmail.com';
        $mail->Password = 'gadi ulhb sxoj zkvo';
        $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;
        $mail->Port = 587;


        //Recipients
        $mail->setFrom('poreberwakanda71@gmail.com', 'SCERNS Admin');
        $mail->addAddress($rec); // Add a recipient

        // Content
        $mail->isHTML(true);
        $mail->Subject = $sub;
        $mail->Body = $msg;

        // Send email
        $mail->send();

        // You should store the OTP in the database or session for validation on the OTP page.
        $_SESSION['otp'] = $otp;
        $_SESSION['email'] = $email;
        $_SESSION['user_id'] = $userID;
        header('Location: ../../Views/ForgotPassword/otp.php');
        exit();
    } catch (Exception $e) {
        // Email could not be sent, handle the error
        echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}";
    }
} else {
    // Email does not exist, display SweetAlert or handle errors as needed
    $errors[] = "No email found!";
}

// Check for errors
if (!empty($errors)) {
    // Redirect back to the login page with the error messages
    $errorString = implode(',', $errors);
    header('Location: ../ForgotPassword/forgotpass.php?errors=' . urlencode($errorString));
    exit();
}
