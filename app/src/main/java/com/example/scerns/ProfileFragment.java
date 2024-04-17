package com.example.scerns;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private int userId;
    private ImageView imageViewProfile;
    private TextView textViewFullName;
    private TextView textViewAddress;
    private TextView textViewPhone;
    private TextView textViewEmail;
    private TextView textViewNoImage;
    private Button buttonEditProfile;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (userId != -1) {
//            Toast.makeText(requireContext(), "User ID: " + userId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show();
        }

        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        textViewFullName = view.findViewById(R.id.textViewFullName);
        textViewAddress = view.findViewById(R.id.textViewAddress);
        textViewPhone = view.findViewById(R.id.textViewPhone);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
        textViewNoImage = view.findViewById(R.id.textViewNoImage);

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileEditFragment profileEditFragment = new ProfileEditFragment();
                Bundle args = new Bundle();
                args.putInt("userId", userId);
                args.putString("fullName", textViewFullName.getText().toString());
                args.putString("address", textViewAddress.getText().toString());
                args.putString("phone", textViewPhone.getText().toString());
                args.putString("email", textViewEmail.getText().toString());
                profileEditFragment.setArguments(args);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, profileEditFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new FetchUserDetails(this).execute(userId);
    }

    public void populateProfile(JSONObject userDetails) {
        try {
            String fullName = userDetails.getString("fullName");
            String address = userDetails.getString("address");
            String phone = userDetails.getString("phone");
            String email = userDetails.getString("email");
            String profileImage = userDetails.getString("image");

            textViewFullName.setText(fullName);
            textViewAddress.setText(address);
            textViewPhone.setText(phone);
            textViewEmail.setText(email);

            if (profileImage != null && !profileImage.isEmpty()) {
                // If profile image is available, set the image resource and hide the "No Image Uploaded" text
                int drawableId = getResources().getIdentifier(profileImage, "drawable", requireContext().getPackageName());
                if (drawableId != 0) {
                    imageViewProfile.setImageResource(drawableId);
                    imageViewProfile.setVisibility(View.VISIBLE);
                    textViewNoImage.setVisibility(View.GONE);
                } else {
                    // If the drawable resource does not exist, hide the image view and show the "No Image Uploaded" text
                    imageViewProfile.setVisibility(View.GONE);
                    textViewNoImage.setVisibility(View.VISIBLE);
                }
            } else {
                // If no profile image is available, hide the image view and show the "No Image Uploaded" text
                imageViewProfile.setVisibility(View.GONE);
                textViewNoImage.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}

