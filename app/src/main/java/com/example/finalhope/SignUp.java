package com.example.finalhope;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    //Variables
    TextInputLayout rRegName, rRegUsername, rRegEmail, rRegPhoneNo, rRegNIC, rRegPassword;
    TextView logoText;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button rRegBtn, rRegToLoginBtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Hooks to all xml elements in activity_sign_up.xml
        radioGroup = findViewById(R.id.radioGroup);
        rRegName = findViewById(R.id.reg_fullName);
        rRegUsername = findViewById(R.id.reg_username);
        rRegEmail = findViewById(R.id.reg_email);
        rRegPhoneNo = findViewById(R.id.reg_phoneNo);
        rRegNIC = findViewById(R.id.reg_nic_regno);
        rRegPassword = findViewById(R.id.reg_password);
        rRegBtn = findViewById(R.id.btn_signup);
        rRegToLoginBtn = findViewById(R.id.btn_exist_login);
        logoText = findViewById(R.id.logo);

//        //Save data in FireBase on button click
//        rRegBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //calling the FireBase database root node
//                rootNode = FirebaseDatabase.getInstance();
//                //calling the relevant reference
//                reference = rootNode.getReference("users");
//
//                //Get all the values from the textFields
//                int selectedID = radioGroup.getCheckedRadioButtonId();
//                radioButton = findViewById(selectedID);
//                String type = radioButton.getText().toString();
//                String fullName = rRegName.getEditText().getText().toString();
//                String username = rRegUsername.getEditText().getText().toString();
//                String email = rRegEmail.getEditText().getText().toString();
//                String phoneNo = rRegPhoneNo.getEditText().getText().toString();
//                String nic_reg = rRegNIC.getEditText().getText().toString();
//                String password = rRegPassword.getEditText().getText().toString();
//
//                //Setting values inside users branch
//                UserHelperClass helperClass = new UserHelperClass(type,fullName, username, email, phoneNo, nic_reg, password);
//                reference.child(phoneNo).setValue(helperClass);
//            }
//        });
    }

    //Form validation
    private Boolean validateName(){
        String val = rRegName.getEditText().getText().toString();

        if(val.isEmpty()) {
            rRegName.setError("Field cannot be empty");
            return false;
        }
        else {
            rRegName.setError(null);
            rRegName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername(){
        String val = rRegUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()) {
            rRegUsername.setError("Field cannot be empty");
            return false;
        }
        else if(val.length()>=15) {
            rRegUsername.setError("Username is too long");
            return false;
        }
        else if(!val.matches(noWhiteSpace)) {
            rRegUsername.setError("White Spaces are not allowed");
            return false;
        }
        else {
            rRegUsername.setError(null);
            rRegUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = rRegEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            rRegEmail.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern)) {
            rRegEmail.setError("Invalid email address");
            return false;
        }
        else {
            rRegEmail.setError(null);
            rRegEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo(){
        String val = rRegPhoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            rRegPhoneNo.setError("Field cannot be empty");
            return false;
        }
        else {
            rRegPhoneNo.setError(null);
            rRegPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateNIC(){
        String val = rRegNIC.getEditText().getText().toString();

        if (val.isEmpty()) {
            rRegNIC.setError("Field cannot be empty");
            return false;
        }
        else {
            rRegNIC.setError(null);
            rRegNIC.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = rRegPassword.getEditText().getText().toString();
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
            rRegPassword.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(passwordVal)) {
            rRegPassword.setError("Password is too weak");
            return false;
        }
        else {
            rRegPassword.setError(null);
            rRegPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void registerUser(View view) {

        if (!validateName() | !validateUsername() | !validateEmail() | !validatePhoneNo() | !validateNIC() | !validatePassword()) {
            return;
        }

            //Save data in FireBase on button click
            //calling the FireBase database root node
            rootNode = FirebaseDatabase.getInstance();
            //calling the relevant reference
            reference = rootNode.getReference("users");

            //Get all the values from the textFields
            int selectedID = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(selectedID);
            String type = radioButton.getText().toString();
            String fullName = rRegName.getEditText().getText().toString();
            String username = rRegUsername.getEditText().getText().toString();
            String email = rRegEmail.getEditText().getText().toString();
            String phoneNo = rRegPhoneNo.getEditText().getText().toString();
            String nic_reg = rRegNIC.getEditText().getText().toString();
            String password = rRegPassword.getEditText().getText().toString();

            //Setting values inside users branch
            UserHelperClass helperClass = new UserHelperClass(type, fullName, username, email, phoneNo, nic_reg, password);
            reference.child(username).setValue(helperClass);
            Intent intent = new Intent(SignUp.this,MainActivity.class);
            startActivity(intent);
        }

        public void backToLogin(View view) {
            Intent intent = new Intent(SignUp.this,MainActivity.class);
            Pair[] pairs = new Pair[5];
            pairs[0] = new Pair<View,String>(logoText,"logo_text");
            pairs[1] = new Pair<View,String>(rRegUsername,"username_tran");
            pairs[2] = new Pair<View,String>(rRegPassword,"password_tran");
            pairs[3] = new Pair<View,String>(rRegBtn,"btn_login_tran");
            pairs[4] = new Pair<View,String>(rRegToLoginBtn,"login_signup_tran");

            ActivityOptions options = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);
            }
            startActivity(intent,options.toBundle());
        }
}