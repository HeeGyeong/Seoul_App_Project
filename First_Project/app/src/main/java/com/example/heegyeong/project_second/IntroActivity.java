package com.example.heegyeong.project_second;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IntroActivity extends AppCompatActivity {
    String data = null;
    int index = 0;

    public static Product[] cuPro = new Product[21];
    public static Product[] gsPro = new Product[21];
    public static Product[] elevenPro = new Product[21];
    public static Product[] miniPro = new Product[21];

    int DBindex = 0;
    int gsDBindex = 0;
    int elevenDBindex = 0;
    int miniDBindex = 0;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#ccffff"));
        }


        databaseReference.child("product").child("cu").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product proDuct = dataSnapshot.getValue(Product.class);  // proDuct를 가져오고
                if (proDuct != null && DBindex <= 20) {
                    cuPro[DBindex] = new Product(proDuct.getproName(), proDuct.getproPrice(),proDuct.getproEvent(),
                            proDuct.getproUrl(),proDuct.getproCategory());
                    if(DBindex == 20){ Log.d("cu DB loading", "success");}
                    DBindex++;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        databaseReference.child("product").child("gs").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product proDuct = dataSnapshot.getValue(Product.class);  // proDuct를 가져오고
                if (proDuct != null && gsDBindex <= 20) {
                    gsPro[gsDBindex] = new Product(proDuct.getproName(), proDuct.getproPrice(),proDuct.getproEvent(),
                            proDuct.getproUrl(),proDuct.getproCategory());
                    if(gsDBindex == 20){ Log.d("gs DB loading","success");}
                    gsDBindex++;
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        databaseReference.child("product").child("eleven").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product proDuct = dataSnapshot.getValue(Product.class);  // proDuct를 가져오고
                if (proDuct != null && elevenDBindex <= 20) {
                    elevenPro[elevenDBindex] = new Product(proDuct.getproName(), proDuct.getproPrice(),proDuct.getproEvent(),
                            proDuct.getproUrl(),proDuct.getproCategory());
                    if (elevenDBindex == 20) {
                        Log.d("eleven DB loading", "success");
                    }
                    elevenDBindex++;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        databaseReference.child("product").child("mini").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product proDuct = dataSnapshot.getValue(Product.class);  // proDuct를 가져오고
                if (proDuct != null && miniDBindex <= 20) {
                    miniPro[miniDBindex] = new Product(proDuct.getproName(), proDuct.getproPrice(),proDuct.getproEvent(),
                            proDuct.getproUrl(),proDuct.getproCategory());
                    if (miniDBindex == 20) {
                        Log.d("mini DB loading", "success");
                    }
                    miniDBindex++;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        // 값 호출
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        String text = prefs.getString("data", null);// 값이 없을시 null을 넣음
        int save_index = prefs.getInt("index",0); // 값이 없을시 0을 넣음

        Intent intent = getIntent();
        // intent값 호출
        data = intent.getStringExtra("value"); // 처음 시작시 data는 null 무조건 null이됨
        index = intent.getIntExtra("index",0);

        if(data == null && text != null) // 초기값 설정이 되어있으면 값 불러오기
        {
            data = text;
            index = save_index;
        }

        // 값 저장
        prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("data", data);
        editor.putInt("index",index);
        editor.commit();

        Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    if(cuPro[0] != null){
                        if(index == 1) {
                            Intent intent = new Intent(IntroActivity.this,
                                    CU_Layout.class);
                            for(int i=0;i<=20;i++){
                                intent.putExtra("cuImage"+i,  cuPro[i].getproUrl());
                                intent.putExtra("cuName"+i,   cuPro[i].getproName());
                                intent.putExtra("cuPrice"+i,  cuPro[i].getproPrice());
                                intent.putExtra("cuEvent"+i,  cuPro[i].getproEvent());
                                intent.putExtra("cuCategory"+i,  cuPro[i].getproCategory());
                            }
                            startActivity(intent);
                            finish();
                        } else if(index == 2) {
                            Intent intent = new Intent(IntroActivity.this,
                                    GS_Layout.class);
                            for(int i=0;i<=20;i++){
                                intent.putExtra("gsImage"+i,  gsPro[i].getproUrl());
                                intent.putExtra("gsName"+i,   gsPro[i].getproName());
                                intent.putExtra("gsPrice"+i,  gsPro[i].getproPrice());
                                intent.putExtra("gsEvent"+i,  gsPro[i].getproEvent());
                                intent.putExtra("gsCategory"+i,  gsPro[i].getproCategory());
                            }
                            startActivity(intent);
                            finish();
                        } else if(index == 3) {
                            Intent intent = new Intent(IntroActivity.this,
                                    Eleven_Layout.class);
                            for(int i=0;i<=20;i++){
                                intent.putExtra("elevenImage"+i,  elevenPro[i].getproUrl());
                                intent.putExtra("elevenName"+i,   elevenPro[i].getproName());
                                intent.putExtra("elevenPrice"+i,  elevenPro[i].getproPrice());
                                intent.putExtra("elevenEvent"+i,  elevenPro[i].getproEvent());
                                intent.putExtra("elevenCategory"+i,  elevenPro[i].getproCategory());
                            }
                            startActivity(intent);
                            finish();
                        }
                        else if(index == 4) {
                            Intent intent = new Intent(IntroActivity.this,
                                    Mini_Layout.class);
                            for(int i=0;i<=20;i++){
                                intent.putExtra("miniImage"+i,  miniPro[i].getproUrl());
                                intent.putExtra("miniName"+i,   miniPro[i].getproName());
                                intent.putExtra("miniPrice"+i,  miniPro[i].getproPrice());
                                intent.putExtra("miniEvent"+i,  miniPro[i].getproEvent());
                                intent.putExtra("miniCategory"+i,  miniPro[i].getproCategory());
                            }
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(IntroActivity.this,
                                    MainActivity.class);
                            for(int i=0;i<=20;i++){
                                intent.putExtra("cuImage"+i,  cuPro[i].getproUrl());
                                intent.putExtra("cuName"+i,   cuPro[i].getproName());
                                intent.putExtra("cuPrice"+i,  cuPro[i].getproPrice());
                                intent.putExtra("cuEvent"+i,  cuPro[i].getproEvent());
                                intent.putExtra("cuCategory"+i,  cuPro[i].getproCategory());

                                intent.putExtra("gsImage"+i,  gsPro[i].getproUrl());
                                intent.putExtra("gsName"+i,   gsPro[i].getproName());
                                intent.putExtra("gsPrice"+i,  gsPro[i].getproPrice());
                                intent.putExtra("gsEvent"+i,  gsPro[i].getproEvent());
                                intent.putExtra("gsCategory"+i,  gsPro[i].getproCategory());

                                intent.putExtra("elevenImage"+i,  elevenPro[i].getproUrl());
                                intent.putExtra("elevenName"+i,   elevenPro[i].getproName());
                                intent.putExtra("elevenPrice"+i,  elevenPro[i].getproPrice());
                                intent.putExtra("elevenEvent"+i,  elevenPro[i].getproEvent());
                                intent.putExtra("elevenCategory"+i,  elevenPro[i].getproCategory());

                                intent.putExtra("miniImage"+i,  miniPro[i].getproUrl());
                                intent.putExtra("miniName"+i,   miniPro[i].getproName());
                                intent.putExtra("miniPrice"+i,  miniPro[i].getproPrice());
                                intent.putExtra("miniEvent"+i,  miniPro[i].getproEvent());
                                intent.putExtra("miniCategory"+i,  miniPro[i].getproCategory());
                            }
                            startActivity(intent);
                            finish();
                        }
                    } else{
                        Toast.makeText(getApplicationContext(),"DB를 읽어오지 못하였습니다." +
                                "인터넷 상태를 확인해 주세요.",Toast.LENGTH_SHORT).show();
                    }
                }
            }, 4000);

    }
}
