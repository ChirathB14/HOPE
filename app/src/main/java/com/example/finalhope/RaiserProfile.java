package com.example.finalhope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RaiserProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variables
    TextInputLayout fullName, username, nic, email, phoneNo, password;
    TextView txt_fullName, txt_username;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button buttonDelete;

    //Global variables to hold user data inside this activity
    String _NAME, _USERNAME, _NIC, _EMAIL, _PHONENO, _PASSWORD;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raiser_profile);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        reference = FirebaseDatabase.getInstance().getReference("users");

        //Hooks
        fullName = findViewById(R.id.mat_fullName);
        username = findViewById(R.id.mat_username);
        nic = findViewById(R.id.mat_nic);
        email = findViewById(R.id.mat_email);
        phoneNo = findViewById(R.id.mat_phone);
        password = findViewById(R.id.mat_password);
        txt_fullName = findViewById(R.id.txt_name);
        txt_username = findViewById(R.id.txt_uname);
        buttonDelete = findViewById(R.id.btn_delete);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar_Rprofile);

        /*-----------------------Tool Bar------------------*/
        setSupportActionBar(toolbar);

        /*-------------------Navigation Drawer Menu-----------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //make menu clickable
        navigationView.setNavigationItemSelectedListener(this);

        //When application launches, direct to the home
        //navigationView.setCheckedItem(R.id.nav_home);

        //Show all data
        ShowAllRaiserData();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRaiser(_USERNAME);
            }
        });
    }

    private void deleteRaiser(String rUsername) {
        DatabaseReference dRaiser = FirebaseDatabase.getInstance().getReference("users").child(rUsername);

        dRaiser.removeValue();

        Toast.makeText(this, "Profile Deleted", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RaiserProfile.this,SignUp.class);
        startActivity(intent);
    }

    //If nav menu is open and when we press back button instead of going back to the previous activity, just close the nav menu
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    private void ShowAllRaiserData() {

        Intent intent = getIntent();
        _NAME = intent.getStringExtra("fullName");
        _USERNAME = intent.getStringExtra("username");
        _NIC = intent.getStringExtra("nic_reg");
        _EMAIL = intent.getStringExtra("email");
        _PHONENO = intent.getStringExtra("phoneNo");
        _PASSWORD = intent.getStringExtra("password");

        txt_fullName.setText(_NAME);
        txt_username.setText(_USERNAME);
        fullName.getEditText().setText(_NAME);
        username.getEditText().setText(_USERNAME);
        nic.getEditText().setText(_NIC);
        email.getEditText().setText(_EMAIL);
        phoneNo.getEditText().setText(_PHONENO);
        password.getEditText().setText(_PASSWORD);
    }

    public void updateRaiser(View view) {
        //call Database only if any there is any data update
        if(isNameChanged() || isPasswordChanged() || isEmailChanged() || isNICChanged() || isPhoneChanged() || isUsernameChanged()) {
            Toast.makeText(this, "Data has been Updated", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Data is same and cannot be Updated", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isPasswordChanged() {
        if(!_PASSWORD.equals(password.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("password").setValue(password.getEditText().getText().toString());
            _PASSWORD = password.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isNameChanged() {
        if(!_NAME.equals(fullName.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("fullName").setValue(fullName.getEditText().getText().toString());
            _NAME = fullName.getEditText().getText().toString();
            txt_fullName.setText(_NAME);
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isUsernameChanged() {
        if(!_USERNAME.equals(username.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("username").setValue(username.getEditText().getText().toString());
            _USERNAME = username.getEditText().getText().toString();
            txt_username.setText(_USERNAME);
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isNICChanged() {
        if(!_NIC.equals(nic.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("nic_reg").setValue(nic.getEditText().getText().toString());
            _NIC = nic.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isEmailChanged() {
        if(!_EMAIL.equals(email.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("email").setValue(email.getEditText().getText().toString());
            _EMAIL = email.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isPhoneChanged() {
        if(!_PHONENO.equals(phoneNo.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("phoneNo").setValue(phoneNo.getEditText().getText().toString());
            _PHONENO = phoneNo.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
//            case  R.id.nav_raise_fund:
//                Intent intent = new Intent(getApplicationContext(),);
//                startActivity(intent);
//                break;
//            case R.id.nav_create_event:
//                Intent intent1 = new Intent(getApplicationContext(),);
//                startActivity(intent1);
//                break;
//            case R.id.nav_home;
//                Intent intent2 = new Intent(getApplicationContext(),);
//                startActivity(intent2);
//                break;
            case R.id.nav_profile_manage:
                Intent intent3 = new Intent(getApplicationContext(),RaiserProfileManage.class);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}