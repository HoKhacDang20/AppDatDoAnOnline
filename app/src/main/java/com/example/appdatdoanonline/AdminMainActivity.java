package com.example.appdatdoanonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appdatdoanonline.data_models.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Intent intent;
    private TextView txtAdminUserName;
    private Button btnFoodManagment, btnManagmentEmployee, btnOrderManagement;
    private Button btnLogout, btnAccountEdit, btnPasswordChange;
    private String sUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_layout);
        btnOrderManagement = (Button) findViewById(R.id.btnOrderManagement);
        btnFoodManagment = (Button) findViewById(R.id.btnManagmentFood);
        btnManagmentEmployee = (Button) findViewById(R.id.btnManagmentEmployee);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        txtAdminUserName = (TextView) findViewById(R.id.txtAdminUserName);
        btnAccountEdit = (Button) findViewById(R.id.btnAccountEdit);
        btnPasswordChange = (Button) findViewById(R.id.btnPasswordChange);

        if (getIntent().getExtras() != null) {
            intent = getIntent();
            sUserName = intent.getExtras().getString("UserName");
        }
        btnOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminMainActivity.this, AdminDonHangActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        btnFoodManagment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(AdminMainActivity.this, FoodManagmentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        btnManagmentEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminMainActivity.this, EmployeeManagmentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                intent = new Intent(AdminMainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
                builder.setMessage("Bạn muốn đăng xuất?").setNegativeButton("No", dialogClick).setPositiveButton("Yes", dialogClick).show();
            }
        });

        btnAccountEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminMainActivity.this, AdminAccountEdit.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("UserName", sUserName);
                startActivity(intent);
            }
        });
        btnPasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminMainActivity.this, AdminPasswordChange.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("UserName", sUserName);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (getIntent().getExtras() != null) {
            intent = getIntent();
            sUserName = intent.getExtras().getString("UserName");
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("User").addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getValue(UserData.class).getsUserName().equals(sUserName)) {
                            txtAdminUserName.setText(dataSnapshot.getValue(UserData.class).getsFullName());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
