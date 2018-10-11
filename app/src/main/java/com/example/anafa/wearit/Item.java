package com.example.anafa.wearit;

public class Item {
    private String itemListName;
    private String itemListImage;
    private String itemListPrice;
    private String itemListLink;
    private String itemListStars;


    public Item(String itemName, String itemImage, String itemPrice, String itemLink, String itemStars)
    {
        this.itemListName = itemName;
        this.itemListImage = itemImage;
        this.itemListPrice = itemPrice;
        this.itemListLink = itemLink;
        this.itemListStars = itemStars;
    }

    public String getItemListName() {
        return itemListName;
    }

    public String getItemListImage() {
        return itemListImage;
    }

    public String getItemListPrice() {
        return itemListPrice;
    }

    public String getItemListLink() {
        return itemListLink;
    }

    public String getItemListStars() {
        return itemListStars;
    }
}
