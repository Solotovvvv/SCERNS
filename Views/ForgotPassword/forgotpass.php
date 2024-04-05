<?php
session_start();
include '../../includes/config.php';

// if (isset($_SESSION['status'])) {
//     if($_SESSION['status'] == "Logged In As User"){
//         header("Location: user/home.php");
//         exit();
//     }
// }

?>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SCERNS | Forgot Password</title>
  <?php include 'headers/header.php'; ?>

<body style="background-color: #618264;">

  <form action="../../Controller/ForgotPassword/email_check.php" method="post">
    <section class="vh-100">
      <div class="container p-5 h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
          <div class="col-12 col-md-8 col-lg-6 col-xl-5 shadow rounded-4 p-4 bg-light">
            <div class="text-center" style="border-radius: 1rem;">

              <h1>Forgot Password</h1>

              <p class="text-muted">We will send you a new password in your email.</p>

              <div class="form-floating mb-2" id="emailForm">
                <input type="email" id="emailInput" class="form-control rounded-4" placeholder="Email" name="email" autocomplete="" required>
                <label for="emailInput" class="form-label">Email</label>
              </div>

              <button type="submit" class="btn btn-primary btn-lg rounded-pill w-50 mt-2">Confirm</button>

              <div class="d-flex justify-content-around mt-5">
                <a class="text-decoration-none text-dark">You remembered your account?</a>
                <a class="text-decoration-none text-primary" href="../../index.php">Login</a>
              </div>

            </div>
          </div>
        </div>
      </div>
    </section>
  </form>
  <?php include 'footers/footer.php'; ?>
  <?php
  // Display error messages if they were passed in the URL
  if (isset($_GET['errors'])) {
    $errors = explode(',', $_GET['errors']);
    foreach ($errors as $error) {
      echo "<script>Swal.fire({
                  icon: 'error',
                  title: 'ERROR',
                  text: '$error'
              });</script>";
    }
    unset($_GET['errors']);
  }
  ?>

  <script>
    if (window.performance) {
      if (performance.navigation.type == 1) {
        // Reloaded the page using the browser's reload button
        window.location.href = "forgotpass.php";
      }
    }
  </script>
</body>

</html>