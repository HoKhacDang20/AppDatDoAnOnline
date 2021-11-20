package com.example.datdoanonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datdoanonline.R;
import com.example.datdoanonline.adapter.KitchenCookAdapter;
import com.example.datdoanonline.data_models.CartItems;
import com.example.datdoanonline.data_models.DonHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CookingActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Intent intent;
    private TextView txtUserFullName;
    private GridView gridCart;
    private Button btnBack, btnDelivery;
    private ArrayList<DonHang> donHangArrayList;
    private ArrayList<CartItems> cartItemsArrayList;
    private String sIDDonHang, sItemName;
    private KitchenCookAdapter kitchenCookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.cooking_layout);
        txtUserFullName = findViewById(R.id.txtUserFullName);
        gridCart = findViewById(R.id.cookGridView);
        btnBack = findViewById(R.id.btnBack);
        btnDelivery = findViewById(R.id.btnDelivery);

        donHangArrayList = new ArrayList<>();
        cartItemsArrayList = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cartItemsArrayList.clear();
        donHangArrayList.clear();
        if(getIntent().getExtras() != null){
            intent = getIntent();
//            sIDDonHang = "-MDQM6cOmhdA2_ty0o22";
            sIDDonHang = intent.getExtras().getString("IDDonHang");
            databaseReference.child("Cook").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    donHangArrayList.add(snapshot.getValue(DonHang.class));
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
        }

        final Handler handler = new Handler();
        final int delay = 500;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DonHangLoad();
            }
        }, delay);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(DonHang donHang : donHangArrayList){
                    if(donHang.getsIDDonHang().equals(sIDDonHang)){
                        databaseReference.child("Kitchen").child(donHang.getsIDDonHang()).setValue(donHang);
//                        databaseReference.child("Cook").child(donHang.getsIDDonHang()).removeValue();
                    }
                }
                intent = new Intent(CookingActivity.this, KitchenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(DonHang donHang : donHangArrayList){
                    if(donHang.getsIDDonHang().equals(sIDDonHang)){
                        databaseReference.child("DonHang").child(donHang.getsIDDonHang()).child("iTinhTrang").setValue(2);
                        databaseReference.child("ShipPacket").child(donHang.getsIDDonHang()).setValue(donHang);
                        databaseReference.child("Kitchen").child(donHang.getsIDDonHang()).removeValue();
                        databaseReference.child("Cook").child(donHang.getsIDDonHang()).removeValue();
                    }
                }
                intent = new Intent(CookingActivity.this, KitchenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }
    public void DonHangLoad(){
        for(final DonHang donHang : donHangArrayList){
            if(donHang.getsIDDonHang().equals(sIDDonHang)){
                txtUserFullName.setText(donHang.getsHoTen());
                kitchenCookAdapter = new KitchenCookAdapter(CookingActivity.this,R.layout.cooking_items_layout,donHang.getCartItemsList(),sIDDonHang);
                boolean bCheck = true;
                for(CartItems cartItems : donHang.getCartItemsList()){
                    if(!cartItems.isCheck()){
                        bCheck = false;
                    }
                }
                if(bCheck == true){
                    btnDelivery.setEnabled(true);
                }
                gridCart.setAdapter(kitchenCookAdapter);
                gridCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        sItemName = donHang.getCartItemsList().get(position).getItemName();
                        DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        boolean bCheck = true;
                                        donHang.getCartItemsList().get(position).setCheck(true);
                                        kitchenCookAdapter.notifyDataSetChanged();
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("Cook").child(sIDDonHang).child("cartItemsList").child(donHang.getCartItemsList().indexOf(donHang.getCartItemsList().get(position)) + "").child("check").setValue(true);
                                        for(CartItems cartItems : donHang.getCartItemsList()){
                                            if(!cartItems.isCheck()){
                                                bCheck = false;
                                            }
                                        }
                                        if(bCheck == true){
                                            btnDelivery.setEnabled(true);
                                        }
                                        return;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        return;

                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CookingActivity.this);
                        builder.setMessage("Đã nấu xong món " + sItemName + "?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
                    }
                });
            }
        }
    }
}