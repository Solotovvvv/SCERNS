<?php include 'headers/header.php'; ?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Profile Page</title>

    <style>
       
        .card {
            padding: 1rem;
            width: 40%;    
        }
        

        .profile img {
            border-radius: 5rem;
        }

        .name {
            margin: 0.5rem 0 0.4rem;
        }
    </style>
</head>

<body class="hold-transition sidebar-mini layout-fixed">
    <div class="wrapper">
        <?php include 'sidenav.php'; ?>

        <div class="container">
            <div class="row justify-content-center mb-3">
                <div class="card mt-3">
                    <div class="text-center">
                        <div class="profile">
                            <img id="uploadedImage" src="https://as1.ftcdn.net/v2/jpg/03/53/11/00/1000_F_353110097_nbpmfn9iHlxef4EDIhXB1tdTD0lcWhG9.jpg" alt="Profile Picture" width="150">
                        </div>
                        <div class="name">
                            <h3><?php echo strtoupper($_SESSION['fullname']) ?></h3>
                        </div>
                        <div class="form-group">
                            <input type="file" class="form-control-file" id="fileUpload" style="display: none">
                            <button type="button" class="btn btn-primary" onclick="document.getElementById('fileUpload').click()">Upload Photo</button>
                        </div>
                    </div>
                    <div class="additional-fields">
                        <div class="form-group">
                            <label for="department" class="input-label">Department:</label>
                            <input type="text" id="department" class="form-control" value="<?php echo strtoupper($_SESSION['type']) ?>" readonly>
                        </div>

                        <div class="form-group">
                            <label for="contact" class="input-label">Username:</label>
                            <input type="text" id="contact" class="form-control" readonly value="<?php echo strtoupper($_SESSION['username']) ?>">
                        </div>
                        <div class="form-group">
                            <label for="email" class="input-label">Email:</label>
                            <input type="text" id="email" class="form-control" value="<?php echo strtoupper($_SESSION['email']) ?>" readonly>
                        </div>

                        <div class="save mt-3 d-flex justify-content-end">
                            <button id="saveBtn" class="btn btn-success">Save</button>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <?php include 'footers/footer.php'; ?>


        <script>
            $(document).ready(function() {
                $('#saveBtn').click(function() {
                    var formData = new FormData();
                    formData.append('file', $('#fileUpload')[0].files[0]);

                    $.ajax({
                        url: '../../Controller/Profiles/insertprofile.php',
                        type: 'POST',
                        data: formData,
                        contentType: false,
                        processData: false,
                        success: function(response) {
                            $('#uploadedImage').attr('src', response);
                        },
                        error: function(xhr, status, error) {
                            console.error(xhr.responseText);
                        }
                    });
                });
            });

            function fetchAndDisplayImage() {
                $.ajax({
                    url: '../../Controller/Profiles/fetch_images.php', // Change the URL to your PHP script
                    type: 'GET',
                    success: function(response) {
                        $('#uploadedImage').attr('src', response);
                    },
                    error: function(xhr, status, error) {
                        console.error(xhr.responseText);
                    }
                });
            }

            // Call the function on page load
            fetchAndDisplayImage();
        </script>
</body>

</html>