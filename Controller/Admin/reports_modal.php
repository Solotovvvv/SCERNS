<?php
session_start();
include '../../includes/config.php';

// Check if the request method is POST
if ($_SERVER["REQUEST_METHOD"] === "POST") {
    // Check if 'id' is set in POST data
    if (isset($_POST['id'])) {
        // Get database connection
        $pdo = Database::connection();

        // Prepare SQL query to retrieve report data
        $select_query = "SELECT r.*, rd.Dispatcher_Code, rd.Name, rd.Category, ud.Fullname, ud.Phone, ud.Email FROM scerns_reports AS r LEFT JOIN scerns_respondents AS rd ON r.Dispatcher_Id = rd.Id INNER JOIN scerns_user_details AS ud ON r.User_id = ud.User_id WHERE r.Id = :id";
        $stmt = $pdo->prepare($select_query);

        // Bind parameters and execute query
        $id = $_POST['id'];
        $stmt->bindParam(':id', $id, PDO::PARAM_INT);
        $stmt->execute();

        // Fetch data
        $data = $stmt->fetch(PDO::FETCH_ASSOC);

        // Check if data was retrieved successfully
        if ($data) {
            // Get the latitude and longitude from OpenStreetMap
            $address = $data['Address'];
            $nom_url = "https://nominatim.openstreetmap.org/search?format=json&q=" . urlencode($address);

            // Create a stream context with custom headers
            $context = stream_context_create(array(
                'http' => array(
                    'header' => "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36"
                )
            ));

            // Fetch data using file_get_contents with the created context
            $nom_response = file_get_contents($nom_url, false, $context); // Suppress warnings
            if ($nom_response !== false) {
                $coordinates = json_decode($nom_response, true);
                if (!empty($coordinates)) {
                    $data['latitude'] = $coordinates[0]['lat'];
                    $data['longitude'] = $coordinates[0]['lon'];
                } else {
                    $data['latitude'] = null;
                    $data['longitude'] = null;
                }
            } else {
                // Error fetching coordinates
                $data['latitude'] = null;
                $data['longitude'] = null;
                $data['error'] = "Failed to fetch coordinates: HTTP request failed";
            }

            // Send JSON response
            echo json_encode($data);
        } else {
            // No data found for the given ID
            echo json_encode(["error" => "No data found for the given ID"]);
        }
    } else {
        // 'id' parameter is missing in POST data
        echo json_encode(["error" => "ID parameter is missing"]);
    }
} else {
    // Invalid request method
    echo json_encode(["error" => "Invalid request method"]);
}
