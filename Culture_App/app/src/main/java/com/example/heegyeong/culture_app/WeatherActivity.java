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

    Spinner spinner;	//스피너
    Button getBtn;		//날씨 가져오는 버튼
    TextView text;		//날씨 뿌려주는 텍스트창
    String sCategory;	//동네
    String sTm;			//발표시각
    String [] sHour;	//예보시간(총 15개정도 받아옴 3일*5번)
    String [] sDay;		//날짜(몇번째날??)
    String [] sTemp;	//현재온도
    String [] sTmx;
    String [] sTmn;
    String [] sPop;
    String [] sReh;		//습도
    String [] sWfKor;	//날씨

    int data=0;	//이건 파싱해서 array로 넣을때 번지

    boolean bCategory;	//여긴 저장을 위한 플래그들
    boolean bTm;
    boolean bHour;
    boolean bDay;
    boolean bTemp;
    boolean bReh;
    boolean bWfKor;
    boolean bTmx;
    boolean bTmn;
    boolean bPop;

    boolean tCategory;	//이건 text로 뿌리기위한 플래그
    boolean tTm;
    boolean tItem;

    Handler handler;	//핸들러


    String dongcode[]={"1168066000","1174051500","1130553500","1150060300","1162058500","1121581000","1153059500","1154551000"
            ,"1135059500","1132052100","1123060000","1159051000","1144056500","1141069000","1165066000","1120059000"
            ,"1129066000","1171063100","1147051000","1156055000","1117053000","1138055100","1111060000","1114059000","1126065500"};
    //동네 이름
    String donglist[]={"강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구","도봉구","동대문구"
            ,"동작구","마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로구","중구","중랑구"};
    String dong;	//최종적으로 가져다 붙일 동네코드가 저장되는 변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#bebebe"));
        }

        airTypeA = (AirQualityButtonTypeA) findViewById(R.id.ins_mini);

        airTypeA.setOpenAPIKey(openApiKey);

        airTypeA.setButtonImage(R.drawable.enter);


        handler=new Handler();	//스레드&핸들러처리

        bCategory=bTm=bHour=bTemp=bReh=bDay=bWfKor=bTmx=bTmn=bPop=tCategory=tTm=tItem=false;	//부울상수는 false로 초기화해주자

        sHour=new String[20];	//예보시간(사실 15개밖에 안들어오지만 넉넉하게 20개로 잡아놓음)
        sDay=new String[20];	//날짜
        sTemp=new String[20];	//현재온도
        sTmx = new String[20];
        sTmn = new String[20];
        sPop = new String[20];
        sReh=new String[20];	//습도
        sWfKor=new String[20];	//날씨

        spinner = (Spinner) findViewById(R.id.spinner);		//스피너 객체생성
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {	//이부분은 스피너에 나타나는 내용

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {	//선택시
                dong=dongcode[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {	//미선택시
                dong=dongcode[0];

            }
        });
        // 어댑터 객체 생성
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, donglist);	//어댑터를 통해 스피너에 donglist 넣어줌
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	//dropdown형식

        // 어댑터 설정
        spinner.setAdapter(adapter);



        text=(TextView) findViewById(R.id.textView1);	//텍스트 객체생성
        getBtn=(Button) findViewById(R.id.getBtn);		//버튼 객체생성
        getBtn.setOnClickListener(new OnClickListener() {	//버튼을 눌러보자

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                text.setText("");	//일단 중복해서 누를경우 대비해서 내용 지워줌
                network_thread thread=new network_thread();		//스레드생성(UI 스레드사용시 system 뻗는다)
                thread.start();	//스레드 시작
            }
        });
    }


    class network_thread extends Thread{	//기상청 연결을 위한 스레드

        public void run(){

            try{

                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();	//이곳이 풀파서를 사용하게 하는곳
                factory.setNamespaceAware(true);									//이름에 공백도 인식
                XmlPullParser xpp=factory.newPullParser();							//풀파서 xpp라는 객체 생성

                String weatherUrl="http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone="+dong;	//이곳이 기상청URL
                URL url=new URL(weatherUrl);		//URL객체생성
                InputStream is=url.openStream();	//연결할 url을 inputstream에 넣어 연결을 하게된다.
                xpp.setInput(is,"UTF-8");			//이렇게 하면 연결이 된다. 포맷형식은 utf-8로

                int eventType=xpp.getEventType();	//풀파서에서 태그정보를 가져온다.

                while(eventType!=XmlPullParser.END_DOCUMENT){	//문서의 끝이 아닐때

                    switch(eventType){
                        case XmlPullParser.START_TAG:	//'<'시작태그를 만났을때

                            if(xpp.getName().equals("category")){	//태그안의 이름이 카테고리일떄 (이건 동네이름이 나온다)
                                bCategory=true;

                            } if(xpp.getName().equals("tm")){		//발표시각정보
                            bTm=true;

                        } if(xpp.getName().equals("hour")){		//예보시간
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
                            if(bCategory){				//동네이름
                                sCategory=xpp.getText();
                                bCategory=false;
                            } if(bTm){					//발표시각
                            sTm=xpp.getText();
                            bTm=false;
                        }  if(bHour){				//예보시각
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

                            if(xpp.getName().equals("item")){	//태그가 끝나느 시점의 태그이름이 item이면(이건 거의 문서의 끝
                                tItem=true;						//따라서 이때 모든 정보를 화면에 뿌려주면 된다.
                                view_text();					//뿌려주는 곳~
                            } if(xpp.getName().equals("tm")){	//이건 발표시각정보니까 1번만나오므로 바로 뿌려주자
                            tTm=true;
                            view_text();
                        } if(xpp.getName().equals("category")){	//이것도 동네정보라 바로 뿌려주면 됨
                            tCategory=true;
                            view_text();
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

                    if(tCategory){	//동네이름 들어왔다
                     //   text.setText(text.getText()+"지역:"+sCategory+"\n");
                        tCategory=false;
                    }if(tTm){		//발표시각 들어왔다
                    //    text.setText(text.getText()+"발표시각:"+sTm+"\n\n");
                        tTm=false;
                    }if(tItem){		//문서를 다 읽었다
                        Log.d("checkList", "view_text, Item start");
                        for(int i=0;i<data;i++){	//array로 되어있으니 for문으로
                            Log.d("checkList", "view_text, Item first for");
                            if(sDay[i]!=null){		//이건 null integer 에러 예방을 위해(String은 null이 가능하지만intger는 안되니깐)
                                Log.d("checkList", "view_text, Item first if (sDay)");
                                if(Integer.parseInt(sDay[i])==0){	//발표시각이 0이면 오늘
                                    Log.d("checkList", "view_text, Item 2dn if (Integer.parseInt sDay 0)");
                                    text.setText(text.getText() + "오늘 날씨" + "\n");
                                }else if(Integer.parseInt(sDay[i])==1){	//1이면 내일
                                    Log.d("checkList", "view_text, Item 2dn if (Integer.parseInt sDay 1)");
                                    text.setText(text.getText() + "내일 날씨" + "\n");
                                }else if(Integer.parseInt(sDay[i])==2){	//2이면 모레
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
                                break;  // 하나 출력하면 종료되게 설정.
                            }
                        }

                        tItem=false;
                        data=0;		//다음에 날씨를 더가져오게 되면 처음부터 저장해야겠지?

                    }

                }
            });
        }
    }



}
