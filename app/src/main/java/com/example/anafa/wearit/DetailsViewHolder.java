package com.example.anafa.wearit;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailsViewHolder {
    private ImageView image;
    private TextView nameTextView;
    private TextView priceTextView;
    private TextView linkTextView;

    public DetailsViewHolder(View v) {
        this.image = (ImageView) v.findViewById(R.id.itemImageView);
        this.nameTextView = (TextView) v.findViewById(R.id.itemNameTextView);
        this.priceTextView = (TextView) v.findViewById(R.id.itemPriceTextView);
        this.linkTextView = (TextView) v.findViewById(R.id.itemLinkTextView);
    }

    public ImageView getImage() {
        return image;
    }

    public TextView getNameTextView() {
        return nameTextView;
    }

    public TextView getPriceTextView() {
        return priceTextView;
    }

    public TextView getLinkTextView() {
        return linkTextView;
    }
}
