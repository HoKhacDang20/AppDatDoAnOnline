package com.example.datdoanonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datdoanonline.R;
import com.example.datdoanonline.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditAccountActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private EditText edtUserName, edtFullName, edtSDT, edtDiaChi;
    private Spinner spnGender;
    private ArrayList<UserData> user;
    private Intent intent;
    private Button btnSave, btnBack;
    private String sUserName, sFullname, sSDT, sGenDer, sDiaChi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.edit_account_layout);

        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtFullName = findViewById(R.id.edtFullName);
        edtSDT = findViewById(R.id.edtSDT);
        edtUserName = findViewById(R.id.edtUserName);
        spnGender = findViewById(R.id.spnGender);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),UserMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sUserName = edtUserName.getText().toString();
                sFullname = edtFullName.getText().toString();
                sGenDer = spnGender.getSelectedItem().toString();
                sDiaChi = edtDiaChi.getText().toString();
                sSDT = edtSDT.getText().toString();

                if(sFullname.isEmpty()){
                    edtFullName.setError("Họ tên không được để trống!");
                }
                else if(sSDT.isEmpty()) {
                    edtSDT.setError("Số điện thoại không được để trống!");
                }
                else if(sSDT.length() > 11 || sSDT.length() < 10){
                    edtSDT.setError("Bạn nhập sai số điện thoại!");
                }
                else if(sSDT.isEmpty()){
                    edtDiaChi.setError("Địa chỉ không được để trống!");
                }
                else{
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){
                                UserData userUpdate = new UserData(sUserName,sFullname,sSDT,sGenDer,sDiaChi,snapshot.getValue(UserData.class).getsPassword(),0);
                                databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                intent = new Intent(EditAccountActivity.this,UserMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                Toast.makeText(EditAccountActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        intent = getIntent();
        edtUserName.setText(intent.getExtras().getString("UserName"));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getValue(UserData.class).getsUserName().equals(edtUserName.getText().toString())){
                        edtFullName.setText(dataSnapshot.getValue(UserData.class).getsFullName());
                        edtSDT.setText(dataSnapshot.getValue(UserData.class).getsSdt());
                        edtDiaChi.setText(dataSnapshot.getValue(UserData.class).getsDiaChi());
                        if(dataSnapshot.getValue(UserData.class).getsGioiTinh().equals("Nam"))
                        {
                            spnGender.setSelection(0);
                        }
                        else {
                            spnGender.setSelection(1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}