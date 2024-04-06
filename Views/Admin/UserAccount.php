<?php
session_start();
if (!isset($_SESSION['role']) || ($_SESSION['role'] != 1)) {
    header('Location: ../../../../Scerns/index.php');
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

    <!-- add registrar modal -->
    <!-- <div class="modal fade" id="addUser" tabindex="-1" role="dialog" aria-labelledby="addRegistrarModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addRegistrarModalLabel">Add Admin Account</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="row mb-3">
                        <div class="col-sm-6">
                            <p class="m-0 font-weight-bold">Fullname <span class="text-danger">*</span></p>
                            <input type="email" class="form-control" placeholder="Enter Full Name" id="fullnameR">
                        </div>

                        <div class="col-sm-6">
                            <p class="m-0 font-weight-bold">Email <span class="text-danger">*</span></p>
                            <input type="email" class="form-control" placeholder="Enter Email" id="email">
                        </div>
                    </div>

                  <div class="row mb-3">
                        <div class="col-sm-12">
                            <p class="m-0 font-weight-bold">Address <span class="text-danger">*</span></p>
                            <input type="text" class="form-control" placeholder="Enter Address" id="address">
                        </div>

                        <div class="col-sm-12">
                            <p class="m-0 font-weight-bold">Phone <span class="text-danger">*</span></p>
                            <input type="text" class="form-control" placeholder="Enter Phone" id="phone">
                        </div>
                    </div> -->

    <!-- 
    <div class="row mb-3">
        <div class="col-sm-12">
            <p class="m-0 font-weight-bold">Username <span class="text-danger">*</span></p>
            <input type="text" class="form-control" placeholder="Enter username" id="registrar">
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12">
            <p class="m-0 font-weight-bold">Password <span class="text-danger">*</span></p>
            <input type="password" class="form-control" placeholder="Enter password" id="passwordR">
        </div>
    </div>
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick="addAdmin()">SUBMIT</button>
    </div>
    </div>
    </div> -->
    <!-- </div> -->


    <div class="modal fade" id="edit_user" tabindex="-1" role="dialog" aria-labelledby="edit_user" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="edit_user">Edit Admin Account</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="row mb-3">
                        <div class="col-sm-6">
                            <p class="m-0 font-weight-bold">Fullname <span class="text-danger">*</span></p>
                            <input type="email" class="form-control" placeholder="Enter Full Name" id="edit_fullname">
                        </div>

                        <div class="col-sm-6">
                            <p class="m-0 font-weight-bold">Email <span class="text-danger">*</span></p>
                            <input type="email" class="form-control" placeholder="Enter Email" id="edit_email">
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-sm-6">
                            <p class="m-0 font-weight-bold">Phone <span class="text-danger">*</span></p>
                            <input type="email" class="form-control" placeholder="Enter Full Name" id="edit_Phone">
                        </div>

                        <div class="col-sm-6">
                            <p class="m-0 font-weight-bold">Address <span class="text-danger">*</span></p>
                            <input type="email" class="form-control" placeholder="Enter Email" id="edit_Address">
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-sm-12">
                            <p class="m-0 font-weight-bold">Username <span class="text-danger">*</span></p>
                            <input type="text" class="form-control" placeholder="Enter username" id="edit_username">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-sm-12">
                            <p class="m-0 font-weight-bold">Password <span class="text-danger">*</span></p>
                            <input type="password" class="form-control" placeholder="Enter password" id="edit_password">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="updateAdmin()">SAVE CHANGES</button>
                    <input type="text" id="hiddendata_Admin">
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




            function addAdmin() {

                var fullname = $('#fullnameR').val();
                var email = $('#email').val();
                var username = $('#registrar').val();
                var password = $('#passwordR').val();

                $.ajax({
                    url: '../../Controller/Admin/Admin_Account.php',
                    method: 'POST',
                    data: {
                        fullname: fullname,
                        email: email,
                        username: username,
                        password: password
                    },
                    success: function(response) {
                        var data = JSON.parse(response);
                        if (data.status == 'data_exist') {
                            Swal.fire({
                                title: 'Record Already Exist!',
                                icon: 'warning',
                                showConfirmButton: false,
                                timer: 1000
                            });
                        } else if (data.status == 'success') {
                            var c = $('#admin_tbls').DataTable().ajax.reload();
                            Swal.fire({
                                title: 'Record Added!',
                                icon: 'success',
                                showConfirmButton: false,
                                timer: 1000
                            });
                        } else {
                            alert('Failed to add data.');
                        }

                        $('#addAdmin').modal("hide");
                    },
                    error: function(xhr, status, error) {
                        alert('Error: ' + error);
                    }
                });
            }


            function view_user(id) {
                $('#hiddendata_Admin').val(id);
                $.post("../../Controller/Admin/Admin_Account.php", {
                    id: id
                }, function(data,
                    status) {
                    var userids = JSON.parse(data);
                    $('#edit_username').val(userids.Username);
                    $('#edit_password').val(userids.Password);
                    $('#edit_email').val(userids.Email);
                    $('#edit_fullname').val(userids.Fullname);
                    // $('#edit_password').val(userids.password);
                });
                $('#edit_user').modal("show");
            }

            // function updateAdmin() {
            //     var username = $('#edit_username').val();
            //     var password = $('#edit_password').val();
            //     var email = $('#edit_email').val();
            //     var fullname = $('#edit_fullname').val();
            //     var id = $('#hiddendata_Admin').val();

            //     $.post("../../Controller/Admin/Update_admin.php", {
            //         password: password,
            //         username: username,
            //         fullname: fullname,
            //         email: email,
            //         id_a: id
            //     }, function(data, status) {
            //         var jsons = JSON.parse(data);
            //         status = jsons.status;
            //         if (status == 'success') {
            //             Swal.fire({
            //                 title: 'Record Updated!',
            //                 icon: 'success',
            //                 showConfirmButton: false,
            //                 timer: 1000
            //             });
            //             var update = $('#admin_tbls').DataTable().ajax.reload();
            //         }
            //         $('#editAdminModal').modal("hide");
            //     });

            // }

            // function delete_admin(id) {
            //     Swal.fire({
            //         title: 'Are you sure?',
            //         text: 'You are about to delete this registrar record.',
            //         icon: 'warning',
            //         showCancelButton: true,
            //         confirmButtonColor: '#3085d6',
            //         cancelButtonColor: '#d33',
            //         confirmButtonText: 'Yes, delete it!'
            //     }).then((result) => {
            //         if (result.isConfirmed) {
            //             // User confirmed deletion
            //             $.ajax({
            //                 url: '../../Controller/Admin/Admin_Account.php',
            //                 type: 'post',
            //                 data: {
            //                     remove: id
            //                 },
            //                 success: function(data, status) {
            //                     var json = JSON.parse(data);
            //                     status = json.status;
            //                     if (status == 'success') {
            //                         Swal.fire({
            //                             title: 'Record Deleted!',
            //                             text: 'The admin record has been successfully deleted.',
            //                             icon: 'success',
            //                         });
            //                         $('#admin_tbls').DataTable().ajax.reload();
            //                     }
            //                 }
            //             });
            //         }
            //     });
            // }
        </script>
</body>

</html>