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
import com.example.datdoanonline.adapter.KitchenOrderAdapter;
import com.example.datdoanonline.data_models.BlackList;
import com.example.datdoanonline.data_models.DonHang;
import com.example.datdoanonline.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class KitchenActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Button btnLogout, btnAccountEdit, btnPasswordChange;
    private GridView orderGridView;
    private TextView txtCookUserName;
    private Intent intent;
    private String sUserName, sIDDonHang;
    private ArrayList<DonHang> donHangArrayList;
    private DonHang donHang;
    private ArrayList<BlackList> blackListArrayList;
    private KitchenOrderAdapter kitchenOrderAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.kitchen_layout);

        btnLogout = findViewById(R.id.btnLogOut);
        orderGridView = findViewById(R.id.orderGridView);
        txtCookUserName = findViewById(R.id.txtCookUserName);
        btnAccountEdit = findViewById(R.id.btnAccountEdit);
        btnPasswordChange = findViewById(R.id.btnPasswordChange);

        donHangArrayList = new ArrayList<>();
        blackListArrayList = new ArrayList<>();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                intent = new Intent(KitchenActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(KitchenActivity.this);
                builder.setMessage("Bạn muốn đăng xuất?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }
        });
        orderGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sIDDonHang = donHangArrayList.get(position).getsIDDonHang();
                donHang = donHangArrayList.get(position);
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                databaseReference.child("Cook").child(sIDDonHang).setValue(donHang);
//                                databaseReference.child("Kitchen").child(sIDDonHang).removeValue();
                                intent = new Intent(KitchenActivity.this, CookingActivity.class);
                                intent.putExtra("IDDonHang",sIDDonHang);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(KitchenActivity.this);
                builder.setMessage("Tiến hành nấu đơn hàng của khách " + donHangArrayList.get(position).getsHoTen() + "?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }
        });
        btnAccountEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(KitchenActivity.this, ChefAccountEdit.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("UserName", sUserName);
                startActivity(intent);
            }
        });
        btnPasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(KitchenActivity.this, ChefPasswordChange.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("UserName", sUserName);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getExtras() != null){
            intent = getIntent();
            sUserName = intent.getExtras().getString("UserName");
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){
                        txtCookUserName.setText(snapshot.getValue(UserData.class).getsFullName());
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
        }
        blackListArrayList.clear();
        donHangArrayList.clear();
        databaseReference.child("Kitchen").addChildEventListener(new ChildEventListener() {
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
                LitchenOrderLoad();
            }
        }, delay);
    }

    public void LitchenOrderLoad(){
        kitchenOrderAdapter = new KitchenOrderAdapter(this, R.layout.kitchen_don_hang_layout, donHangArrayList, blackListArrayList);
        orderGridView.setAdapter(kitchenOrderAdapter);
    }
}