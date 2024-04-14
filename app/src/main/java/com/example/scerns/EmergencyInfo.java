package com.example.scerns;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmergencyInfo extends AppCompatActivity implements AddressSuggestionTask.AddressSuggestionListener{

    private int userId;
    private EditText editTextAddress, getEditTextLandmark;
    private Spinner spinnerLevels;

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
        getEditTextLandmark = findViewById(R.id.editTextLandmark);
        spinnerLevels = findViewById(R.id.spinnerLevels);
        Button btnConfirmRequest = findViewById(R.id.btnConfirmRequest);

        List<String> levelsList = new ArrayList<>();
        levelsList.add("Select Level");
        levelsList.add("Level 1");
        levelsList.add("Level 2");
        levelsList.add("Level 3");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, levelsList) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevels.setAdapter(spinnerAdapter);
        spinnerLevels.setSelection(0, false);

        textWelcomeScerns.setText(emergencyType.toUpperCase());

        editTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = s.toString();
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
                String landmark = getEditTextLandmark.getText().toString().trim();
                String level = spinnerLevels.getSelectedItem().toString();

                String selectedLevel = spinnerLevels.getSelectedItem().toString();

                if (!address.isEmpty() && !landmark.isEmpty()) {
                    if (selectedLevel.equals("Select Level")) {
                        showToast("Please select a level");
                        return;
                    }
                } else {
                    if (address.isEmpty() || landmark.isEmpty()) {
                        showToast("Please fill all fields");
                        return;
                    }
                }
                String numericLevel = extractNumericLevel(level);
                saveDataToDatabase(userId, role, emergencyType, address, landmark, numericLevel);
            }
        });
    }

    private String extractNumericLevel(String level) {
        String numericPart = level.replaceAll("\\D+", "");
        return numericPart.isEmpty() ? "0" : numericPart;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveDataToDatabase(final int userId, final String role, final String emergencyType,
                                    final String address, final String landmark, final String level) {
        // other url for hosting
        // https://capstone-it4b.com/Scerns/user/user_reports.php
        // https://nutrilense.ucc-bscs.com/SCERNS/reports.php
        // http://scerns.ucc-bscs.com/user/user_reports.php
        // http://Scerns.ucc-bscs.com/User/reports.php

        String url = "http://scerns.ucc-bscs.com/User/reports.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ServerResponse", "Response: " + response);
                        showToast("Server Response: " + response);

                        Intent intent = new Intent(EmergencyInfo.this, ReportInfo.class);
                        intent.putExtra("userId", userId);
                        intent.putExtra("address", editTextAddress.getText().toString().trim());
                        intent.putExtra("landmark", getEditTextLandmark.getText().toString().trim());
                        intent.putExtra("level", spinnerLevels.getSelectedItem().toString());
                        intent.putExtra("emergencyType", getIntent().getStringExtra("emergencyType"));

                        startActivity(intent);
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
        if (suggestions != null && suggestions.length() > 0) {
            List<String> suggestionList = new ArrayList<>();
            for (int i = 0; i < suggestions.length(); i++) {
                try {
                    JSONObject suggestion = suggestions.getJSONObject(i);
                    String suggestedAddress = suggestion.getString("display_name");
                    suggestionList.add(suggestedAddress);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Ensure at least three suggestions are available
            while (suggestionList.size() < 3) {
                suggestionList.add(""); // Add empty strings as placeholders
            }

            ListView suggestionListView = findViewById(R.id.suggestionListView);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, suggestionList);
            suggestionListView.setAdapter(adapter);

            // Set maximum height for the ListView
            suggestionListView.getLayoutParams().height = 300; // Adjust as needed
            suggestionListView.requestLayout();

            LinearLayout addressSuggestionLayout = findViewById(R.id.addressSuggestionLayout);
            addressSuggestionLayout.setVisibility(View.VISIBLE);

            suggestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedAddress = (String) parent.getItemAtPosition(position);
                    EditText editTextAddress = findViewById(R.id.editTextAddress);
                    editTextAddress.setText(selectedAddress);
                    addressSuggestionLayout.setVisibility(View.GONE);
                }
            });
        }
    }


}