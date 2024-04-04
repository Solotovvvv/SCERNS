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
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Daily Reports</title>
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

    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.7/css/dataTables.bootstrap4.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" />
    <link rel="stylesheet" href="../../dist/css/style.css">

    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />
</head>

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
                <div class="container">
                    <div class="table-responsive card p-3">
                        <table id="reports_Dt" class="table table-striped table-bordered text-center" style="width: 100%">
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
            </section>
        </div>
    </div>
    <div class="modal fade bd-example-modal-lg" id="reportModal" tabindex="-1" role="dialog" aria-labelledby="reportModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-xl" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="reportModalLabel">Report Details</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">

                    <div class="container">
                        <div class="mb-1 text-center">
                            <h3>Type of Incident: <span>Medic</span></h3>
                        </div>
                        <div id="map" style="height: 200px; width: 100%;" class="mb-3"></div>
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
                                        <span id="name"></span>
                                    </div>
                                </div>
                                <div class="row justify-content-start">
                                    <div class="col-auto">
                                        <label for="">Phone:</label>
                                        <span id="phone"></span>
                                    </div>
                                </div>
                                <div class="row justify-content-start">
                                    <div class="col-auto">
                                        <label for="">Email:</label>
                                        <span id="email"></span>
                                    </div>
                                </div>

                                <div class="row justify-content-start">
                                    <div class="col-auto">
                                        <label for="">Level:</label>
                                        <span id="level"></span>
                                    </div>
                                </div>
                                <div class="row justify-content-start">
                                    <div class="col-auto">
                                        <label for="">Date:</label>
                                        <span id="date"></span>
                                    </div>
                                </div>
                                <div class="row justify-content-start">
                                    <div class="col-auto">
                                        <label>Location:</label>
                                        <span id="location" style="font-size: 16px ;word-wrap: break-word;"></span>
                                    </div>
                                </div>


                                <div class="row">
                                    <div class="col-12">
                                        <h4>Remarks</h4>
                                        <textarea id="textarea1" class="form-control mb-2" rows="4" placeholder="Enter your text here"></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-5 text-center" style="border-left: 2px solid rgba(0, 0, 0, 0.2);">
                                <h3 class="mb-3">Assign Respondent</h3>
                                <hr>


                                <div class="form-group">
                                    <label for="departmentCombo">Department</label>
                                    <select class="form-control mb-2" id="departmentCombo">
                                        <option value="" selected disabled>Select Department</option>
                                        <option value="Fire">Fire</option>
                                        <option value="Crime">Crime</option>
                                        <option value="Medic">Medic</option>
                                        <option value="Natural Disaster">Natural Disaster</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="dispatcherCodeCombo">Dispatcher-Code</label>
                                    <select class="form-control mb-2" id="dispatcherCodeCombo">
                                        <option value="option1">Option 1</option>

                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="teamCombo">Team</label>
                                    <select class="form-control mb-2" id="teamCombo">
                                        <option value="option1">Option 1</option>

                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="statusCombo">Status</label>
                                    <select class="form-control mb-2" id="statusCombo">
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
                        <button type="button" class="btn btn-primary">Save changes</button>
                        <input type="text" id="hiddendata_report">
                    </div>
                </div>
            </div>
        </div>
    </div>



    <script src="https://adminlte.io/themes/v3/plugins/jquery/jquery.min.js"></script>
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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap-datepicker@1.9.0/dist/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
    <script>
        let map;
        $(document).ready(function() {
            $('#reports_Dt').DataTable({
                'serverSide': true,
                'processing': true,
                'paging': true,
                "columnDefs": [{
                    "className": "dt-center",
                    "targets": "_all"
                }],
                'ajax': {
                    'url': 'tables/reports_tbl.php',
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
                    $('#reports_Dt tbody tr').each(function() {
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

            $('#reportModal').on('shown.bs.modal', function() {
                // Remove the existing map
                if (map) {
                    map.remove();
                }

                const userids = $('#hiddendata_report').data('userids');

                // Check if latitude and longitude are available
                if (userids && userids.latitude && userids.longitude) {
                    map = L.map('map').setView([userids.latitude, userids.longitude], 13);
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    }).addTo(map);
                    L.marker([userids.latitude, userids.longitude]).addTo(map)
                        .bindPopup(userids.Address)
                        .openPopup();
                } else {
                    // Handle the case when latitude and longitude are not available
                    console.log("Latitude and longitude not available");
                }
            });
            $('#reportModal').on('hidden.bs.modal', function () {
        resetComboBoxes();
    });


            $('#departmentCombo').on('change', function() {
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
                            var dispatcherCodeCombo = $('#dispatcherCodeCombo');
                            dispatcherCodeCombo.empty();
                            dispatcherCodeCombo.append($('<option>', {
                                value: '',
                                text: 'Select Dispatcher-Code',
                                disabled: true,
                                selected: true
                            }));
                            $.each(data, function(index, respondent) {
                                dispatcherCodeCombo.append($('<option>', {
                                    value: respondent.Id,
                                    text: respondent.Dispatcher_Code
                                }));
                            });
                        }
                    });
                } else {
                    $('#dispatcherCodeCombo').empty();
                    $('#dispatcherCodeCombo').append($('<option>', {
                        value: '',
                        text: 'Select Dispatcher-Code',
                        disabled: true,
                        selected: true
                    }));
                    $('#teamCombo').empty();
                    $('#teamCombo').append($('<option>', {
                        value: '',
                        text: 'Select Team',
                        disabled: true,
                        selected: true
                    }));
                }
            });

            $('#dispatcherCodeCombo').on('change', function() {
                var dispatcherCodeId = $(this).val();
                if (dispatcherCodeId) {
                    $.ajax({
                        url: '../../Controller/Admin/fetch_respondents.php',
                        type: 'POST',
                        data: {
                            dispatcherCodeId: dispatcherCodeId
                        },
                        dataType: 'json',
                        success: function(data) {
                            var teamCombo = $('#teamCombo');
                            teamCombo.empty();
                            teamCombo.append($('<option>', {
                                value: '',
                                text: 'Select Team',
                                disabled: true,
                                selected: true
                            }));
                            teamCombo.append($('<option>', {
                                value: data,
                                text: data
                            }));
                        }
                    });
                } else {
                    $('#teamCombo').empty();
                    $('#teamCombo').append($('<option>', {
                        value: '',
                        text: 'Select Team',
                        disabled: true,
                        selected: true
                    }));
                }
            });

            $('#departmentCombo, #dispatcherCodeCombo').on('change', function() {
                resetTeamCombo(); // Reset team combo on change of either department or dispatcher code
            });

        });

        function resetTeamCombo() {
            $('#teamCombo').empty().append($('<option>', {
                value: '',
                text: 'Select Team',
                disabled: true,
                selected: true
            }));
        }

        function view_reports(id) {
            $('#hiddendata_report').val(id);
            $.post("../../Controller/Admin/reports_modal.php", {
                id: id
            }, function(data, status) {
                var userids = JSON.parse(data);
                $('#name').text(userids.Fullname);
                $('#phone').text(userids.Phone);
                $('#email').text(userids.Email);
                $('#level').text(userids.Level);
                $('#date').text(userids.Date);
                $('#location').text(userids.Address);
                $('#textarea1').val(userids.Remarks);

                // Load respondents and populate dropdowns if Dispatcher_Id is not null
                if (userids.Dispatcher_Id !== null) {
                    loadRespondents(function() {
                        // Set values of dropdowns after loading respondents
                        $('#departmentCombo').val(userids.TypeOfEmergency);
                        $('#dispatcherCodeCombo').val(userids.Dispatcher_Code);
                        $('#teamCombo').val(userids.Name);
                        $('#statusCombo').val(userids.Status);
                    });
                }

                // Store the userids data in the modal for later use
                $('#hiddendata_report').data('userids', userids);

                $('#reportModal').modal("show");
            });
        }


        function loadRespondents(callback) {
            $.ajax({
                url: '../../Controller/Admin/fetch_all_respondents.php',
                type: 'GET',
                dataType: 'json',
                success: function(data) {
                    // Populate dropdowns
                    populateDropdowns(data);

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

        function populateDropdowns(data) {
            // Populate Dispatcher-Code dropdown
            var dispatcherCodeCombo = $('#dispatcherCodeCombo');
            dispatcherCodeCombo.empty();
            dispatcherCodeCombo.append($('<option>', {
                value: '',
                text: 'Select Dispatcher-Code',
                disabled: true,
                selected: true
            }));
            $.each(data, function(index, respondent) {
                dispatcherCodeCombo.append($('<option>', {
                    value: respondent.Dispatcher_Code,
                    text: respondent.Dispatcher_Code
                }));
            });

            // Populate Team dropdown
            var teamCombo = $('#teamCombo');
            teamCombo.empty();
            teamCombo.append($('<option>', {
                value: '',
                text: 'Select Team',
                disabled: true,
                selected: true
            }));
            $.each(data, function(index, respondent) {
                teamCombo.append($('<option>', {
                    value: respondent.Name,
                    text: respondent.Name
                }));
            });
        }

        function resetComboBoxes() {
            $('#departmentCombo').val(null);
            $('#dispatcherCodeCombo').val(null);
            $('#teamCombo').val(null);
            $('#statusCombo').val(null);
        }
    </script>
</body>

</html>