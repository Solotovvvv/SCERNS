<?php
$server="localhost";//server name
$user="root";		//user name ytoovumw_bscs3a
$pass="";			//user password kaAGi]gz8H2*
$dbname="scerns_v2";//database name


$conn= new mysqli($server,$user,$pass,$dbname);
if($conn->connect_error){
	die('Connection Failed'.$conn->connect_error);
}

class Database {
    private static $host = "localhost";
    private static $dbname = "scerns_v2";
    private static $user = "root";
    private static $pass = "";
    // private static $host = "localhost";
    // private static $dbname = "ucc_admission";
    // private static $user = "ucc_admin";
    // private static $pass = "e0pgATND&fj{";

    public static function connection()
    {
        try {
            date_default_timezone_set('Asia/Manila');
            $pdo = new PDO('mysql:host='. self::$host .';dbname='.self::$dbname, self::$user, self::$pass);
            $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
            return $pdo;
        } catch (PDOException $e) {
            print "Error!: " . $e->getMessage() . "<br/>";
            die();
        }
    }
}



?>
