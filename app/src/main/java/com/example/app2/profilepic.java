package com.example.app2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class profilepic extends AppCompatActivity {

    //widgets
    private Button UploadBtn, Showbtn;
    private ImageView imageView2;
    private ProgressBar progressBar;
    //counters
    private  int mCounter = 0;
    Button btn;
    TextView tx;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepic);

        //all the ids
        UploadBtn = findViewById(R.id.button4);
        Showbtn = findViewById(R.id.button6);
        imageView2 = findViewById(R.id.imageView2);
        progressBar = findViewById(R.id.progressBar);
//counters
        tx = (TextView) findViewById(R.id.tx);
        btn = (Button) findViewById(R.id.bt);

        progressBar.setVisibility(View.INVISIBLE);



        Showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profilepic.this, donor_profilemanagement.class);
                startActivity(intent);
            }
        });

                imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent galleryIntent = new Intent();
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        //get all type of images
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 2);

                    }
                });

                UploadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (imageUri != null) {

                            UploadToFirebase(imageUri);

                        } else {
                            Toast.makeText(profilepic.this, "Please select image", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                //counters
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCounter ++;
                tx.setText(Integer.toString(mCounter));
            }
        });
            }


            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

                    imageUri = data.getData();
                    imageView2.setImageURI(imageUri);

                }
            }

            private void UploadToFirebase(Uri uri) {

                StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
                fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Model model = new Model(uri.toString());
                                String modelId = root.push().getKey();
                                root.child(modelId).setValue(model);
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(profilepic.this, "Uploaded Successful", Toast.LENGTH_SHORT);


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

                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(profilepic.this, "Uploading Failed!!!", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            private String getFileExtension(Uri mUri) {

                ContentResolver cr = getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                return mime.getExtensionFromMimeType(cr.getType(mUri));
            }}