package com.example.heegyeong.seoul_maptagging;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Heegyeong on 2017-09-01.
 */
public class ConvenActivity  extends AppCompatActivity {

    Intent intent;

    String local="";
    String[] wlocal = {
            "강북구","강동구","강남구","강서구","관악구",
            "광진구","구로구","금천구","노원구","도봉구",
            "동대문구","동작구","마포구","서대문구","서초구",
            "성동구","성북구","송파구","양천구","영등포구",
            "용산구","은평구","종로구","중구","중랑구" };

    String userID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conven);

        SharedPrefereneceUtil sharedPrefereneceUtil = new SharedPrefereneceUtil(this);
        userID = sharedPrefereneceUtil.getSharedPreference("userID",null);

        intent = getIntent();
        local = intent.getStringExtra("local");

        if(local.equals("gangbuk")) local = wlocal[0];
        else if(local.equals("gangdong"))local = wlocal[1];
        else if( local.equals("gangnam")) local = wlocal[2];
        else if( local.equals("gangseo")) local = wlocal[3];
        else if( local.equals("gwanak")) local = wlocal[4];
        else if( local.equals("gwangjin")) local = wlocal[5];
        else if( local.equals("guro")) local = wlocal[6];
        else if( local.equals("geumcheon")) local = wlocal[7];
        else if( local.equals("nowon")) local = wlocal[8];
        else if( local.equals("dobong")) local = wlocal[9];
        else if( local.equals("dongdaemun")) local = wlocal[10];
        else if( local.equals("dongjak")) local = wlocal[11];
        else if( local.equals("mapo")) local = wlocal[12];
        else if( local.equals("seodaemun")) local = wlocal[13];
        else if( local.equals("seocho")) local = wlocal[14];
        else if( local.equals("seongdong")) local = wlocal[15];
        else if( local.equals("seongbuk")) local = wlocal[16];
        else if( local.equals("songpa")) local = wlocal[17];
        else if( local.equals("yangcheon")) local = wlocal[18];
        else if( local.equals("yeongdeungpo")) local = wlocal[19];
        else if( local.equals("youngsan")) local = wlocal[20];
        else if( local.equals("eunpyeong")) local = wlocal[21];
        else if( local.equals("jongno")) local = wlocal[22];
        else if( local.equals("jung")) local = wlocal[23];
        else if( local.equals("jungnang")) local = wlocal[24];

        Button b1 = (Button)findViewById(R.id.btn1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 1;
                Intent intent = new Intent(ConvenActivity.this, MapTagging.class);
                intent.putExtra("num",num);
                intent.putExtra("local",local);

                Toast.makeText(getApplicationContext(), "공중 화장실을 선택하셨습니다." , Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        Button b2 = (Button)findViewById(R.id.btn2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 2;
                Intent intent = new Intent(ConvenActivity.this, MapTagging.class);
                intent.putExtra("num",num);
                intent.putExtra("local",local);
                Toast.makeText(getApplicationContext(), "공공 와이파이를 선택하셨습니다." , Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        Button b3 = (Button)findViewById(R.id.btn3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 3;
                Intent intent = new Intent(ConvenActivity.this, MapTagging.class);
                intent.putExtra("num",num);
                intent.putExtra("local",local);
                Toast.makeText(getApplicationContext(), "도서관 및 서점을 선택하셨습니다." , Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        Button b4 = (Button)findViewById(R.id.btn4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 4;
                Intent intent = new Intent(ConvenActivity.this, MapTagging.class);
                intent.putExtra("num",num);
                intent.putExtra("local",local);
                Toast.makeText(getApplicationContext(), "산과 공원을 선택하셨습니다." , Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        Button b5 = (Button)findViewById(R.id.btn5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 5;
                Intent intent = new Intent(ConvenActivity.this, MapTagging.class);
                intent.putExtra("num",num);
                intent.putExtra("local",local);
                Toast.makeText(getApplicationContext(), "자전거대여소를 선택하셨습니다." , Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        Button b6 = (Button)findViewById(R.id.btn6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 6;
                Intent intent = new Intent(ConvenActivity.this, MapTagging.class);
                intent.putExtra("num",num);
                intent.putExtra("local",local);
                Toast.makeText(getApplicationContext(), "택시승차대를 선택하셨습니다." , Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        Button b7 = (Button)findViewById(R.id.btn7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 7;
                Intent intent = new Intent(ConvenActivity.this, MapTagging.class);
                intent.putExtra("num",num);
                intent.putExtra("local",local);
                Toast.makeText(getApplicationContext(), "전기휠체어 충전기를 선택하셨습니다." , Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    @Override
    public  boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent1 = new Intent(ConvenActivity.this,MainActivity.class);
            if(userID == null){
                intent1.putExtra("guestID","guestID");
            }
            startActivity(intent1);
            return true;
        }
        return false;
    }

}
