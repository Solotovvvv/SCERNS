package com.example.scerns;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText editTextFullname, editTextEmail, editTextContactNumber, editTextUsername, editTextPassword, editTextAddress, editTextConfirmPassword;
    private Button registerButton, buttonSelectImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    private static final String API_URL = "http://scerns.ucc-bscs.com/User/register.php";

    // other url for hosting
    // https://capstone-it4b.com/Scerns/user/user_register.php
    // https://nutrilense.ucc-bscs.com/SCERNS/register.php
    // http://scerns.ucc-bscs.com/user/user_register.php
    // http://scerns.ucc-bscs.com/User/register.php

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        editTextFullname = findViewById(R.id.editTextFullname);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContactNumber = findViewById(R.id.editTextContactNumber);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.registerButton);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegistration();
            }
        });

        TextView textViewCreateAccount = findViewById(R.id.textViewLogin);
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            String fileName = getFileName(selectedImageUri);
            TextView textViewSelectedFileName = findViewById(R.id.textViewSelectedFileName);
            textViewSelectedFileName.setText(fileName);
            textViewSelectedFileName.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateRegistration() {
        if (areFieldsEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        sendRegistrationData();
    }

    private boolean areFieldsEmpty() {
        return editTextFullname.getText().toString().isEmpty() ||
                editTextEmail.getText().toString().isEmpty() ||
                editTextAddress.getText().toString().isEmpty() ||
                editTextContactNumber.getText().toString().isEmpty() ||
                editTextUsername.getText().toString().isEmpty() ||
                editTextPassword.getText().toString().isEmpty() ||
                editTextConfirmPassword.getText().toString().isEmpty();
    }

    private void sendRegistrationData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if (status.equals("success")) {
                                Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Register.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "An error occurred during registration: " + error.getMessage(), error);

                Toast.makeText(Register.this, "Error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", editTextFullname.getText().toString());
                params.put("email", editTextEmail.getText().toString());
                params.put("address", editTextAddress.getText().toString());
                params.put("contact_number", editTextContactNumber.getText().toString());
                params.put("username", editTextUsername.getText().toString());
                params.put("password", editTextPassword.getText().toString());

                if (selectedImageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                        String fileExtension = getMimeType(selectedImageUri);

                        if (fileExtension != null && (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg"))) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        } else if (fileExtension != null && fileExtension.equalsIgnoreCase("png")) {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        } else {
                            Toast.makeText(Register.this, "Unsupported image format", Toast.LENGTH_SHORT).show();
                            return null;
                        }

                        byte[] imageBytes = byteArrayOutputStream.toByteArray();
                        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        params.put("image_data", encodedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(Register.this, "File not found", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(Register.this, "Error reading image", Toast.LENGTH_SHORT).show();
                    }
                }
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private String getMimeType(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


}
