package com.example.scerns;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
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

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_info);

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else{
            Toast.makeText(this, "User ID: " + userId, Toast.LENGTH_SHORT).show();
        }

        address = getIntent().getStringExtra("address");
        landmark = getIntent().getStringExtra("landmark");
        type = getIntent().getStringExtra("emergencyType");
        level = getIntent().getStringExtra("level");

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportInfo.this, MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout loadingLayout = findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);

        if (address != null) {
            TextView textViewAddress = findViewById(R.id.textViewAddress);
            String addressLabel = "Address: " + address;
            textViewAddress.setText(addressLabel);

            // Initialize the map view
            Configuration.getInstance().load(getApplicationContext(), getSharedPreferences("osmdroid", MODE_PRIVATE));
            mapView = findViewById(R.id.mapView);
            mapView.setTileSource(TileSourceFactory.MAPNIK);
            mapView.setMultiTouchControls(true);

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

    private class GeocodeTask extends AsyncTask<String, Void, GeoPoint> {

        @Override
        protected GeoPoint doInBackground(String... params) {
            String address = params[0];
            try {
                URL url = new URL("https://nominatim.openstreetmap.org/search?format=json&q=" + address);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                JSONArray jsonArray = new JSONArray(response.toString());
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    double lat = jsonObject.getDouble("lat");
                    double lon = jsonObject.getDouble("lon");
                    return new GeoPoint(lat, lon);
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

                adjustZoomToAddress(result);

                // Add a marker to indicate the address location
                Marker marker = new Marker(mapView);
                marker.setPosition(result);
                mapView.getOverlays().add(marker);
            }
        }

        private void adjustZoomToAddress(GeoPoint addressLocation) {
            final int zoomLevel = 18;
            mapView.getController().setZoom(zoomLevel);
            mapView.getController().setCenter(addressLocation);
        }
    }
}