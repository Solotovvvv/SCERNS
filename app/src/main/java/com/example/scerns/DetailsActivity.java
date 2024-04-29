package com.example.scerns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class DetailsActivity extends AppCompatActivity {

    private int userId, reportId;
    private TextView textViewStatus, textViewDateTime;
    private Pusher pusher;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        reportId = getIntent().getIntExtra("Id", -1);

        if (reportId == -1) {
            Toast.makeText(this, "Report ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        PusherOptions options = new PusherOptions().setCluster("ap1").setEncrypted(true);
        pusher = new Pusher("b26a50e9e9255fc95c8f", options);
        pusher.connect();

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
                Intent intent = new Intent();
                intent.putExtra("userId", userId);
                intent.putExtra("Id", "");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        triggerPusherEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pusher != null) {
            pusher.disconnect();
        }
    }

    private void triggerPusherEvent() {
        Channel channel = pusher.subscribe("Scerns");

        SubscriptionEventListener eventListener = new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                try {
                    JSONObject eventData = new JSONObject(event.getData());
                    String status = eventData.getString("status");
                    String arrived_time = eventData.getString("arrived_time");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewStatus.setText("Status: " + status);
                            if (!status.equals("Pending")) {
                                LinearLayout loadingLayout = findViewById(R.id.loadingLayout);
                                loadingLayout.setVisibility(View.GONE);
                            }
                            textViewDateTime.setText("Completion Time: " + arrived_time);
                        }
                    });
                    Log.d("Pusher", "Received user-report event with status: " + status);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Pusher", "Error parsing event data: " + e.getMessage());
                }
            }
        };

        channel.bind(reportId + "-user-report", eventListener);

        pusher.getConnection().bind(ConnectionState.ALL, new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.d("Pusher", "State changed to: " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.e("Pusher", "Error: " + message);
            }
        });
    }

    private void setupViews(JSONObject jsonObject) {
        TextView textViewType = findViewById(R.id.textViewType);
        TextView textViewAddress = findViewById(R.id.textViewAddress);
        TextView textViewLandmark = findViewById(R.id.textViewLandmark);
        TextView textViewLevel = findViewById(R.id.textViewLevel);
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewDateTime = findViewById(R.id.textViewDateTime);
        LinearLayout loadingLayout = findViewById(R.id.loadingLayout);
        ProgressBar loadingProgressBar = findViewById(R.id.loadingProgressBar);
        TextView textViewWaiting = findViewById(R.id.textViewWaiting);
        mapView = findViewById(R.id.mapView);

        if (mapView != null) {
            mapView.setTileSource(TileSourceFactory.MAPNIK);
        } else {
            Log.e("DetailsActivity", "mapView is null");
        }

        textViewType.setText("Emergency Type: " + jsonObject.optString("TypeOfEmergency", ""));
        textViewAddress.setText("Address: " + jsonObject.optString("Address", ""));
        textViewLandmark.setText("Landmark: " + jsonObject.optString("Landmark", ""));
        textViewLevel.setText("Level: " + jsonObject.optString("Level", ""));
        textViewStatus.setText("Status: " + jsonObject.optString("Status", ""));
        textViewDateTime.setText("Completion Time: " + jsonObject.optString("Arrived_Time", ""));

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

                        if (mapView != null) {
                            mapView.getController().setZoom(19);
                            mapView.getController().setCenter(point);

                            Marker marker = new Marker(mapView);
                            marker.setPosition(point);
                            mapView.getOverlays().add(marker);
                            mapView.invalidate();
                        } else {
                            Log.e("DetailsActivity", "mapView is null");
                        }
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
