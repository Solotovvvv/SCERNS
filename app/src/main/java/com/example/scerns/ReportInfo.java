package com.example.scerns;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;
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
    private Pusher pusher;
    private int userId, reportId;

    private TextView textViewStatus, textViewAddress, textViewLandmark, textViewLevel, textViewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_info);

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        address = getIntent().getStringExtra("address");
        landmark = getIntent().getStringExtra("landmark");
        type = getIntent().getStringExtra("emergencyType");
        level = getIntent().getStringExtra("level");

        reportId = getIntent().getIntExtra("reportId", -1);
        if (reportId != -1) {
            fetchReportDetails(reportId);
        } else if (address != null) {
            fetchReportDetailsByAddress(address);
        }

        PusherOptions options = new PusherOptions().setCluster("ap1").setEncrypted(true);
        pusher = new Pusher("b26a50e9e9255fc95c8f", options);
        pusher.connect();

        textViewAddress = findViewById(R.id.textViewAddress);
        textViewLandmark = findViewById(R.id.textViewLandmark);
        textViewLevel = findViewById(R.id.textViewLevel);
        textViewType = findViewById(R.id.textViewType);
        textViewStatus = findViewById(R.id.textViewStatus);

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

        triggerPusherEvent();

        if (address != null) {
            TextView textViewAddress = findViewById(R.id.textViewAddress);
            String addressLabel = "Address: " + address;
            textViewAddress.setText(addressLabel);

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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewStatus.setText("Status: " + status);
                            if (!status.equals("Pending")) {
                                LinearLayout loadingLayout = findViewById(R.id.loadingLayout);
                                loadingLayout.setVisibility(View.GONE);
                            }
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

    private void fetchReportDetails(int reportId) {
        String url = "http://scerns.ucc-bscs.com/User/getReport.php?Id=" + reportId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String address = jsonObject.getString("Address");
                            String landmark = jsonObject.getString("Landmark");
                            String level = jsonObject.getString("Level");
                            String type = jsonObject.getString("TypeOfEmergency");
                            String status = jsonObject.getString("Status");

                            textViewStatus.setText("Status: " + status);
                            textViewAddress.setText("Address: " + address);
                            textViewLandmark.setText("Landmark: " + landmark);
                            textViewLevel.setText("Level: " + level);
                            textViewType.setText("Type: " + type);

//                            Toast.makeText(ReportInfo.this, "Report details fetched successfully", Toast.LENGTH_SHORT).show();

                            Log.d("ReportInfo", "Report details fetched successfully");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ReportInfo.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                            Log.e("ReportInfo", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ReportInfo", "Error fetching report details: " + error.getMessage());
                Toast.makeText(ReportInfo.this, "Error fetching report details", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void fetchReportDetailsByAddress(String address) {
        String url = "http://scerns.ucc-bscs.com/User/getReportByAddress.php?address=" + address;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            int reportId = jsonObject.getInt("Id");
                            fetchReportDetails(reportId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ReportInfo.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ReportInfo", "Error fetching report details by address: " + error.getMessage());
                        Toast.makeText(ReportInfo.this, "Error fetching report details by address", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
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
            if (result != null && mapView != null) {
                mapView.getController().setCenter(result);

                adjustZoomToAddress(result);

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
