package com.example.artists.adapters;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.artists.R;
import com.example.artists.model.Fund;

import java.util.List;

public class FundPostList extends ArrayAdapter<Fund> {
    private Activity context;
    List<Fund> funds;

    public FundPostList(Activity context, List<Fund> funds) {
        super(context, R.layout.post_layout, funds);
        this.context = context;
        this.funds = funds;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.post_layout, null, true);

        TextView textViewHeading = (TextView) listViewItem.findViewById(R.id.postHeading);
        TextView textViewDecrp = (TextView) listViewItem.findViewById(R.id.postDescript);
        TextView textViewCategory = (TextView) listViewItem.findViewById(R.id.postCategory);

        Fund fund = funds.get(position);
        textViewHeading.setText(fund.getFundName());
        textViewDecrp.setText(fund.getFundDescription());
        textViewCategory.setText(fund.getFundCategory());

        return listViewItem;
    }
}