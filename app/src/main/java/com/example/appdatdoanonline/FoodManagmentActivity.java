package com.example.appdatdoanonline;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appdatdoanonline.adapter.MyAdapter;
import com.example.appdatdoanonline.data_models.MonAn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FoodManagmentActivity extends AppCompatActivity {
    private EditText edtFoodName, edtfoodPrice, edtfoodDescribe;
    private Button btnAddImage, btnAdd, btnRemove, btnEdit, btnBack;
    private Spinner spnType;
    ImageView addImageView;
    private ArrayList<MonAn> lstMonAn;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private GridView gridView;
    private MyAdapter myAdapter;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    int PICK_IMAGE = 1, iFoodType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.foodmanagment_layout);

        final StorageReference storageRef = storage.getReferenceFromUrl("gs://appdatdoanonline.appspot.com");

        edtFoodName = (EditText) findViewById(R.id.edtNameFood);
        edtfoodPrice = (EditText) findViewById(R.id.edtfoodPrice);
        edtfoodDescribe = (EditText) findViewById(R.id.edtfoodDescribe);
        addImageView = (ImageView) findViewById(R.id.addImageView);
        spnType = (Spinner) findViewById(R.id.spnType);
        btnAddImage = (Button) findViewById(R.id.btnAddImageView);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnRemove = (Button) findViewById(R.id.btnRemove);
        gridView = findViewById(R.id.gridView);
        btnEdit = (Button) findViewById(R.id.btnEdit);

        lstMonAn = new ArrayList<>();

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < lstMonAn.size(); i++) {
                    if (lstMonAn.get(i).isCheck()) {
                        if (edtFoodName.getText().toString().isEmpty()) {
                            edtFoodName.setError("Bạn chưa nhập tên thức ăn!");
                        } else if (foodNameCheck(lstMonAn, edtFoodName.getText().toString()) == false) {
                            edtFoodName.setError("Tên món ăn đã tồn tại!");
                        } else if (edtfoodPrice.getText().toString().isEmpty()) {
                            edtfoodPrice.setError("Bạn chưa nhập giá tiền món ăn!");
                        } else if (edtfoodDescribe.getText().toString().isEmpty()) {
                            edtfoodDescribe.setError("Bạn chưa nhập mô tả thức ăn!");
                        } else {
                            if (spnType.getSelectedItem().toString().equals("Món chiên")) {
                                iFoodType = 1;
                            } else if (spnType.getSelectedItem().toString().equals("Thức ăn nhanh")) {
                                iFoodType = 2;
                            } else if (spnType.getSelectedItem().toString().equals("Nước uống")) {
                                iFoodType = 3;
                            }

                            MonAn monAn = new MonAn(edtFoodName.getText().toString(), edtfoodDescribe.getText().toString(), Double.valueOf(edtfoodPrice.getText().toString()), lstMonAn.get(i).getImage(), iFoodType);
                            mDatabase.child("Food").child(lstMonAn.get(i).getImage()).setValue(monAn);

                            edtFoodName.setText("");
                            edtfoodPrice.setText("");
                            edtfoodDescribe.setText("");
                            spnType.setSelection(0);
                            edtFoodName.requestFocus();

                            lstMonAn.clear();
                            mDatabase.child("Food").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    lstMonAn.add(snapshot.getValue(MonAn.class));
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
                            final int delay = 1000;
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    FoodLoad();
                                }
                            }, delay);
                        }

                    }
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtFoodName.getText().toString().isEmpty()) {
                    edtFoodName.setError("Bạn chưa nhập tên thức ăn!");
                } else if (foodNameCheck(lstMonAn, edtFoodName.getText().toString()) == false) {
                    edtFoodName.setError("Tên món ăn đã tồn tại!");
                } else if (edtfoodPrice.getText().toString().isEmpty()) {
                    edtfoodPrice.setError("Bạn chưa nhập giá tiền món ăn!");
                } else if (edtfoodDescribe.getText().toString().isEmpty()) {
                    edtfoodDescribe.setError("Bạn chưa nhập mô tả thức ăn!");
                } else {
                    if (spnType.getSelectedItem().toString().equals("Món chiên")) {
                        iFoodType = 1;
                    } else if (spnType.getSelectedItem().toString().equals("Thức ăn nhanh")) {
                        iFoodType = 2;
                    } else if (spnType.getSelectedItem().toString().equals("Nước uống")) {
                        iFoodType = 3;
                    }
                    String skey = mDatabase.push().getKey();
                    MonAn monAn = new MonAn(edtFoodName.getText().toString(), edtfoodDescribe.getText().toString(), Double.valueOf(edtfoodPrice.getText().toString()), skey, iFoodType);

                    mDatabase.child("Food").child(skey).setValue(monAn);

                    edtFoodName.setText("");
                    edtfoodPrice.setText("");
                    edtfoodDescribe.setText("");
                    spnType.setSelection(0);
                    edtFoodName.requestFocus();

                    final StorageReference mountainsRef = storageRef.child(skey + ".png");

                    addImageView.setDrawingCacheEnabled(true);
                    addImageView.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) addImageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();
                    final UploadTask uploadTask = mountainsRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(FoodManagmentActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(FoodManagmentActivity.this, "Thêm Thành công", Toast.LENGTH_SHORT).show();
                            lstMonAn.clear();
                            mDatabase.child("Food").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    lstMonAn.add(snapshot.getValue(MonAn.class));
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
                            final int delay = 1000;
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    FoodLoad();
                                }
                            }, delay);
                        }
                    });
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AdminMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < lstMonAn.size(); i++) {
                    if (lstMonAn.get(i).isCheck()) {
                        mDatabase.child("Food").child(lstMonAn.get(i).getImage()).removeValue();

                        StorageReference desertRef = FirebaseStorage.getInstance().getReference();
                        desertRef.child(lstMonAn.get(i).getImage() + ".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FoodManagmentActivity.this, "Xoa hinh thanh cong", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(FoodManagmentActivity.this, "Xoa hinh that bai", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                lstMonAn.clear();
                mDatabase.child("Food").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        lstMonAn.add(snapshot.getValue(MonAn.class));
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
                final int delay = 1000;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        FoodLoad();
                    }
                }, delay);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        lstMonAn.clear();
        mDatabase.child("Food").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                lstMonAn.add(snapshot.getValue(MonAn.class));
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
            public void run() {
                FoodLoad();
            }
        }, delay);
    }

    private void FoodLoad() {
        myAdapter = new MyAdapter(this, R.layout.listview_foodmanagment_layout, lstMonAn);
        gridView.setAdapter(myAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            addImageView.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Boolean foodNameCheck(ArrayList<MonAn> lstMonAn, String sFoodName) {
        for (MonAn monAn : lstMonAn) {
            if (monAn.getFoodName().equals(sFoodName)) {
                return false;
            }
        }
        return true;
    }
}
