package com.example.artists.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.artists.R;
import com.example.artists.model.Event;

import java.util.List;

public class EventList extends ArrayAdapter<Event> {
    private Activity context;
    List<Event> events;

    public EventList(Activity context, List<Event> events) {
        super(context, R.layout.layout_event_list, events);
        this.context = context;
        this.events = events;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_event_list, null, true);

        TextView textViewHeading = (TextView) listViewItem.findViewById(R.id.textViewEventName);
        TextView textViewCategory = (TextView) listViewItem.findViewById(R.id.textViewEventCategory);

        Event event = events.get(position);
        textViewHeading.setText(event.getEventName());
        textViewCategory.setText(event.getEventCategory());

        return listViewItem;
    }
}