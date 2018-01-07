package com.example.heegyeong.culture_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;

import kr.go.seoul.airquality.AirQualityTypeMini;
import kr.go.seoul.culturalevents.CulturalEventButtonTypeA;
import kr.go.seoul.culturalevents.CulturalEventTypeMini;

public class MainActivity extends AppCompatActivity {


    //////////////////////
    String [] sHour;
    String [] sDay;
    String [] sTemp;
    String [] sTmx;
    String [] sTmn;
    String [] sPop;
    String [] sReh;
    String [] sWfKor;

    int data=0;

    boolean bHour;
    boolean bDay;
    boolean bTemp;
    boolean bReh;
    boolean bWfKor;
    boolean bTmx;
    boolean bTmn;
    boolean bPop;

    boolean tItem;

    Handler handler;

    private CulturalEventButtonTypeA typeA;
    private CulturalEventTypeMini typeMini;
    private AirQualityTypeMini airTypeMini;

    private String openApiKey = "5973587361666873353941544d6271";

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#bebebe"));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffbebebe));

        typeA = (CulturalEventButtonTypeA) findViewById(R.id.type_a);
        typeMini = (CulturalEventTypeMini) findViewById(R.id.type_mini);
        airTypeMini = (AirQualityTypeMini) findViewById(R.id.ins_mini);

        typeA.setOpenAPIKey(openApiKey);
        typeMini.setOpenAPIKey(openApiKey);
        airTypeMini.setOpenAPIKey(openApiKey);

        typeA.setButtonImage(R.drawable.ic_pageview_24dp);

        Button otherSetting = (Button)findViewById(R.id.movebtn);
        otherSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSelectOption();
            }
        });


        handler=new Handler();

        bHour=bTemp=bReh=bDay=bWfKor=bTmx=bTmn=bPop=tItem=false;

        sHour=new String[20];
        sDay=new String[20];
        sTemp=new String[20];
        sTmx = new String[20];
        sTmn = new String[20];
        sPop = new String[20];
        sReh=new String[20];
        sWfKor=new String[20];

        network_thread thread = new network_thread();
        thread.start();
    }

    private void DialogSelectOption() {
        final String items[] = { "대중교통", "자전거" };
        index = 0;
        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
        ab.setTitle("교통 정보");
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        switch (whichButton){
                            case 0:
                                index = 0;
                                break;
                            case 1:
                                index = 1;
                                break;
                            default:
                                index = 0;
                                break;
                        }
                    }
                }).setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        switch (index){
                            case 0:
                        //        Toast.makeText(getApplicationContext(), "item1 Click : 대중교통 " , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                     //           Toast.makeText(getApplicationContext(), "item2 Click : 자전거 " , Toast.LENGTH_SHORT).show();
                                Intent intent3 = new Intent(MainActivity.this, ExBycleActivitiy.class);
                                startActivity(intent3);
                                break;
                            default:
                         //       Toast.makeText(getApplicationContext(), "no Click " , Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        index = 0;
                    }
                });
        ab.show();

    }

    public void click(View view){
        switch (view.getId()) {
            case R.id.wthbtn :
                Intent intent2 = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    class network_thread extends Thread{

        public void run(){

            try{
                Log.d("checkList", "thread, RUN");

                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp=factory.newPullParser();

                String weatherUrl="http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1114059000";
                URL url=new URL(weatherUrl);
                InputStream is=url.openStream();
                xpp.setInput(is,"UTF-8");

                int eventType=xpp.getEventType();

                while(eventType!=XmlPullParser.END_DOCUMENT){

                    switch(eventType){
                        case XmlPullParser.START_TAG:
                            Log.d("checkList", "START_TAG, RUN");

                            if(xpp.getName().equals("hour")){
                                bHour=true;

                            } if(xpp.getName().equals("day")){
                            bDay=true;

                        } if(xpp.getName().equals("temp")){
                            bTemp=true;

                        } if(xpp.getName().equals("tmx")){
                            bTmx=true;

                        } if(xpp.getName().equals("tmn")){
                            bTmn=true;

                        } if(xpp.getName().equals("wfKor")){
                            bWfKor=true;

                        } if(xpp.getName().equals("pop")){
                            bPop=true;

                        } if(xpp.getName().equals("reh")){
                            bReh=true;

                        }

                            break;

                        case XmlPullParser.TEXT:
                            Log.d("checkList", "TEXT, RUN");
                            if(bHour){
                                sHour[data]=xpp.getText();
                                bHour=false;
                            }  if(bDay){
                            sDay[data]=xpp.getText();
                            bDay=false;
                        }  if(bTemp){
                            sTemp[data]=xpp.getText();
                            bTemp=false;
                        }  if(bTmx){
                            sTmx[data]=xpp.getText();

                            bTmx=false;
                        }  if(bTmn){
                            sTmn[data]=xpp.getText();

                            bTmn=false;
                        }  if(bWfKor){
                            sWfKor[data]=xpp.getText();
                            bWfKor=false;
                        }  if(bPop){
                            sPop[data]=xpp.getText();
                            bPop=false;
                        }  if(bReh){
                            sReh[data]=xpp.getText();
                            bReh=false;
                        }
                            break;

                        case XmlPullParser.END_TAG:
                            Log.d("checkList", "END_TAG, run");
                            if(xpp.getName().equals("item")){
                                Log.d("checkList", "END_TAG, call view_text()");
                                tItem=true;
                                view_text();
                            } if(xpp.getName().equals("data")){
                            data++;
                        }
                            break;
                    }
                    eventType=xpp.next();
                }

            }catch(Exception e){
                e.printStackTrace();
            }

        }

        private void view_text(){

            handler.post(new Runnable() {	//기본 핸들러니깐 handler.post하면됨

                @Override
                public void run() {
                    Log.d("checkList", "view_text, RUN");
                    if(tItem){
                        Log.d("checkList", "view_text, Item start" + sTemp[0]);
                        if(Float.parseFloat(sTmx[0]) == -999){
                            sTmx[0] = "서버 정보 오류";
                        }else {
                            sTmx[0] += " 도";
                        }
                        if(Float.parseFloat(sTmn[0]) == -999){
                            sTmn[0] = "서버 정보 오류";
                        }else{
                            sTmn[0] += " 도";
                        }
                        Toast.makeText(getApplicationContext(), "현재 온도 : " + sTemp[0] + " 도"+"\n"
                                +"최고 온도 : "+sTmx[0]+"\n"
                                +"최저 온도 : "+sTmn[0]+"\n"
                                +"날씨 : "+sWfKor[0]+"\n"
                                +"강수 확률 : "+sPop[0]+"%",Toast.LENGTH_LONG).show();

                        if(sTmx[0].equals("서버 정보 오류") || sTmn[0].equals("서버 정보 오류")){
                            Toast.makeText(getApplicationContext(), "기상청 서버 오류 발생\n잠시 후 다시 확인해주세요.",Toast.LENGTH_LONG).show();
                        }
                        tItem=false;
                        data=0;

                    }

                    if(sTemp[0] == null){
                        Toast.makeText(getApplicationContext(), "서버 정보 오류",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}
