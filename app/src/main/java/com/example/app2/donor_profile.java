package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class donor_profile extends AppCompatActivity {
    //variables
    TextInputLayout fullName, username, email, phoneNo, password;
    TextView txt_fullName, txt_username;
    Button btnDelete;



    //Global variables to hold user data inside this activity
    String _DNAME, _DUSERNAME, _DEMAIL, _DPHONENO, _DPASSWORD;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);


        reference = FirebaseDatabase.getInstance().getReference("Donors");

        //Hooks
        fullName = findViewById(R.id.Dmat_fullName);
        username = findViewById(R.id.Dmat_username);
        email = findViewById(R.id.Dmat_email);
        phoneNo = findViewById(R.id.Dmat_phone);
        password = findViewById(R.id.Dmat_password);
        txt_fullName = findViewById(R.id.Dtxt_name);
        txt_username = findViewById(R.id.Dtxt_uname);
        btnDelete = findViewById(R.id.Dbtn_delete);

        //Show all data
        ShowAllDonorData();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDonor(_DUSERNAME);
            }
        });
    }

    private void deleteDonor(String dusername) {
        DatabaseReference donor = FirebaseDatabase.getInstance().getReference("Donors").child(dusername);

        donor.removeValue();

        Toast.makeText(this, "Profile Deleted", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(donor_profile.this,donor_signup.class);
        startActivity(intent);
    }


    private void ShowAllDonorData() {

        Intent intent = getIntent();
        _DNAME = intent.getStringExtra("name");
        _DUSERNAME = intent.getStringExtra("username");
        _DEMAIL = intent.getStringExtra("email");
        _DPHONENO = intent.getStringExtra("phoneno");
        _DPASSWORD = intent.getStringExtra("password");

        txt_fullName.setText(_DNAME);
        txt_username.setText(_DUSERNAME);
        fullName.getEditText().setText(_DNAME);
        username.getEditText().setText(_DUSERNAME);
        email.getEditText().setText(_DEMAIL);
        phoneNo.getEditText().setText(_DPHONENO);
        password.getEditText().setText(_DPASSWORD);
    }


    public void updateRaiser(View view) {
        //call Database only if any there is any data update
        if (isDNameChanged() || isDPasswordChanged() || isDEmailChanged() || isDPhoneChanged()) {
            Toast.makeText(this, "Data has been Updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Data is same and cannot be Updated", Toast.LENGTH_LONG).show();
        }
    }




    private boolean isDPasswordChanged() {
        if (!_DPASSWORD.equals(password.getEditText().getText().toString())) {
            reference.child(_DUSERNAME).child("password").setValue(password.getEditText().getText().toString());
            _DPASSWORD = password.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isDNameChanged() {
        if (!_DNAME.equals(fullName.getEditText().getText().toString())) {
            reference.child(_DUSERNAME).child("name").setValue(fullName.getEditText().getText().toString());
            _DNAME = fullName.getEditText().getText().toString();
            txt_fullName.setText(_DNAME);
            return true;
        } else {
            return false;
        }
    }

    private boolean isDEmailChanged() {
        if (!_DEMAIL.equals(email.getEditText().getText().toString())) {
            reference.child(_DUSERNAME).child("email").setValue(email.getEditText().getText().toString());
            _DEMAIL = email.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isDPhoneChanged() {
        if (!_DPHONENO.equals(phoneNo.getEditText().getText().toString())) {
            reference.child(_DUSERNAME).child("phoneno").setValue(phoneNo.getEditText().getText().toString());
            _DPHONENO = phoneNo.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }


}