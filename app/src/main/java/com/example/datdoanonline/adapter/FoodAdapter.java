package com.example.datdoanonline.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.bumptech.glide.Glide;
import com.example.datdoanonline.R;
import com.example.datdoanonline.data_models.MonAn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class FoodAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<MonAn> monAnList;

    public FoodAdapter(Context context, int layout, List<MonAn> monAnList) {
        this.context = context;
        this.layout = layout;
        this.monAnList = monAnList;
    }

    @Override
    public int getCount() {
        return monAnList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView imgMonAn;
        TextView txtFoodName, txtFoodPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.imgMonAn = (ImageView) convertView.findViewById(R.id.imgMonAn);
            viewHolder.txtFoodName = (TextView) convertView.findViewById(R.id.txtFoodName);
            viewHolder.txtFoodPrice = (TextView) convertView.findViewById(R.id.txtFoodPrice);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MonAn monAn = monAnList.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(monAn.getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgMonAn);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.txtFoodName.setText(monAn.getFoodName());
        viewHolder.txtFoodPrice.setText(monAn.getFoodPrice() + "vnd");

        return convertView;
    }
}
