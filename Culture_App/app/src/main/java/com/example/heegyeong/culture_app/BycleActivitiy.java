package com.example.heegyeong.culture_app;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by Heegyeong on 2017-10-27.
 */
public class BycleActivitiy extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Data> DataList;

    protected Handler handler;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_bycle);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#bebebe"));
        }


        //     tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        DataList = new ArrayList<Data>();
        handler = new Handler();

        Intent intent = getIntent();
        for (int i = 0; i <= 50;i++) {
            final String oldAddress = intent.getStringExtra("oldAddress"+i);
            final String contsName = intent.getStringExtra("contsName"+i);
            final String coordX = intent.getStringExtra("coordX"+i);
            final String coordY = intent.getStringExtra("coordY" + i);
            final String guName = intent.getStringExtra("guName"+i);

            if(contsName != null){
                DataList.add(new Data(oldAddress, contsName, coordX, coordY));
                count++;
            }
        }
        if(count == 0){
            Toast.makeText(getApplicationContext(), "해당 지역에 자전거 대여소가 존재하지 않습니다.",Toast.LENGTH_LONG).show();
        }


        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DataAdapter(DataList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Data list = DataList.get(position);

                Intent intent = new Intent(BycleActivitiy.this, BycleSearchActivity.class);
                intent.putExtra("CoordX", list.getCoordX());
                intent.putExtra("CoordY", list.getCoordY());
                startActivity(intent);


            }

        }));

    }
}
