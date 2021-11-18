package com.example.appdatdoanonline.datamodels;

public class BlackList {
    private String sSDT;

    public BlackList() {
    }

    public BlackList(String sSDT) {
        this.sSDT = sSDT;
    }

    public String getsSDT() {
        return sSDT;
    }

    public void setsSDT(String sSDT) {
        this.sSDT = sSDT;
    }
}
