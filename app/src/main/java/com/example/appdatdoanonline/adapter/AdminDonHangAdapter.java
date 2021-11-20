package com.example.appdatdoanonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.appdatdoanonline.R;
import com.example.appdatdoanonline.data_models.BlackList;
import com.example.appdatdoanonline.data_models.CartItems;
import com.example.appdatdoanonline.data_models.DonHang;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminDonHangAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<DonHang> donHangList;
    private List<BlackList> blackLists;

    public AdminDonHangAdapter(Context context, int layout, List<DonHang> donHangList, List<BlackList> blackLists) {
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
        TextView txtSDT, txtUserName, txtTongTien, txtTinhTrang;
        CheckBox cbDonHang;
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
            viewHolder.txtSDT = (TextView) convertView.findViewById(R.id.txtSDT);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            viewHolder.txtTongTien = (TextView) convertView.findViewById(R.id.txtTongTien);
            viewHolder.txtTinhTrang = (TextView) convertView.findViewById(R.id.txtTinhTrang);
            viewHolder.cbDonHang = (CheckBox) convertView.findViewById(R.id.cbDonHang);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DonHang donHang = donHangList.get(position);

        if(donHang.isCheck()){
            viewHolder.cbDonHang.setChecked(true);
        }
        else {
            viewHolder.cbDonHang.setChecked(false);
        }

        if(donHang.getiTinhTrang() > 0){
            viewHolder.cbDonHang.setEnabled(false);
        }
        else {
            viewHolder.cbDonHang.setEnabled(true);
        }

        viewHolder.cbDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.cbDonHang.isChecked()){
                    donHang.setCheck(true);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("DonHang").child(donHang.getsIDDonHang()).child("check").setValue(true);
                }
                else if(!viewHolder.cbDonHang.isChecked()){
                    donHang.setCheck(false);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("DonHang").child(donHang.getsIDDonHang()).child("check").setValue(false);
                }
            }
        });

        for (CartItems cartItems : donHang.getCartItemsList()){
            dPrice += cartItems.getiTemPrice();
        }

        for (BlackList blackList : blackLists){
            if(donHang.getsSDT().equals(blackList.getsSDT())){
                bBlack = true;
            }
        }

        viewHolder.txtTongTien.setText(dPrice + "vnd");
        viewHolder.txtSDT.setText(donHang.getsSDT());
        viewHolder.txtUserName.setText(donHang.getsHoTen());
        if(bBlack == true){
            viewHolder.txtTinhTrang.setText("Black List!");
        }
        else {
            viewHolder.txtTinhTrang.setText("Normal!");
        }

        return convertView;
    }
}
