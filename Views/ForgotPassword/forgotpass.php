<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SCERNS | Forgot Password</title>
  <?php include 'headers/header.php'; ?>

  <!-- Include jQuery library -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

</head>

<body style="background-color: #618264;">

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

            <button type="button" id="confirmButton" class="btn btn-primary btn-lg rounded-pill w-50 mt-2">Confirm</button>

            <div class="d-flex justify-content-around mt-5">
              <a class="text-decoration-none text-dark">You remembered your account?</a>
              <a class="text-decoration-none text-primary" href="../../index.php">Login</a>
            </div>

          </div>
        </div>
      </div>
    </div>
  </section>

  <?php include 'footers/footer.php'; ?>

  <script>
    // jQuery AJAX to handle form submission
    $(document).ready(function() {
      $('#confirmButton').click(function() {
        var email = $('#emailInput').val();

        $.ajax({
          type: 'POST',
          url: '../../Controller/ForgotPassword/email_check.php',
          data: {
            email: email
          },
          success: function(response) {
            // Handle success response
            // You can process the response here, maybe show a success message
            console.log(response);
               window.location.href = '../../Views/ForgotPassword/otp.php';
          },
          error: function(xhr, status, error) {
            // Handle error
            // You can show an error message or handle errors accordingly
            console.error(xhr.responseText);
          }
        });
      });
    });

    // This script checks if the page is reloaded using the browser's reload button and redirects to the same page
    if (window.performance) {
      if (performance.navigation.type == 1) {
        window.location.href = "forgotpass.php";
      }
    }
  </script>
</body>

</html>