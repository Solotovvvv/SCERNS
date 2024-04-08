package com.example.scerns;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ReportInfo extends AppCompatActivity {

    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_info);

        // Retrieve address information from intent extras
        address = getIntent().getStringExtra("address");

        // Use the address as needed
        if (address != null) {
            // Example: Display the address in a TextView
            TextView textViewAddress = findViewById(R.id.textViewAddress);
            textViewAddress.setText(address);
        }
    }
}

