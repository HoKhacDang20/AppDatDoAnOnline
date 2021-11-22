package com.example.datdoanonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.datdoanonline.R;
import com.example.datdoanonline.data_models.CartItems;
import com.example.datdoanonline.data_models.DonHang;

import java.util.List;

public class UserDonHangAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<DonHang> donHangList;

    public UserDonHangAdapter(Context context, int layout, List<DonHang> donHangList) {
        this.context = context;
        this.layout = layout;
        this.donHangList = donHangList;
    }

    @Override
    public int getCount() {
        return donHangList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        TextView txtMaDonHang, txtItemsQuantity, txtTongTien, txtTinhTrang;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        int iSoLuong = 0;
        double dPrice = 0.0;
        String sTinhTrang = "";

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.txtItemsQuantity = (TextView) convertView.findViewById(R.id.txtItemsQuantity);
            viewHolder.txtMaDonHang = (TextView) convertView.findViewById(R.id.txtMaDonHang);
            viewHolder.txtTongTien = (TextView) convertView.findViewById(R.id.txtTongTien);
            viewHolder.txtTinhTrang = (TextView) convertView.findViewById(R.id.txtTinhTrang);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DonHang donHang = donHangList.get(position);

        if(donHang.getiTinhTrang() == 1){
            sTinhTrang = "Processing";
        }
        else if(donHang.getiTinhTrang() == 3){
            sTinhTrang = "Completed";
        }
        else if(donHang.getiTinhTrang() == 0){
            sTinhTrang = "Pending";
        }
        else if(donHang.getiTinhTrang() == 2){
            sTinhTrang = "Delivery";
        }

        for (CartItems cartItems : donHang.getCartItemsList()){
            dPrice += cartItems.getiTemPrice();
            iSoLuong += cartItems.getiSLItem();
        }

        viewHolder.txtItemsQuantity.setText("" + iSoLuong);
        viewHolder.txtMaDonHang.setText(donHang.getsIDDonHang());
        viewHolder.txtTinhTrang.setText(sTinhTrang);
        viewHolder.txtTongTien.setText(dPrice + "vnd");

        return convertView;
    }
}
