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
                                <img src="..." class="card-img-top" alt="No Image Available">
                                <div class="card-body">
                                    <h5 class="card-title">Name: <?php echo strtoupper($_SESSION['fullname']) ?></h5>
                                    <p class="card-text">Contacts: <?php echo strtoupper($_SESSION['email']) ?></p>
                                    <button href="#" class="btn btn-primary">Upload Image</button> <!-- Changed from button to anchor -->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

        </div>
    </div>

    <?php include 'footers/footer.php' ?>
</body>

</html>