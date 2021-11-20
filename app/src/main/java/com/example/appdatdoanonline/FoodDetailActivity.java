package com.example.appdatdoanonline;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.appdatdoanonline.R;
import com.example.appdatdoanonline.data_model.CartItems;
import com.example.appdatdoanonline.data_model.MonAn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FoodDetailActivity extends AppCompatActivity {
    private Button btnBack, btnAddToCart;
    private TextView txtFoodName, txtFoodPrice, txtFoodDescription;
    private double dFoodPrice;
    private ImageView foodIMG;
    private Intent intent;
    private String foodID, sUserName;
    private DatabaseReference databaseReference;
    private ElegantNumberButton elegantNumberButton;
    private int iFoodQuantity;
    private double dPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.food_detail_layout);

        foodIMG = findViewById(R.id.foodIMG);
        btnBack = findViewById(R.id.btnBack);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        txtFoodDescription = findViewById(R.id.txtFoodDescription);
        txtFoodName = findViewById(R.id.txtFoodName);
        txtFoodPrice = findViewById(R.id.txtFoodPrice);
        elegantNumberButton = findViewById(R.id.elegantButton);

        elegantNumberButton.setNumber("1");


    }

    @Override
    protected void onResume() {
        super.onResume();
        intent = getIntent();
        foodID = intent.getExtras().getString("FoodID");
        sUserName = intent.getExtras().getString("UserName");
        dFoodPrice = intent.getDoubleExtra("FoodPrice",0);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(foodID + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(FoodDetailActivity.this).load(uri).into(foodIMG);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodDetailActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getValue(MonAn.class).getImage().equals(foodID)){
                        txtFoodName.setText(dataSnapshot.getValue(MonAn.class).getFoodName());
                        txtFoodPrice.setText(dFoodPrice + " vnd");
                        txtFoodDescription.setText(dataSnapshot.getValue(MonAn.class).getFoodDescribe());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),UserMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iFoodQuantity = Integer.parseInt(elegantNumberButton.getNumber());
                dPrice = dFoodPrice * iFoodQuantity;
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Cart").child(sUserName).child(foodID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int iNewQuantity = iFoodQuantity + snapshot.getValue(CartItems.class).getiSLItem();
                            double dNewPrice = dPrice + snapshot.getValue(CartItems.class).getiTemPrice();
                            CreateNewItemCart(foodID,txtFoodName.getText().toString(),iNewQuantity,dNewPrice);
                        }
                        else {
                            CreateNewItemCart(foodID,txtFoodName.getText().toString(),iFoodQuantity,dPrice);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                intent = new Intent(v.getContext(),CartActivity.class);
                intent.putExtra("UserName", sUserName);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }
    public void CreateNewItemCart(String foodIMG, String foodName, int iFoodQuantity, double dFoodPrice){
        CartItems cartItems = new CartItems(foodIMG,foodName,iFoodQuantity,dFoodPrice);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Cart").child(sUserName).child(foodIMG).setValue(cartItems);
    }
}