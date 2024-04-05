<?php include 'headers/header.php' ?>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
</head>

<body class="hold-transition sidebar-mini layout-fixed">
    <div class="wrapper">
        <?php
        include 'sidenav.php';
        ?>
        <div class="content-wrapper">
            <div class="container">
                <label for="" class="m-0">Hotlines</label>
            </div>

            <section class="content">
                <div class="container">
                    <div class="card">
                        <div class="card-title d-flex justify-content-end">
                            <button type="button" class="btn btn-primary mx-5 mt-4" data-toggle="modal" data-target="#hotlineModal">
                                <i class="fas fa-plus"></i>
                                Add Hotlines
                            </button>
                        </div>
                        <div class="card-body">
                            <table id="hotlines" class="table" style="width:100%">
                                <thead>
                                    <tr>
                                        <th>No.</th>
                                        <th>Dispatcher Code</th>
                                        <th>Name</th>
                                        <th>Hotline Number</th>
                                        <th>Category</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
    <!-- Add Hotline Modal -->
    <div class="modal fade" id="hotlineModal" tabindex="-1" aria-labelledby="hotlineModalLabel" data-backdrop="static" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Add Hotline</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form id="hotlineForm">
                        <div class="form-group">
                            <label class="col-form-label">Dispatcher Code:</label>
                            <input type="text" class="form-control" id="dispatchID" placeholder="ABC123">
                        </div>
                        <div class="form-group">
                            <label class="col-form-label">Organization Name:</label>
                            <input type="text" class="form-control" id="orgnameID" placeholder="XYZ Volunteers Inc."></input>
                        </div>
                        <div class="form-group">
                            <label class="col-form-label">Hotline Number:</label>
                            <input type="number" class="form-control" id="numberID" placeholder="12-345-678 or 09123456789"></input>
                        </div>
                        <div class="form-group">
                            <label class="col-form-label">Emergency Type:</label>
                            <select class="form-control" id="emergencyID">
                                <option value="<?php echo strtoupper($_SESSION['type']) ?>"><?php echo strtoupper($_SESSION['type']) ?></option>
                            </select>
                        </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary" id="submitButton">Add Hotline</button>
                </div>
                </form>
            </div>
        </div>
    </div>
    <!-- View/Edit Hotline Modal -->
    <div class="modal fade" id="viewhotlineModal" tabindex="-1" aria-labelledby="hotlineModalLabel" data-backdrop="static" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Edit Hotline</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form id="hotlineForm">
                        <div class="form-group">
                            <label class="col-form-label">Dispatcher Code:</label>
                            <input type="text" class="form-control" id="editdispatchID" placeholder="ABC123">
                        </div>
                        <div class="form-group">
                            <label class="col-form-label">Organization Name:</label>
                            <input type="text" class="form-control" id="editorgnameID" placeholder="XYZ Volunteers Inc."></input>
                        </div>
                        <div class="form-group">
                            <label class="col-form-label">Hotline Number:</label>
                            <input type="number" class="form-control" id="editnumberID" placeholder="12-345-678 or 09123456789"></input>
                        </div>
                        <div class="form-group">
                            <label class="col-form-label">Emergency Type:</label>
                            <input type="text" class="form-control" id="editemergencyID" disabled>
                            </input>
                        </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" onclick="updateHotline()" id="submitButton">Save Changes</button>
                    <input type="text" hidden id="hotlineid">
                </div>
                </form>
            </div>
        </div>
    </div>

    <?php include 'footers/footer.php' ?>

    <script>
        $(document).ready(function() {

            $('#hotlines').DataTable({
                'serverside': true,
                'processing': true,
                'paging': true,
                "columnDefs": [{
                    "className": "dt-center",
                    "targets": "_all"
                }, ],
                'ajax': {
                    'url': '../../Controller/Hotlines/gethotline.php',
                    'type': 'get',

                },
            });

            $('#hotlineForm').submit(function(e) {
                e.preventDefault();

                var number = $("#numberID").val();
                var code = $("#dispatchID").val();
                var name = $("#orgnameID").val();
                var emergency = $("#emergencyID").val();
                emergency = emergency.toLowerCase().replace(/(?:^|\s)\S/g, function(a) {
                    return a.toUpperCase();
                });

                const payload = {
                    number: number,
                    code: code,
                    name: name,
                    emergency: emergency
                }

                console.log(payload);

                $.ajax({
                    type: 'POST',
                    url: '../../Controller/Hotlines/addhotline.php',
                    data: payload,
                    success: function(response) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Success!',
                            text: 'Hotline added successfully!',
                            willClose: function() {
                                $('#hotlineModal').modal('hide'); // Close modal
                                var update = $('#hotlines').DataTable().ajax.reload();
                            }
                        });
                    },
                    error: function(xhr, status, error) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'Something went wrong!',
                        });
                        console.error("AJAX request failed:", error);
                    }
                });
            });


        });

        function view_hotline(id) {
            var rpid = $('#hotlineid').val();
            var number = $("#editnumberID").val();
            var code = $("#editdispatchID").val();
            var name = $("#editorgnameID").val();
            $.post('../../Controller/Hotlines/viewhotline.php', {
                id: id
            }, function(data,
                status) {
                var fetchedData = JSON.parse(data);
                $('#hotlineid').val(fetchedData.Id);
                $('#editnumberID').val(fetchedData.HotlineNumber);
                $("#editdispatchID").val(fetchedData.Dispatcher_Code);
                $("#editorgnameID").val(fetchedData.Name);
                $('#editemergencyID').val(fetchedData.Category);

            });
            $('#viewhotlineModal').modal("show");
        }

        function updateHotline() {
            var number = $("#editnumberID").val();
            var code = $("#editdispatchID").val();
            var name = $("#editorgnameID").val();
            var rpid = $("#hotlineid").val();

            $.post("../../Controller/Hotlines/edithotline.php", {
                name: name,
                code: code,
                number: number,
                id: rpid
            }, function(data, status) {
                var jsons = JSON.parse(data);
                status = jsons.status;
                Swal.fire({
                    title: 'Record Updated!',
                    icon: 'success',
                    showConfirmButton: false,
                    timer: 1000
                });
                var update = $('#hotlines').DataTable().ajax.reload();
                $('#viewhotlineModal').modal("hide");
            });
        }

        function delete_hotline(id) {
            Swal.fire({
                title: 'Are you sure?',
                text: 'You are about to delete this hotline record.',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    // User confirmed deletion
                    $.ajax({
                        url: "../../Controller/Hotlines/deletehotline.php",
                        type: 'post',
                        data: {
                            id: id
                        },
                        success: function(data, status) {
                            var json = JSON.parse(data);
                            status = json.status;
                            Swal.fire({
                                title: 'Record Deleted!',
                                text: 'The hotline record has been successfully deleted.',
                                icon: 'success',
                            });
                            $('#hotlines').DataTable().ajax.reload();
                        }
                    });
                }
            });
        }
    </script>

</body>

</html>