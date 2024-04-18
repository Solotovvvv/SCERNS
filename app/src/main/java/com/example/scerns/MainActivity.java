package com.example.scerns;

import android.content.Context;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.navigation.NavigationView;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    private long lastVolumeUpPressTime = 0;
    private long lastVolumeDownPressTime = 0;

    private int userId;
    private TextView badgeTextView;
    private RequestQueue requestQueue;
    // Define constants for requesting location permission
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    // Location variables
    private double latitude;
    private double longitude;

    // Location manager
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Pusher pusher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId != -1) {
            Toast.makeText(MainActivity.this, "User ID: " + userId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "User ID not found", Toast.LENGTH_SHORT).show();
        }

        PusherOptions options = new PusherOptions().setCluster("ap1").setEncrypted(true);
        pusher = new Pusher("b26a50e9e9255fc95c8f", options);
        pusher.connect();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        triggerPusherEvent();

        badgeTextView = findViewById(R.id.badge);

        requestQueue = Volley.newRequestQueue(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setUserId(userId);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

        navigationView.setCheckedItem(R.id.nav_home);

        fetchBadgeCountFromAPI(userId);

        Button btnAlert = findViewById(R.id.btnAlert);
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryFragment historyFragment = new HistoryFragment();
                historyFragment.setUserId(userId);
                navigationView.setCheckedItem(R.id.nav_history);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, historyFragment).commit();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Initialize location listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Update latitude and longitude when location changes
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        // Request location updates
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, request location updates
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
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

                    // Call the function to fetch data from API
                    fetchBadgeCountFromAPI(userId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Pusher", "Error parsing event data: " + e.getMessage());
                }
            }
        };

        channel.bind("count-report", eventListener);

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


    private void fetchBadgeCountFromAPI(int userId) {
        String url = "http://scerns.ucc-bscs.com/User/getCount.php?userId=" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int badgeCount = Integer.parseInt(response.trim());
//                            Toast.makeText(MainActivity.this, "Badge Count: " + badgeCount, Toast.LENGTH_SHORT).show();

                            updateBadge(badgeCount);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error: Invalid badge count format", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void updateBadge(int badgeCount) {
        Log.d("BadgeCount", "Count: " + badgeCount);
        if (badgeCount != 0) {
            badgeTextView.setText(String.valueOf(badgeCount));
            badgeTextView.setVisibility(View.VISIBLE);
        } else {
            badgeTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setUserId(userId);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        } else if (itemId == R.id.nav_profile) {
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setUserId(userId);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
        } else if (itemId == R.id.nav_contact) {
            EContactsFragment eContactsFragment = new EContactsFragment();
            eContactsFragment.setUserId(userId);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, eContactsFragment).commit();
        } else if (itemId == R.id.nav_history) {
            HistoryFragment historyFragment = new HistoryFragment();
            historyFragment.setUserId(userId);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, historyFragment).commit();
        } else if (itemId == R.id.nav_logout) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userId");
            editor.apply();
            Intent loginIntent = new Intent(MainActivity.this, Login.class);
            startActivity(loginIntent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastVolumeUpPressTime <= 300) {
                getLocation();
                reportCrimeGesture(userId, longitude, latitude);
                lastVolumeUpPressTime = 0; // Reset last press time
                return true;
            }
            lastVolumeUpPressTime = currentTime;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastVolumeDownPressTime <= 300) {
                getLocation();
                reportMedicGesture(userId, longitude, latitude);
                lastVolumeDownPressTime = 0; // Reset last press time
                return true;
            }
            lastVolumeDownPressTime = currentTime;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void reportCrimeGesture(final int UserID, final double LONGITUDE, final double LATITUDE ) {
    String TOE = "Crime"; // TOE = Type Of Emergency
        executeReport(this, UserID, TOE, LATITUDE, LONGITUDE);;
    }

    private void reportMedicGesture(final int UserID, final double LONGITUDE, final double LATITUDE ) {
    String TOE = "Medic"; // TOE = Type Of Emergency
        executeReport(this, UserID, TOE, LATITUDE, LONGITUDE);
    }

    private void getLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();

        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void executeReport(Context context, final int userID, final String TOE, final double LAT, final double LONG) {
        String URL = "http://scerns.ucc-bscs.com/Controller/Gesture/gesture.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        Log.d("Lugars", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(context, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                String message = jsonResponse.getString("message");
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(context, "Something Went Wrong. Please Try Again Later.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", String.valueOf(userID));
                params.put("TypeOfEmergency", TOE);
                params.put("latitude", String.valueOf(LAT));
                params.put("longitude", String.valueOf(LONG));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }


}

