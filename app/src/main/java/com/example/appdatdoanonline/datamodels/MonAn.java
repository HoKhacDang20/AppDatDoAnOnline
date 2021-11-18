package com.example.appdatdoanonline.datamodels;

public class MonAn {
    public String foodName, foodDescribe;
    public double foodPrice;
    private boolean check = false;
    public String image;
    public int iType;

    public MonAn() {
    }

    public MonAn(String foodName, String foodDescribe, double foodPrice, String image, int iType) {
        this.foodName = foodName;
        this.foodDescribe = foodDescribe;
        this.foodPrice = foodPrice;
        this.image = image;
        this.iType = iType;
    }

    public MonAn(String foodName, String foodDescribe, double foodPrice, String image) {
        this.foodName = foodName;
        this.foodDescribe = foodDescribe;
        this.foodPrice = foodPrice;
        this.image = image;
    }

    public MonAn(String foodName, String foodDescribe, double foodPrice) {
        this.foodName = foodName;
        this.foodDescribe = foodDescribe;
        this.foodPrice = foodPrice;
    }

    public int getiType() {
        return iType;
    }

    public void setiType(int iType) {
        this.iType = iType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescribe() {
        return foodDescribe;
    }

    public void setFoodDescribe(String foodDescribe) {
        this.foodDescribe = foodDescribe;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
