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

import com.example.artists.adapters.ArtistList;
import com.example.artists.adapters.PostList;
import com.example.artists.model.Artist;
import com.example.artists.model.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HopeHomeActivty extends AppCompatActivity {
    //we will use these constants later to pass the artist name and id to another activity
    public static final String EVENT_NAME = "com.example.artists.eventName";
    public static final String EVENT_ID = "com.example.artists.eventId";

    ConstraintLayout fundsLayout;
    ConstraintLayout eventsLayout;
    ListView listPosts;
    ProgressBar progressBar;

    //a list to store all the artist from firebase database
    List<Event> events;

    //our database reference object
    DatabaseReference databaseEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hope_home_activty);

        //getting the reference of artists node
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

        fundsLayout = findViewById(R.id.fundsLayout);
        eventsLayout = findViewById(R.id.eventsLayout);
        listPosts = findViewById(R.id.listViewPosts);
        progressBar = findViewById(R.id.progressBar2);


        fundsLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(HopeHomeActivty.this , FundPostListActivity.class));
            }
        });

        eventsLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(HopeHomeActivty.this , PostListActivity.class));
            }
        });

        //list to store artists
        events = new ArrayList<>();

        //attaching listener to listview
        listPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Event event = events.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), SinglePostActivity.class);

                //putting artist name and id to intent
                intent.putExtra(EVENT_ID, event.getEventId());
                intent.putExtra(EVENT_NAME, event.getEventName());

                //starting the activity with intent
                startActivity(intent);
            }
        });

//        listPosts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Artist artist = artists.get(i);
//                showUpdateDeleteDialog(artist.getArtistId(), artist.getArtistName(), artist.getArtistGenre());
//                return true;
//            }
//        });

    }

    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.VISIBLE);


                //clearing the previous artist list
                events.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting event
                    Event event = postSnapshot.getValue(Event.class);
                    //adding artist to the list
                    events.add(event);
                }

                //creating adapter
                PostList posAdapter = new PostList(HopeHomeActivty.this, events);
                //attaching adapter to the listview
                listPosts.setAdapter(posAdapter);

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDeleteDialog(final String artistId, String artistName, String artistGenre) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextType = (EditText) dialogView.findViewById(R.id.editTextType);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteArtist);
        final Button buttonShare = (Button) dialogView.findViewById(R.id.buttonShareArtist);

        dialogBuilder.setTitle(artistName);
        final AlertDialog b = dialogBuilder.create();
        b.show();
        editTextName.setText(artistName);
        editTextType.setText(artistGenre);



        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String genre = editTextType.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(genre)) {
                    updateArtist(artistId, name, genre);
                    b.dismiss();
                }

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //deleteArtist(artistId);
                //b.dismiss();
                showDeleteDialog(artistId, artistName, artistGenre);
                b.dismiss();
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareArtist(artistId,artistName,artistGenre);
                b.dismiss();
            }
        });
    }

    private void showDeleteDialog(final String artistId, String artistName, String artistGenre){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_confirm, null);
        dialogBuilder.setView(dialogView);

        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancelDelete);
        final Button buttonConfirm = (Button) dialogView.findViewById(R.id.buttonConfirmDelete);
        //final TextView textview = (TextView) dialogView.findViewById(R.id.textView2);

        dialogBuilder.setTitle("Are you Sure to delete?");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                showUpdateDeleteDialog(artistId,artistName,artistGenre);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                deleteArtist(artistId);
                b.dismiss();
            }
        });
    }



    private boolean updateArtist(String id, String name, String genre) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(id);

        //updating artist
        Event event = new Event(id, name, genre);
        dR.setValue(event);
        Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteArtist(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(id);

        //removing artist
        dR.removeValue();

//        //getting the tracks reference for the specified artist
//        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);
//
//        //removing all tracks
//        drTracks.removeValue();
        Toast.makeText(getApplicationContext(), "event Deleted", Toast.LENGTH_LONG).show();

        return true;
    }
    private boolean shareArtist(String id, String name, String venue) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(id);

        Event event = new Event(id, name, venue);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,event.getEventName()+ ", " + event.getEventVenue() );
        sendIntent.setType("text/plain");
        Intent.createChooser(sendIntent,"Share via");
        startActivity(sendIntent);
        return true;
    }
}