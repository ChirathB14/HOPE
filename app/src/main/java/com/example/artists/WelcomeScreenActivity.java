package com.example.artists;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WelcomeScreenActivity extends AppCompatActivity {

    Button btnDonor, btnRaiseAFund, btnLogin;
    ImageView btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        btnDonor = findViewById(R.id.btnDonor);
        btnLogin = findViewById(R.id.btnLogin);
        btnRaiseAFund = findViewById(R.id.btnRaiseFund);
        //fabHope = findViewById(R.id.fabHope);
        btnHelp = findViewById(R.id.btnHelp);


        btnDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRaiseAFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}