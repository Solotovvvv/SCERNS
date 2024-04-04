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

  <form action="../ForgotPassword/reset_password.php" id="formnewpass" method="post">
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
                <a class="text-decoration-none text-primary" href="./index.php">Login</a>
              </div>

            </div>
          </div>
        </div>
      </div>
    </section>
  </form>

  <!-- Confirmation Script -->
  <script>
    const newPasswordInput = document.getElementById("NewPassword");
    const confirmPasswordInput = document.getElementById("ConfirmPassword");
    const passwordMatchLabel = document.getElementById("passwordMatch");
    const form = document.getElementById("formnewpass");

    function validatePassword() {
      const newPassword = newPasswordInput.value;
      const confirmPassword = confirmPasswordInput.value;

      if (newPassword !== confirmPassword) {
        passwordMatchLabel.textContent = "Passwords do not match";
      } else {
        passwordMatchLabel.textContent = "";
      }
    }

    function togglePasswordVisibility(passwordInput, togglePassword) {
      const type = passwordInput.type === "password" ? "text" : "password";
      passwordInput.type = type;
      togglePassword.innerHTML = type === "text" ? '<i class="fa-regular fa-eye-slash"></i>' : '<i class="fa-regular fa-eye"></i>';
    }

    function handleFormSubmission(event) {
      validatePassword();

      if (newPasswordInput.value !== confirmPasswordInput.value) {
        event.preventDefault();
        event.stopPropagation();
      }
    }

    newPasswordInput.addEventListener("input", validatePassword);
    confirmPasswordInput.addEventListener("input", validatePassword);
    form.addEventListener("submit", handleFormSubmission);

    const togglePassword1 = document.getElementById("togglePassword1");
    const togglePassword2 = document.getElementById("togglePassword2");

    togglePassword1.addEventListener("click", () => {
      togglePasswordVisibility(newPasswordInput, togglePassword1);
    });

    togglePassword2.addEventListener("click", () => {
      togglePasswordVisibility(confirmPasswordInput, togglePassword2);
    });
  </script>



  <?php include 'footers/footer.php'; ?>

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