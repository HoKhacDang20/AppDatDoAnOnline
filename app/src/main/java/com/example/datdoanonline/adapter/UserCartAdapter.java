package com.example.datdoanonline.adapter;

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

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.datdoanonline.R;
import com.example.datdoanonline.data_models.CartItems;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UserCartAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<CartItems> cartItemsList;
    private String sUserName;

    public UserCartAdapter(Context context, int layout, List<CartItems> cartItemsList, String sUserName) {
        this.context = context;
        this.layout = layout;
        this.cartItemsList = cartItemsList;
        this.sUserName = sUserName;
    }

    @Override
    public int getCount() {
        return cartItemsList.size();
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
        ImageView cartFoodIMG;
        TextView txtCartFoodName, txtCartFoodPrice, txtItemsQuantity;
        CheckBox cartItemsCheck;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.cartFoodIMG = (ImageView) convertView.findViewById(R.id.cartFoodIMG);
            viewHolder.txtCartFoodName = (TextView) convertView.findViewById(R.id.txtCartFoodName);
            viewHolder.txtCartFoodPrice = (TextView) convertView.findViewById(R.id.txtCartFoodPrice);
            viewHolder.txtItemsQuantity = (TextView) convertView.findViewById(R.id.txtItemsQuantity);
            viewHolder.cartItemsCheck = (CheckBox) convertView.findViewById(R.id.cartItemsCheck);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final CartItems cartItems = cartItemsList.get(position);

        if(cartItems.isCheck()){
            viewHolder.cartItemsCheck.setChecked(true);
        }
        else {
            viewHolder.cartItemsCheck.setChecked(false);
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(cartItems.getItemImage()+ ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.cartFoodIMG);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.cartItemsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.cartItemsCheck.isChecked()){
                    cartItems.setCheck(true);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Cart").child(sUserName).child(cartItems.getItemImage()).child("check").setValue(true);
                }
                else if(!viewHolder.cartItemsCheck.isChecked()){
                    cartItems.setCheck(false);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Cart").child(sUserName).child(cartItems.getItemImage()).child("check").setValue(false);
                }
            }
        });

        viewHolder.txtCartFoodName.setText(cartItems.getItemName());
        viewHolder.txtCartFoodPrice.setText(cartItems.getiTemPrice() + "vnd");
        viewHolder.txtItemsQuantity.setText("Số lượng: " + cartItems.getiSLItem());

        return convertView;
    }
}
