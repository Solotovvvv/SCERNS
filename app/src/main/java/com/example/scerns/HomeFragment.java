package com.example.scerns;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private int userId; // Assuming you have userId variable declared

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Show userId in toast
        if (userId != -1) {
            Toast.makeText(requireContext(), "User ID: " + userId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show();
        }

        // Find the emergency button by its ID
        Button emergencyButton = view.findViewById(R.id.btnEmergency);

        // Set onClickListener for the emergency button
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVictimOrWitnessDialog();
            }
        });

        return view;
    }

    private void showVictimOrWitnessDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.victimorwitness, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        Button btnVictim = dialogView.findViewById(R.id.btnVictim);
        Button btnWitness = dialogView.findViewById(R.id.btnWitness);
        Button btnBackVW = dialogView.findViewById(R.id.vwbackBTN);

        btnBackVW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView selectedTypeTextView = dialogView.findViewById(R.id.selectedTypeTextView);

        btnVictim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTypeTextView.setText("Victim");
                dialog.dismiss();
                showOptionsDialog("Victim");
            }
        });

        btnWitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTypeTextView.setText("Witness");
                dialog.dismiss();
                showOptionsDialog("Witness");
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showOptionsDialog(final String role) {
        View dialogOptionsView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_options, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogOptionsView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();

        Button btnPolice = dialogOptionsView.findViewById(R.id.btnPolice);
        Button btnMedic = dialogOptionsView.findViewById(R.id.btnMedic);
        Button btnFire = dialogOptionsView.findViewById(R.id.btnFire);
        Button btnNaturalDisaster = dialogOptionsView.findViewById(R.id.btnNaturalDisaster);
        Button btnBack = dialogOptionsView.findViewById(R.id.btnBack);

        TextView selectedDialogOptions = dialogOptionsView.findViewById(R.id.selectedDialogOptions);

        btnPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Police selected");
                selectedDialogOptions.setText("Crime");
                Intent intent = new Intent(requireContext(), EmergencyInfo.class);
                intent.putExtra("userId", userId);
                intent.putExtra("role", role);
                intent.putExtra("emergencyType", "Crime");
                startActivity(intent);

                dialog.dismiss();
            }
        });

        btnMedic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Medic selected");
                selectedDialogOptions.setText("Medic");

                Intent intent = new Intent(requireContext(), EmergencyInfo.class);
                intent.putExtra("userId", userId);
                intent.putExtra("role", role);
                intent.putExtra("emergencyType", "Medic");
                startActivity(intent);

                dialog.dismiss();
            }
        });

        btnFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Fire selected");
                selectedDialogOptions.setText("Fire");

                Intent intent = new Intent(requireContext(), EmergencyInfo.class);
                intent.putExtra("userId", userId);
                intent.putExtra("role", role);
                intent.putExtra("emergencyType", "Fire");
                startActivity(intent);

                dialog.dismiss();
            }
        });

        btnNaturalDisaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Natural Disaster selected");
                selectedDialogOptions.setText("Natural Disaster");

                Intent intent = new Intent(requireContext(), EmergencyInfo.class);
                intent.putExtra("userId", userId);
                intent.putExtra("role", role);
                intent.putExtra("emergencyType", "Natural Disaster");
                startActivity(intent);

                dialog.dismiss();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
