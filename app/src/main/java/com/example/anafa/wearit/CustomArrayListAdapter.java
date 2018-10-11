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
    int showResultsAt;

    public CustomArrayListAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);

        itemList = objects;
        showResultsAt = textViewResourceId;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(showResultsAt, null);

        TextView itemNameTextView = (TextView) convertView.findViewById(R.id.itemNameTextView);
        ImageView itemImageView = (ImageView) convertView.findViewById(R.id.itemImageView);
        TextView itemPriceTextView = (TextView) convertView.findViewById(R.id.itemPriceTextView);
        TextView itemLinkTextView = (TextView) convertView.findViewById(R.id.itemLinkTextView);

        itemNameTextView.setText(itemList.get(position).getItemListName());
        //itemImageView.setImageResource(itemList.get(position).getItemListImage());
        itemPriceTextView.setText(itemList.get(position).getItemListPrice());
        itemLinkTextView.setText(itemList.get(position).getItemListLink());


        TextView itemStarsTextView = (TextView) convertView.findViewById(R.id.itemStarsTextView);
        itemStarsTextView.setText(itemList.get(position).getItemListStars() + "");

//        if(itemList.get(position).getItemListStars() != 0){
//            TextView itemStarsTextView = (TextView) convertView.findViewById(R.id.itemStarsTextView);
//            itemStarsTextView.setText(itemList.get(position).getItemListStars() + "");
//        } else {
//            TextView itemStarsTextView = (TextView) convertView.findViewById(R.id.itemStarsTextView);
//            itemStarsTextView.setText(itemList.get(position).getItemListStars() + "");
//        }
        return convertView;
    }
}
