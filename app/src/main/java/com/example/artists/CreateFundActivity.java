package com.example.artists;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artists.model.Fund;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateFundActivity extends AppCompatActivity {

    //we will use these constants later to pass the artist name and id to another activity
    public static final String ARTIST_NAME = "com.example.artists.artistname";
    public static final String ARTIST_ID = "com.example.artists.artistid";

    EditText fundName, fundDate, fundAmount, fundDescription,
            fundContactName, fundContactNumber, fundContactNIC,
            fundContactMail;

    Spinner fundCategory;
    ImageView fundCoverImage;
    Button btncreateFund,btnClose;
    Uri imageUri;
    ProgressBar progressBar;
    Fund fund;

    DatabaseReference fundRef;
    private StorageReference fundStorageRef = FirebaseStorage.getInstance().getReference();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("funds");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_fund);

        fundRef = FirebaseDatabase.getInstance().getReference("funds");

        fundName = findViewById(R.id.et_fundName);
        fundDate = findViewById(R.id.et_fundDate);
        fundAmount = findViewById(R.id.et_fundAmount);
        fundDescription = findViewById(R.id.etml_fundDescription);
        fundContactName = findViewById(R.id.et_contact_name);
        fundContactNumber = findViewById(R.id.et_contact_phone);
        fundContactMail = findViewById(R.id.et_contactEmail);
        fundContactNIC = findViewById(R.id.et_contactNic);
        fundCategory = findViewById(R.id.sp_fundCategory);
        btncreateFund = findViewById(R.id.btn_submitFund);
        btnClose = findViewById(R.id.btnClose);
        fundCoverImage = findViewById(R.id.ib_fundCoverImg);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        /*
        *Image upload
         */

        fundCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                //get all type of images
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2);

            }
        });

        btncreateFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageUri != null) {

                    UploadToFirebase(imageUri);

                }else {
                    Toast.makeText(CreateFundActivity.this,"Please select image",Toast.LENGTH_SHORT).show();
                }
            }
        });




        /*
         *Image upload
         */

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.fundCategories)){
            @Override
            public boolean isEnabled(int position){

                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fundCategory.setAdapter(myAdapter);


        //adding an onclicklistener to button
        btncreateFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addFund();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateFundActivity.this , FundListActivity.class));
            }
        });

        fundCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                //get all type of images
                galleryIntent.setType("image/*");

            }
        });

        btncreateFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imageUri!= null) {

                    UploadToFirebase(imageUri);
                }
                else {

                    Toast.makeText(CreateFundActivity.this,"Please select image",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    /*
     * Image Upload
     * */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            fundCoverImage.setImageURI(imageUri);

        }
    }


    private void UploadToFirebase(Uri uri) {

        StorageReference fileRef = fundStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Fund fund = new Fund(uri.toString());
                        String  FundId = root.push().getKey();
                        root.child(FundId).setValue(fund);

                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(CreateFundActivity.this,"Success",Toast.LENGTH_SHORT);
//                        imageView1.setImageResource(R.drawable.ic_launcher_background);

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                progressBar.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(CreateFundActivity.this,"Uploading Failed!!!",Toast.LENGTH_SHORT).show();

            }
        });

    }



    /*
     * Image Upload
     * */

    /*
     * This method is saving a new artist to the
     * Firebase Realtime Database
     * */
    private void addFund() {
        //getting the values to save
        String fund_name = fundName.getText().toString().trim();
        String fund_date = fundDate.getText().toString().trim();
        String fund_amount = fundAmount.getText().toString().trim();
        String fund_description = fundDescription.getText().toString().trim();
        String fund_contact_name = fundContactName.getText().toString().trim();
        String fund_contact_number = fundContactNumber.getText().toString().trim();
        String fund_contact_mail = fundContactMail.getText().toString().trim();
        String fund_contact_nic= fundContactNIC.getText().toString().trim();
        String fund_category = fundCategory.getSelectedItem().toString();
        //String event_category = "Blood donation";


        //checking if the value is provided
        if (!TextUtils.isEmpty(fund_name) && !TextUtils.isEmpty(fund_date) &&
                !TextUtils.isEmpty(fund_amount) && !TextUtils.isEmpty(fund_description) &&
                !TextUtils.isEmpty(fund_contact_name) && !TextUtils.isEmpty(fund_contact_number) &&
                !TextUtils.isEmpty(fund_contact_mail) && !TextUtils.isEmpty(fund_contact_nic) &&
                !(fundCategory.getSelectedItemPosition() == 0) ) {


            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
             String fund_ID  = fundRef.push().getKey();
           // String fundId = "asdf" ;

            //creating an Artist Object

            Fund fund = new Fund( fund_ID,  fund_name,  fund_date,
                    fund_amount,  fund_description, fund_category, fund_contact_name,
                    fund_contact_number,  fund_contact_mail,  fund_contact_nic);

            //Saving the Artist
            //assert fund_ID != null;
            fundRef.child(fund_ID).setValue(fund);

            //setting edittext to blank again
            //editTextName.setText("");
            //editTextType.setText("");

            //displaying a success toast
            Toast.makeText(this, "Fund added", Toast.LENGTH_LONG).show();
            startActivity(new Intent(CreateFundActivity.this , HopeHomeActivty.class));
        } else {
            //if the value is not given displaying a toast
            if(fundCategory.getSelectedItemPosition() == 0){
                Toast.makeText(this, "Please select an fund category", Toast.LENGTH_LONG).show();
            }
            if(TextUtils.isEmpty(fund_name))
                Toast.makeText(this, "Please fill the fund name", Toast.LENGTH_LONG).show();
            if(TextUtils.isEmpty(fund_date))
                Toast.makeText(this, "Please fill the fund date", Toast.LENGTH_LONG).show();
            if(TextUtils.isEmpty(fund_amount))
                Toast.makeText(this, "Please fill the fund venue", Toast.LENGTH_LONG).show();
            if(TextUtils.isEmpty(fund_description))
                Toast.makeText(this, "Please fill the fund description", Toast.LENGTH_LONG).show();

//            if (imageUri != null){
//                Toast.makeText(CreateEventActivity.this,"Please select image",Toast.LENGTH_SHORT).show();
//            }

        }
    }



    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }




}