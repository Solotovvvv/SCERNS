<?php include 'headers/header.php' ?>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
</head>

<body class="hold-transition sidebar-mini layout-fixed">
    <?php
    include 'sidenav.php';
    ?>

<body class="hold-transition sidebar-mini layout-fixed">
    <div class="wrapper">
        <?php include 'sidenav.php'; ?>
        <div class="content-wrapper">
            <div class="content-header">
                <div class="container">
                    <h1 class="m-0">Daily Reports</h1>
                </div>
            </div>
            <section class="content">
                <div class="container-fluid">
                 <div class="card p-3">
                    <div class="table-responsive card p-3">
                        <table id="reports_Dt_respondent" class="table table-striped table-bordered" style="width: 100%">
                            <thead>
                                <tr>
                                    <th>Type of Incident</th>
                                    <th>Date and Time</th>
                                    <th>Location</th>
                                    <th>Level</th>
                                    <th>Dispatcher Code</th>
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
    <div class="modal fade bd-example-modal-lg" id="reportModal_respondent" tabindex="-1" role="dialog" aria-labelledby="reportModalLabel_respondent" aria-hidden="true">
        <div class="modal-dialog modal-xl" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="reportModalLabel_respondent">Report Details</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">

                    <div class="container">
                        <div class="mb-1 text-center">
                            <h3>Type of Incident: <span id="type_respondent">Medic</span></h3>
                        </div>
                        <div id="map_respondent" style="height: 200px; width: 100%;" class="mb-3"></div>
                        <hr>
                        <div class="row">
                            <div class="col-md-7 text-center">
                                <h3 class="mb-3">Requester Info</h3>
                                <hr>
                                <div class="row">
                                    <div class="col text-center">
                                        <h4>Victim</h4>
                                    </div>
                                </div>
                                <div class="row justify-content-start">
                                    <div class="col-auto">
                                        <label for="">Name:</label>
                                        <span id="name_respondent"></span>
                                    </div>
                                </div>
                                <div class="row justify-content-start">
                                    <div class="col-auto">
                                        <label for="">Phone:</label>
                                        <span id="phone_respondent"></span>
                                    </div>
                                </div>
                                <div class="row justify-content-start">
                                    <div class="col-auto">
                                        <label for="">Email:</label>
                                        <span id="email_respondent"></span>
                                    </div>
                                </div>

                                <div class="row justify-content-start">
                                    <div class="col-auto">
                                        <label for="">Level:</label>
                                        <span id="level_respondent"></span>
                                    </div>
                                </div>
                                <div class="row justify-content-start">
                                    <div class="col-auto">
                                        <label for="">Date:</label>
                                        <span id="date_respondent"></span>
                                    </div>
                                </div>
                                <div class="row justify-content-start">
                                    <div class="col-auto">
                                        <label>Location:</label>
                                        <span id="location_respondent" style="font-size: 16px ;word-wrap: break-word;"></span>
                                    </div>
                                </div>


                                <div class="row">
                                    <div class="col-12">
                                        <h4>Remarks</h4>
                                        <textarea id="textarea1_respondent" class="form-control mb-2" rows="4" placeholder="Enter your text here"></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-5 text-center" style="border-left: 2px solid rgba(0, 0, 0, 0.2);">
                                <h3 class="mb-3">Assign Respondent</h3>
                                <hr>


                                <div class="form-group">
                                    <label for="departmentCombo_respondent">Department</label>
                                    <select class="form-control mb-2" id="departmentCombo_respondent">
                                        <option value="" selected disabled>Select Department</option>
                                        <option value="Fire">Fire</option>
                                        <option value="Crime">Crime</option>
                                        <option value="Medic">Medic</option>
                                        <option value="Natural Disaster">Natural Disaster</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="dispatcherCodeCombo_respondent">Dispatcher-Code</label>
                                    <select class="form-control mb-2" id="dispatcherCodeCombo_respondent">
                                        <option value="option1_respondent">Option 1</option>

                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="teamCombo_respondent">Team</label>
                                    <select class="form-control mb-2" id="teamCombo_respondent">
                                        <option value="option1_respondent">Option 1</option>

                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="statusCombo_respondent">Status</label>
                                    <select class="form-control mb-2" id="statusCombo_respondent">
                                        <option value="" selected disabled>Select Status</option>
                                        <option value="Arriving">Arriving</option>
                                        <option value="Enroute">Enroute</option>
                                        <option value="Arrived">Arrived</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" onclick="updateReport_respondent()">Save changes</button>
                        <input type="text" id="hiddendata_report_respondent">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <?php include 'footers/footer.php' ?>


    <script>
        let map_respondent;
        $(document).ready(function() {
            $('#reports_Dt_respondent').DataTable({
                'serverSide': true,
                'processing': true,
                'paging': true,
                "columnDefs": [{
                    "className": "dt-center",
                    "targets": "_all"
                }],
                'ajax': {
                    'url': 'tables/reports_tbl_respondent.php',
                    'type': 'post',
                },
                'columns': [{
                        'data': '0'
                    },
                    {
                        'data': '1'
                    }, // Date and Time
                    {
                        'data': '2'
                    }, // Location
                    {
                        'data': '3'
                    }, // Level
                    {
                        'data': '4'
                    }, // dispatcher
                    {
                        'data': '5'
                    }, // Status
                    {
                        'data': '6'
                    } // Actions
                ],
                'order': [
                    [3, 'desc']
                ],
                'drawCallback': function(settings) {
                    // Apply background color based on level
                    $('#reports_Dt_respondent tbody tr').each(function() {
                        var level = $(this).find('td:eq(3)').text(); // Get the text from the 4th column (index 3)
                        var backgroundColor;
                        switch (level) {
                            case '1':
                                backgroundColor = '#f8bc4c';
                                break;
                            case '2':
                                backgroundColor = '#f76707';
                                break;
                            case '3':
                                backgroundColor = '#c92a2a';
                                break;
                            default:
                                backgroundColor = '';
                        }
                        $(this).find('td').css('background-color', backgroundColor);
                    });
                }
            });

            $('#reportModal_respondent').on('shown.bs.modal', function() {
                // Remove the existing map
                if (map_respondent) {
                    map_respondent.remove();
                }

                const userids_respondent = $('#hiddendata_report_respondent').data('userids');

                // Check if latitude and longitude are available
                if (userids_respondent && userids_respondent.latitude && userids_respondent.longitude) {
                    map_respondent = L.map('map_respondent').setView([userids_respondent.latitude, userids_respondent.longitude], 13);
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    }).addTo(map_respondent);
                    L.marker([userids_respondent.latitude, userids_respondent.longitude]).addTo(map_respondent)
                        .bindPopup(userids_respondent.Address)
                        .openPopup();
                } else {
                    // Handle the case when latitude and longitude are not available
                    console.log("Latitude and longitude not available");
                }
            });
            $('#reportModal_respondent').on('hidden.bs.modal', function() {
                resetComboBoxes_respondent();
            });


            $('#departmentCombo_respondent').on('change', function() {
                var category = $(this).val();
                if (category) {
                    $.ajax({
                        url: '../../Controller/Admin/fetch_respondents.php',
                        type: 'POST',
                        data: {
                            category: category
                        },
                        dataType: 'json',
                        success: function(data) {
                            var dispatcherCodeCombo_respondent = $('#dispatcherCodeCombo_respondent');
                            dispatcherCodeCombo_respondent.empty();
                            dispatcherCodeCombo_respondent.append($('<option>', {
                                value: '',
                                text: 'Select Dispatcher-Code',
                                disabled: true,
                                selected: true
                            }));
                            $.each(data, function(index, respondent) {
                                dispatcherCodeCombo_respondent.append($('<option>', {
                                    value: respondent.Dispatcher_Code,
                                    text: respondent.Dispatcher_Code
                                }));
                            });
                        }
                    });
                } else {
                    $('#dispatcherCodeCombo_respondent').empty();
                    $('#dispatcherCodeCombo_respondent').append($('<option>', {
                        value: '',
                        text: 'Select Dispatcher-Code',
                        disabled: true,
                        selected: true
                    }));
                    $('#teamCombo_respondent').empty();
                    $('#teamCombo_respondent').append($('<option>', {
                        value: '',
                        text: 'Select Team',
                        disabled: true,
                        selected: true
                    }));
                }
            });

            $('#dispatcherCodeCombo_respondent').on('change', function() {
                var dispatcherCodeId_respondent = $(this).val();
                console.log(dispatcherCodeId_respondent)
                if (dispatcherCodeId_respondent) {
                    $.ajax({
                        url: '../../Controller/Admin/fetch_respondents.php',
                        type: 'POST',
                        data: {
                            dispatcherCodeId: dispatcherCodeId_respondent
                        },
                        dataType: 'json',
                        success: function(data) {
                            var teamCombo_respondent = $('#teamCombo_respondent');
                            teamCombo_respondent.empty();
                            teamCombo_respondent.append($('<option>', {
                                value: '',
                                text: 'Select Team',
                                disabled: true,
                                selected: true
                            }));
                            teamCombo_respondent.append($('<option>', {
                                value: data,
                                text: data
                            }));
                        }
                    });
                } else {
                    $('#teamCombo_respondent').empty();
                    $('#teamCombo_respondent').append($('<option>', {
                        value: '',
                        text: 'Select Team',
                        disabled: true,
                        selected: true
                    }));
                }
            });

            $('#departmentCombo_respondent, #dispatcherCodeCombo_respondent').on('change', function() {
                resetTeamCombo_respondent(); // Reset team combo on change of either department or dispatcher code
            });

        });

        function resetTeamCombo_respondent() {
            $('#teamCombo_respondent').empty().append($('<option>', {
                value: '',
                text: 'Select Team',
                disabled: true,
                selected: true
            }));
        }

        function view_reports_respondent(id) {
            $('#hiddendata_report_respondent').val(id);
            $.post("../../Controller/Admin/reports_modal.php", {
                id: id
            }, function(data, status) {
                var userids_respondent = JSON.parse(data);
                $('#type_respondent').text(userids_respondent.TypeOfEmergency);
                $('#name_respondent').text(userids_respondent.Fullname);
                $('#phone_respondent').text(userids_respondent.Phone);
                $('#email_respondent').text(userids_respondent.Email);
                $('#level_respondent').text(userids_respondent.Level);
                $('#date_respondent').text(userids_respondent.Date);
                $('#location_respondent').text(userids_respondent.Address);
                $('#textarea1_respondent').val(userids_respondent.Remarks);

                // Load respondents and populate dropdowns if Dispatcher_Id is not null
                if (userids_respondent.Dispatcher_Id !== null) {
                    loadRespondents_respondent(function() {
                        // Set values of dropdowns after loading respondents
                        $('#departmentCombo_respondent').val(userids_respondent.Category);
                        $('#dispatcherCodeCombo_respondent').val(userids_respondent.Dispatcher_Code);
                        $('#teamCombo_respondent').val(userids_respondent.Name);
                        $('#statusCombo_respondent').val(userids_respondent.Status);
                    });
                }

                // Store the userids data in the modal for later use
                $('#hiddendata_report_respondent').data('userids', userids_respondent);

                $('#reportModal_respondent').modal("show");
            });
        }


        function loadRespondents_respondent(callback) {
            $.ajax({
                url: '../../Controller/Admin/fetch_all_respondents.php',
                type: 'GET',
                dataType: 'json',
                success: function(data) {
                    // Populate dropdowns
                    populateDropdowns_respondent(data);

                    // Call the callback function if provided
                    if (typeof callback === 'function') {
                        callback();
                    }
                },
                error: function(xhr, status, error) {
                    console.error("Error fetching respondents: ", error);
                }
            });
        }

        function populateDropdowns_respondent(data) {
            // Populate Dispatcher-Code dropdown
            var dispatcherCodeCombo_respondent = $('#dispatcherCodeCombo_respondent');
            dispatcherCodeCombo_respondent.empty();
            dispatcherCodeCombo_respondent.append($('<option>', {
                value: '',
                text: 'Select Dispatcher-Code',
                disabled: true,
                selected: true
            }));
            $.each(data, function(index, respondent) {
                dispatcherCodeCombo_respondent.append($('<option>', {
                    value: respondent.Dispatcher_Code,
                    text: respondent.Dispatcher_Code
                }));
            });

            // Populate Team dropdown
            var teamCombo_respondent = $('#teamCombo_respondent');
            teamCombo_respondent.empty();
            teamCombo_respondent.append($('<option>', {
                value: '',
                text: 'Select Team',
                disabled: true,
                selected: true
            }));
            $.each(data, function(index, respondent) {
                teamCombo_respondent.append($('<option>', {
                    value: respondent.Name,
                    text: respondent.Name
                }));
            });
        }

        function resetComboBoxes_respondent() {
            $('#departmentCombo_respondent').val('');
            $('#dispatcherCodeCombo_respondent').val('');
            $('#teamCombo_respondent').val('');
            $('#statusCombo_respondent').val('');
        }

        function updateReport_respondent() {
            var dispatcher_respondent = $('#dispatcherCodeCombo_respondent').val();
            var status_respondent = $('#statusCombo_respondent').val();
            var id_respondent = $('#hiddendata_report_respondent').val();

            console.log(dispatcher_respondent);
            $.post("../../Controller/Admin/Update_Status.php", {
                id: id_respondent,
                dispatcher: dispatcher_respondent,
                status: status_respondent
            }, function(data, status) {
                var jsons_respondent = JSON.parse(data);
                status = jsons_respondent.status;
                if (status == 'success') {
                    Swal.fire({
                        title: 'Record Updated!',
                        icon: 'success',
                        showConfirmButton: false,
                        timer: 1000
                    });
                    var update_respondent = $('#reports_Dt_respondent').DataTable().ajax.reload();
                }
                $('#reportModal_respondent').modal("hide");
            });
        }
    </script>

</body>



</html>