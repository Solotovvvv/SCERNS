package com.example.scerns;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class EmergencyInfo extends AppCompatActivity implements AddressSuggestionTask.AddressSuggestionListener{

    private int userId;
    private EditText editTextAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_info_layout);

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String role = getIntent().getStringExtra("role");
        String emergencyType = getIntent().getStringExtra("emergencyType");

        TextView textWelcomeScerns = findViewById(R.id.textWelcomeScerns);
        editTextAddress = findViewById(R.id.editTextAddress);
        EditText editTextLandmark = findViewById(R.id.editTextLandmark);
        Spinner spinnerLevels = findViewById(R.id.spinnerLevels);
        Button btnConfirmRequest = findViewById(R.id.btnConfirmRequest);

        textWelcomeScerns.setText(emergencyType.toUpperCase());

        // Execute AddressSuggestionTask when address text changes
        editTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = s.toString();
                // Execute AsyncTask to fetch suggestions
                new AddressSuggestionTask(EmergencyInfo.this).execute(address);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnConfirmRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = editTextAddress.getText().toString().trim();
                String landmark = editTextLandmark.getText().toString().trim();
                String level = spinnerLevels.getSelectedItem().toString();

                if (address.isEmpty() || landmark.isEmpty()) {
                    showToast("Please fill all fields");
                    return;
                }

                saveDataToDatabase(userId, role, emergencyType, address, landmark, level);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveDataToDatabase(final int userId, final String role, final String emergencyType,
                                    final String address, final String landmark, final String level) {
        // other url for hosting
        // https://capstone-it4b.com/SCERNS/user/user_reports.php
        String url = "https://nutrilense.ucc-bscs.com/SCERNS/reports.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from server
                        Log.d("ServerResponse", "Response: " + response);
                        showToast("Server Response: " + response);
                        // Optionally, you can finish the activity here or perform any other action
                        Intent intent = new Intent(EmergencyInfo.this, ReportsInfo.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("Error sending data to server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                params.put("role", role);
                params.put("emergencyType", emergencyType);
                params.put("address", address);
                params.put("landmark", landmark);
                params.put("level", level);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onAddressSuggestionReceived(JSONArray suggestions) {
        // Handle the received suggestions here, update UI accordingly
        if (suggestions != null && suggestions.length() > 0) {
            try {
                // Extract suggestion from JSONArray and update UI accordingly
                JSONObject suggestion = suggestions.getJSONObject(0);
                String suggestedAddress = suggestion.getString("display_name");
                editTextAddress.setText(suggestedAddress);
                // Optionally, you can show the suggestions in a dropdown or list for user selection
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
