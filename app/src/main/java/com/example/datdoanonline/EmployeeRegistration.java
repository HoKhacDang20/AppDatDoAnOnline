package com.example.datdoanonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.datdoanonline.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datdoanonline.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EmployeeRegistration extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private Button btnBack, btnRegistry, btnHome;
    private EditText edtUserName, edtFullName, edtDiaChi, edtSDT;
    private Spinner spnGender, spnUserType;
    private String sUserName, sFullName, sSDT, sDiaChi, sGioiTinh, sUserType;
    private Intent intent;
    private ArrayList<UserData> userDataArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.employee_registration_layout);

        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtFullName = (EditText) findViewById(R.id.edtFullName);
        edtSDT = (EditText) findViewById(R.id.edtSDT);
        spnGender = (Spinner) findViewById(R.id.spnGender);
        spnUserType = (Spinner) findViewById(R.id.spnUserType);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnRegistry = (Button) findViewById(R.id.btnRegistry);

        userDataArrayList = new ArrayList<>();

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(EmployeeRegistration.this, AdminMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        btnRegistry.setOnClickListener(OnClick);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(EmployeeRegistration.this, EmployeeManagmentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        userDataArrayList.clear();

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
    }

    public Boolean UserNameCheck(ArrayList<UserData> userList, String sUserName){
        for(UserData user : userList){
            if(user.getsUserName().equals(sUserName)){
                return false;
            }
        }
        return true;
    }

    View.OnClickListener OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sUserName = edtUserName.getText().toString();
            sFullName = edtFullName.getText().toString();
            sSDT = edtSDT.getText().toString();
            sDiaChi = edtDiaChi.getText().toString();
            sGioiTinh = spnGender.getSelectedItem().toString();
            sUserType = spnUserType.getSelectedItem().toString();
            if(sUserName.isEmpty()){
                edtUserName.setError("Bạn chưa nhập user name!");
            }
            else if(UserNameCheck(userDataArrayList,sUserName) == false){
                edtUserName.setError("User name đã tồn tại!");
            }
            else if(sFullName.isEmpty()){
                edtFullName.setError("Bạn chưa nhập họ tên!");
            }
            else if(sSDT.isEmpty()) {
                edtSDT.setError("Bạn chưa nhập số điện thoại!");
            }
            else if(sSDT.length() > 11 || sSDT.length() < 10){
                edtSDT.setError("Bạn nhập sai số điện thoại!");
            }
            else if(sDiaChi.isEmpty()){
                edtDiaChi.setError("Bạn chưa nhập địa chỉ!");
            }
            else{
                if(sUserType.equals("Bếp")){
                    Toast.makeText(EmployeeRegistration.this, "Thêm nhân viên thành công!", Toast.LENGTH_SHORT).show();
                    UserData user = new UserData();
                    CreateUser(sUserName, sFullName, sSDT, sGioiTinh, sDiaChi, "000000", 2);
                    intent = new Intent(EmployeeRegistration.this,EmployeeManagmentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if(sUserType.equals("Shipper")){
                    Toast.makeText(EmployeeRegistration.this, "Thêm nhân viên thành công!", Toast.LENGTH_SHORT).show();
                    UserData user = new UserData();
                    CreateUser(sUserName, sFullName, sSDT, sGioiTinh, sDiaChi, "000000", 3);
                    intent = new Intent(EmployeeRegistration.this,EmployeeManagmentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        }
    };
    private void CreateUser(String sUserName, String sFullName, String sSdt, String sGioiTinh, String sDiaChi, String sPassword, int iPermission){
        UserData user = new UserData(sUserName,sFullName,sSdt,sGioiTinh,sDiaChi,sPassword, iPermission);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String sKey = databaseReference.push().getKey();
        databaseReference.child("User").child(sKey).setValue(user);
    }
}