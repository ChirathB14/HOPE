
package com.example.artists;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.artists.adapters.FundPostList;
import com.example.artists.model.Event;
import com.example.artists.model.Fund;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FundPostListActivity extends AppCompatActivity {

    //we will use these constants later to pass the artist name and id to another activity
    public static final String EVENT_NAME = "com.example.artists.eventName";
    public static final String EVENT_ID = "com.example.artists.eventId";

    //ConstraintLayout fundsLayout;
    //ConstraintLayout eventsLayout;
    ListView listPosts;
    ProgressBar progressBar;

    //a list to store all the artist from firebase database
    List<Fund> funds;

    //our database reference object
    DatabaseReference databaseEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_post_list);


        //getting the reference of artists node
        databaseEvents = FirebaseDatabase.getInstance().getReference("funds");

//        fundsLayout = findViewById(R.id.fundsLayout);
//        eventsLayout = findViewById(R.id.eventsLayout);
        listPosts = findViewById(R.id.listViewPosts);
        progressBar = findViewById(R.id.progressBar2);


//        fundsLayout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(PostListActivity.this , CreateEventActivity.class));
//            }
//        });
//
//        eventsLayout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(PostListActivity.this , CreateEventActivity.class));
//            }
//        });

        //list to store artists
        funds = new ArrayList<>();

        //attaching listener to listview
        listPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Fund event = funds.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), SinglePostActivity.class);

                //putting artist name and id to intent
                intent.putExtra(EVENT_ID, event.getFundId());
                intent.putExtra(EVENT_NAME, event.getFundName());

                //starting the activity with intent
                startActivity(intent);
            }
        });
    }

    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.VISIBLE);


                //clearing the previous artist list
                funds.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting event
                    Fund fund = postSnapshot.getValue(Fund.class);
                    //adding artist to the list
                    funds.add(fund);
                }

                //creating adapter
                FundPostList posAdapter = new FundPostList(FundPostListActivity.this, funds);
                //attaching adapter to the listview
                listPosts.setAdapter(posAdapter);

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

