package com.example.scerns;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditFragment extends Fragment {

    private EditText editTextFullName;
    private EditText editTextAddress;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private static final int PICK_IMAGE = 1;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Bitmap selectedImageBitmap;
    private String encodedImage;
    private ImageView imageViewSelectedImage;
    private TextView textViewImageName;

    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        Bundle args = getArguments();
        if (args != null) {
            userId = args.getInt("userId", 0);
            String fullName = args.getString("fullName", "");
            String address = args.getString("address", "");
            String phone = args.getString("phone", "");
            String email = args.getString("email", "");

            editTextFullName = view.findViewById(R.id.editTextFullName);
            editTextAddress = view.findViewById(R.id.editTextAddress);
            editTextPhone = view.findViewById(R.id.editTextPhone);
            editTextEmail = view.findViewById(R.id.editTextEmail);

            editTextFullName.setText(fullName);
            editTextAddress.setText(address);
            editTextPhone.setText(phone);
            editTextEmail.setText(email);
        }

        Button selectImageButton = view.findViewById(R.id.buttonSelectImage);
        imageViewSelectedImage = view.findViewById(R.id.imageViewSelectedImage);
        textViewImageName = view.findViewById(R.id.textViewImgName);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();

            }
        });

        Button saveButton = view.findViewById(R.id.buttonSaveProfile);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updatedFullName = editTextFullName.getText() != null ? editTextFullName.getText().toString() : "";
                String updatedAddress = editTextAddress.getText() != null ? editTextAddress.getText().toString() : "";
                String updatedPhone = editTextPhone.getText() != null ? editTextPhone.getText().toString() : "";
                String updatedEmail = editTextEmail.getText() != null ? editTextEmail.getText().toString() : "";

                if (selectedImageBitmap != null) {
                    updateUserDetails(userId, updatedFullName, updatedAddress, updatedPhone, updatedEmail, encodedImage, true);
                } else {
                    updateUserDetails(userId, updatedFullName, updatedAddress, updatedPhone, updatedEmail, null, false);
                }

            }
        });

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                imageViewSelectedImage.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    } else {
                        Log.e("ProfileEditFragment", "Error: DISPLAY_NAME column not found.");
                    }
                }
            } catch (Exception e) {
                Log.e("ProfileEditFragment", "Error getting file name: " + e.getMessage());
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void updateUserDetails(int userId, String fullName, String address, String phone, String email, final String image, boolean hasImage) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "http://scerns.ucc-bscs.com/User/updateProfile.php";

        // Convert the selected image to base64 if available
        String encodedImage = "";
        if (hasImage && selectedImageBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }

        final String finalEncodedImage = encodedImage;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ProfileEditFragment", "Response: " + response);
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

                        if (response.equals("User details updated successfully")) {
                            // Handle success, maybe navigate back or show a success message
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ProfileEditFragment", "Error: " + error.toString());
                        Toast.makeText(getActivity(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                params.put("fullName", fullName);
                params.put("address", address);
                params.put("phone", phone);
                params.put("email", email);

                // Add the encoded image data only if available
                if (hasImage) {
                    params.put("image", finalEncodedImage);
                }

                return params;
            }
        };

        queue.add(postRequest);
    }

}
