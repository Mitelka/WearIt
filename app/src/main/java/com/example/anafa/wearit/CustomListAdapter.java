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

    public CustomListAdapter(Activity context, String[] itemName, String[] itemPrice, String[] itemLink, Integer[] imageID) {
        super(context, R.layout.content_list_view_results, itemName);

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

        if(res == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            res = layoutInflater.inflate(R.layout.content_list_view_results, null, true);
            detailsViewHolder = new DetailsViewHolder(res);
            res.setTag(detailsViewHolder);
        } else {
            detailsViewHolder = (DetailsViewHolder) res.getTag();
        }

        detailsViewHolder.image.setImageResource(imageIDArr[position]);
        detailsViewHolder.nameTextView.setText(itemNameArr[position]);
        detailsViewHolder.priceTextView.setText(itemPriceArr[position]);
        detailsViewHolder.linkTextView.setText(itemLinkArr[position]);

        return res;
    }

    //    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        return null;
//    }
}
