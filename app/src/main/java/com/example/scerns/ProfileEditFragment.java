// ProfileEditFragment.java

package com.example.scerns;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditFragment extends Fragment {

    private EditText editTextFullName;
    private EditText editTextAddress;
    private EditText editTextPhone;
    private EditText editTextEmail;

    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        // Retrieve user details from arguments and set EditText fields
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

            // Set user details to EditText fields
            editTextFullName.setText(fullName);
            editTextAddress.setText(address);
            editTextPhone.setText(phone);
            editTextEmail.setText(email);
        }

        // Find the save button
        Button saveButton = view.findViewById(R.id.buttonSaveProfile);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve updated values from EditText fields
                String updatedFullName = editTextFullName.getText().toString();
                String updatedAddress = editTextAddress.getText().toString();
                String updatedPhone = editTextPhone.getText().toString();
                String updatedEmail = editTextEmail.getText().toString();

                // Send POST request to update user details
                updateUserDetails(userId, updatedFullName, updatedAddress, updatedPhone, updatedEmail);
            }
        });



        return view;
    }

    private void updateUserDetails(int userId, String fullName, String address, String phone, String email) {
        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Define URL of PHP script
        String url = "http://scerns.ucc-bscs.com/User/updateProfile.php";

        // Create POST request
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Response from PHP script
                        Log.d("ProfileEditFragment", "Response: " + response);
                        // Handle response as needed
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

                        // Check if the response indicates success
                        if (response.equals("User details updated successfully")) {
                            // Navigate back to the ProfileFragment
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        Log.e("ProfileEditFragment", "Error: " + error.toString());
                        Toast.makeText(getActivity(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Add POST parameters
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId)); // userId is already an integer
                params.put("fullName", fullName);
                params.put("address", address);
                params.put("phone", phone);
                params.put("email", email);
                return params;
            }

        };

        // Add the request to the RequestQueue
        queue.add(postRequest);
    }
}
