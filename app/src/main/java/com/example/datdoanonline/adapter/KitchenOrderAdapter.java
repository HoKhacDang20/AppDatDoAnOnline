package com.example.datdoanonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.datdoanonline.R;
import com.example.datdoanonline.data_models.BlackList;
import com.example.datdoanonline.data_models.CartItems;
import com.example.datdoanonline.data_models.DonHang;

import java.util.List;

public class KitchenOrderAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<DonHang> donHangList;
    private List<BlackList> blackLists;

    public KitchenOrderAdapter(Context context, int layout, List<DonHang> donHangList, List<BlackList> blackLists) {
        this.context = context;
        this.layout = layout;
        this.donHangList = donHangList;
        this.blackLists = blackLists;
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
        TextView txtMaDonHang, txtSoLuong, txtGiaDon, txtTinhTrang;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        double dPrice = 0.0;
        boolean bBlack = false;
        int iSoLuong = 0;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.txtMaDonHang = (TextView) convertView.findViewById(R.id.txtMaDonHang);
            viewHolder.txtSoLuong = (TextView) convertView.findViewById(R.id.txtSoLuong);
            viewHolder.txtGiaDon = (TextView) convertView.findViewById(R.id.txtGiaDon);
            viewHolder.txtTinhTrang = (TextView) convertView.findViewById(R.id.txtTinhTrang);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DonHang donHang = donHangList.get(position);

        for (CartItems cartItems : donHang.getCartItemsList()){
            dPrice += cartItems.getiTemPrice();
            iSoLuong += cartItems.getiSLItem();
        }

        for (BlackList blackList : blackLists){
            if(donHang.getsSDT().equals(blackList.getsSDT())){
                bBlack = true;
            }
        }

        viewHolder.txtGiaDon.setText(dPrice + "vnd");
        viewHolder.txtMaDonHang.setText(donHang.getsIDDonHang());
        viewHolder.txtSoLuong.setText(iSoLuong + "");
        if(bBlack == true){
            viewHolder.txtTinhTrang.setText("Black List!");
        }
        else {
            viewHolder.txtTinhTrang.setText("Normal!");
        }

        return convertView;
    }
}
