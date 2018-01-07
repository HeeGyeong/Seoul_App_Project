package com.example.heegyeong.project_second;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Like_Layout extends AppCompatActivity {
// 삭제해도 상관 없는 부분. 각 편의점 Layout에서 즐겨찾기 부분 삭제 후에 삭제할 것.
/*
    String image = null;
    String name =  null;
    String price = null;
    boolean clear = false;

    int[] imgId = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listview;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);


        // S_~ 로 되있는 값은 앱에 저장되어있는 값.
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        String[] S_image  = new String[10];
        String[] S_name  = new String[10];
        String[] S_price  = new String[10];

        for(int i=0;i<10;i++) {
            S_image[i] = prefs.getString("image"+i, null);// 값이 없을시 null을 넣음
            S_name[i] = prefs.getString("name"+i, null);// 값이 없을시 null을 넣음
            S_price[i] = prefs.getString("price"+i, null);// 값이 없을시 null을 넣음
        }


        Intent intent = getIntent();
        // intent값 호출  S_~ 가 붙어있지 않는 값은 실행시에 넘겨주는 값.
        image = intent.getStringExtra("image");// 처음 시작시 각 string은 null 무조건 null이됨
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        clear = intent.getBooleanExtra("clear", false);


        if(clear == false) {
            for (int i = 0; i < 10; i++) {
                if (name != null) // intent로 값이 넘어온 값이 있다.
                {
                    if (S_name[i] != null) {  // S_name[i]에 값이 이미 저장되어 있으면
                        for(int j=0;j<10;j++) {
                            if (S_name[j] != null) {
                                String a = S_name[j];
                                if (a.equals(name)) {
                                    Toast.makeText(getApplicationContext(), name +"제품은 이미 추가되어 있습니다.", Toast.LENGTH_SHORT).show();
                                    name = null;
                                }
                            }
                        }
                    } else{//  S_name에 저장된 값이 없으면, 해당 index에 값을 저장한다.
                        S_image[i] = image;
                        S_name[i] = name;
                        S_price[i] = price;
                        Toast.makeText(getApplicationContext(), name + " 제품이 즐겨찾기에 추가되었습니다.",Toast.LENGTH_SHORT).show();
                        name = null;
                    }
                }
            }
        } else if(clear == true){
            // 삭제 할때 사용. 아무것도 하지않는다.
       //     Toast.makeText(getApplicationContext(),"삭제를 눌렀다 테스트"+ clear, Toast.LENGTH_SHORT).show();
        }

        // 값 저장
        prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(clear == false) {
            for (int i = 0; i < 10; i++) {
                if (S_name[i] != null) {
                    editor.putString("image" + i, S_image[i]);
                    editor.putString("name" + i, S_name[i]);
                    editor.putString("price" + i, S_price[i]);
                    editor.commit();

                    imgId[i] = getApplicationContext().getResources().getIdentifier(S_image[i],"drawable", "com.example.heegyeong.project_second");
                } else{

                }
            }
        } else if(clear == true){
            for(int i=0;i<10;i++){
                if(S_name[i].equals(name)){
                    editor.remove("iamge"+i);
                    editor.remove("name"+i);
                    editor.remove("price" + i);
                    editor.commit();

                //    imgId[i] = 0;
                    for(int j=i;j<9;j++) {
                        editor.putString("imgae" + j, S_image[j + 1]);
                        editor.putString("name" + j, S_name[j + 1]);
                        editor.putString("price" + j, S_price[j + 1]);

                 //       imgId[j] = imgId[j+1];

                        editor.remove("iamge" + j + 1);
                        editor.remove("name" + j + 1);
                        editor.remove("price" + j + 1);
                        editor.commit();

                //        imgId[j+1] = 0;
                    }
                    adapter.notifyDataSetChanged();
                    finish();
                    break;
                } else {
          //          break;
                }
            }
        }


      데이터 잘 넘어가는지 확인 완료
        TextView ts_tv1 = (TextView)findViewById(R.id.test_tv1);
        TextView ts_tv2 = (TextView)findViewById(R.id.test_tv2);
        TextView ts_tv3 = (TextView)findViewById(R.id.test_tv3);

        ts_tv1.setText(S_image);
        ts_tv2.setText(S_name);
        ts_tv3.setText(S_price);


        // 아이템 추가.
        for(int i=0;i<10;i++) {
            if (S_name[i] != null) {
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_face_24dp),
                        S_name[i], S_price[i]);
                adapter.addItem(ContextCompat.getDrawable(this, imgId[i]),
                        S_name[i], S_price[i]);
            }
        }
    }
*/
    // 버튼 만들어서, 즐겨찾기 전체 제거 하나 만들기.
}
