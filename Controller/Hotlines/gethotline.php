<?php
session_start();
include '../../includes/config.php';

$pdo = Database::connection();

$data = [];
$count = 0;

if (isset($_SESSION['type'])) {
    // If the 'type' is set in the session
    $type = $_SESSION['type'];

    // Convert 'type' to sentence case
    $sentenceCaseType = ucwords(strtolower($type));

    $stmt = $pdo->prepare('SELECT * FROM respondent WHERE Category = :Category');
    $stmt->bindParam(':Category', $sentenceCaseType);
    $stmt->execute();

    if ($stmt->execute()) {
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $count++;
            $subarray = [
                '<td>' . $count . '</td>',
                '<td>' . $row['Dispatcher_Code'] . '</td>',
                '<td>' . $row['Name'] . '</td>',
                '<td>' . $row['HotlineNumber'] . '</td>',
                '<td>' . $row['Category'] . '</td>',
                '<td>
                <button class="btn btn-primary" onclick="view_hotline(' . $row['Id'] . ')"><i class="nav-icon fas fa-edit"></i></button>
                <button class="btn btn-danger" onclick="delete_hotline(' . $row['Id'] . ')"><i class="nav-icon fas fa-trash"></i></button>
                </td>',
            ];
            $data[] = $subarray;
        }
    }

    $output = [
        'data' => $data,
    ];

    echo json_encode($output);
} else {
    echo json_encode(['message' => 'Department not Found'], 404);
}
