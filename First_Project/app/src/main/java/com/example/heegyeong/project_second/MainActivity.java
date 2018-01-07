package com.example.heegyeong.project_second;

import android.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends RuntimePermission
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FIRST_VIEW_SELECT = 1; // 시작 화면 세팅
    private BackPressCloseHandler backPressCloseHandler;    // 뒤로가기 종료

    private static final int REQUEST_PERMISSION = 10;   // 런타임 퍼미션


    public static Product[] cuPro = new Product[21];
    public static Product[] gsPro = new Product[21];
    public static Product[] elevenPro = new Product[21];
    public static Product[] miniPro = new Product[21];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestAppPermissions(new String[]{
                        android.Manifest.permission.CALL_PHONE,
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.READ_SMS},
                R.string.msg, REQUEST_PERMISSION);


        // 값 호출
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        for(int i =0;i<=20;i++){
            cuPro[i] = new Product(prefs.getString("cuName" + i, null),prefs.getString("cuPrice" + i, null),prefs.getString("cuEvent" + i, null),
                    prefs.getString("cuImage" + i, null),prefs.getString("cuCategory" + i, null));

            gsPro[i] = new Product(prefs.getString("gsName" + i, null),prefs.getString("gsPrice" + i, null),prefs.getString("gsEvent" + i, null),
                    prefs.getString("gsImage" + i, null),prefs.getString("gsCategory" + i, null));

            elevenPro[i] = new Product(prefs.getString("elevenName" + i, null),prefs.getString("elevenPrice" + i, null),
                    prefs.getString("elevenEvent" + i, null),prefs.getString("elevenImage" + i, null),prefs.getString("elevenCategory" + i, null));

            miniPro[i] = new Product(prefs.getString("miniName"+i,null),prefs.getString("miniPrice"+i,null),prefs.getString("miniEvent" + i, null),
                    prefs.getString("miniImage"+i,null),prefs.getString("miniCategory"+i,null));
        }

        if(cuPro[20].getproName() == null){
            Intent intent = getIntent();
            // intent값 호출
            for(int i=0;i<=20;i++){
                cuPro[i] = new Product(intent.getStringExtra("cuName"+i),intent.getStringExtra("cuPrice"+i),
                        intent.getStringExtra("cuEvent" + i),intent.getStringExtra("cuImage"+i),intent.getStringExtra("cuCategory"+i));

                gsPro[i] = new Product(intent.getStringExtra("gsName"+i),intent.getStringExtra("gsPrice"+i),
                        intent.getStringExtra("gsEvent" + i),intent.getStringExtra("gsImage"+i),intent.getStringExtra("gsCategory"+i));

                elevenPro[i] = new Product(intent.getStringExtra("elevenName"+i),intent.getStringExtra("elevenPrice"+i),
                        intent.getStringExtra("elevenEvent" + i),intent.getStringExtra("elevenImage"+i),intent.getStringExtra("elevenCategory"+i));

                miniPro[i] = new Product(intent.getStringExtra("miniName"+i),intent.getStringExtra("miniPrice"+i),
                        intent.getStringExtra("miniEvent" + i),intent.getStringExtra("miniImage"+i),intent.getStringExtra("miniCategory"+i));

                //Log.d("cuPro",i+"번 객체 : "+intent.getStringExtra("cuName"+i)+" : "+intent.getStringExtra("cuPrice"+i)+" : "+
                //        intent.getStringExtra("cuEvent" + i)+" : "+intent.getStringExtra("cuImage"+i)+" : "+intent.getStringExtra("cuCategory"+i));
            }

            // 값 저장
            prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            for(int i=0;i<=20;i++){
                editor.putString("cuName"+i, cuPro[i].getproName());
                editor.putString("cuPrice"+i, cuPro[i].getproPrice());
                editor.putString("cuEvent"+i, cuPro[i].getproEvent());
                editor.putString("cuImage"+i, cuPro[i].getproUrl());
                editor.putString("cuCategory"+i, cuPro[i].getproCategory());

                editor.putString("gsName"+i, gsPro[i].getproName());
                editor.putString("gsPrice"+i, gsPro[i].getproPrice());
                editor.putString("gsEvent"+i, gsPro[i].getproEvent());
                editor.putString("gsImage"+i, gsPro[i].getproUrl());
                editor.putString("gsCategory"+i, gsPro[i].getproCategory());

                editor.putString("elevenName"+i, elevenPro[i].getproName());
                editor.putString("elevenPrice"+i, elevenPro[i].getproPrice());
                editor.putString("elevenEvent"+i, elevenPro[i].getproEvent());
                editor.putString("elevenImage"+i, elevenPro[i].getproUrl());
                editor.putString("elevenCategory"+i, elevenPro[i].getproCategory());

                editor.putString("miniName"+i, miniPro[i].getproName());
                editor.putString("miniPrice"+i, miniPro[i].getproPrice());
                editor.putString("miniEvent"+i, miniPro[i].getproEvent());
                editor.putString("miniImage"+i, miniPro[i].getproUrl());
                editor.putString("miniCategory"+i, miniPro[i].getproCategory());
            }
            editor.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#3cb371"));
        }

        // 기본 layout에서 노출되는 버튼들
        Button b1 = (Button)findViewById(R.id.cu);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CU_Layout.class);
                for(int i=0;i<=20;i++){
                    intent.putExtra("cuImage"+i,  cuPro[i].getproUrl());
                    intent.putExtra("cuName"+i,   cuPro[i].getproName());
                    intent.putExtra("cuPrice"+i,  cuPro[i].getproPrice());
                    intent.putExtra("cuEvent"+i,  cuPro[i].getproEvent());
                    intent.putExtra("cuCategory"+i,  cuPro[i].getproCategory());
                }
                startActivity(intent);
            }
        });

        Button b2 = (Button)findViewById(R.id.gs);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GS_Layout.class);
                for(int i=0;i<=20;i++){
                    intent.putExtra("gsImage"+i,  gsPro[i].getproUrl());
                    intent.putExtra("gsName"+i,   gsPro[i].getproName());
                    intent.putExtra("gsPrice"+i,  gsPro[i].getproPrice());
                    intent.putExtra("gsEvent"+i,  gsPro[i].getproEvent());
                    intent.putExtra("gsCategory"+i,  gsPro[i].getproCategory());
                }
                startActivity(intent);
            }
        });

        Button b3 = (Button)findViewById(R.id.eleven);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Eleven_Layout.class);
                for(int i=0;i<=20;i++){
                    intent.putExtra("elevenImage"+i,  elevenPro[i].getproUrl());
                    intent.putExtra("elevenName"+i,   elevenPro[i].getproName());
                    intent.putExtra("elevenPrice"+i,  elevenPro[i].getproPrice());
                    intent.putExtra("elevenEvent"+i,  elevenPro[i].getproEvent());
                    intent.putExtra("elevenCategory"+i,  elevenPro[i].getproCategory());
                }
                startActivity(intent);
            }
        });

        Button b4 = (Button)findViewById(R.id.mini);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Mini_Layout.class);
                for(int i=0;i<=20;i++){
                    intent.putExtra("miniImage"+i,  miniPro[i].getproUrl());
                    intent.putExtra("miniName"+i,   miniPro[i].getproName());
                    intent.putExtra("miniPrice"+i,  miniPro[i].getproPrice());
                    intent.putExtra("miniEvent"+i,  miniPro[i].getproEvent());
                    intent.putExtra("miniCategory"+i,  miniPro[i].getproCategory());
                }
                startActivity(intent);
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);    // 뒤로가기 종료 java class 객체 생성

    }

    @Override
    public void onPermissionsGranted(int requsetCode) {
        // 권한 설정 전부다 완료 되면 실행시에 뜨는 부분
        //  Toast.makeText(getApplicationContext(), "Permission granted",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    // 이 부분은 삭제해도 됨. 따로 생성했음.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            final Dialog SearchDialog = new Dialog(this);
            SearchDialog.setContentView(R.layout.map_search);
            SearchDialog.setTitle("매장 찾기");
            SearchDialog.show();
        } else if (id == R.id.nav_setting) {
            final Dialog OptionDialog = new Dialog(this);
            OptionDialog.setContentView(R.layout.option_main);
            OptionDialog.setTitle("옵션 설정");
            OptionDialog.show();
        }   else if (id == R.id.nav_other) {
            final Dialog InquireDialog = new Dialog(this);
            InquireDialog.setContentView(R.layout.inquire);
            InquireDialog.setTitle("문의하기");
            InquireDialog.show();
        } else if (id == R.id.nav_version) {
            Toast.makeText(this,
                   "Ver.2016.09.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_shared) {
            final Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.shared_link));
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.kakao.talk");
            try {
                startActivity(sendIntent);
            } catch (Exception e) {
                e.printStackTrace();
                // 카톡이 없다는 팝업을 뛰워주자.
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final CharSequence[] conven = {"Default", "CU","GS 25","7 - eleven", "Mini Stop"};
        final CharSequence[] name = {"Default", "CU","GS 25","7 - eleven", "Mini Stop"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("초기 화면을 선택하세요.");
        builder.setItems(conven, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                if (conven[item] == name[0]) {
                    intent.putExtra("value", "Default");
                    intent.putExtra("index", 0);
                    startActivity(intent);
                } else if (conven[item] == name[1]) {
                    intent.putExtra("value", "CU");
                    intent.putExtra("index", 1);
                    startActivity(intent);
                } else if (conven[item] == name[2]) {
                    intent.putExtra("value", "GS");
                    intent.putExtra("index", 2);
                    startActivity(intent);
                } else if (conven[item] == name[3]) {
                    intent.putExtra("value", "eleven");
                    intent.putExtra("index", 3);
                    startActivity(intent);
                } else if (conven[item] == name[4]) {
                    intent.putExtra("value", "mini");
                    intent.putExtra("index", 4);
                    startActivity(intent);
                }
            }
        });
        AlertDialog alert = builder.create();

        return alert;
    }

    public void onClick(View view){
        Intent intent = null;

        switch(view.getId()){
            case R.id.call_button :
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01089149295"));
                break;
            case R.id.email_button   :
                Intent intentEmail = new Intent(MainActivity.this, Email_Layout.class);
                startActivity(intentEmail);
                break;
        }
        if(intent != null)
            startActivity(intent);
    }

    public void onMapClick(View view){
        Intent intent = null;

        switch(view.getId()) {
            case R.id.cu_map:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cu.bgfretail.com/store/list.do?category=store"));
                break;
            case R.id.gs_map:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://gs25.gsretail.com/gscvs/ko/store-services/locations?uiel=Mobile"));
                    //  모바일로 수정. ?uiel=Mobile 추가됨
                break;
            case R.id.eleven_map:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.7-eleven.co.kr/store/storeSearch1.asp"));
                break;
            case R.id.mini_map:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ministop.co.kr//MiniStopHomePage/page/store/store.do"));
                break;
        }
        if(intent != null)
            startActivity(intent);
    }

    public void onNeviButton(View view){
        switch(view.getId()) {
            case R.id.nav_b_search:
                final Dialog SearchDialog = new Dialog(this);
                SearchDialog.setContentView(R.layout.map_search);
                SearchDialog.setTitle("매장 찾기");
                SearchDialog.show();
                break;
            case R.id.nav_b_service:
                final Dialog ServiceDialog = new Dialog(this);
                ServiceDialog.setContentView(R.layout.service);
                ServiceDialog.setTitle("서비스");
                ServiceDialog.show();
                break;
            case R.id.nav_b_setting:
                showDialog(FIRST_VIEW_SELECT);
                break;
            case R.id.nav_b_other:
                Intent intentEmail = new Intent(MainActivity.this, Email_Layout.class);
                startActivity(intentEmail);
                break;
            case R.id.nav_b_version:
                Toast.makeText(this,
                        "Ver.2016.11.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_b_shared:
                final Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.shared_link));
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.kakao.talk");
                try {
                    startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    // 카톡이 없다는 팝업을 뛰워주자.
                }
                break;
        }
    }


    public void onServiceClick(View view){
        Intent intent = null;

        switch(view.getId()) {
            case R.id.service_btn_cu:
                intent = new Intent(MainActivity.this, CU_Service.class);
                break;
            case R.id.service_btn_gs:
                intent = new Intent(MainActivity.this, GS_Service.class);
                break;
            case R.id.service_btn_eleven:
                intent = new Intent(MainActivity.this, Eleven_Service.class);
                break;
            case R.id.service_btn_mini:
                intent = new Intent(MainActivity.this, Mini_Service.class);
                break;
        }
        if(intent != null)
            startActivity(intent);
    }

}
