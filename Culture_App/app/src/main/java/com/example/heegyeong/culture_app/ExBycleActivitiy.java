package com.example.heegyeong.culture_app;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Heegyeong on 2017-10-31.
 */
public class ExBycleActivitiy extends AppCompatActivity {

    int data = 0;
    String [] oldAddress = new String[770];
    String [] contsName = new String[770];
    String [] coordX = new String[770];
    String [] coordY = new String[770];
    String [] guName = new String[770];

    Spinner spinner;
    int i = 0;

    String local = "";
    String donglist[]={"강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구","도봉구","동대문구"
            ,"동작구","마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로구","중구","중랑구"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_exbycle);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#bebebe"));
        }

        for(int i = 0; i < 770 ; i++){  // 각 String 배열 Null로 초기화
            Log.d("chogi", "count : "+ i);
            oldAddress[i] = "";
            contsName[i] = "";
            coordY[i] = "";
            coordX[i] = "";
            guName[i] = "";
        }

        spinner=(Spinner)findViewById(R.id.list);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {	//이부분은 스피너에 나타나는 내용

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {	//선택시
                local=donglist[position];
                network_thread2 thread=new network_thread2();		//스레드생성(UI 스레드사용시 system 뻗는다)
                thread.start();	//스레드 시작
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {	//미선택시
                local=donglist[0];
                network_thread2 thread=new network_thread2();		//스레드생성(UI 스레드사용시 system 뻗는다)
                thread.start();	//스레드 시작


            }
        });
        // 어댑터 객체 생성
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, donglist);	//어댑터를 통해 스피너에 donglist 넣어줌
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	//dropdown형식

        // 어댑터 설정
        spinner.setAdapter(adapter);

    }

    public void click(View view){
        switch (view.getId()) {
            case R.id.nextAct :
                Intent intent1 = new Intent(ExBycleActivitiy.this, BycleActivitiy.class);
                for(int i=0;i<data;i++){
                    intent1.putExtra("oldAddress"+i, oldAddress[i]);
                    intent1.putExtra("contsName"+i,  contsName[i]);
                    intent1.putExtra("coordX"+i, coordX[i]);
                    intent1.putExtra("coordY"+i,  coordY[i]);
                    intent1.putExtra("guName"+i,  guName[i]);
                }
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    class network_thread2 extends Thread{

        public void run(){

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                String address = "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/Mgisbicycleconvenience/1/764/";
                URL url = new URL(address);
                InputStream in = url.openStream();

                Log.i("===parser==", "ok");
                parser.setInput(in, "utf-8");

                int eventType = parser.getEventType();
                String START = "";
                String TEXT = "";
                String END = "";

                String COT_ADDR_FULL_NEW = "", COT_ADDR_FULL_OLD = "", COT_COORD_X = "", COT_COORD_Y = "", COT_GU_NAME = "", COT_CONTS_NAME = "";

                boolean isTag = false;
                data = 0;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {                                   //태그값, 데이터값 읽는 부분
                        case XmlPullParser.START_TAG:
                            START = parser.getName();
                            break;
                        case XmlPullParser.TEXT:
                            TEXT = parser.getText();
                            if (START.equals("COT_ADDR_FULL_OLD")) {
                                if (parser.getText().length() > 1)
                                    COT_ADDR_FULL_OLD = parser.getText();
                            }
                            if (START.equals("COT_COORD_X")) {
                                if (parser.getText().length() > 1)
                                    COT_COORD_X = parser.getText();
                            }
                            if (START.equals("COT_COORD_Y")) {
                                if (parser.getText().length() > 1)
                                    COT_COORD_Y = parser.getText();
                            }
                            if (START.equals("COT_CONTS_NAME")) {
                                if(parser.getText().length()> 1)
                                    COT_CONTS_NAME = parser.getText();
                            }
                            if (START.equals("COT_GU_NAME")) {
                                COT_GU_NAME = parser.getText();
                            }

                            if(COT_GU_NAME.equals(local)){
                                if(COT_ADDR_FULL_OLD.contains(COT_GU_NAME)){
                                    if(data < 770){
                                        Log.d("cck","local : " + local + " i : " + i);
                                        Log.d("cck","value : " + COT_ADDR_FULL_OLD + " : " + COT_GU_NAME);
                                        oldAddress[data] = COT_ADDR_FULL_OLD;
                                        contsName[data] = COT_CONTS_NAME;
                                        coordX[data] = COT_COORD_X;
                                        coordY[data] = COT_COORD_Y;
                                        guName[data] = COT_GU_NAME;
                                        data++;
                                    }
                                }
                            }

                            // for문을 반복할 때 index번호는 계속 증가되어야 제대로 된 index값이 저장된다.

                            break;

                        case XmlPullParser.END_TAG:
                            END = parser.getName();
                            break;
                    }
                    if (START.equals(END)) {
                        //textView.append(a);
                    }
                    eventType = parser.next(); // 다음 태그로 이동
                }

            } catch (Exception e) {//예외처리
                e.printStackTrace();
                Log.e("dd", "Error in network call", e);
            }

        }
    }
}

