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

public class ShipperPacketAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<DonHang> donHangList;

    public ShipperPacketAdapter(Context context, int layout, List<DonHang> donHangList) {
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
        TextView txtGuestName, txtGuestSDT, txtGuestAddress, txtPacketPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        double dPrice = 0.0;
        boolean bBlack = false;


        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.txtGuestName = (TextView) convertView.findViewById(R.id.txtGuestName);
            viewHolder.txtGuestSDT = (TextView) convertView.findViewById(R.id.txtGuestSDT);
            viewHolder.txtGuestAddress = (TextView) convertView.findViewById(R.id.txtGuestAddress);
            viewHolder.txtPacketPrice = (TextView) convertView.findViewById(R.id.txtPacketPrice);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DonHang donHang = donHangList.get(position);

        for (CartItems cartItems : donHang.getCartItemsList()){
            dPrice += cartItems.getiTemPrice();
        }

        viewHolder.txtPacketPrice.setText(dPrice + "vnd");
        viewHolder.txtGuestSDT.setText(donHang.getsSDT());
        viewHolder.txtGuestName.setText(donHang.getsHoTen());
        viewHolder.txtGuestAddress.setText(donHang.getsDiaChi());

        return convertView;
    }
}
