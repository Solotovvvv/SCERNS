
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
                    <a href="#" class="text-center">Admin</a>
                    <a href="#" class="d-block">
                        <?php echo strtoupper($_SESSION['fullname']) ?>
                    </a>
                </div>
            </div>
        </div>

        <nav class="mt-2">
            <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
                <li class="nav-item">
                    <a href="#" class="nav-link <?php echo (basename($_SERVER['PHP_SELF']) == 'Admin_Account.php' || basename($_SERVER['PHP_SELF']) == 'Admin_Respondent.php') ? 'active' : ''; ?>">
                        <i class="nav-icon fas fa-user mr-3"></i>
                        <p>Admin Account</p>
                        <i class="right fas fa-angle-left"></i>
                    </a>
                    <ul class="nav nav-treeview">
                   
                    <li class="nav-item">
                            <a href="Admin_Account.php" class="nav-link <?php echo basename($_SERVER['PHP_SELF']) == 'UserAccount.php' ? 'active' : ''; ?>">
                                <p>User Accounts</p>
                            </a>
                        </li>


                        <li class="nav-item">
                            <a href="Admin_Account.php" class="nav-link <?php echo basename($_SERVER['PHP_SELF']) == 'Admin_Account.php' ? 'active' : ''; ?>">
                                <p>Admin Account</p>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="Admin_Respondent.php" class="nav-link <?php echo basename($_SERVER['PHP_SELF']) == 'Admin_Respondent.php' ? 'active' : ''; ?>">
                                <p> Respondent Account</p>
                            </a>
                        </li>
                    </ul>
                </li>

                <li class="nav-item">
                    <a href="reports.php" class="nav-link <?php echo basename($_SERVER['PHP_SELF']) == 'reports.php' ? 'active' : ''; ?>">
                        <i class="nav-icon fas fa-graduation-cap mr-3"></i>
                        <p>Reports</p>
                    </a>
                </li>

                <li class="nav-item">
                    <a href="contacts.php" class="nav-link <?php echo basename($_SERVER['PHP_SELF']) == 'contacts.php' ? 'active' : ''; ?>">
                        <i class="nav-icon fas fa-calendar-alt mr-3"></i>
                        <p>Contact List</p>
                    </a>
                </li>
               
                <li class="nav-item">
                    <a href="history.php" class="nav-link <?php echo basename($_SERVER['PHP_SELF']) == 'history.php' ? 'active' : ''; ?>">
                        <i class="nav-icon fas fa-list mr-3"></i>
                        <p>History</p>
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