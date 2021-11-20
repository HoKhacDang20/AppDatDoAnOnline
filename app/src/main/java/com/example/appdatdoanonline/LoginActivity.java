package com.example.appdatdoanonline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdatdoanonline.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    static public Context context;
    private TextView txtRegistry;
    private EditText edtUserName, edtPass;
    static public Intent intent;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private Button btnLogin;
    private ArrayList<UserData> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.login_layout);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegistry = findViewById(R.id.txtRegistry);
        edtUserName = findViewById(R.id.edtLoginName);
        edtPass = findViewById(R.id.edtLoginPass);
        list = new ArrayList<UserData>();


        txtRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Registry", Toast.LENGTH_SHORT).show();
                intent = new Intent(v.getContext(), RegistryActivity.class);
                startActivity(intent);
            }
        });
    }

    public int LoginCheck(ArrayList<UserData> list, String sUserName, String sPass) {
        int loginResult = -1;
        for (UserData user : list) {
            if (user.getsUserName().equals(sUserName) && user.getsPassword().equals(sPass)) {
                loginResult = user.getiPermission();
            }
        }
        return loginResult;
    }


    @Override
    protected void onResume() {
        super.onResume();
        edtPass.setText("");
        list.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                UserData user = snapshot.getValue(UserData.class);
                list.add(user);

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

        btnLogin.setOnClickListener(LoginClick);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    View.OnClickListener LoginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int iLogin = LoginCheck(list, edtUserName.getText().toString(), edtPass.getText().toString());
            if (iLogin == 1) {
                intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                intent.putExtra("UserName", edtUserName.getText().toString());
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
