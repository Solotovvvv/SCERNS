package com.example.scerns;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmergencyInfo extends AppCompatActivity implements AddressSuggestionTask.AddressSuggestionListener {

    private int userId, reportId;
    private EditText editTextAddress, getEditTextLandmark, editTextRemarks;
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
        editTextRemarks = findViewById(R.id.editTextRemarks);
        Button btnConfirmRequest = findViewById(R.id.btnConfirmRequest);

        fetchLevelsFromServer(emergencyType);

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

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyInfo.this, MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

        btnConfirmRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = editTextAddress.getText().toString().trim();
                String landmark = getEditTextLandmark.getText().toString().trim();
                String selectedLevel = spinnerLevels.getSelectedItem().toString();
                String level = extractNumericLevel(selectedLevel);
                String remarks = editTextRemarks.getText().toString().trim();

                if (!address.isEmpty() && !landmark.isEmpty()) {
                    if (selectedLevel.equals("Select Level")) {
                        showToast("Please select a level");
                        return;
                    }
                } else {
                    showToast("Please fill all fields");
                    return;
                }

                saveDataToDatabase(userId, role, emergencyType, address, landmark, level, remarks);
            }
        });

    }

    private void fetchLevelsFromServer(final String emergencyType) {
        String url = "http://scerns.ucc-bscs.com/User/getLevels.php?emergencyType=" + emergencyType;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            List<String> levelsList = new ArrayList<>();
                            levelsList.add("Select Level");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String level = jsonObject.getString("level");
                                String description = jsonObject.getString("description");
                                levelsList.add(level + " - " + description);
                            }

                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(EmergencyInfo.this, android.R.layout.simple_spinner_item, levelsList);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerLevels.setAdapter(spinnerAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("Error parsing JSON for levels");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("Error fetching levels from server");
                Log.e("fetchLevelsFromServer", "Volley error: " + error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private String extractNumericLevel(String level) {
        String[] parts = level.split(" - ");
        if (parts.length > 1) {
            return parts[0];
        } else {
            return level;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveDataToDatabase(final int userId, final String role, final String emergencyType,
                                    final String address, final String landmark, final String level, final String remarks) {

        String url = "http://scerns.ucc-bscs.com/User/reports.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ServerResponse", "Response: " + response);
                        if (response.startsWith("Error")) {
                            showToast("Error: " + response);
                        } else if (response.equals("This address is already reported.")) {
                            showToast("This Address is already reported");
                            Intent intent = new Intent(EmergencyInfo.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            try {
                                reportId = Integer.parseInt(response.trim());
                                startReportInfoActivity(userId, reportId, address, landmark, level, emergencyType);
                                showToast("Request Report Success");
                            } catch (NumberFormatException e) {
                                showToast("Invalid report ID received from server");
                            }
                        }
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
                params.put("remarks", remarks);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void startReportInfoActivity(int userId, int reportId, String address, String landmark, String level, String emergencyType) {
        Intent intent = new Intent(EmergencyInfo.this, ReportInfo.class);
        intent.putExtra("userId", userId);
        intent.putExtra("reportId", reportId);
        intent.putExtra("address", address);
        intent.putExtra("landmark", landmark);
        intent.putExtra("level", level);
        intent.putExtra("emergencyType", emergencyType);
        startActivity(intent);
        finish();
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

            while (suggestionList.size() < 3) {
                suggestionList.add("");
            }

            ListView suggestionListView = findViewById(R.id.suggestionListView);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, suggestionList);
            suggestionListView.setAdapter(adapter);

            suggestionListView.getLayoutParams().height = 300;
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
