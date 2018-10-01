package com.example.anafa.wearit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomArrayListAdapter extends ArrayAdapter {

    ArrayList<Item> itemList = new ArrayList<Item>();

    public CustomArrayListAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);

        itemList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.content_grid_view_results, null);

        TextView itemNameTextView = (TextView) v.findViewById(R.id.itemNameTextView);
        ImageView itemImageView = (ImageView) v.findViewById(R.id.itemImageView);
        TextView itemPriceTextView = (TextView) v.findViewById(R.id.itemPriceTextView);
        TextView itemLinkTextView = (TextView) v.findViewById(R.id.itemLinkTextView);

        itemNameTextView.setText(itemList.get(position).getItemListName());
        itemImageView.setImageResource(itemList.get(position).getItemListImage());
        itemPriceTextView.setText(itemList.get(position).getItemListPrice());
        itemLinkTextView.setText(itemList.get(position).getItemListLink());

        return v;
    }
}
