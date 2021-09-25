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

public class PostList extends ArrayAdapter<Event> {
    private Activity context;
    List<Event> events;

    public PostList(Activity context, List<Event> events) {
        super(context, R.layout.post_layout, events);
        this.context = context;
        this.events = events;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.post_layout, null, true);

        TextView textViewHeading = (TextView) listViewItem.findViewById(R.id.postHeading);
        TextView textViewDecrp = (TextView) listViewItem.findViewById(R.id.postDescript);
        TextView textViewCategory = (TextView) listViewItem.findViewById(R.id.postCategory);

        Event event = events.get(position);
        textViewHeading.setText(event.getEventName());
        textViewDecrp.setText(event.getEventDescription());
        textViewCategory.setText(event.getEventCategory());

        return listViewItem;
    }
}