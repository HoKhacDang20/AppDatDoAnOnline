package com.example.datdoanonline.data_models;

public class UserData {
    private String sUserName, sFullName, sSdt, sGioiTinh, sDiaChi, sPassword;
    private int iPermission;
    public UserData() {

    }

    public UserData(String sUserName, String sFullName, String sSdt, String sGioiTinh, String sDiaChi, String sPassword, int iPermission) {
        this.sUserName = sUserName;
        this.sFullName = sFullName;
        this.sSdt = sSdt;
        this.sGioiTinh = sGioiTinh;
        this.sDiaChi = sDiaChi;
        this.sPassword = sPassword;
        this.iPermission = iPermission;
    }

    public String getsUserName() {
        return sUserName;
    }

    public void setsUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    public String getsFullName() {
        return sFullName;
    }

    public void setsFullName(String sFullName) {
        this.sFullName = sFullName;
    }

    public String getsSdt() {
        return sSdt;
    }

    public void setsSdt(String sSdt) {
        this.sSdt = sSdt;
    }

    public String getsGioiTinh() {
        return sGioiTinh;
    }

    public void setsGioiTinh(String sGioiTinh) {
        this.sGioiTinh = sGioiTinh;
    }

    public String getsDiaChi() {
        return sDiaChi;
    }

    public void setsDiaChi(String sDiaChi) {
        this.sDiaChi = sDiaChi;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public int getiPermission() {
        return iPermission;
    }

    public void setiPermission(int iPermission) {
        this.iPermission = iPermission;
    }
}
