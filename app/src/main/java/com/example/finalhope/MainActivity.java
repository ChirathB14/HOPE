package com.example.finalhope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button callSignUp, login_btn;
    TextView logoText;
    TextInputLayout username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Hooks
        callSignUp = findViewById(R.id.btn_newuser_signup);
        logoText = findViewById(R.id.logoName);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.btn_login);

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);

                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View,String>(logoText,"logo_text");
                pairs[1] = new Pair<View,String>(username,"username_tran");
                pairs[2] = new Pair<View,String>(password,"password_tran");
                pairs[3] = new Pair<View,String>(login_btn,"btn_login_tran");
                pairs[4] = new Pair<View,String>(callSignUp,"login_signup_tran");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(intent,options.toBundle());
                }
            }
        });
    }

    private Boolean validateUsername(){
        String val = username.getEditText().getText().toString();

        if(val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        }
        else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString();

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        }
        else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void loginReceiver(View view) {
        //validate login info
        if (!validateUsername() | !validatePassword()) {
            return;
        }
        else{
            isUser();
        }
    }

    //method to check whether the user is in the system
    private void isUser() {
        //getting user entered values
        String raiserEnteredUsername = username.getEditText().getText().toString().trim();
        String raiserEnteredPassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        //checking the entered username with username in the database
        Query checkUser = reference.orderByChild("username").equalTo(raiserEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    //if 2nd time entered correctly,remove error
                    username.setError(null);
                    username.setErrorEnabled(false);//remove space error

                    //if username exists, get the relevant password
                    String passwordFromDB = snapshot.child(raiserEnteredUsername).child("password").getValue(String.class);

                    //checking the entered password with password in the database
                    if(passwordFromDB.equals(raiserEnteredPassword)) {
                        //if 2nd time entered correctly,remove error
                        username.setError(null);
                        username.setErrorEnabled(false);//remove space error

                        String nameFromDB = snapshot.child(raiserEnteredUsername).child("fullName").getValue(String.class);
                        String usernameFromDB = snapshot.child(raiserEnteredUsername).child("username").getValue(String.class);
                        String emailFromDB = snapshot.child(raiserEnteredUsername).child("email").getValue(String.class);
                        String phoneFromDB = snapshot.child(raiserEnteredUsername).child("phoneNo").getValue(String.class);
                        String nicFromDB = snapshot.child(raiserEnteredUsername).child("nic_reg").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(),RaiserProfile.class);

                        intent.putExtra("fullName",nameFromDB);
                        intent.putExtra("username",usernameFromDB);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("phoneNo",phoneFromDB);
                        intent.putExtra("nic_reg",nicFromDB);
                        intent.putExtra("password",passwordFromDB);

                        startActivity(intent);
                    }
                    else {
                        password.setError("Wrong Password");
                        password.requestFocus(); //automatically request the focus and start pointing on specific field
                    }
                }
                else {
                    username.setError("No such User exist");
                    username.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
}