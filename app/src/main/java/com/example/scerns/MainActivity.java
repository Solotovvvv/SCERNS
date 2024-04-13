package com.example.scerns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    private int userId;

    private TextView selectedTypeTextView;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getIntent().getIntExtra("userId", -1);
        if (userId != -1) {
            Toast.makeText(MainActivity.this, "User ID: " + userId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "User ID not found", Toast.LENGTH_SHORT).show();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_item1);
        }


        Button emergencyButton = findViewById(R.id.btnEmergency);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVictimOrWitnessDialog();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_item1) {
            // Handle Profile item click
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_item2) {
            // Handle Emergency Contacts item click
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_item3) {
            // Handle History item click
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EContactsFragment()).commit();
            Toast.makeText(this, "Emergency Contacts clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_item4) {
            // Handle Logout item click
            // Add your logout logic here
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryFragment()).commit();
            Toast.makeText(this, "History clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_item5) {
            // Handle Logout item click
            // Add your logout logic here
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
        }

        // Close the navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showVictimOrWitnessDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.victimorwitness, null);

        Button btnVictim = dialogView.findViewById(R.id.btnVictim);
        Button btnWitness = dialogView.findViewById(R.id.btnWitness);
        Button btnBackVW = dialogView.findViewById(R.id.vwbackBTN);

        btnBackVW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        selectedTypeTextView = dialogView.findViewById(R.id.selectedTypeTextView);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showOptionsDialog(final String role) {
        View dialogOptionsView = LayoutInflater.from(this).inflate(R.layout.dialog_options, null);

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

                Intent intent = new Intent(MainActivity.this, EmergencyInfo.class);
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

                Intent intent = new Intent(MainActivity.this, EmergencyInfo.class);
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

                Intent intent = new Intent(MainActivity.this, EmergencyInfo.class);
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

                Intent intent = new Intent(MainActivity.this, EmergencyInfo.class);
                intent.putExtra("userId", userId);
                intent.putExtra("role", role);
                intent.putExtra("emergencyType", "Natural Disaster");
                startActivity(intent);

                dialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogOptionsView);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
