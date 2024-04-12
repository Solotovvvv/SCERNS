package com.example.scerns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPassOTP extends AppCompatActivity {

    protected Button btnOTP;
    protected TextView toLogin;
    protected EditText editTextOTP;

    private SharedPreferences sharedPreferences;
    private String SET_OTP;
    private String USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass_otp);

        btnOTP = findViewById(R.id.otpConfirmBTN);
        toLogin = findViewById(R.id.tvLogin);
        editTextOTP = findViewById(R.id.otpEditText);

        sharedPreferences = getSharedPreferences("ForgotPassPREF", MODE_PRIVATE);

        SET_OTP = sharedPreferences.getString("otp", "");
        USER_ID = sharedPreferences.getString("userID", "");
        Log.d("tings", SET_OTP + " " + USER_ID);

        UICHanges();
        toLogin();
        buttonListener();
    }

    private void UICHanges() {
        // Button of Sending OTP Color
        btnOTP.setBackgroundColor(ContextCompat.getColor(this, R.color.scernscolor));
    }

    private void toLogin() {
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassOTP.this, Login.class));
            }
        });
    }

    private void buttonListener() {
        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP = editTextOTP.getText().toString();
                if (OTP != null && !OTP.isEmpty()) {
                    checkOTP(OTP);
                } else {
                    Toast.makeText(ForgotPassOTP.this, "This Field Cannot Be Empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkOTP(final String onetimepass) {
        if (onetimepass.equals(SET_OTP)) {
            Intent intent = new Intent(ForgotPassOTP.this, ForgotPassChange.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(ForgotPassOTP.this, "Invalid OTP. Try Again.", Toast.LENGTH_SHORT).show();
        }
    }
}
