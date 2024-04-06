<?php
// Include your database configuration file
session_start();
include '../../includes/config.php';

// Establish database connection
$pdo = Database::connection();

// Prepare SQL query to fetch the count of reports
$sql = "SELECT COUNT(*) AS count FROM `scerns_reports` WHERE Status != 'Arrived' AND `TypeOfEmergency` = :typeOfEmergency";

// Prepare the SQL statement
$stmt = $pdo->prepare($sql);

// Bind the session variable to the parameter
$stmt->bindParam(':typeOfEmergency', $_SESSION['type'], PDO::PARAM_STR);

// Execute the prepared statement
$stmt->execute();

// Fetch the count
$count = $stmt->fetch(PDO::FETCH_ASSOC)['count'];

// Return count as JSON
echo json_encode($count);
