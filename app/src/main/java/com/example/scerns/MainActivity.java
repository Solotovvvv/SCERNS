package com.example.scerns;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    private long lastVolumeUpPressTime = 0;
    private long lastVolumeDownPressTime = 0;

    private int userId;
    private int badgeCount = 0;
    private TextView badgeTextView;
    private RequestQueue requestQueue;

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        badgeTextView = findViewById(R.id.badge);

        requestQueue = Volley.newRequestQueue(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setUserId(userId);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

        navigationView.setCheckedItem(R.id.nav_home);

        Button btnAlert = findViewById(R.id.btnAlert);
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchBadgeCountFromAPI();
                HistoryFragment historyFragment = new HistoryFragment();
                historyFragment.setUserId(userId);
                navigationView.setCheckedItem(R.id.nav_history);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, historyFragment).commit();
            }
        });
    }

    private void fetchBadgeCountFromAPI() {
        // URL of the API endpoint with userId parameter
        String url = "http://scerns.ucc-bscs.com/User/getCount.php?userId=" + userId;

        // Create a JSON object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Log response to see if it's correct
                            Log.d("Response", response.toString());

                            // Parse response and update badge count
                            badgeCount = response.getInt("count");
                            Log.d("BadgeCount", "Count: " + badgeCount);
                            // Update badge count text
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateBadge();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        error.printStackTrace();
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    private void updateBadge() {
        Log.d("BadgeCount", "Count: " + badgeCount); // Add this line to log the badge count
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
            historyFragment.setUserId(userId); // Set the userId
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
                Toast.makeText(this, "Double Click", Toast.LENGTH_SHORT).show();
                lastVolumeUpPressTime = 0; // Reset last press time
                return true;
            }
            lastVolumeUpPressTime = currentTime;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastVolumeDownPressTime <= 300) {
                Toast.makeText(this, "3ple Click", Toast.LENGTH_SHORT).show();
                lastVolumeDownPressTime = 0; // Reset last press time
                return true;
            }
            lastVolumeDownPressTime = currentTime;
        }
        return super.onKeyDown(keyCode, event);
    }
}

