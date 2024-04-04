<?php
session_start();
include '../../includes/config.php';

// if (isset($_SESSION['status'])) {
//     if($_SESSION['status'] == "Logged In As User"){
//         header("Location: user/home.php");
//         exit();
//     }
// }
if (isset($_SESSION['email'])) {
  $email = $_SESSION['email'];
  $id = $_SESSION['user_id'];
}

?>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SCERNS | New Password</title>
  <?php include 'headers/header.php'; ?>

</head>

<body style="background-color: #618264;">

  <form action="" id="formnewpass" method="post">
    <section class="vh-100">
      <div class="container p-5 h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
          <div class="col-12 col-md-8 col-lg-6 col-xl-5 shadow rounded-4 p-4 bg-light">
            <div class="text-center" style="border-radius: 1rem;">

              <h1>Forgot Password</h1>

              <p class="text-muted">Create a new password on your account.</p>

              <div class="form-floating mb-3 text-start" style="position: relative;">
                <input type="password" class="form-control rounded-4" id="NewPassword" name="password" placeholder="Password" value="" required>
                <label for="NewPassword">New Password</label>
                <span class="toggle-password mt-1" id="togglePassword1"><i class="fa-regular fa-eye"></i></span>
              </div>

              <div class="form-floating mb-3 text-start" style="position: relative;">
                <input type="hidden" name="email" value="<?php echo $email; ?>">
                <input type="password" class="form-control rounded-4" id="ConfirmPassword" name="confirm_password" placeholder="Confirm Password" value="" required>
                <label for="ConfirmPassword">Confirm Password</label>
                <span class="toggle-password mt-1" id="togglePassword2"><i class="fa-regular fa-eye"></i></span>
                <span id="passwordMatch" style="color: red;"></span>
              </div>

              <button type="submit" class="btn btn-primary btn-lg rounded-pill w-50 mt-2">Change Password</button>

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

  <!-- Confirmation Script -->
  <script>
    $(document).ready(function() {
      // Function to toggle password visibility
      $('.toggle-password').click(function() {
        var $input = $(this).prev('input');
        var type = $input.attr('type') === 'password' ? 'text' : 'password';
        $input.attr('type', type);
        $(this).find('i').toggleClass('fa-eye fa-eye-slash');
      });

      $('#formnewpass').submit(function(event) {
        passwordMatch.textContent = ''; // Clear password match label
        event.preventDefault(); // Prevent form submission

        var pass = $('#NewPassword').val();
        var Confirmpass = $('#ConfirmPassword').val();
        var userid = '<?= $id ?>';
        var resetpasslink = '../../Controller/ForgotPassword/reset_password.php';

        if (pass !== Confirmpass) {
          passwordMatch.textContent = "Passwords do not match.";
          return;
        } else if (!pass || !Confirmpass) {
          passwordMatch.textContent = "Passwords are empty.";
          return;
        }

        $.ajax({
          url: resetpasslink,
          method: 'POST',
          data: {
            user_id: userid,
            password: pass,
            confirmpass: Confirmpass,
          },
          success: function(response) {
            Swal.fire({
              text: "Changing Password Success!",
              title: 'Success',
              icon: 'success',
              showConfirmButton: false,
              timer: 1000,
              willClose: function() {
                window.location.href = "../../index.php"
              }
            });
          },
          error: function(xhr, status, error) {
            Swal.fire({
              text: "An error occurred while processing your request. Please try again later.",
              title: 'Error',
              icon: 'error'
            });
          }
        });
      });
    });
  </script>

  <script>
    if (window.performance) {
      if (performance.navigation.type == 1) {
        // Reloaded the page using the browser's reload button
        window.location.href = "newpass.php";
      }
    }
  </script>

</body>

</html>