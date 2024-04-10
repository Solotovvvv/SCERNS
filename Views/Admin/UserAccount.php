<?php
session_start();
if (!isset($_SESSION['role']) || ($_SESSION['role'] != 1)) {
    header('Location: ../../../../Client3/index.php');
    exit;
}
?>


<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Admin Account</title>
    <link rel="icon" href="../../img/images/orig-logo.png" />
    <link rel="stylesheet" href="https://adminlte.io/themes/v3/plugins/fontawesome-free/css/all.min.css" />
    <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" />
    <link rel="stylesheet" href="https://adminlte.io/themes/v3/plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="stylesheet" href="https://adminlte.io/themes/v3/plugins/icheck-bootstrap/icheck-bootstrap.min.css" />
    <link rel="stylesheet" href="https://adminlte.io/themes/v3/plugins/jqvmap/jqvmap.min.css" />
    <link rel="stylesheet" href="https://adminlte.io/themes/v3/dist/css/adminlte.min.css?v=3.2.0" />
    <link rel="stylesheet" href="https://adminlte.io/themes/v3/plugins/overlayScrollbars/css/OverlayScrollbars.min.css" />
    <link rel="stylesheet" href="https://adminlte.io/themes/v3/plugins/daterangepicker/daterangepicker.css" />
    <link rel="stylesheet" href="https://adminlte.io/themes/v3/plugins/summernote/summernote-bs4.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.2/css/bootstrap.css" />
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.7/css/dataTables.bootstrap4.min.css" />
    <link rel="stylesheet" href="../../dist//css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.11.3/css/lightbox.min.css">


</head>

<body class="hold-transition sidebar-mini layout-fixed">
    <div class="wrapper">

        <?php include 'sidenav.php'; ?>


        <div class="content-wrapper">
            <div class="content-header">
                <div class="container">
                    <h1 class="m-0">User Account</h1>
                </div>
            </div>

            <section class="content">
                <div class="container">
                    <div class="table-responsive card p-3">
                        <!-- <button class="btn btn-primary mb-3 " data-toggle="modal" data-target="#addAdmin" style="width: 20%";>ADD
                        </button> -->

                        <div class="table-responsive card p-3">
                            <table id="user_tbl" class="table table-striped table-bordered text-center" style="width: 100%">
                                <thead>
                                    <tr>
                                        <th>Username</th>
                                        <th>Status</th>
                                        <th>ACTIONS</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>




    <div class="modal fade" id="edit_user" tabindex="-1" role="dialog" aria-labelledby="edit_user" aria-hidden="true">
        <div class="modal-dialog modal-xl modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="edit_user">Edit Admin Account</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="row">

                        <div class="col-sm-6">
                            <!-- Left Column -->
                            <h3 class="text-center mb-3">User Details</h3>
                            <hr>
                            <div class="row mb-3">
                                <div class="col-sm-12">
                                    <p class="m-0 font-weight-bold">Fullname <span class="text-danger">*</span></p>
                                    <input type="email" class="form-control" placeholder="Enter Full Name" id="edit_fullname">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-sm-12">
                                    <p class="m-0 font-weight-bold">Username <span class="text-danger">*</span></p>
                                    <input type="text" class="form-control" placeholder="Enter username" id="edit_username">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-sm-12">
                                    <p class="m-0 font-weight-bold">Email <span class="text-danger">*</span></p>
                                    <input type="email" class="form-control" placeholder="Enter Email" id="edit_email">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-sm-12">
                                    <p class="m-0 font-weight-bold">Phone <span class="text-danger">*</span></p>
                                    <input type="email" class="form-control" placeholder="Enter Phone" id="edit_Phone">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-sm-12">
                                    <p class="m-0 font-weight-bold">Address <span class="text-danger">*</span></p>
                                    <input type="email" class="form-control" placeholder="Enter Address" id="edit_Address">
                                </div>
                            </div>

                        </div>
                        <!-- Right Column -->
                        <div class="col-sm-6" style="border-left: 2px solid rgba(0, 0, 0, 0.2);">
                            <h3 class="text-center mb-3">Requirements</h3>
                            <hr>

                            <a href="#" id="userImageLink" data-lightbox="user-image" data-title="User Image">
                                <img id="userImage" src="#" alt="Requirements" style="max-width: 100%; height: 300px;display: block; margin: 0 auto; border: 2px solid #000; padding: 5px;">
                            </a>

                            <div class="d-flex justify-content-center w-100">
                                <select id="statusSelect" class="form-control mt-4 col-sm-6">
                                    <option selected disabled>Select Status</option>
                                    <option value="Pending">Pending</option>
                                    <option value="0">Approved</option>
                                </select>
                            </div>

                        </div>
                    </div>
                </div>



                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="updateAdmin_User()">SAVE CHANGES</button>
                    <input type="text" id="hiddendata_Admin_user">
                </div>
            </div>
        </div>
    </div>


    < <script src="https://adminlte.io/themes/v3/plugins/jquery/jquery.min.js">
        </script>
        <script src="https://adminlte.io/themes/v3/plugins/jquery-ui/jquery-ui.min.js"></script>
        <script>
            $.widget.bridge("uibutton", $.ui.button);
        </script>
        <script src="https://adminlte.io/themes/v3/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="https://adminlte.io/themes/v3/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"></script>
        <script src="https://adminlte.io/themes/v3/dist/js/adminlte.js?v=3.2.0"></script>
        <script src="https://cdn.datatables.net/1.13.7/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.7/js/dataTables.bootstrap4.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.11.3/js/lightbox.min.js"></script>


        <script>
            $(document).ready(function() {

                $('#user_tbl').DataTable({
                    'serverside': true,
                    'processing': true,
                    'paging': true,
                    "columnDefs": [{
                        "className": "dt-center",
                        "targets": "_all"
                    }, ],
                    'ajax': {
                        'url': 'tables/user_tbl.php',
                        'type': 'post',

                    },
                });

            });




        
            function view_user(id) {
                $('#hiddendata_Admin_user').val(id);
                $.post("../../Controller/Admin/Admin_Account.php", {
                    id: id
                }, function(data, status) {
                    var userids = JSON.parse(data);
                    $('#edit_username').val(userids.Username);
                    $('#edit_password').val(userids.Password);
                    $('#edit_email').val(userids.Email);
                    $('#edit_fullname').val(userids.Fullname);
                    $('#edit_Address').val(userids.Address);
                    $('#edit_Phone').val(userids.Phone);

                    // Display the image using Base64 data if available
                    if (userids.RequirementBase64) {
                        $('#userImage').attr('src', 'data:image/jpeg;base64,' + userids.RequirementBase64);
                        $('#userImageLink').attr('href', 'data:image/jpeg;base64,' + userids.RequirementBase64);
                    }

                    $('#edit_user').modal("show");
                });
            }

            function updateAdmin_User() {
                var status = $('#statusSelect').val();
                var id = $('#hiddendata_Admin_user').val();


                $.post("../../Controller/Admin/Update_user_status.php", {
                    status: status,
                    id_s: id
                }, function(data, status) {
                    var jsons = JSON.parse(data);
                    status = jsons.status;
                    if (status == 'success') {
                        Swal.fire({
                            title: 'Record Updated!',
                            icon: 'success',
                            showConfirmButton: false,
                            timer: 1000
                        });
                        var update = $('#user_tbl').DataTable().ajax.reload();
                    }
                    $('#edit_user').modal("hide");
                });

            }
        </script>
</body>

</html>