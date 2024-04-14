package com.example.scerns;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private int userId;
    private ImageView imageViewProfile;
    private TextView textViewFullName;
    private TextView textViewAddress;
    private TextView textViewPhone;
    private TextView textViewEmail;
    private Button buttonEditProfile;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        textViewFullName = view.findViewById(R.id.textViewFullName);
        textViewAddress = view.findViewById(R.id.textViewAddress);
        textViewPhone = view.findViewById(R.id.textViewPhone);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);

        // Set click listener for edit profile button
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit profile button click
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fetch user details when the fragment is resumed
        new FetchUserDetails(this).execute(userId);
    }

    public void populateProfile(JSONObject userDetails) {
        try {
            // Populate the profile fragment views with user details
            String fullName = userDetails.getString("fullName");
            String address = userDetails.getString("address");
            String phone = userDetails.getString("phone");
            String email = userDetails.getString("email");

            // Set text to TextViews and load image into ImageView as needed
            textViewFullName.setText(fullName);
            textViewAddress.setText(address);
            textViewPhone.setText(phone);
            textViewEmail.setText(email);

            // Load image from the URL or set a default image
            // Example:
            // Picasso.get().load(userDetails.getString("image")).into(imageViewProfile);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

