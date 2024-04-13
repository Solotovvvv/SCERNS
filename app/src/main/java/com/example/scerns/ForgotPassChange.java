package com.example.scerns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ForgotPassChange extends AppCompatActivity {

    protected Button btnchangepass;
    protected EditText passEditText, confirmpassEditText;
    protected ImageView passVisibilityToggle, confirmPassVisibilityToggle;
    private SharedPreferences sharedPreferences;
    private String USER_ID;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass_change_password);

        btnchangepass = findViewById(R.id.changepassBTN);
        passEditText = findViewById(R.id.passEditText);
        confirmpassEditText = findViewById(R.id.confirmpassEditText);
        passVisibilityToggle = findViewById(R.id.passVisibilityToggle);
        confirmPassVisibilityToggle = findViewById(R.id.confirmPassVisibilityToggle);
        sharedPreferences = getSharedPreferences("ForgotPassPREF", MODE_PRIVATE);

        USER_ID = sharedPreferences.getString("userID", "");
        Log.d("User", USER_ID);


        UICHanges();
        changePassBTN();

        // Toggle password visibility for password EditText
        passVisibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(passEditText, passVisibilityToggle);
            }
        });

        // Toggle password visibility for confirm password EditText
        confirmPassVisibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(confirmpassEditText, confirmPassVisibilityToggle);
            }
        });
    }

    private void UICHanges() {
        // Button of Sending OTP Color
        btnchangepass.setBackgroundColor(ContextCompat.getColor(this, R.color.scernscolor));
    }

    private void togglePasswordVisibility(EditText editText, ImageView visibilityToggle) {
        // Get the current input type of the EditText
        int currentInputType = editText.getInputType();

        // Toggle the password visibility
        if (currentInputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            // Password is currently visible, so hide it
            editText.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            // Change the icon to show the password as hidden
            visibilityToggle.setImageResource(R.drawable.ic_password_visibility_off);
        } else {
            // Password is currently hidden, so show it
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            // Change the icon to show the password as visible
            visibilityToggle.setImageResource(R.drawable.ic_password_visibility_on);
        }

        // Move the cursor to the end of the input for better user experience
        editText.setSelection(editText.getText().length());
    }

    private void changePassBTN() {
        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from passEditText and confirmpassEditText
                String pass = passEditText.getText().toString().trim();
                String confirmpass = confirmpassEditText.getText().toString().trim();

                // Check if pass and confirmpass are not empty
                if (!pass.isEmpty() && !confirmpass.isEmpty()) {
                    // Call resetpass method with pass and confirmpass arguments
                    resetpass(pass, confirmpass);
                } else {
                    // Show a toast message indicating that the fields are required
                    Toast.makeText(ForgotPassChange.this, "Password fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetpass(final String pass, final String confirmpass) {
        if (!pass.equals(confirmpass)) {
            // Show a toast message indicating that the passwords do not match
            Toast.makeText(ForgotPassChange.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue queue = Volley.newRequestQueue(this);
            String URL = "http://scerns.ucc-bscs.com/Controller/ForgotPassword/reset_password.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            Toast.makeText(ForgotPassChange.this, "Changing of Password Success", Toast.LENGTH_LONG).show();
                          Intent intent = new Intent(ForgotPassChange.this, Login.class);
                          startActivity(intent);
                          finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMessage = error.getMessage();
                    if (errorMessage != null) {
                        Log.e("Error", errorMessage);
                    } else {
                        Toast.makeText(ForgotPassChange.this, "Please Try Again Later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("password", pass);
                    params.put("confirmpass", confirmpass);
                    params.put("user_id", USER_ID);
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

}
