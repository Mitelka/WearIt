package com.example.anafa.wearit;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailsViewHolder {
    ImageView image;
    TextView nameTextView;
    TextView priceTextView;
    TextView linkTextView;

    public DetailsViewHolder(View v) {
        this.image = (ImageView) v.findViewById(R.id.itemImageView);
        this.nameTextView = (TextView) v.findViewById(R.id.itemNameTextView);
        this.priceTextView = (TextView) v.findViewById(R.id.itemPriceTextView);
        this.linkTextView = (TextView) v.findViewById(R.id.itemLinkTextView);
    }
}
