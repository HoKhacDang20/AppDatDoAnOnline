package com.example.appdatdoanonline.datamodels;

import java.util.ArrayList;

public class DonHang {
    private String sIDDonHang, sHoTen, sUserName, sSDT, sDiaChi;
    private ArrayList<CartItems> cartItemsList;
    private int iTinhTrang = 0;
    private boolean Check = false;
    private String oke;

    public DonHang() {
    }

    public DonHang(String sIDDonHang, String sUserName, String sHoTen, String sSDT, String sDiaChi, ArrayList<CartItems> cartItemsList, int iTinhTrang) {
        this.sIDDonHang = sIDDonHang;
        this.sUserName = sUserName;
        this.sHoTen = sHoTen;
        this.sSDT = sSDT;
        this.sDiaChi = sDiaChi;
        this.cartItemsList = cartItemsList;
        this.iTinhTrang = iTinhTrang;
    }

    public String getsHoTen() {
        return sHoTen;
    }

    public void setsHoTen(String sHoTen) {
        this.sHoTen = sHoTen;
    }

    public boolean isCheck() {
        return Check;
    }

    public void setCheck(boolean check) {
        Check = check;
    }

    public String getsIDDonHang() {
        return sIDDonHang;
    }

    public void setsIDDonHang(String sIDDonHang) {
        this.sIDDonHang = sIDDonHang;
    }

    public String getsUserName() {
        return sUserName;
    }

    public void setsUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    public String getsSDT() {
        return sSDT;
    }

    public void setsSDT(String sSDT) {
        this.sSDT = sSDT;
    }

    public String getsDiaChi() {
        return sDiaChi;
    }

    public void setsDiaChi(String sDiaChi) {
        this.sDiaChi = sDiaChi;
    }

    public ArrayList<CartItems> getCartItemsList() {
        return cartItemsList;
    }

    public void setCartItemsList(ArrayList<CartItems> cartItemsList) {
        this.cartItemsList = cartItemsList;
    }

    public int getiTinhTrang() {
        return iTinhTrang;
    }

    public void setiTinhTrang(int iTinhTrang) {
        this.iTinhTrang = iTinhTrang;
    }
}

