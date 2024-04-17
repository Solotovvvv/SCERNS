<?php
include '../../includes/config.php';

function toAddress($latitude, $longitude) {
        // $api_url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=$latitude&lon=$longitude";
    $api_url = "https://nominatim.openstreetmap.org/reverse.php?lat=$latitude&lon=$longitude&zoom=18&format=jsonv2";
    
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $api_url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_USERAGENT, 'SCERNS');
    
    $response = curl_exec($ch);
    $http_status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    
    curl_close($ch);
    
    if ($http_status == 200) {
        $data = json_decode($response, true);
        
        if (isset($data['display_name'])) {
            return $data['display_name'];
        } else {
            return "Address not found for the given coordinates.";
        }
    } else {
        return "Failed to fetch data from OpenStreetMap API. HTTP Status: $http_status";
    }
}

// Check if the request method is POST and required parameters are set
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['userID']) && isset($_POST['TypeOfEmergency']) && isset($_POST['latitude']) && isset($_POST['longitude'])) {
    // Declarations
    $date = date('Y-m-d H:i:s'); 
    $status = 'Pending';
    $remarks = "Sent by the user through emergency gesture."; 
    $role = "Urgent";
    $level = 3; 
    $landmark = null; 
    $lat = $_POST['latitude'];
    $long = $_POST['longitude'];
    
    $lat = strval($lat);
    $long = strval($long);
    
 

    $user_id = $_POST['userID'];
    $emergency_type = $_POST['TypeOfEmergency'];
    $address = toAddress($lat, $long);

    $user_id = mysqli_real_escape_string($conn, $user_id);
    $emergency_type = mysqli_real_escape_string($conn, $emergency_type);
    $address = mysqli_real_escape_string($conn, $address);

    $query = "INSERT INTO reports (User_ID, Role, TypeOfEmergency, Address, Landmark, Level, Date, Status, Remarks) 
            VALUES ('$user_id', '$role', '$emergency_type', '$address', '$landmark', '$level', '$date', '$status', '$remarks')";

    if (mysqli_query($conn, $query)) {
        echo json_encode(['message' => 'Report submitted successfully.', 'success' => true]);
    } else {
        echo json_encode(['message' => mysqli_error($conn), 'success' => false]);
    }
} else {
    echo json_encode(['message' => "Invalid Request.", 'success' => false]);
}
?>
