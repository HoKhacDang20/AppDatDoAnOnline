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
import com.example.datdoanonline.adapter.EmployeeAdapter;
import com.example.datdoanonline.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EmployeeManagmentActivity extends AppCompatActivity {

    private Intent intent;
    private Button btnBack, btnRegistry;
    private DatabaseReference databaseReference;
    private ArrayList<UserData> userDataArrayList;
    private ArrayList<UserData> employeeArrayList;
    private GridView employeeGirview;
    private EmployeeAdapter employeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.employeemanagment_layout);

        btnBack = findViewById(R.id.btnBack);
        btnRegistry = findViewById(R.id.btnRegistry);
        employeeGirview = findViewById(R.id.employeeGirview);


        userDataArrayList = new ArrayList<>();
        employeeArrayList = new ArrayList<>();

        btnRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(EmployeeManagmentActivity.this, EmployeeRegistration.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(EmployeeManagmentActivity.this, AdminMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDataArrayList.clear();
        employeeArrayList.clear();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                userDataArrayList.add(snapshot.getValue(UserData.class));
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
        final int delay = 1000; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                UserLoad();
//                handler.postDelayed(this, delay);
            }
        }, delay);
    }
    public void UserLoad(){
        for(UserData userData : userDataArrayList){
            if(userData.getiPermission() != 0){
                employeeArrayList.add(userData);
            }
        }
        employeeAdapter = new EmployeeAdapter(this,R.layout.employee_layout,employeeArrayList);
        employeeGirview.setAdapter(employeeAdapter);
    }
}