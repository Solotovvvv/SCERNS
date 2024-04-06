<?php
$server = "localhost"; //server name
$user = "gmylcsnm_bsit4buser";        //user name ytoovumw_bscs3a
$pass = "2+_hj@+?DWQ?";            //user password kaAGi]gz8H2*
$dbname = "gmylcsnm_bsit4b"; //database name


$conn = new mysqli($server, $user, $pass, $dbname);
if ($conn->connect_error) {
    die('Connection Failed' . $conn->connect_error);
}

class Database
{
    private static $host = "localhost";
    private static $dbname = "gmylcsnm_bsit4b";
    private static $user = "gmylcsnm_bsit4buser";
    private static $pass = "2+_hj@+?DWQ?";
    // private static $host = "localhost";
    // private static $dbname = "ucc_admission";
    // private static $user = "ucc_admin";
    // private static $pass = "e0pgATND&fj{";

    public static function connection()
    {
        try {
            date_default_timezone_set('Asia/Manila');
            $pdo = new PDO('mysql:host=' . self::$host . ';dbname=' . self::$dbname, self::$user, self::$pass);
            $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
            return $pdo;
        } catch (PDOException $e) {
            print "Error!: " . $e->getMessage() . "<br/>";
            die();
        }
    }
}
