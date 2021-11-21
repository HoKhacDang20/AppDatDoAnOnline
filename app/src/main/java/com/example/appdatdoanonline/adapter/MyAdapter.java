package com.example.appdatdoanonline.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appdatdoanonline.R;
import com.example.appdatdoanonline.data_models.MonAn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<MonAn> monAnList;

    public MyAdapter(Context context, int layout, List<MonAn> monAnList) {
        this.context = context;
        this.layout = layout;
        this.monAnList = monAnList;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView lblName, lblPrice;
        CheckBox chkb;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.lblName = (TextView) convertView.findViewById(R.id.lblName);
            viewHolder.lblPrice = (TextView) convertView.findViewById(R.id.lblPrice);
            viewHolder.chkb = (CheckBox) convertView.findViewById(R.id.chkb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MonAn monAn = monAnList.get(position);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(monAn.getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.chkb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.chkb.isChecked()) {
                    monAn.setCheck(true);
                } else if (!viewHolder.chkb.isChecked()) {
                    monAn.setCheck(false);
                }
            }
        });

        viewHolder.lblName.setText(monAn.getFoodName());
        viewHolder.lblPrice.setText(monAn.getFoodPrice() + "vnd");

        return convertView;
    }


}
