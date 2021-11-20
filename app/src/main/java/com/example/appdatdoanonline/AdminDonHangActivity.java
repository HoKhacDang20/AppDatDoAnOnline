package com.example.appdatdoanonline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.appdatdoanonline.adapter.AdminDonHangAdapter;
import com.example.appdatdoanonline.data_models.BlackList;
import com.example.appdatdoanonline.data_models.CartItems;
import com.example.appdatdoanonline.data_models.DonHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDonHangActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<DonHang> donHangArrayList;
    private ArrayList<BlackList> blackListArrayList;
    private Intent intent;
    private AdminDonHangAdapter adminDonHangAdapter;
    private Button btnConfirm, btnBack, btnCancel;
    private GridView gridDonHang;
    private double dDoanhThu;
    private TextView txtDoanhThu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.admin_donhang);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        gridDonHang = (GridView) findViewById(R.id.gridDonHang);
        txtDoanhThu = (TextView) findViewById(R.id.txtDoanhThu);

        donHangArrayList = new ArrayList<>();
        blackListArrayList = new ArrayList<>();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminDonHangActivity.this, AdminMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        btnCancel.setOnClickListener(CancelClick);
        btnConfirm.setOnClickListener(ConfirmClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        donHangArrayList.clear();
        blackListArrayList.clear();

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
        databaseReference.child("BlackList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                blackListArrayList.add(snapshot.getValue(BlackList.class));
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
        final int delay = 500;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DonHangLoad();
            }
        }, delay);
    }

    View.OnClickListener ConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < donHangArrayList.size(); i++) {
                if (donHangArrayList.get(i).isCheck()) {
//                    Toast.makeText(AdminDonHangActivity.this, donHangArrayList.get(i).getsHoTen(), Toast.LENGTH_SHORT).show();

                    databaseReference.child("DonHang").child(donHangArrayList.get(i).getsIDDonHang()).child("iTinhTrang").setValue(1);
                    databaseReference.child("Kitchen").child(donHangArrayList.get(i).getsIDDonHang()).setValue(donHangArrayList.get(i));
                }
            }
            donHangArrayList.clear();
            blackListArrayList.clear();

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
            databaseReference.child("BlackList").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    blackListArrayList.add(snapshot.getValue(BlackList.class));
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
            final int delay = 500;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DonHangLoad();
                }
            }, delay);
        }
    };

    View.OnClickListener CancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < donHangArrayList.size(); i++) {
                if (donHangArrayList.get(i).isCheck() && donHangArrayList.get(i).getiTinhTrang() == 0) {
//                    Toast.makeText(AdminDonHangActivity.this, donHangArrayList.get(i).getsHoTen(), Toast.LENGTH_SHORT).show();
                    BlackList blackList = new BlackList(donHangArrayList.get(i).getsSDT());
                    databaseReference.child("BlackList").child(donHangArrayList.get(i).getsUserName()).setValue(blackList);
                    databaseReference.child("DonHang").child(donHangArrayList.get(i).getsIDDonHang()).removeValue();
                }
            }
            donHangArrayList.clear();
            blackListArrayList.clear();

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
            databaseReference.child("BlackList").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    blackListArrayList.add(snapshot.getValue(BlackList.class));
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
            final int delay = 500;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DonHangLoad();
                }
            }, delay);
        }
    };

    public void DonHangLoad() {
        dDoanhThu = 0.0;
        for (DonHang donHang : donHangArrayList) {
            if (donHang.getiTinhTrang() == 3) {
                for (CartItems cartItems : donHang.getCartItemsList()) {
                    dDoanhThu += cartItems.getiTemPrice();
                }
            }
        }
        adminDonHangAdapter = new AdminDonHangAdapter(this, R.layout.admin_donhang_item_layout, donHangArrayList, blackListArrayList);
        gridDonHang.setAdapter(adminDonHangAdapter);
        txtDoanhThu.setText("Doanh thu: " + dDoanhThu + "vnd");
    }
}
