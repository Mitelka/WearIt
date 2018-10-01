package com.example.anafa.wearit;

public class Item {
    private String itemListName;
    private int itemListImage;
    private String itemListPrice;
    private String itemListLink;

    public Item(String itemName, int itemImage, String itemPrice, String itemLink)
    {
        this.itemListName = itemName;
        this.itemListImage = itemImage;
        this.itemListPrice = itemPrice;
        this.itemListLink = itemLink;
    }

    public String getItemListName() {
        return itemListName;
    }

    public int getItemListImage() {
        return itemListImage;
    }

    public String getItemListPrice() {
        return itemListPrice;
    }

    public String getItemListLink() {
        return itemListLink;
    }
}
