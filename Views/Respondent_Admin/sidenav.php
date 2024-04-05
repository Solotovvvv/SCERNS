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
                        <p>Incidents</p>
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