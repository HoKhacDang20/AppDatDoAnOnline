package com.example.appdatdoanonline.datamodels;

public class CartItems {
    private String itemImage;
    private String itemName;
    private int iSLItem;
    private double iTemPrice;
    private boolean check = false;

    public CartItems() {
    }

    public CartItems(String itemImage, String itemName, int iSLItem, double iTemPrice) {
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.iSLItem = iSLItem;
        this.iTemPrice = iTemPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getiSLItem() {
        return iSLItem;
    }

    public void setiSLItem(int iSLItem) {
        this.iSLItem = iSLItem;
    }

    public double getiTemPrice() {
        return iTemPrice;
    }

    public void setiTemPrice(double iTemPrice) {
        this.iTemPrice = iTemPrice;
    }
}
