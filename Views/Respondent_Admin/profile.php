<?php include 'headers/header.php' ?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
</head>

<body class="hold-transition sidebar-mini layout-fixed">
    <div class="wrapper">
        <?php include 'sidenav.php' ?>
        <div class="content-wrapper">
            <div class="container">
                <label for="" class="m-0">Profile</label>
            </div>

            <section class="content">
                <div class="container">
                    <div class="row justify-content-center"> <!-- Center the card horizontally -->
                        <div class="col-md-6"> <!-- Adjust width of the column as needed -->
                            <div class="card">
                                <div class="card-title">
                                    <img src="<?= $_SESSION['image'] ?>" class="mt-4" alt="Profile Image" style="max-width: 250px; max-height: 250px; display: block; margin-left: auto; margin-right: auto;">
                                </div>
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#imageModal">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <div class="card-body">
                                    <h5 class="card-title">Name: <?php echo strtoupper($_SESSION['fullname']) ?></h5>
                                    <p class="card-text">Contacts: <?php echo strtoupper($_SESSION['email']) ?></p>
                                    <p class="card-text">Department: <?php echo strtoupper($_SESSION['type']) ?></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- ImageModal -->
            <div class="modal fade" data-backdrop="static" id="imageModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Change Profile Picture</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <img id="previewImage" src="" alt="No Image Detected" style="max-width: 250px; max-height: 250px; display: block; margin-left: auto; margin-right: auto;">
                            </div>
                            <div class="custom-file">
                                <input type="file" accept=".jpg, .jpeg, .png" class="custom-file-input" id="profilepicture" aria-describedby="inputGroupFileAddon01">
                                <label class="custom-file-label" for="profilepicture">Choose file</label>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="button" id="savepfp" class="btn btn-primary">Save changes</button>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <?php include 'footers/footer.php' ?>
</body>

<script>
    $(document).ready(function() {
        $('#imageModal').on('hidden.bs.modal', function() {
            // Clear the FileReader
            $('#previewImage').attr('src', ''); // Reset the src attribute

            // Clear the file input
            $('#profilepicture').val(''); // Reset the file input value
        });

        $('#profilepicture').change(function() {
            var file = this.files[0];
            var reader = new FileReader();
            var preview = $('#previewImage');

            reader.onloadend = function() {
                preview.attr('src', reader.result);
            }

            if (file) {
                if (file.size > 5 * 1024 * 1024) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'The file is too large. Please select a file smaller than 5MB.'
                    });
                    return;
                }

                if (/\.(jpe?g|png)$/i.test(file.name)) {
                    reader.readAsDataURL(file);

                } else {
                    preview.attr('src', '');
                    Swal.fire({
                        icon: 'error',
                        title: 'Invalid File',
                        text: 'Please select a JPEG or PNG file.'
                    });
                }
            }
        });

        $('#savepfp').click(function() {
            var fileInput = $('#profilepicture')[0];
            if (fileInput.files.length > 0) {
                var formData = new FormData();
                formData.append('file', fileInput.files[0]);

                $.ajax({
                    url: "../../Controller/Profiles/insertprofile.php",
                    type: 'POST',
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function(response) {
                        console.log(response);
                        Swal.fire({
                            text: 'Image uploaded successfully!',
                            icon: 'success',
                            title: 'Success',
                            willClose: function() {
                                $('#imageModal').modal('hide');
                            }
                        });
                    },
                    error: function(xhr, status, error) {
                        console.error(xhr.responseText);
                        Swal.fire({
                            text: "Error uploading image. Please try again.",
                            icon: "error",
                            title: "Oops..."
                        })
                    }
                });
            } else {
                Swal.fire({
                    text: "Try to select an image first.",
                    icon: "warning",
                    title: "Oops..."
                })
            }
        });
    });
</script>

</html>