package com.example.scerns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPass extends AppCompatActivity {
    private Button btnSend;
    private TextView txtviewLogin;
    private EditText editTextEmail;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass_email);

        // Initialize views
        btnSend = findViewById(R.id.btnSendOTP);
        txtviewLogin = findViewById(R.id.textViewLogin);
        editTextEmail = findViewById(R.id.emailEditText);
        sharedPreferences = getSharedPreferences("ForgotPassPREF", this.MODE_PRIVATE);

        // Apply UI changes
        UICHanges();

        // Set click listeners
        OTPbtnListener();
        toLogin();
    }

    private void UICHanges() {
        // Button of Sending OTP Color
        btnSend.setBackgroundColor(ContextCompat.getColor(this, R.color.scernscolor));
    }

    private void OTPbtnListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = editTextEmail.getText().toString();
                if (userEmail != null && !userEmail.isEmpty()) {
                    checkEmail(userEmail);
                } else {
                    Toast.makeText(ForgotPass.this, "Email Field Cannot Be Empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void toLogin() {
        txtviewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPass.this, Login.class));
            }
        });
    }

    public void  checkEmail(final String email) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "http://scerns.ucc-bscs.com/Controller/ForgotPassword/email_check.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            // Check if the response contains any data
                            if (jsonResponse.has("id") && jsonResponse.has("otp")) {
                                // Data found, proceed with storing and navigation
                                String userID = jsonResponse.getString("id");
                                String otp = jsonResponse.getString("otp");

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("otp", otp);
                                editor.putString("userID", userID);
                                editor.apply();

                                Intent intent = new Intent(ForgotPass.this, ForgotPassOTP.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // No records found, display Toast message
                                Toast.makeText(ForgotPass.this, "No records found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                if (errorMessage != null) {
                    Log.e("Error", errorMessage);
                } else {
                    Toast.makeText(ForgotPass.this, "Please Try Again Later.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        queue.add(stringRequest);
    }

}
