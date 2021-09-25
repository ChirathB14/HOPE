package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class donor_profilemanagement extends AppCompatActivity {


Button callmyprofile;
Button callmyevent;
Button callchangeprof;


@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_donor_profilemanagement);

        callmyprofile = findViewById(R.id.button2);


    callmyprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(donor_profilemanagement.this, donor_profile.class);
                startActivity(intent);

                callmyevent = findViewById(R.id.button3);
                callmyevent.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(donor_profilemanagement.this, donor_myevents.class);
                        startActivity(intent);

                        callchangeprof = findViewById(R.id.button8);
                        callchangeprof.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(donor_profilemanagement.this, profilepic.class);
                                startActivity(intent);

                            }
                        });

                    }
                });

            }
        });

            }
        }