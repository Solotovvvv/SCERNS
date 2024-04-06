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
  <title>Respondent Admin</title>
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
          <h1 class="m-0">Contact Respondent</h1>
        </div>
      </div>

      <section class="content">
        <div class="container">
          <div class="table-responsive card p-3">
            <button class="btn btn-primary mb-3 admin-btn" data-toggle="modal" data-target="#addRespondent" style="width: 20%" ;>ADD
            </button>

            <div class="table-responsive card p-3">
              <table id="Contacts_table" class="table table-striped table-bordered text-center" style="width: 100%">
                <thead>
                  <tr>
                    <th>Hotline</th>
                    <th>Dispatcher Code</th>
                    <th>Name</th>
                    <th>Department</th>
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
  <div class="modal fade" id="addRespondent" tabindex="-1" role="dialog" aria-labelledby="addRegistrarModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="addRegistrarModalLabel">Add Contact Respondent</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="row mb-3">
            <div class="col-sm-6">
              <p class="m-0 font-weight-bold">Hotline <span class="text-danger">*</span></p>
              <input type="email" class="form-control" placeholder="Enter Full Name" id="hotline">
            </div>

            <div class="col-sm-6">
              <p class="m-0 font-weight-bold">Dispatcher Code <span class="text-danger">*</span></p>
              <input type="email" class="form-control" placeholder="Enter Email" id="DispatcherCode">
            </div>
          </div>



          <div class="row mb-3">
            <div class="col-sm-12">
              <p class="m-0 font-weight-bold">name<span class="text-danger">*</span></p>
              <input type="text" class="form-control" placeholder="Enter username" id="name">
            </div>
          </div>


          <div class="row mt-3">
            <div class="col-sm-12">
              <p class="m-0 font-weight-bold">Department <span class="text-danger">*</span></p>
              <select class="form-control" id="Department">
                <option value="" disabled selected>Select Type of Deparment</option>
                <option value="Fire">Fire</option>
                <option value="Medic">Medic</option>
                <option value="Crime">Crime</option>
                <option value="Natural Disaster">Natural Disaster</option>
              </select>
            </div>
          </div>
        </div>


        <div class="modal-footer">
          <button type="button" class="btn btn-primary" onclick="addRespondentAdmin()">SUBMIT</button>
        </div>
      </div>
    </div>
  </div>


  <div class="modal fade" id="edit_contacts" tabindex="-1" role="dialog" aria-labelledby="editRegistrarModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="editRegistrarModalLabel">Edit Registrar</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="row mb-3">
            <div class="col-sm-6">
              <p class="m-0 font-weight-bold">Hotline <span class="text-danger">*</span></p>
              <input type="email" class="form-control" placeholder="Enter Full Name" id="Edit_hotline">
            </div>

            <div class="col-sm-6">
              <p class="m-0 font-weight-bold">Dispatcher Code <span class="text-danger">*</span></p>
              <input type="email" class="form-control" placeholder="Enter Email" id="Edit_DispatcherCode">
            </div>
          </div>



          <div class="row mb-3">
            <div class="col-sm-12">
              <p class="m-0 font-weight-bold">name<span class="text-danger">*</span></p>
              <input type="text" class="form-control" placeholder="Enter username" id="Edit_name">
            </div>
          </div>


          <div class="row mt-3">
            <div class="col-sm-12">
              <p class="m-0 font-weight-bold">Department <span class="text-danger">*</span></p>
              <select class="form-control" id="Edit_Department">
                <option value="" disabled selected>Select Type of Deparment</option>
                <option value="Fire">Fire</option>
                <option value="Medic">Medic</option>
                <option value="Crime">Crime</option>
                <option value="Natural Disaster">Natural Disaster</option>
              </select>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" onclick="updateContacts()">SAVE CHANGES</button>
          <input type="text" id="hiddendata_respondent_contact">
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

        $('#Contacts_table').DataTable({
          'serverside': true,
          'processing': true,
          'paging': true,
          "columnDefs": [{
            "className": "dt-center",
            "targets": "_all"
          }, ],
          'ajax': {
            'url': 'tables/Contacts_tbl.php',
            'type': 'post',

          },
        });

      });




      function addRespondentAdmin() {

        var name = $('#name').val();
        var hotline = $('#hotline').val();
        var DispatcherCode = $('#DispatcherCode').val();
        var department = $('#Department').val();

        $.ajax({
          url: '../../Controller/Admin/contact_respondent_controller.php',
          method: 'POST',
          data: {
            name: name,
            hotline: hotline,
            DispatcherCode: DispatcherCode,
            department: department
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
              $('#name').val('');
              $('#hotline').val('');
              $('#DispatcherCode').val('');
              $('#Department').val('');
              var c = $('#Contacts_table').DataTable().ajax.reload();
              Swal.fire({
                title: 'Record Added!',
                icon: 'success',
                showConfirmButton: false,
                timer: 1000
              });
            } else {
              alert('Failed to add data.');
            }

            $('#addRespondent').modal("hide");
          },
          error: function(xhr, status, error) {
            alert('Error: ' + error);
          }
        });
      }


      function view_contacts(id) {
        $('#hiddendata_respondent_contact').val(id);
        $.post("../../Controller/Admin/contact_respondent_controller.php", {
          id: id
        }, function(data,
          status) {
          var userids = JSON.parse(data);
          $('#Edit_hotline').val(userids.HotlineNumber);
          $('#Edit_name').val(userids.Name);
          $('#Edit_DispatcherCode').val(userids.Dispatcher_Code);
          $('#Edit_Department').val(userids.Category);
          // $('#edit_password').val(userids.password);
        });
        $('#edit_contacts').modal("show");
      }

      function updateContacts() {
        var hotline = $('#Edit_hotline').val();
        var name = $('#Edit_name').val();
        var dpcode = $('#Edit_DispatcherCode').val();
        var department = $('#Edit_Department').val();
        var id = $('#hiddendata_respondent_contact').val();

        $.post("../../Controller/Admin/Update_contacts_respondents.php", {
          name: name,
          hotline: hotline,
          dpcode: dpcode,
          department: department,
          id_c: id
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
            var update = $('#Contacts_table').DataTable().ajax.reload();
          }
          $('#edit_contacts').modal("hide");
        });

      }

      function delete_contacts(id) {
        Swal.fire({
          title: 'Are you sure?',
          text: 'You are about to delete this registrar record.',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
          if (result.isConfirmed) {
            // User confirmed deletion
            $.ajax({
              url: '../../Controller/Admin/contact_respondent_controller.php',
              type: 'post',
              data: {
                removes: id
              },
              success: function(data, status) {
                var json = JSON.parse(data);
                status = json.status;
                if (status == 'success') {
                  Swal.fire({
                    title: 'Record Deleted!',
                    text: 'The admin record has been successfully deleted.',
                    icon: 'success',
                  });
                  $('#Contacts_table').DataTable().ajax.reload();
                }
              }
            });
          }
        });
      }
    </script>
</body>

</html>