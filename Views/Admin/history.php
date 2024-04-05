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
    <title>History</title>
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
</head>

<body class="hold-transition sidebar-mini layout-fixed">
    <div class="wrapper">
        <?php include 'sidenav.php'; ?>

        <div class="content-wrapper">
            <div class="content-header">
                <div class="container-fluid">
                    <h1 class="m-0">History</h1>
                </div>
            </div>

            <section class="content">
                <div class="container-fluid">
                    <div class="card p-3">

                        <div class="input-group mb-3">
                            <table>
                                <tr>

                                    <td><input type="text" id="datepicker-start" class="form-control" placeholder="Select Start Date"></td>
                                    <td><input type="text" id="datepicker-end" class="form-control" placeholder="Select End Date"></td>
                                    <td><button id="filterBtn" class="btn bg-color2">FILTER</button></td>

                                    <td style="text-align: right; padding-left: 750px;"><button class="btn bg-color2 print-btn">PRINT REPORT</button></td>


                                </tr>

                            </table>
                        </div>

                        <div class="table-responsive card p-3">
                            <table id="history_dt" class="table table-striped table-bordered" style="width: 100%">
                                <thead>
                                    <tr>
                                        <th>NO.</th>
                                        <th>NAME</th>
                                        <th>Dispatcher Code</th>
                                        <th>Type Of Emergency</th>
                                        <th>Level</th>
                                        <th>Date</th>
                                        <th>STATUS</th>
                                    </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <!-- Include necessary libraries -->
    <script src="https://adminlte.io/themes/v3/plugins/jquery/jquery.min.js">
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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap-datepicker@1.9.0/dist/js/bootstrap-datepicker.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.66/pdfmake.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.66/vfs_fonts.js"></script>

    <!-- Your script -->
    <script>
        var table; // Declare table variable in the outer scope

        $(document).ready(function() {
            table = $('#history_dt').DataTable({
                'serverside': true,
                'processing': true,
                'paging': true,
                "columnDefs": [{
                    "className": "dt-center",
                    "targets": "_all"
                }],
                'ajax': {
                    'url': 'tables/history_tbl.php',
                    'type': 'get', // Change to 'get' since you are using $_GET in your PHP script
                    'data': function(d) {
                        // Pass start and end dates as parameters to the PHP script
                        d.startDate = $('#datepicker-start').val();
                        d.endDate = $('#datepicker-end').val();
                    }
                },
                'columns': [{
                        'data': '0'
                    },
                    {
                        'data': '1'
                    },
                    {
                        'data': '2'
                    },
                    {
                        'data': '3'
                    },
                    {
                        'data': '4'
                    },
                    {
                        'data': '5'
                    },
                    {
                        'data': '6'
                    },
                ],
            });

            table.on('init', function() {
                $("#datepicker-start, #datepicker-end").datepicker({
                    autoclose: true,
                    todayHighlight: true,
                    format: 'yyyy-mm-dd',
                    orientation: 'bottom',
                    clearBtn: true,
                }).on('clearDate', function() {
                    table.clear().draw();
                });

                $('.print-btn').on('click', function() {
                    var startDate = $('#datepicker-start').val();
                    var endDate = $('#datepicker-end').val();

                    if (startDate && endDate) {
                        generatePdfReport(startDate, endDate);
                    } else {
                        generatePdfReport();
                    }
                });
            });


            // Handle filter button click
            $('#filterBtn').on('click', function() {
                var startDate = $('#datepicker-start').val();
                var endDate = $('#datepicker-end').val();

                // Update DataTable with new date range
                table.clear().draw();
                table.ajax.url('./tables/history_tbl.php?startDate=' + startDate + '&endDate=' + endDate).load();
            });

            // (rest of the document.ready code)
        });

        function generatePdfReport(startDate, endDate) {
            var columns = ['NO.', 'NAME', 'Dispatcher Code', 'Type Of Emergency', 'Level', 'Date', 'Status'];


            // Fetch data from DataTables API
            var data = table.rows().data().toArray();

            // Function to strip HTML tags from the content
            function stripHtmlTags(html) {
                var doc = new DOMParser().parseFromString(html, 'text/html');
                return doc.body.textContent || "";
            }

            // Extract values from HTML table cells and convert to plain array
            var pdfData = data.map(row =>
                row.map(cell => stripHtmlTags(cell))
            );

            // Add columns to the beginning of the data array
            pdfData.unshift(columns);



            // Create a document definition
            var docDefinition = {
                content: [{
                        text: 'History Report',
                        style: 'header',
                        alignment: 'center'
                    },
                    {
                        table: {
                            body: pdfData,
                            widths: Array(pdfData[0].length).fill('*'),

                        },
                        margin: [0, 0, 0, 0],
                        alignment: 'center'
                    }
                ],
                styles: {
                    header: {
                        fontSize: 18,
                        bold: true,
                        margin: [0, 0, 0, 10]
                    }
                }
            };

            // Generate the PDF
            pdfMake.createPdf(docDefinition).download('History.pdf');
        }
    </script>

</body>


</html>