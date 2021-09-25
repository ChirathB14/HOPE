package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class donor_signup extends AppCompatActivity {
    Button callsignup, callwithout;
    TextInputLayout Name, Email, phoneNo, userName, passWord;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.domain_signup);
        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        phoneNo = findViewById(R.id.phoneno);
        userName = findViewById(R.id.uname);
        passWord = findViewById(R.id.password);
        callsignup = findViewById(R.id.signup1);
        callwithout = findViewById(R.id.signup2);

        callwithout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(donor_signup.this, donor_profilemanagement.class);
                    startActivity(intent);

//                    callsignup.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            rootNode = FirebaseDatabase.getInstance();
//                            reference = rootNode.getReference("Donors");
//
//                            String name = Name.getEditText().getText().toString();
//                            String email = Email.getEditText().getText().toString();
//                            String phoneno = phoneNo.getEditText().getText().toString();
//                            String username = userName.getEditText().getText().toString();
//                            String password = passWord.getEditText().getText().toString();
//
//
//                            UserHelperClass helperClass = new UserHelperClass(name, email, phoneno, username, password);
//                            reference.child(username).setValue(helperClass);
//
//                            Intent intent = new Intent(donor_signup.this, MainActivity.class);
//                            startActivity(intent);
//                        }
//                    });

                }
        });
    }

    //Form validation
    private Boolean validateDName(){
        String val = Name.getEditText().getText().toString();

        if(val.isEmpty()) {
            Name.setError("Field cannot be empty");
            return false;
        }
        else {
            Name.setError(null);
            Name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateDUsername(){
        String val = userName.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()) {
            userName.setError("Field cannot be empty");
            return false;
        }
        else if(val.length()>=15) {
            userName.setError("Username is too long");
            return false;
        }
        else if(!val.matches(noWhiteSpace)) {
            userName.setError("White Spaces are not allowed");
            return false;
        }
        else {
            userName.setError(null);
            userName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateDEmail(){
        String val = Email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            Email.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern)) {
            Email.setError("Invalid email address");
            return false;
        }
        else {
            Email.setError(null);
            Email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateDPhoneNo(){
        String val = phoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            phoneNo.setError("Field cannot be empty");
            return false;
        }
        else {
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validateDPassword(){
        String val = passWord.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +     //at least 1 digit
                "(?=.*[a-z])" +     //at least 1 lowercase letter
                "(?=.*[A-Z])" +     //at least 1 uppercase letter
                "(?=.*[a-zA-Z])" +     //any letter
                "(?=.*[@#$%^&+=])" +     //at least 1 special character
                "(?=\\S+$)" +     //no white spaces
                ".{6,}" +     //at least 6 characters
                "$";

        if (val.isEmpty()) {
            passWord.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(passwordVal)) {
            passWord.setError("Password is too weak");
            return false;
        }
        else {
            passWord.setError(null);
            passWord.setErrorEnabled(false);
            return true;
        }
    }

    public void registerDonor(View view) {

        if (!validateDName() | !validateDUsername() | !validateDEmail() | !validateDPhoneNo() | !validateDPassword()) {
            return;
        }

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Donors");

                String name = Name.getEditText().getText().toString();
                String email = Email.getEditText().getText().toString();
                String phoneno = phoneNo.getEditText().getText().toString();
                String username = userName.getEditText().getText().toString();
                String password = passWord.getEditText().getText().toString();


                UserHelperClass helperClass = new UserHelperClass(name, email, phoneno, username, password);
                reference.child(username).setValue(helperClass);

                Intent intent = new Intent(donor_signup.this, MainActivity.class);
                startActivity(intent);

    }

}

