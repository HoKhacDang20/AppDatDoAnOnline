package com.example.appdatdoanonline;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.appdatdoanonline.datamodels.MonAn;
import com.example.appdatdoanonline.datamodels.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserMainActivity extends AppCompatActivity {
    private Intent intent;
    private Button btnEditAccount, btnLogout, btnPasswordChange, btnAllFood, btnFriedFood, btnFastFood, btnDrinks, btnCart, btnOrder;
    private TextView txtUserName;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ArrayList<UserData> userList;
    private String sUserName;
    private ArrayList<MonAn> lstMonAn;
    private GridView foodGridview;
    private String foodAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.edit_account_layout);
//        final ImageView foodIMG = findViewById(R.id.foodIMG);


        lstMonAn = new ArrayList<>();

//        final ImageView imageView = findViewById(R.id.imageView);
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//        storageReference.child("-MD4EcjdiurHi_1hB9RY.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(UserMainActivity.this).load(uri).into(imageView);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(UserMainActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
//            }
//        });


        userList = new ArrayList<UserData>();
        if(getIntent().getExtras() != null){
            sUserName = intent.getExtras().getString("UserName");
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                intent = new Intent(UserMainActivity.this, UserMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
                builder.setMessage("Bạn muốn đăng xuất?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }
        });

        btnEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserMainActivity.this, UserMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("UserName", sUserName);
                intent.putExtra("FullName", txtUserName.getText().toString());
                startActivity(intent);
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserMainActivity.this,UserMainActivity.class);
                intent.putExtra("UserName", sUserName);
                startActivity(intent);
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserMainActivity.this,UserMainActivity.class);
                intent.putExtra("UserName", sUserName);
                startActivity(intent);
            }
        });
        btnPasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserMainActivity.this, UserMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("UserName", sUserName);
                startActivity(intent);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MonAn monAn = dataSnapshot.getValue(MonAn.class);
                    lstMonAn.add(monAn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                FoodLoad();
//                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void FoodLoad() {
//        foodAdapter = new FoodAdapter(this,R.layout.mon_an_layout,lstMonAn);
//        foodGridview.setAdapter(foodAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getValue(UserData.class).getsUserName().equals(intent.getExtras().getString("UserName"))){
                        txtUserName.setText(dataSnapshot.getValue(UserData.class).getsFullName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAllFood.setOnClickListener(allFoodClick);
        btnFriedFood.setOnClickListener(friedFoodClick);
        btnFastFood.setOnClickListener(fastFoodClick);
        btnDrinks.setOnClickListener(DrinksClick);
        foodGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), UserMainActivity.class);
                intent.putExtra("FoodID",lstMonAn.get(position).getImage());
                intent.putExtra("UserName",sUserName);
                intent.putExtra("FoodPrice",lstMonAn.get(position).getFoodPrice());
                startActivity(intent);
            }
        });
    }
    View.OnClickListener allFoodClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            lstMonAn.clear();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Food").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        MonAn monAn = dataSnapshot.getValue(MonAn.class);
                        lstMonAn.add(monAn);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            final Handler handler = new Handler();
            final int delay = 1000; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    FoodLoad();
//                handler.postDelayed(this, delay);
                }
            }, delay);
        }
    };
    View.OnClickListener friedFoodClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            lstMonAn.clear();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Food").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(dataSnapshot.getValue(MonAn.class).getiType() == 1){
                            MonAn monAn = dataSnapshot.getValue(MonAn.class);
                            lstMonAn.add(monAn);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            final Handler handler = new Handler();
            final int delay = 1000; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    FoodLoad();
//                handler.postDelayed(this, delay);
                }
            }, delay);
        }
    };
    View.OnClickListener fastFoodClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            lstMonAn.clear();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Food").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(dataSnapshot.getValue(MonAn.class).getiType() == 2){
                            MonAn monAn = dataSnapshot.getValue(MonAn.class);
                            lstMonAn.add(monAn);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            final Handler handler = new Handler();
            final int delay = 1000; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    FoodLoad();
//                handler.postDelayed(this, delay);
                }
            }, delay);
        }
    };
    View.OnClickListener DrinksClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            lstMonAn.clear();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Food").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(dataSnapshot.getValue(MonAn.class).getiType() == 3){
                            MonAn monAn = dataSnapshot.getValue(MonAn.class);
                            lstMonAn.add(monAn);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            final Handler handler = new Handler();
            final int delay = 1000; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    FoodLoad();
//                handler.postDelayed(this, delay);
                }
            }, delay);
        }
    };
}