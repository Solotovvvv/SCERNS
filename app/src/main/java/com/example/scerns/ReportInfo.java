package com.example.scerns;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReportInfo extends AppCompatActivity {

    private String address, landmark, level, type;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_info);

        address = getIntent().getStringExtra("address");
        landmark = getIntent().getStringExtra("landmark");
        type = getIntent().getStringExtra("emergencyType");
        level = getIntent().getStringExtra("level");

        if (address != null) {
            TextView textViewAddress = findViewById(R.id.textViewAddress);
            String addressLabel = "Address: " + address;
            textViewAddress.setText(addressLabel);

            // Initialize the map view
            Configuration.getInstance().load(getApplicationContext(), getSharedPreferences("osmdroid", MODE_PRIVATE));
            mapView = findViewById(R.id.mapView);
            mapView.setTileSource(TileSourceFactory.MAPNIK);
            mapView.setMultiTouchControls(true);

            // Geocode the address to get its location
            new GeocodeTask().execute(address);
        }

        if (type != null) {
            TextView textViewType = findViewById(R.id.textViewType);
            String typeLabel = "Type: " + type;
            textViewType.setText(typeLabel);
        }

        if (landmark != null) {
            TextView textViewLandmark = findViewById(R.id.textViewLandmark);
            String landmarkLabel = "Landmark: " + landmark;
            textViewLandmark.setText(landmarkLabel);
        }

        if (level != null) {
            TextView textViewLevel = findViewById(R.id.textViewLevel);
            String levelLabel = "Level: " + level;
            textViewLevel.setText(levelLabel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources when the activity is destroyed
        if (mapView != null) {
            mapView.onDetach();
        }
    }

    // AsyncTask to fetch latitude and longitude from Nominatim API
    private class GeocodeTask extends AsyncTask<String, Void, GeoPoint> {

        @Override
        protected GeoPoint doInBackground(String... params) {
            String address = params[0];
            try {
                URL url = new URL("https://nominatim.openstreetmap.org/search?format=json&q=" + address);

                // Open connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read response
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                // Parse JSON response
                JSONArray jsonArray = new JSONArray(response.toString());
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    double lat = jsonObject.getDouble("lat");
                    double lon = jsonObject.getDouble("lon");
                    return new GeoPoint(lat, lon); // Return the obtained GeoPoint
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(GeoPoint result) {
            if (result != null) {
                // Set map's center to the address location
                mapView.getController().setCenter(result);

                // Adjust zoom level to fit the pin on the map
                adjustZoomToAddress(result);

                // Add a marker to indicate the address location
                Marker marker = new Marker(mapView);
                marker.setPosition(result);
                mapView.getOverlays().add(marker);
            }
        }

        private void adjustZoomToAddress(GeoPoint addressLocation) {
            // Zoom level adjustment based on address location
            final int zoomLevel = 18;

            // Set the zoom level and center of the map
            mapView.getController().setZoom(zoomLevel);
            mapView.getController().setCenter(addressLocation);
        }
    }
}