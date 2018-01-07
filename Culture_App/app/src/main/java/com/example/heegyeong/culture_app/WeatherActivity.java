package com.example.heegyeong.culture_app;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;

import android.view.View.OnClickListener;
import android.widget.Toast;

import kr.go.seoul.airquality.AirQualityButtonTypeA;

/**
 * Created by Heegyeong on 2017-10-27.
 */
public class WeatherActivity extends AppCompatActivity {

    private AirQualityButtonTypeA airTypeA;
    private String openApiKey = "5973587361666873353941544d6271";

    Spinner spinner;	
    Button getBtn;
    TextView text;
    String sCategory;
    String sTm;
    String [] sHour;
    String [] sDay;
    String [] sTemp;
    String [] sTmx;
    String [] sTmn;
    String [] sPop;
    String [] sReh;
    String [] sWfKor;

    int data=0;

    boolean bCategory;
    boolean bTm;
    boolean bHour;
    boolean bDay;
    boolean bTemp;
    boolean bReh;
    boolean bWfKor;
    boolean bTmx;
    boolean bTmn;
    boolean bPop;

    boolean tCategory;
    boolean tTm;
    boolean tItem;

    Handler handler;


    String dongcode[]={"1168066000","1174051500","1130553500","1150060300","1162058500","1121581000","1153059500","1154551000"
            ,"1135059500","1132052100","1123060000","1159051000","1144056500","1141069000","1165066000","1120059000"
            ,"1129066000","1171063100","1147051000","1156055000","1117053000","1138055100","1111060000","1114059000","1126065500"};
    String donglist[]={"강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구","도봉구","동대문구"
            ,"동작구","마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로구","중구","중랑구"};
    String dong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#bebebe"));
        }

        airTypeA = (AirQualityButtonTypeA) findViewById(R.id.ins_mini);

        airTypeA.setOpenAPIKey(openApiKey);

        airTypeA.setButtonImage(R.drawable.enter);


        handler=new Handler();

        bCategory=bTm=bHour=bTemp=bReh=bDay=bWfKor=bTmx=bTmn=bPop=tCategory=tTm=tItem=false;

        sHour=new String[20];
        sDay=new String[20];
        sTemp=new String[20];
        sTmx = new String[20];
        sTmn = new String[20];
        sPop = new String[20];
        sReh=new String[20];
        sWfKor=new String[20];

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                dong=dongcode[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dong=dongcode[0];

            }
        });
        // 어댑터 객체 생성
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, donglist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(adapter);



        text=(TextView) findViewById(R.id.textView1);
        getBtn=(Button) findViewById(R.id.getBtn);
        getBtn.setOnClickListener(new OnClickListener() {	//버튼을 눌러보자

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                text.setText("");
                network_thread thread=new network_thread();
                thread.start();
            }
        });
    }


    class network_thread extends Thread{

        public void run(){

            try{

                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp=factory.newPullParser();

                String weatherUrl="http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone="+dong;
                URL url=new URL(weatherUrl);
                InputStream is=url.openStream();
                xpp.setInput(is,"UTF-8");

                int eventType=xpp.getEventType();

                while(eventType!=XmlPullParser.END_DOCUMENT){

                    switch(eventType){
                        case XmlPullParser.START_TAG:

                            if(xpp.getName().equals("category")){
                                bCategory=true;

                            } if(xpp.getName().equals("tm")){
                            bTm=true;

                        } if(xpp.getName().equals("hour")){
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
                            if(bCategory){
                                sCategory=xpp.getText();
                                bCategory=false;
                            } if(bTm){
                            sTm=xpp.getText();
                            bTm=false;
                        }  if(bHour){
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

                            if(xpp.getName().equals("item")){
                                tItem=true;
                                view_text();
                            } if(xpp.getName().equals("tm")){
                            tTm=true;
                            view_text();
                        } if(xpp.getName().equals("category")){
                            tCategory=true;
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

                    if(tCategory){
                        tCategory=false;
                    }if(tTm){
                        tTm=false;
                    }if(tItem){
                        Log.d("checkList", "view_text, Item start");
                        for(int i=0;i<data;i++){
                            Log.d("checkList", "view_text, Item first for");
                            if(sDay[i]!=null){
                                Log.d("checkList", "view_text, Item first if (sDay)");
                                if(Integer.parseInt(sDay[i])==0){
                                    Log.d("checkList", "view_text, Item 2dn if (Integer.parseInt sDay 0)");
                                    text.setText(text.getText() + "오늘 날씨" + "\n");
                                }else if(Integer.parseInt(sDay[i])==1){
                                    Log.d("checkList", "view_text, Item 2dn if (Integer.parseInt sDay 1)");
                                    text.setText(text.getText() + "내일 날씨" + "\n");
                                }else if(Integer.parseInt(sDay[i])==2){
                                    Log.d("checkList", "view_text, Item 2dn if (Integer.parseInt sDay 2)");
                                    text.setText(text.getText() + "모레 날씨" + "\n");
                                }
                                if(Float.parseFloat(sTmx[i]) == -999){
                                    sTmx[i] = "서버 정보 오류";
                                }else {
                                    sTmx[i] += " 도";
                                }
                                if(Float.parseFloat(sTmn[0]) == -999){
                                    sTmn[i] = "서버 정보 오류";
                                }else{
                                    sTmn[i] += " 도";
                                }
                                if(sTmx[0].equals("서버 정보 오류") || sTmn[0].equals("서버 정보 오류")){
                                    Toast.makeText(getApplicationContext(), "기상청 서버 오류 발생\n잠시 후 다시 확인해주세요.",Toast.LENGTH_LONG).show();
                                }

                                Log.d("checkList", "view_text, Item first if ending position..");
                                text.setText(text.getText()+"현재 온도 : "+sTemp[i]+" 도"+"\n");	//온도
                                text.setText(text.getText()+"최고 온도 : "+sTmx[i]+"\n");
                                text.setText(text.getText()+"최저 온도 : "+sTmn[i]+"\n");
                                text.setText(text.getText()+"날씨 : "+sWfKor[i]+"\n");			//날씨
                                text.setText(text.getText()+"강수 확률 : "+sPop[i]+"%"+"\n");
                                text.setText(text.getText()+"습도 : "+sReh[i]+"%"+"\n\n\n");			//습도
                                break;
                            }
                        }

                        tItem=false;
                        data=0;

                    }

                }
            });
        }
    }



}
