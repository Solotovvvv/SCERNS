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

        userId = getIntent().getIntExtra("userId", -1);
        if (userId != -1) {
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
                selectedTypeTextView.setText("Victim");
                dialog.dismiss();
//                sendDataToServer(userId, "Victim", "");
                showOptionsDialog("Victim"); // Pass the selected type to showOptionsDialog
            }
        });

        btnWitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Witness selected");
                selectedTypeTextView.setText("Witness");
                dialog.dismiss();
//                sendDataToServer(userId, "Witness", "");
                showOptionsDialog("Witness"); // Pass the selected type to showOptionsDialog
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();
    }

        // Inside your MainActivity class
        private void sendDataToServer(int userId, String role, String typeOfEmergency) {
            // Prepare the URL for sending data to the server
            String url = "https://nutrilense.ucc-bscs.com/SCERNS/reports.php?userId=" + userId + "&role=" + role + "&typeOfEmergency=" + typeOfEmergency;

            // Make an HTTP request using Volley
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Handle response from server
                            Log.d("ServerResponse", "Response: " + response);
                            showToast("Server Response: " + response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error
                    showToast("Error sending data to server");
                }
            });

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(stringRequest);
        }


    private void showOptionsDialog(final String role) {
        View dialogOptionsView = LayoutInflater.from(this).inflate(R.layout.dialog_options, null);

        Button btnPolice = dialogOptionsView.findViewById(R.id.btnPolice);
        Button btnMedic = dialogOptionsView.findViewById(R.id.btnMedic);
        Button btnFire = dialogOptionsView.findViewById(R.id.btnFire);
        Button btnNaturalDisaster = dialogOptionsView.findViewById(R.id.btnNaturalDisaster);
        Button btnBack = dialogOptionsView.findViewById(R.id.btnBack);

        TextView selectedDialogOptions = dialogOptionsView.findViewById(R.id.selectedDialogOptions);

        btnPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Police selected");
                selectedDialogOptions.setText("Police");

                Intent intent = new Intent(MainActivity.this, PoliceDetails.class);
                intent.putExtra("userId", userId);
                intent.putExtra("role", role); // Assuming you have the role value stored somewhere
                intent.putExtra("emergencyType", "Police");
                startActivity(intent);

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        btnMedic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Medic selected");
                selectedDialogOptions.setText("Medic");

                // Pass the selected role and emergency type to the next activity
                Intent intent = new Intent(MainActivity.this, MedicDetails.class);
                intent.putExtra("userId", userId);
                intent.putExtra("role", role); // Assuming you have the role value stored somewhere
                intent.putExtra("emergencyType", "Medic");
                startActivity(intent);

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        btnFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Fire selected");
                selectedDialogOptions.setText("Fire");

                // Pass the selected role and emergency type to the next activity
                Intent intent = new Intent(MainActivity.this, FireDetails.class);
                intent.putExtra("userId", userId);
                intent.putExtra("role", role); // Assuming you have the role value stored somewhere
                intent.putExtra("emergencyType", "Fire");
                startActivity(intent);

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        btnNaturalDisaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Natural Disaster selected");
                selectedDialogOptions.setText("Natural Disaster");

                // Pass the selected role and emergency type to the next activity
                Intent intent = new Intent(MainActivity.this, NaturalDisasterDetails.class);
                intent.putExtra("userId", userId);
                intent.putExtra("role", role); // Assuming you have the role value stored somewhere
                intent.putExtra("emergencyType", "Natural Disaster");
                startActivity(intent);

                // Dismiss the dialog
                dialog.dismiss();

                // Dismiss the dialog
                dialog.dismiss();
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
