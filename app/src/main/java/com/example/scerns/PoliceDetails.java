package com.example.scerns;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PoliceDetails extends AppCompatActivity {

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_info_layout);

        // Retrieve the userId from intent extras
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            // Handle the case when userId is not found
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
            return;
        }

        // Retrieve the role and emergencyType from intent extras
        String role = getIntent().getStringExtra("role");
        String emergencyType = getIntent().getStringExtra("emergencyType");

        // Access views in the layout
        TextView textWelcomeScerns = findViewById(R.id.textWelcomeScerns);
        EditText editTextAddress = findViewById(R.id.editTextAddress);
        EditText editTextLandmark = findViewById(R.id.editTextLandmark);
        Spinner spinnerLevels = findViewById(R.id.spinnerLevels);
        Button btnConfirmRequest = findViewById(R.id.btnConfirmRequest);

        // Set the title based on the emergency type
        textWelcomeScerns.setText(emergencyType.toUpperCase());

        // Set onClickListener for the confirm request button
        btnConfirmRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered values
                String address = editTextAddress.getText().toString().trim();
                String landmark = editTextLandmark.getText().toString().trim();
                String level = spinnerLevels.getSelectedItem().toString();

                // Validate if all fields are filled
                if (address.isEmpty() || landmark.isEmpty()) {
                    showToast("Please fill all fields");
                    return;
                }

                // Save data to database
                saveDataToDatabase(userId, role, emergencyType, address, landmark, level);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveDataToDatabase(final int userId, final String role, final String emergencyType,
                                    final String address, final String landmark, final String level) {
        // Prepare the URL for sending data to the server
        String url = "https://nutrilense.ucc-bscs.com/SCERNS/reports.php";

        // Make an HTTP request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        Log.d("ServerResponse", "Response: " + response);
                        showToast("Server Response: " + response);
                        // Optionally, you can finish the activity here or perform any other action
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                showToast("Error sending data to server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Create a HashMap to store the parameters to be sent to the server
                Map<String, String> params = new HashMap<>();
                // Add parameters to the HashMap
                params.put("userId", String.valueOf(userId));
                params.put("role", role);
                params.put("emergencyType", emergencyType);
                params.put("address", address);
                params.put("landmark", landmark);
                params.put("level", level);
                // Return the HashMap
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
