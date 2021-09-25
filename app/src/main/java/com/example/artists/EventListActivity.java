package com.example.artists;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artists.adapters.EventList;
import com.example.artists.model.Artist;
import com.example.artists.model.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class EventListActivity extends AppCompatActivity {

    //we will use these constants later to pass the artist name and id to another activity
    public static final String EVENT_NAME = "com.example.artists.eventName";
    public static final String EVENT_ID = "com.example.artists.eventId";
    public static final String USER_ID = "com.example.artists.userId";

    TextView textViewEvent;
    Button btnCreateEvent;
    ListView listEvents;

    //a list to store all the artist from firebase database
    List<Event> events;

    //our database reference object
    DatabaseReference databaseEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        //getting the reference of artists node
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");
        /*
         * this line is important
         * this time we are not getting the reference of a direct node
         * but inside the node track we are creating a new child with the artist id
         * and inside that node we will store all the tracks with unique ids
         * */
        //databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(intent.getStringExtra(MainActivity.ARTIST_ID));

        textViewEvent = findViewById(R.id.textViewEvent);
        btnCreateEvent =  findViewById(R.id.buttonAddEvent);
        listEvents = findViewById(R.id.listViewEvents);

        //list to store artists
        events = new ArrayList<>();

        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        listEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event event = events.get(i);
                showOptionMenu(event.getEventId(), event.getEventName(),
                                        event.getEventDate(),event.getEventDescription(),
                                        event.getEventVenue(),event.getEventCategory(),
                                        event.getEventContactName(),event.getEventContactNumber(),
                                            event.getEventContactEmail(),event.getEventContactNIC());
                return true;
            }
        });

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating an intent
                Intent intent = new Intent(EventListActivity.this, CreateEventActivity.class);

                //putting artist name and id to intent
                intent.putExtra(USER_ID,USER_ID);


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

                //clearing the previous artist list
                events.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Event event = postSnapshot.getValue(Event.class);
                    //adding artist to the list
                    events.add(event);
                }

                //creating adapter
                EventList eventAdapter = new EventList(EventListActivity.this, events);
                //attaching adapter to the listview
                listEvents.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showOptionMenu(final String eventId, String eventName,String eventDate,
                                        String eventDescr, String eventVenue, String eventCategory,
                                        String eventContactName, String eventContactNumber,
                                        String eventContactEmail, String eventContactNIC) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.event_list_menu, null);
        dialogBuilder.setView(dialogView);

        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateEvent);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteEvent);
        final Button buttonShare = (Button) dialogView.findViewById(R.id.buttonShareEvent);

        dialogBuilder.setTitle(eventName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showUpdateMenu(eventId);
                b.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDeleteDialog(eventId,eventName);
                b.dismiss();
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareEvent(eventName,eventDate,eventVenue,eventCategory,eventContactName,
                            eventContactNumber);
                b.dismiss();
            }
        });
    }

    private void showDeleteDialog(final String eventId, String eventName){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
        //final View dialogView = inflater.inflate(R.layout.delete_confirm, null);
        //dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Are you Sure to delete?");
        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteEvent(eventId);
            }

        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        final AlertDialog b = dialogBuilder.create();
        b.show();



//        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancelDelete);
//        final Button buttonConfirm = (Button) dialogView.findViewById(R.id.buttonConfirmDelete);
//
//
//        dialogBuilder.setTitle("Are you Sure to delete?");
//        final AlertDialog b = dialogBuilder.create();
//        b.show();
//
//        buttonCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                b.dismiss();
//            }
//        });
//
//        buttonConfirm.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                deleteEvent(eventId);
//                b.dismiss();
//            }
//        });


    }



    private void showUpdateMenu(final String eventId){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_event_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText event_Name = (EditText) dialogView.findViewById(R.id.et_eventName);
        final EditText event_Date = (EditText) dialogView.findViewById(R.id.et_eventDate);
        final EditText event_Venue = (EditText) dialogView.findViewById(R.id.et_eventVenue);
        final EditText event_Descr = (EditText) dialogView.findViewById(R.id.etml_eventDescription);
        final EditText event_ContactName = (EditText) dialogView.findViewById(R.id.et_contact_name);
        final EditText event_ContactNumber = (EditText) dialogView.findViewById(R.id.et_contact_phone);
        final EditText event_ContactNic= (EditText) dialogView.findViewById(R.id.et_contactNic);
        final EditText event_ContactEmail = (EditText) dialogView.findViewById(R.id.et_contactEmail);
        final Spinner event_Category = (Spinner) dialogView.findViewById(R.id.sp_eventCategory);

        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.btn_updateEvent);

        //Event event = getEvent(eventId);

        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("events").child(eventId);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Event event = dataSnapshot.getValue(Event.class);
                event_Name.setText(event.getEventName());
                event_Date.setText(event.getEventDate());
                event_Venue.setText(event.getEventVenue());
                event_Descr.setText(event.getEventDescription());
                event_ContactName.setText(event.getEventContactName());
                event_ContactNumber.setText(event.getEventContactName());
                event_ContactNic.setText(event.getEventContactNIC());
                event_ContactEmail.setText(event.getEventContactEmail());

                String eventCategory = event.getEventCategory();
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EventListActivity.this, R.array.eventCategories, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                event_Category.setAdapter(adapter);
                if (eventCategory != null) {
                    int spinnerPosition = adapter.getPosition(eventCategory);
                    event_Category.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        eventRef.addValueEventListener(postListener);

//        event_Name.setText(event.getEventName());
//        event_Date.setText(event.getEventDate());
//        event_Venue.setText(event.getEventVenue());
//        event_Descr.setText(event.getEventDescription());
//        event_ContactName.setText(event.getEventContactName());
//        event_ContactNumber.setText(event.getEventContactName());
//        event_ContactNic.setText(event.getEventContactNIC());
//        event_ContactEmail.setText(event.getEventContactEmail());
//
//        String eventCategory = event.getEventCategory();
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.eventCategories, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        event_Category.setAdapter(adapter);
//        if (eventCategory != null) {
//            int spinnerPosition = adapter.getPosition(eventCategory);
//            event_Category.setSelection(spinnerPosition);
//        }


        // Add the buttons
        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                String event_name = event_Name.getText().toString().trim();
                String event_date = event_Date.getText().toString().trim();
                String event_venue = event_Venue.getText().toString().trim();
                String event_description = event_Descr.getText().toString().trim();
                String event_contact_name = event_ContactName.getText().toString().trim();
                String event_contact_number = event_ContactNumber.getText().toString().trim();
                String event_contact_mail = event_ContactNic.getText().toString().trim();
                String event_contact_nic= event_ContactEmail.getText().toString().trim();
                String event_category = event_Category.getSelectedItem().toString();

                updateEvent(eventId,event_name,event_date,event_venue,event_description,
                        event_contact_name,event_contact_number,event_contact_mail,
                        event_contact_nic,event_category);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });

        LayoutInflater inflater1 = getLayoutInflater();
        final View updateHeading = inflater1.inflate(R.layout.update_heading, null);
        dialogBuilder.setCustomTitle(updateHeading);
        final AlertDialog b = dialogBuilder.create();
        b.show();


//        buttonUpdate.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                String event_name = event_Name.getText().toString().trim();
//                String event_date = event_Date.getText().toString().trim();
//                String event_venue = event_Venue.getText().toString().trim();
//                String event_description = event_Descr.getText().toString().trim();
//                String event_contact_name = event_ContactName.getText().toString().trim();
//                String event_contact_number = event_ContactNumber.getText().toString().trim();
//                String event_contact_mail = event_ContactNic.getText().toString().trim();
//                String event_contact_nic= event_ContactEmail.getText().toString().trim();
//                String event_category = event_Category.getSelectedItem().toString();
//
//                updateEvent(eventId,event_name,event_date,event_venue,event_description,
//                        event_contact_name,event_contact_number,event_contact_mail,
//                        event_contact_nic,event_category);
//                b.dismiss();
//            }
//        });
    }



    private boolean updateEvent(String eventId,String event_name,String event_date,
                                String event_venue,String event_description, String event_contact_name,
                                String event_contact_number,String event_contact_mail,
                                String event_contact_nic,String event_category) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(eventId);

        //updating event
        Event event = new Event(eventId,event_name,event_date,event_venue,event_description,
                event_category,event_contact_name,event_contact_number,event_contact_mail,
                event_contact_nic);
        dR.setValue(event);
        Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_LONG).show();
        return true;
    }


    private boolean deleteEvent(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(id);

        //removing artist
        dR.removeValue();

//        //getting the tracks reference for the specified artist
//        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);
//
//        //removing all tracks
//        drTracks.removeValue();
        Toast.makeText(getApplicationContext(), "Event Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    private boolean shareEvent(String eventHeading, String eventDate,
                               String eventVenue, String eventDescription,
                               String contactName,String contactNumber) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,"Event heading : "+eventHeading + "\n\n"
                +"Event date : " +eventDate + "\n\n"
                +"Event venue : " +eventVenue + "\n\n"
                +"Event description : " +eventDescription + "\n\n"
                +"Contact name : " +contactName + "\n\n"
                +"Contact number : " +contactNumber + "\n\n"
                +"----Shared from HOPE app----");
        sendIntent.setType("text/plain");
        Intent.createChooser(sendIntent,"Share via");
        startActivity(sendIntent);
        return true;
    }



}





