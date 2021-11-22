package com.example.datdoanonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datdoanonline.adapter.UserCartAdapter;
import com.example.datdoanonline.data_models.CartItems;
import com.example.datdoanonline.data_models.DonHang;
import com.example.datdoanonline.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.datdoanonline.R;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private TextView txtAllFoodPrice;
    private Button btnBack, btnOrder, btnDelete;
    private Intent intent;
    private GridView gridCart;
    private String sUserName, sSDT, sDiaChi, sHoTen;
    private double dFoodPrice;
    private DatabaseReference databaseReference;
    private UserCartAdapter userCartAdapter;
    private ArrayList<CartItems> cartItemsArrayList;
    private ArrayList<UserData> user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.cart_layout);

        btnBack = findViewById(R.id.btnBackC);
        btnOrder = findViewById(R.id.btnOrder);
        btnDelete = findViewById(R.id.btnDelete);
        gridCart = findViewById(R.id.gridCart);
        txtAllFoodPrice = findViewById(R.id.txtAllFoodPrice);

        cartItemsArrayList = new ArrayList<>();

        user = new ArrayList<>();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),UserMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartItemsArrayList.clear();
        user.clear();
        if(getIntent().getExtras() != null){
            intent = getIntent();
            sUserName = intent.getExtras().getString("UserName");
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    user.add(snapshot.getValue(UserData.class));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            databaseReference.child("Cart").child(sUserName).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    cartItemsArrayList.add(snapshot.getValue(CartItems.class));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            btnDelete.setOnClickListener(RemoveItemClick);

            final Handler handler = new Handler();
            final int delay = 500; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    CartLoad();
//                handler.postDelayed(this, delay);
                }
            }, delay);
        }
        else {

            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        }

        btnOrder.setOnClickListener(OrderClick);
    }

    public void CartLoad(){
        dFoodPrice = 0.0;
        userCartAdapter = new UserCartAdapter(this, R.layout.cart_items_layout,cartItemsArrayList, sUserName);
        gridCart.setAdapter(userCartAdapter);
        for(CartItems cartItems : cartItemsArrayList){
            dFoodPrice += cartItems.getiTemPrice();
        }
        txtAllFoodPrice.setText(dFoodPrice + "vnd");
    }

    View.OnClickListener OrderClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(cartItemsArrayList.size() != 0){
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                for(CartItems cartItems : cartItemsArrayList){
                                    if(cartItems.isCheck()){
                                        cartItems.setCheck(false);
                                    }
                                }
                                for(UserData user : user){
                                    if(user.getsUserName().equals(sUserName)){
                                        sDiaChi = user.getsDiaChi();
                                        sSDT = user.getsSdt();
                                        sHoTen = user.getsFullName();
                                    }
                                }
                                databaseReference = FirebaseDatabase.getInstance().getReference();
                                String sID = databaseReference.push().getKey();
                                DonHang dh = new DonHang(sID,sUserName, sHoTen,sSDT,sDiaChi, cartItemsArrayList, 0);
                                databaseReference.child("DonHang").child(sID).setValue(dh);
                                databaseReference.child("Cart").child(sUserName).removeValue();
                                intent = new Intent(CartActivity.this,UserDonHangActivity.class);
                                intent.putExtra("UserName", sUserName);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setMessage("Bạn đã hoàn tất đơn hàng?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }
            else {
                Toast.makeText(CartActivity.this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnClickListener RemoveItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            for(int i = 0; i < cartItemsArrayList.size(); i++){
                if(cartItemsArrayList.get(i).isCheck()){
//                        cartItemsArrayList.remove(i);
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Cart").child(sUserName).child(cartItemsArrayList.get(i).getItemImage()).removeValue();
                }
            }
//                userCartAdapter.notifyDataSetChanged();
            cartItemsArrayList.clear();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Cart").child(sUserName).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    cartItemsArrayList.add(snapshot.getValue(CartItems.class));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            final Handler handler = new Handler();
            final int delay = 500; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    CartLoad();
//                handler.postDelayed(this, delay);
                }
            }, delay);
        }
    };
}