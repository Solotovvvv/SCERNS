<script src="https://js.pusher.com/7.0/pusher.min.js"></script>
<nav class="main-header navbar navbar-expand navbar-white navbar-light">
    <ul class="navbar-nav">
        <li class="nav-item">
            <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
        </li>
    </ul>
</nav>
<aside class="main-sidebar sidebar-dark-primary bg-color1 elevation-4">


    <a href="#" class="brand-link">
        <img src="../../img/images/orig-logo.png" alt="AdminLTE Logo" class="brand-image img-circle elevation-3" style="opacity: 0.8" />
        <span class="brand-text font-weight-light">SCERNS</span>
    </a>

    <div class="sidebar">
        <div class="user-panel mt-3 pb-3 mb-3 d-flex">
            <div class="user-panel mt-3 pb-3 mb-3 d-flex">
                <div class="info">
                    <a href="#" class="text-center">Respondent</a>
                    <a href="#" class="d-block">
                        <?php echo strtoupper($_SESSION['fullname']) ?>
                    </a>
                </div>
            </div>
        </div>

        <nav class="mt-2">
            <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">

                <li class="nav-item">
                    <a href="IncidentReport.php" class="nav-link <?php echo basename($_SERVER['PHP_SELF']) == 'IncidentReport.php' ? 'active' : ''; ?>">
                        <i class="nav-icon fas fa-chart-bar mr-3"></i>
                        <p>Incidents</p> <span class="position-absolute top-6 start-500 translate-middle badge rounded-pill bg-danger" id="report_notifs" style="margin-left: 5rem;"></span>
                    </a>
                </li>

                <li class="nav-item">
                    <a href="history.php" class="nav-link <?php echo basename($_SERVER['PHP_SELF']) == 'history.php' ? 'active' : ''; ?>">
                        <i class="nav-icon fas fa-history mr-3"></i>
                        <p>History</p>
                    </a>
                </li>

                <li class="nav-item">
                    <a href="respondent_hotlines.php" class="nav-link <?php echo basename($_SERVER['PHP_SELF']) == 'respondent_hotlines.php' ? 'active' : ''; ?>">
                        <i class="nav-icon fas fa-phone-alt mr-3"></i>
                        <p>Contacts / Hotlines</p>
                    </a>
                </li>

                <li class="nav-item">
                    <a href="profile.php" class="nav-link <?php echo basename($_SERVER['PHP_SELF']) == 'profile.php' ? 'active' : ''; ?>">
                        <i class="nav-icon fas fa-user mr-3"></i>
                        <p>Profile</p>
                    </a>
                </li>

                <li class="nav-item">
                    <a href="../../logout.php" class="nav-link text-danger <?php echo basename($_SERVER['PHP_SELF']) == '../../logout.php' ? 'active' : ''; ?>">
                        <i class="nav-icon fas fa-power-off mr-3"></i>
                        <p>Logout</p>
                    </a>
                </li>
            </ul>
        </nav>

    </div>
</aside>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    var pusher = new Pusher('b26a50e9e9255fc95c8f', {
        cluster: 'ap1',
        encrypted: true
    });

    var channel = pusher.subscribe('Scerns');
    channel.bind('new-report', function(data) {
        // Call your AJAX function
        sendReportData();
    });

    // Define your AJAX function
    function sendReportData() {
        $.ajax({
            url: "../../Controller/Admin/get_notif_respondent.php",
            method: "GET",
            success: function(response) {
                // Update report_notifs element with received data
                if (response === '0') {
                    $('#report_notifs').hide();
                } else {
                    $('#report_notifs').text(response).show();
                }
            },
            error: function(xhr, status, error) {
                console.error("Error occurred while fetching data:", error);
            }
        });
    }



    $(document).ready(function() {
        sendReportData();
    })
</script>