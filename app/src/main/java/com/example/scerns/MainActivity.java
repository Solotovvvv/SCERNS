package com.example.scerns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private int userId;

    private TextView selectedTypeTextView;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the user ID from the intent extras
        userId = getIntent().getIntExtra("userId", -1);
        if (userId != -1) {
            // Display user ID using a Toast
            Toast.makeText(MainActivity.this, "User ID: " + userId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "User ID not found", Toast.LENGTH_SHORT).show();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });

        Button emergencyButton = findViewById(R.id.btnEmergency);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVictimOrWitnessDialog();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showVictimOrWitnessDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.victimorwitness, null);

        Button btnVictim = dialogView.findViewById(R.id.btnVictim);
        Button btnWitness = dialogView.findViewById(R.id.btnWitness);

        selectedTypeTextView = dialogView.findViewById(R.id.selectedTypeTextView);

        btnVictim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Victim selected");
                selectedTypeTextView.setText("VICTIM"); // Update hidden TextView with "VICTIM"
                saveSelectionToServer();
            }
        });

        btnWitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Witness selected");
                selectedTypeTextView.setText("WITNESS"); // Update hidden TextView with "WITNESS"
                saveSelectionToServer();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();
    }

    private void saveSelectionToServer() {
        // Get the type text from the hidden TextView
        String typeText = selectedTypeTextView.getText().toString();

        // Set dispatcherId directly based on the type text
        int dispatcherId;
        if (typeText.equals("WITNESS")) {
            dispatcherId = 1; // Set dispatcherId to 1 for Witness
        } else {
            dispatcherId = 2; // Set dispatcherId to 2 for Victim
        }

        // Make HTTP request to PHP script with the dispatcher Id and selection
        String url = "https://nutrilense.ucc-bscs.com/SCERNS/reports.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the response for debugging
                        Log.d("Response", response);

                        // Handle response from server
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");
                            showToast(message);
                            if (success) {
                                // If insertion is successful, you can perform additional actions here
                                showToast(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("Error parsing response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        showToast("Error saving selection: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parameters to send to PHP script
                Map<String, String> params = new HashMap<>();
                params.put("dispatcherId", String.valueOf(dispatcherId)); // Send dispatcher Id
//                params.put("type_of_emergency", typeText); // Send type of emergency (VICTIM or WITNESS)
//                // Add other parameters here if needed (landmark, level, date, status, remarks)
//                params.put("landmark", "YourLandmarkValueHere");
//                params.put("level", "YourLevelValueHere");
//                params.put("date", "YourDateValueHere");
//                params.put("status", "YourStatusValueHere");
//                params.put("remarks", "YourRemarksValueHere");
                return params;
            }
        };

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(this).add(stringRequest);
    }



    private void showOptionsDialog() {
        View dialogOptionsView = LayoutInflater.from(this).inflate(R.layout.dialog_options, null);

        Button btnPolice = dialogOptionsView.findViewById(R.id.btnPolice);
        Button btnMedic = dialogOptionsView.findViewById(R.id.btnMedic);
        Button btnFire = dialogOptionsView.findViewById(R.id.btnFire);
        Button btnNaturalDisaster = dialogOptionsView.findViewById(R.id.btnNaturalDisaster);
        Button btnBack = dialogOptionsView.findViewById(R.id.btnBack);

        btnPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Police selected");
                Intent intent = new Intent(MainActivity.this, PoliceDetails.class);
                startActivity(intent);
            }
        });

        btnMedic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showToast("Medic selected");
                Intent intent = new Intent(MainActivity.this, MedicDetails.class);
                startActivity(intent);
            }
        });

        btnFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Fire selected");
                Intent intent = new Intent(MainActivity.this, FireDetails.class);
                startActivity(intent);
            }
        });

        btnNaturalDisaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Natural Disaster selected");
                Intent intent = new Intent(MainActivity.this, NaturalDisasterDetails.class);
                startActivity(intent);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogOptionsView);
        AlertDialog dialog = builder.create();
        dialog.show();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Back Button Selected");
                dialog.dismiss();
            }
        });
    }

}
