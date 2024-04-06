<?php
// Include your database configuration file
include '../../includes/config.php';

// Establish database connection
$pdo = Database::connection();

// Prepare SQL query to fetch the count of reports
$sql = "SELECT COUNT(*) AS count FROM `scerns_reports` WHERE Status != 'Arrived' ";

// Prepare the SQL statement
$stmt = $pdo->prepare($sql);

// Execute the prepared statement
$stmt->execute();

// Fetch the count
$count = $stmt->fetch(PDO::FETCH_ASSOC)['count'];

// Return count as JSON
echo json_encode($count);
