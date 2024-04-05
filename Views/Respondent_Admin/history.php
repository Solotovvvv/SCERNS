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

                                <td><input type="text" id="datepicker-start_respondent" class="form-control" placeholder="Select Start Date"></td>
                                <td><input type="text" id="datepicker-end_respondent" class="form-control" placeholder="Select End Date"></td>
                                <td><button id="filterBtn_respondent" class="btn bg-color2">FILTER</button></td>

                                <td style="text-align: right; padding-left: 750px;"><button class="btn bg-color2 print-btn_respondent">PRINT REPORT</button></td>


                            </tr>

                        </table>
                    </div>

                    <div class="table-responsive card p-3">
                        <table id="history_dt_respondent" class="table table-striped table-bordered" style="width: 100%">
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

    <?php include 'footers/footer.php' ?>
    <script>
        var table_respondent; // Declare table variable in the outer scope

        $(document).ready(function() {
            table_respondent = $('#history_dt_respondent').DataTable({
                'serverside': true,
                'processing': true,
                'paging': true,
                "columnDefs": [{
                    "className": "dt-center",
                    "targets": "_all"
                }],
                'ajax': {
                    'url': 'tables/history_tbl_respondent.php',
                    'type': 'get', // Change to 'get' since you are using $_GET in your PHP script
                    'data': function(d) {
                        // Pass start and end dates as parameters to the PHP script
                        d.startDate = $('#datepicker-start_respondent').val();
                        d.endDate = $('#datepicker-end_respondent').val();
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

            table_respondent.on('init', function() {
                $("#datepicker-start_respondent, #datepicker-end_respondent").datepicker({
                    autoclose: true,
                    todayHighlight: true,
                    format: 'yyyy-mm-dd',
                    orientation: 'bottom',
                    clearBtn: true,
                }).on('clearDate', function() {
                    table_respondent.clear().draw();
                });

                $('.print-btn_respondent').on('click', function() {
                    var startDate = $('#datepicker-start_respondent').val();
                    var endDate = $('#datepicker-end_respondent').val();

                    if (startDate && endDate) {
                        generatePdfReport(startDate, endDate);
                    } else {
                        generatePdfReport();
                    }
                });
            });


            // Handle filter button click
            $('#filterBtn_respondent').on('click', function() {
                var startDate = $('#datepicker-start_respondent').val();
                var endDate = $('#datepicker-end_respondent').val();

                // Update DataTable with new date range
                table_respondent.clear().draw();
                table_respondent.ajax.url('./tables/history_tbl_respondent.php?startDate=' + startDate + '&endDate=' + endDate).load();
            });

            // (rest of the document.ready code)
        });

        function generatePdfReport(startDate, endDate) {
            var columns = ['NO.', 'NAME', 'Dispatcher Code', 'Type Of Emergency', 'Level', 'Date', 'Status'];


            // Fetch data from DataTables API
            var data = table_respondent.rows().data().toArray();

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