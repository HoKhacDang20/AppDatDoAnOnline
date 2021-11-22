package com.example.datdoanonline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.datdoanonline.R;
import com.example.datdoanonline.adapter.UserDonHangAdapter;
import com.example.datdoanonline.data_models.DonHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserDonHangActivity extends AppCompatActivity {
    private Button btnBack;
    private GridView gridDonHang;
    private Intent intent;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();;
    private ArrayList<DonHang> donHangArrayList;
    private ArrayList<DonHang> userDonHangList;
    private UserDonHangAdapter userDonHangAdapter;
    private String sUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_don_hang_layout);
        btnBack = findViewById(R.id.btnBack);
        gridDonHang = findViewById(R.id.gridDonHang);

        donHangArrayList = new ArrayList<>();
        userDonHangList = new ArrayList<>();

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
        donHangArrayList.clear();
        if(getIntent().getExtras() != null){
            intent = getIntent();
            sUserName = intent.getExtras().getString("UserName");
            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
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
            final Handler handler = new Handler();
            final int delay = 500; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    DonHangLoad();
//                handler.postDelayed(this, delay);
                }
            }, delay);
        }
    }
    public void DonHangLoad(){
        for (DonHang donHang : donHangArrayList){
            if(donHang.getsUserName().equals(sUserName)){
                userDonHangList.add(donHang);
            }
        }
        userDonHangAdapter = new UserDonHangAdapter(this, R.layout.don_hang_layout,userDonHangList);
        gridDonHang.setAdapter(userDonHangAdapter);
    }
}