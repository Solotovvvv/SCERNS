package com.example.scerns;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

        ImageButton imageButtonTogglePassword = findViewById(R.id.imageButtonTogglePassword);
        imageButtonTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId != -1) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();
        }
    }

    private void togglePasswordVisibility() {
        int cursorPosition = editTextPassword.getSelectionStart();
        if (editTextPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editTextPassword.setSelection(cursorPosition);
    }

    private void loginUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Please fill up all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                if (jsonObject.has("user_role") && jsonObject.has("user_id")) {
                                    String userRole = jsonObject.getString("user_role");
                                    int userId = jsonObject.getInt("user_id");

                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("userId", userId);
                                    editor.putString("userRole", userRole);
                                    editor.apply();

                                    if (userRole.equals("0")) {
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        intent.putExtra("userId", userId);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                    } else if (userRole.equals("pending")) {
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Login.this, "Invalid user role", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Login.this, "User role or ID not found in response", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
