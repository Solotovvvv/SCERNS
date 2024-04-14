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

        // Create an instance of HomeFragment and set the userId
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setUserId(userId);

        // Replace the fragment container with the HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_contact) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EContactsFragment()).commit();
            Toast.makeText(this, "Emergency Contacts clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_history) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryFragment()).commit();
            Toast.makeText(this, "History clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_logout) {

            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
        }
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
}