<?php
// Include your database configuration file
include '../../includes/config.php';

// Establish database connection
$pdo = Database::connection();

// Prepare SQL query to fetch all respondents
$select_query = "SELECT Id, Dispatcher_Code, Name, HotlineNumber, Category FROM scerns_respondents";
$stmt = $pdo->query($select_query);

// Fetch data
$data = $stmt->fetchAll(PDO::FETCH_ASSOC);

// Return data as JSON
echo json_encode($data);
