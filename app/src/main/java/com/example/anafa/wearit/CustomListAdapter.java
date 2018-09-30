package com.example.anafa.wearit;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CustomListAdapter extends ArrayAdapter<String> {
    private String[] itemNameArr;
    private String[] itemPriceArr;
    private String[] itemLinkArr;
    private Integer[] imageIDArr;
    private Activity context;

//    public CustomListAdapter(Activity context, String[] itemName, String[] itemPrice, String[] itemLink, Integer[] imageID) {
//        super(context, R.layout.content_list_view_results, itemName);
//
//        this.itemNameArr = itemName;
//        this.itemPriceArr = itemPrice;
//        this.itemLinkArr = itemLink;
//        this.imageIDArr = imageID;
//        this.context = context;
//    }

    public CustomListAdapter(Activity context, String[] itemName, String[] itemPrice, String[] itemLink, Integer[] imageID, int content_view_results) {
        super(context, content_view_results, itemName);

        this.itemNameArr = itemName;
        this.itemPriceArr = itemPrice;
        this.itemLinkArr = itemLink;
        this.imageIDArr = imageID;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View res = convertView;
        DetailsViewHolder detailsViewHolder = null;


//        LayoutInflater layoutInflater = context.getLayoutInflater();
//        res = layoutInflater.inflate(R.layout.content_list_view_results, null);
//        detailsViewHolder = new DetailsViewHolder(res);
        //res.setTag(detailsViewHolder);

        if(res == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            res = layoutInflater.inflate(R.layout.content_list_view_results, null, true);
            detailsViewHolder = new DetailsViewHolder(res);
            res.setTag(detailsViewHolder);
        } else {
            detailsViewHolder = (DetailsViewHolder) res.getTag();
        }

        detailsViewHolder.getImage().setImageResource(imageIDArr[position]);
        detailsViewHolder.getNameTextView().setText(itemNameArr[position]);
        detailsViewHolder.getPriceTextView().setText(itemPriceArr[position]);
        detailsViewHolder.getLinkTextView().setText(itemLinkArr[position]);

        return res;
    }

    @Override
    public int getCount() {
        return itemNameArr.length;
    }


//    @Override
//    public Object getItem(int i) {
//        return null;
//    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}
