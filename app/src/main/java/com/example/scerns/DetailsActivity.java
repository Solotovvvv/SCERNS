package com.example.scerns;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class DetailsActivity extends AppCompatActivity {

    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Toast.makeText(this, "User ID:" + userId, Toast.LENGTH_SHORT).show();
        }

        if (getIntent().hasExtra("jsonData") && getIntent().hasExtra("userId")) {
            String jsonDataString = getIntent().getStringExtra("jsonData");
            try {
                JSONObject jsonObject = new JSONObject(jsonDataString);
                setupViews(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(this, "Required data not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the DetailsActivity and return to the previous fragment
            }
        });
    }

    private void setupViews(JSONObject jsonObject) {
        TextView textViewType = findViewById(R.id.textViewType);
        TextView textViewAddress = findViewById(R.id.textViewAddress);
        TextView textViewLandmark = findViewById(R.id.textViewLandmark);
        TextView textViewLevel = findViewById(R.id.textViewLevel);
        TextView textViewStatus = findViewById(R.id.textViewStatus);
        LinearLayout loadingLayout = findViewById(R.id.loadingLayout);
        ProgressBar loadingProgressBar = findViewById(R.id.loadingProgressBar);
        TextView textViewWaiting = findViewById(R.id.textViewWaiting);
        MapView mapView = findViewById(R.id.mapView);

        // Set data to TextViews
        textViewType.setText("Emergency Type: " + jsonObject.optString("TypeOfEmergency", ""));
        textViewAddress.setText("Address: " + jsonObject.optString("Address", ""));
        textViewLandmark.setText("Landmark: " + jsonObject.optString("Landmark", ""));
        textViewLevel.setText("Level: " + jsonObject.optString("Level", ""));
        textViewStatus.setText("Status: " + jsonObject.optString("Status", ""));

        // Handle visibility of loading indicators based on status
        String status = jsonObject.optString("Status", "");
        if (status.equalsIgnoreCase("Pending")) {
            loadingLayout.setVisibility(View.VISIBLE);
            loadingProgressBar.setVisibility(View.VISIBLE);
            textViewWaiting.setVisibility(View.VISIBLE);
        } else {
            loadingLayout.setVisibility(View.GONE);
            loadingProgressBar.setVisibility(View.GONE);
            textViewWaiting.setVisibility(View.GONE);
        }

        // Load map
        String address = jsonObject.optString("Address", "");
        String mapUrl = "https://nominatim.openstreetmap.org/search?format=json&q=" + address;
        VolleyRequestManager volleyRequestManager = new VolleyRequestManager(this);
        volleyRequestManager.get(mapUrl, new VolleyRequestManager.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        JSONObject result = jsonArray.getJSONObject(0);
                        double lat = result.getDouble("lat");
                        double lon = result.getDouble("lon");
                        GeoPoint point = new GeoPoint(lat, lon);

                        mapView.getController().setZoom(19);
                        mapView.getController().setCenter(point);

                        Marker marker = new Marker(mapView);
                        marker.setPosition(point);
                        mapView.getOverlays().add(marker);
                        mapView.invalidate();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                Toast.makeText(DetailsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


}

