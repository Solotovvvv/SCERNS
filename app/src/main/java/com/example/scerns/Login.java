package com.example.scerns;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private TextView textViewForgotPassword;
    private Button loginButton;

    private static final String LOGIN_URL = "http://scerns.ucc-bscs.com/User/login.php";

    // other url for hosting
    // https://capstone-it4b.com/Scerns/user/user_login.php
    // https://nutrilense.ucc-bscs.com/SCERNS/login.php
    // http://scerns.ucc-bscs.com/user/user_login.php
    // http://Scerns.ucc-bscs.com/User/login.php

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        TextView textViewCreateAccount = findViewById(R.id.textViewCreateAccount);
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPass.class));
            }
        });
    }

    private void loginUser() {
        // Retrieve username and password from EditText fields
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        // Check if username or password is empty
        if (username.isEmpty() || password.isEmpty()) {
            // Display a toast message indicating that fields need to be filled
            Toast.makeText(Login.this, "Please fill up all fields.", Toast.LENGTH_SHORT).show();
            return; // Return early as fields are not filled
        }

        // Create a RequestQueue using Volley
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a StringRequest to make a POST request to the login URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        Log.d("Response", response);
                        try {
                            // Parse the JSON response
                            JSONObject jsonObject = new JSONObject(response);
                            // Check if the login was successful
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                // Extract user role and ID from the response
                                if (jsonObject.has("user_role") && jsonObject.has("user_id")) {
                                    String userRole = jsonObject.getString("user_role");
                                    int userId = jsonObject.getInt("user_id");
                                    // Handle different user roles
                                    if (userRole.equals("0")) {
                                        // If user role is '0' (assuming '0' represents a specific role), proceed to MainActivity
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        intent.putExtra("userId", userId);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                    } else if (userRole.equals("pending")) {
                                        // If user role is 'pending', display a message indicating pending approval
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Handle other user roles (if needed)
                                        Toast.makeText(Login.this, "Invalid user role", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // If user role or ID is not found in the response, display an error message
                                    Toast.makeText(Login.this, "User role or ID not found in response", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // If login was not successful, display the error message from the server
                                String message = jsonObject.getString("message");
                                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            e.printStackTrace();
                            Toast.makeText(Login.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle Volley error
                Toast.makeText(Login.this, "Error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Add username and password parameters to the request
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }
}
