<?php
session_start();
include '../../includes/config.php';

// if (isset($_SESSION['status'])) {
//     if($_SESSION['status'] == "Logged In As User"){
//         header("Location: user/home.php");
//         exit();
//     }
// }

if (isset($_SESSION['otp'])) {
  $otp = $_SESSION['otp'];
}
if (isset($_SESSION['email'])) {
  $email = $_SESSION['email'];
}

?>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SCERNS | OTP</title>
  <?php include 'headers/header.php'; ?>
</head>

<body style="background-color: #618264;">

  <form method="post" id="otpForm" action="../ForgotPassword/newpass.php">
    <section class="vh-100">
      <div class="container p-5 h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
          <div class="col-12 col-md-8 col-lg-6 col-xl-5 shadow rounded-4 p-4 bg-light">
            <div class="text-center" style="border-radius: 1rem;">

              <h1>Forgot Password</h1>

              <p class="text-muted">Input the otp code we sent in your email.</p>

              <div class="form-floating mb-2" id="">
                <input type="number" id="number" class="form-control rounded-4" placeholder="OTP" name="otp" autocomplete="" required>
                <label for="number" class="form-label">Input OTP Here</label>
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
  <script>
    $(document).ready(function() {
      $('#otpForm').submit(function(event) {
        event.preventDefault();

        var codeInput = $('#number').val();
        var otp = '<?php echo $otp; ?>';

        if (codeInput === otp) {
          $(this).unbind('submit').submit();
        } else {
          Swal.fire({
            text: "Wrong OTP. Please try again.",
            icon: "warning",
            title: "Oops..."
          });
        }
      });
    });
  </script>

  <?php include 'footers/footer.php'; ?>

  <script>
    if (window.performance && performance.navigation.type == 1) {
      // Reloaded the page using the browser's reload button
      window.location.href = "otp.php";
    }
  </script>
</body>

</html>