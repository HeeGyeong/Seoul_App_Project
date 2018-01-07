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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;

import kr.go.seoul.airquality.AirQualityButtonTypeA;
import kr.go.seoul.airquality.AirQualityTypeMini;
import kr.go.seoul.culturalevents.CulturalEventButtonTypeA;
import kr.go.seoul.culturalevents.CulturalEventTypeMini;

public class MainActivity extends AppCompatActivity {


    //////////////////////
    String [] sHour;	//예보시간(총 15개정도 받아옴 3일*5번)
    String [] sDay;		//날짜(몇번째날??)
    String [] sTemp;	//현재온도
    String [] sTmx;
    String [] sTmn;
    String [] sPop;
    String [] sReh;		//습도
    String [] sWfKor;	//날씨

    int data=0;	//이건 파싱해서 array로 넣을때 번지

    boolean bHour;
    boolean bDay;
    boolean bTemp;
    boolean bReh;
    boolean bWfKor;
    boolean bTmx;
    boolean bTmn;
    boolean bPop;

    boolean tItem;

    Handler handler;	//핸들러
    //////////////////////////


    private CulturalEventButtonTypeA typeA;
    private CulturalEventTypeMini typeMini;
    private AirQualityTypeMini airTypeMini;

    private String openApiKey = "5973587361666873353941544d6271";

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
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


        handler=new Handler();	//스레드&핸들러처리

        bHour=bTemp=bReh=bDay=bWfKor=bTmx=bTmn=bPop=tItem=false;	//부울상수는 false로 초기화해주자

        sHour=new String[20];	//예보시간(사실 15개밖에 안들어오지만 넉넉하게 20개로 잡아놓음)
        sDay=new String[20];	//날짜
        sTemp=new String[20];	//현재온도
        sTmx = new String[20];
        sTmn = new String[20];
        sPop = new String[20];
        sReh=new String[20];	//습도
        sWfKor=new String[20];	//날씨

        network_thread thread = new network_thread();        //스레드생성(UI 스레드사용시 system 뻗는다)
        thread.start();    //스레드 시작
    }

    private void DialogSelectOption() {
        final String items[] = { "대중교통", "자전거" };
        index = 0;
        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
        ab.setTitle("교통 정보");
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
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
                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
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
                        // Cancel 버튼 클릭시
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

    class network_thread extends Thread{	//기상청 연결을 위한 스레드

        public void run(){

            try{
                Log.d("checkList", "thread, RUN");

                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();	//이곳이 풀파서를 사용하게 하는곳
                factory.setNamespaceAware(true);									//이름에 공백도 인식
                XmlPullParser xpp=factory.newPullParser();							//풀파서 xpp라는 객체 생성

                String weatherUrl="http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1114059000";	//이곳이 기상청URL
                URL url=new URL(weatherUrl);		//URL객체생성
                InputStream is=url.openStream();	//연결할 url을 inputstream에 넣어 연결을 하게된다.
                xpp.setInput(is,"UTF-8");			//이렇게 하면 연결이 된다. 포맷형식은 utf-8로

                int eventType=xpp.getEventType();	//풀파서에서 태그정보를 가져온다.

                while(eventType!=XmlPullParser.END_DOCUMENT){	//문서의 끝이 아닐때

                    switch(eventType){
                        case XmlPullParser.START_TAG:	//'<'시작태그를 만났을때
                            Log.d("checkList", "START_TAG, RUN");

                            if(xpp.getName().equals("hour")){		//예보시간
                                bHour=true;

                            } if(xpp.getName().equals("day")){		//예보날(오늘 내일 모레)
                            bDay=true;

                        } if(xpp.getName().equals("temp")){		//예보시간기준 현재온도
                            bTemp=true;

                        } if(xpp.getName().equals("tmx")){
                            bTmx=true;

                        } if(xpp.getName().equals("tmn")){
                            bTmn=true;

                        } if(xpp.getName().equals("wfKor")){	//날씨정보(맑음, 구름조금, 구름많음, 흐림, 비, 눈/비, 눈)
                            bWfKor=true;

                        } if(xpp.getName().equals("pop")){
                            bPop=true;

                        } if(xpp.getName().equals("reh")){		//습도정보
                            bReh=true;

                        }

                            break;

                        case XmlPullParser.TEXT:	//텍스트를 만났을때
                            //앞서 시작태그에서 얻을정보를 만나면 플래그를 true로 했는데 여기서 플래그를 보고
                            //변수에 정보를 넣어준 후엔 플래그를 false로~
                            Log.d("checkList", "TEXT, RUN");
                            if(bHour){				//예보시각
                                sHour[data]=xpp.getText();
                                bHour=false;
                            }  if(bDay){				//예보날짜
                            sDay[data]=xpp.getText();
                            bDay=false;
                        }  if(bTemp){				//현재온도
                            sTemp[data]=xpp.getText();
                            bTemp=false;
                        }  if(bTmx){
                            sTmx[data]=xpp.getText();

                            bTmx=false;
                        }  if(bTmn){				//날씨
                            sTmn[data]=xpp.getText();

                            bTmn=false;
                        }  if(bWfKor){				//날씨
                            sWfKor[data]=xpp.getText();
                            bWfKor=false;
                        }  if(bPop){				//날씨
                            sPop[data]=xpp.getText();
                            bPop=false;
                        }  if(bReh){				//습도
                            sReh[data]=xpp.getText();
                            bReh=false;
                        }
                            break;

                        case XmlPullParser.END_TAG:		//'>' 엔드태그를 만나면 (이부분이 중요)
                            Log.d("checkList", "END_TAG, run");
                            if(xpp.getName().equals("item")){	//태그가 끝나느 시점의 태그이름이 item이면(이건 거의 문서의 끝
                                Log.d("checkList", "END_TAG, call view_text()");
                                tItem=true;						//따라서 이때 모든 정보를 화면에 뿌려주면 된다.
                                view_text();					//뿌려주는 곳~
                            } if(xpp.getName().equals("data")){	//data태그는 예보시각기준 예보정보가 하나씩이다.
                            data++;							//즉 data태그 == 예보 개수 그러므로 이때 array를 증가해주자
                        }
                            break;
                    }
                    eventType=xpp.next();	//이건 다음 이벤트로~
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
                    if(tItem){		//문서를 다 읽었다
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
