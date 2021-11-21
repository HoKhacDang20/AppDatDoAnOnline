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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datdoanonline.R;
import com.example.datdoanonline.adapter.ShipperPacketAdapter;
import com.example.datdoanonline.data_models.DonHang;
import com.example.datdoanonline.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShipperDeliveryActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Intent intent;
    private ArrayList<DonHang> donHangArrayList;
    private ShipperPacketAdapter shipperPacketAdapter;
    private Button btnBack;
//    private TextView txtShipperUserName;
    private GridView donHangGridView;
    private String sUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_delivery_layout);
        btnBack = findViewById(R.id.btnBack);
//        txtShipperUserName = findViewById(R.id.txtShipperUserName);
        donHangGridView = findViewById(R.id.donHangGridView);
        donHangArrayList = new ArrayList<>();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ShipperDeliveryActivity.this, ShipperMainActivity.class);
                intent.putExtra("UserName",sUserName);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        donHangArrayList.clear();
        if(getIntent().getExtras() != null){
            intent = getIntent();
            sUserName = intent.getExtras().getString("UserName");
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){
//                            txtShipperUserName.setText(snapshot.getValue(UserData.class).getsFullName());
                    }
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
            databaseReference.child("Delivery").child(sUserName).addChildEventListener(new ChildEventListener() {
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
                OrderLoad();
            }
        },delay);

        donHangGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                databaseReference.child("DonHang").child(donHangArrayList.get(position).getsIDDonHang()).child("iTinhTrang").setValue(3);
                                databaseReference.child("Delivery").child(sUserName).child(donHangArrayList.get(position).getsIDDonHang()).removeValue();
                                donHangArrayList.remove(donHangArrayList.get(position));
                                shipperPacketAdapter.notifyDataSetChanged();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ShipperDeliveryActivity.this);
                builder.setMessage("Xác nhận giao thành công đơn của khách: " + donHangArrayList.get(position).getsHoTen() + "?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }
        });
    }

    public void OrderLoad(){
        shipperPacketAdapter = new ShipperPacketAdapter(this, R.layout.shipper_packet_layout,donHangArrayList);
        donHangGridView.setAdapter(shipperPacketAdapter);
    }
}