package com.example.heegyeong.seoul_maptagging;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import static net.daum.mf.map.n.api.internal.NativeMapLocationManager.setCurrentLocationTrackingMode;
import static net.daum.mf.map.n.api.internal.NativeMapLocationManager.setShowCurrentLocationMarker;
/*
자전거
http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/Mgisbicycleconvenience/1/5/

문화
http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisCulter/1/5/

도서관
http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisLibrary/1/5/
    //도서관 구이름은 " " 되있음
    // 옆에 space 한번 쳐야 함.

휠체어 충전기
http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisRapidCharge/1/5/

택시
http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/Mgistaxistop/1/5/

화장실
http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisToilet/1/5/

와이파이
http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisWifi/1/5/

 */

public class MapTagging extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MapView.POIItemEventListener,
        MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, LocationListener {

    private HashMap<Integer, ClipData.Item> mTagItemMap = new HashMap<Integer, ClipData.Item>();

    private LocationManager locManager;// 현재위치///
    private Geocoder geoCoder;
    private Location myLocation = null;
    private double latPoint, lngPoint;
///////////

    Data app;
    DataDetail app2;
    int[] DataIndex = new int[200];
    int index = 0;
    int length = 0;

    Vector<Data> Data = new Vector<>();
    Vector<DataDetail> DataDetail = new Vector<>();
    Vector<DataAll> DataAll = new Vector<>();

    Intent intent;
    String DataURL[]={""};
    String[] url1={
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisToilet/1/1000/",
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisToilet/1001/2000/",
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisToilet/2001/3000/",
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisToilet/3001/4000/",
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisToilet/4001/4702/"
    };
    String[] url2={
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisWifi/1/1000/",
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisWifi/1001/2000/",
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisWifi/2001/2927/"
    };
    String[] url3={
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisLibrary/1/1000/",
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisLibrary/1001/1422/"
    };
    String[] url4 = { //문화시설 대체
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisMountainPark/1/645/"
    };
    String[] url5={
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/Mgisbicycleconvenience/1/764/"
    };
    String[] url6={
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/Mgistaxistop/1/431/"
    };
    String[] url7={
            "http://openapi.seoul.go.kr:8088/724775756d66687336326e65415661/xml/MgisRapidCharge/1/208/"
    };
    String local="11";

    int num = 0;
    ADD Sum = new ADD(); //객체 2개를 더하는 클래스 호출
    String cov = "";
    String covName="";

    ////////////
    private GpsInfo gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tagging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /////
        // LocationListener 핸들
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // GPS로 부터 위치 정보를 업데이트 요청
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        // 기지국으로부터 위치 정보를 업데이트 요청
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        // 주소를 가져오기 위해 설정 - KOREA, KOREAN 모두 가능
        geoCoder = new Geocoder(this, Locale.KOREA);
//////////////////////////////////



        //// NavigationView 설정
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //////

        //// Daum Map Api 를 사용하여 Map을 설정.
        final MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("20f431566f4df1a182333ecc61ea87ce");

        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.map_view);
        container.addView(mapView);
        //////////

        final Vector<MapPOIItem> MarkerVector = new Vector<>(); //마커를 저장할 벡터 생성

        intent = getIntent();

        // intent.getIntExtra("num",1)의 범위는 1~7, 배열 index는 0 ~ 6
        if( (intent.getIntExtra("num",1)) == 1) { DataURL = url1; covName = "공중 화장실"; }
        else if( (intent.getIntExtra("num",2)) == 2) { DataURL = url2; covName = "공공 와이파이"; }
        else if( (intent.getIntExtra("num",3)) == 3) { DataURL = url3; covName = "도서관 및 서점"; }
        else if( (intent.getIntExtra("num",4)) == 4) { DataURL = url4; covName = "산과 공원"; }
        else if( (intent.getIntExtra("num",5)) == 5) { DataURL = url5; covName = "공공자전거 대여시설"; }
        else if( (intent.getIntExtra("num",6)) == 6) { DataURL = url6; covName = "택시승차대"; }
        else if( (intent.getIntExtra("num",7)) == 7) { DataURL = url7; covName = "전기휠체어 충전기"; }

        num = intent.getIntExtra("num",1); // url 번호 저장해놓는곳

        local = intent.getStringExtra("local");

        Log.d("000000","======== "+local);

        for (int i = 0; i <DataURL.length; i++) {
            try {
                URL url = new URL(DataURL[i]);
                InputStream in = url.openStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                Log.i("===parser==", "ok");
                parser.setInput(in, "utf-8");

                int eventType = parser.getEventType();
                String START = "";
                String TEXT = "";
                String END = "";

                String COT_ADDR_FULL_NEW = "", COT_ADDR_FULL_OLD = "", COT_COORD_X = "",
                        COT_COORD_Y = "", COT_GU_NAME = "", COT_CONTS_NAME = "", COT_TEL_NO ="";

                boolean isTag = false;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {                                   //태그값, 데이터값 읽는 부분
                        case XmlPullParser.START_TAG:
                            START = parser.getName();
                            break;
                        case XmlPullParser.TEXT:
                            TEXT = parser.getText();
                            if (START.equals("COT_ADDR_FULL_NEW")) {
                                if (parser.getText().length() > 1)
                                    COT_ADDR_FULL_NEW = parser.getText();
                            }
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


                            if (COT_GU_NAME.equals(local)) { // 강북구는 3개밖에 없음. 그 이상을 보려면 강남구로 테스트
                                // 도서관 API는 구 옆에 space 한번 쳐야 함.
                                Log.i("GuNameXY", "xy : " + COT_COORD_X + " , " + COT_COORD_Y);

                                Log.i("GuNameXY", "Guxy : " + COT_COORD_X + " , " + COT_COORD_Y);

                                app = new Data();
                                app.setNewAddress(COT_ADDR_FULL_NEW);
                                app.setOldAddress(COT_ADDR_FULL_OLD);
                                app.setCoordX(COT_COORD_X);
                                app.setCoordY(COT_COORD_Y);
                                app.setGuName(COT_GU_NAME);
                                app.setContsName(COT_CONTS_NAME);
                                // index 번호를 url에 삽입하여 찾아야 하므로 index 번호를 저장함.
                                app.setDataIndex(index);

                                // index 번호를 url에 삽입하여 찾아야 하므로 index 번호를 저장함.
                                DataIndex[length++] = index;

                                Data.add(app);


                            }

                            // for문을 반복할 때 index번호는 계속 증가되어야 제대로 된 index값이 저장된다.
                            index++;

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

                if(Data.size() != 0){

                    // Map Default Position
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Float.parseFloat(Data.get(0).getCoordY()), Float.parseFloat(Data.get(0).getCoordX())), true);

                    for(int j=0;j<30;j++){
                        // 마커 생성 및 설정

                        MapPOIItem marker = new MapPOIItem();
                        marker.setItemName(Data.get(j).getContsName());
                        marker.setTag(j);
                        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Float.parseFloat(Data.get(j).getCoordY()), Float.parseFloat(Data.get(j).getCoordX())));

                        if(num==1) {
                            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                            marker.setCustomImageResourceId(R.drawable.custom_toilet_none); //마커 이미지
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage); //마커 선택시 커스텀마커
                            marker.setCustomSelectedImageResourceId(R.drawable.custom_toilet_selec); //마커 선택시 커스텀 이미지

                        }
                        else if(num==2) {
                            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                            marker.setCustomImageResourceId(R.drawable.custom_wifi_none); // 마커 이미지.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                            marker.setCustomSelectedImageResourceId(R.drawable.custom_wifi_selec);
                        }else if(num==3) {
                            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                            marker.setCustomImageResourceId(R.drawable.custom_lib_none); // 마커 이미지.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                            marker.setCustomSelectedImageResourceId(R.drawable.custom_lib_selec);
                        }else if(num==4) {
                            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                            marker.setCustomImageResourceId(R.drawable.custom_park_none); // 마커 이미지.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                            marker.setCustomSelectedImageResourceId(R.drawable.custom_park_selec);
                        }else if(num==5) {
                            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                            marker.setCustomImageResourceId(R.drawable.custom_bc_none); // 마커 이미지.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                            marker.setCustomSelectedImageResourceId(R.drawable.custom_bc_selected);
                        }else if(num==6) {
                            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                            marker.setCustomImageResourceId(R.drawable.custom_taxi_none); // 마커 이미지.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                            marker.setCustomSelectedImageResourceId(R.drawable.custom_taxi_selec);
                        }else if(num==7) {
                            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                            marker.setCustomImageResourceId(R.drawable.custom_wheel_none); // 마커 이미지.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                            marker.setCustomSelectedImageResourceId(R.drawable.custom_wheel_selec);
                        }

                        mapView.addPOIItem(marker);
                        ClipData.Item item = mTagItemMap.get(marker.getTag());
                        mTagItemMap.put(marker.getTag(), item);

                    }

                }else{
                    Toast.makeText(getApplicationContext(), local + " 에는 " + covName + "(이)가 없습니다다.", Toast.LENGTH_SHORT).show();
                }




            } catch (Exception e) {//예외처리

            }
        }


        for (int i = 0; i < DataURL.length; i++) {
            try {
                URL url = new URL(DataURL[i]);
                InputStream in = url.openStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                int eventType = parser.getEventType();
                Log.i("===parser==", "ok");
                parser.setInput(in, "utf-8");

                String START = "";
                String TEXT = "";
                String END = "";

                app2 = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            START = parser.getName();
                            if (parser.getName().equals("row")) {
                                app2 = new DataDetail();
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            END = parser.getName();
                            if (parser.getName().equals("row")) {
                                DataDetail.add(app2);
                                app2 = null;
                            }
                            break;
                        case XmlPullParser.TEXT: {
                            TEXT = parser.getText();
                            switch (START) {
                                case "COT_COORD_X":
                                    if (parser.getText().length() > 1)
                                        app2.setCoordX(parser.getText());
                                    break;
                                case "COT_VALUE_01":
                                    if (parser.getText().length() > 1)
                                        app2.setValue_01(parser.getText());
                                    break;
                                case "COT_VALUE_02":
                                    if (parser.getText().length() > 1)
                                        app2.setValue_02(parser.getText());
                                    break;
                                case "COT_VALUE_03":
                                    if (parser.getText().length() > 1)
                                        app2.setValue_03(parser.getText());
                                    break;
                                case "COT_VALUE_04":
                                    if (parser.getText().length() > 1)
                                        app2.setValue_04(parser.getText());
                                    break;
                                case "COT_VALUE_05":
                                    if (parser.getText().length() > 1)
                                        app2.setValue_05(parser.getText());
                                    break;
                                case "COT_NAME_01":
                                    if (parser.getText().length() > 1)
                                        app2.setName_01(parser.getText());
                                    break;
                                case "COT_NAME_02":
                                    if (parser.getText().length() > 1)
                                        app2.setName_02(parser.getText());
                                    break;
                                case "COT_NAME_03":
                                    if (parser.getText().length() > 1)
                                        app2.setName_03(parser.getText());
                                    break;
                                case "COT_NAME_04":
                                    if (parser.getText().length() > 1)
                                        app2.setName_04(parser.getText());
                                    break;
                                case "COT_NAME_05":
                                    if (parser.getText().length() > 1)
                                        app2.setName_05(parser.getText());
                                    break;
                                case "COT_TEL_NO":
                                    if (parser.getText().length() > 1)
                                        app2.setNumber(parser.getText());
                                    break;
                            }
                        }
                        break;
                    }
                    eventType = parser.next(); // 다음 태그로 이동
                }

                Log.d("TOILET DETAIL", " == " + DataDetail.size()); // 화장실 상세정보 벡터 크기확인


            } catch (Exception e) {//예외처리
                e.printStackTrace();
                Log.e("dd", "Error in network call", e);
            }
        }

        Sum.ADD(Data, DataDetail, DataAll);

        if(num==1){//해당 마커 클릭시 넘겨받는 url에따라 해당편의시설임을 보여주는곳, 마커를 눌렀을때 나오는 토스트메시지에 추가해놓은상태
            cov = "화장실";
        }else if(num ==2){
            cov = "와이파이";
        }else if(num ==3){
            cov = "도서관 및 서점";
        }else if(num ==4){
            cov = "산과 공원";
        }else if(num ==5){
            cov = "자전거대여소";
        }else if(num ==6){
            cov = "택시승차대";
        }else if(num ==7){
            cov = "전기휠체어 충전기";
        }
        final FloatingActionButton b1 = (FloatingActionButton) findViewById(R.id.fab_gps);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GpsInfo(MapTagging.this);
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {
                    //gps가 사용 가능한 상태라면.
                    if (MarkerVector.size() == 0) {
                        //TODO Auto-generated method stub
                        getGeoLocation();
                        setShowCurrentLocationMarker(true);
                        final MapPOIItem marker1 = new MapPOIItem();
                        marker1.setItemName("현재위치");
                        try {
                            marker1.setMapPoint(MapPoint.mapPointWithGeoCoord(myLocation.getLatitude(), myLocation.getLongitude()));
                            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(myLocation.getLatitude(), myLocation.getLongitude()), true); //현재위치로 화면이동
                            marker1.setMarkerType(MapPOIItem.MarkerType.BluePin);
                            mapView.addPOIItem(marker1);
                            MarkerVector.add(marker1); //생성한 마커 벡터에 저장
                            Toast.makeText(getApplicationContext(), "현재위치입니다.", Toast.LENGTH_SHORT).show();
                        }catch (NullPointerException e){
                            Toast.makeText(getApplicationContext(), "현재위치를 찾는 중 입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // GPS 를 사용할수 없는 상태라면
                    // 해당 설정을 On 시키기 위한 메시지를 띄운다.
                    gps.showSettingsAlert();
                    Toast.makeText(getApplicationContext(), "GPS 사용 설정 후, 10초 후에 클릭해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton b2 = (FloatingActionButton) findViewById(R.id.fab_current_point);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MarkerVector.size()>=1) {
                    mapView.removePOIItem(MarkerVector.get(MarkerVector.size() - 1));
                    Log.d("markervector", " size " + MarkerVector.size());
                    MarkerVector.remove(MarkerVector.get(MarkerVector.size() - 1));
                    Log.d("markervector", " size " + MarkerVector.size());
                }
            }
        });

        // 하이브리드 지도 변환
        final int[] i = {0};
        FloatingActionButton b3 = (FloatingActionButton)findViewById(R.id.satellite);
        b3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(i[0] ==0){
                    mapView.setMapType(MapView.MapType.Hybrid);
                    i[0] = 1;
                }else{
                    mapView.setMapType(MapView.MapType.Standard);
                    i[0] =0;
                }
                Log.d("mapViewType","="+mapView.getMapType());

            }
        });
    }

    @Override
    public void onPause(){ //앱 일시정지시 맵뷰 제거
        super.onPause();
        RelativeLayout container = (RelativeLayout)findViewById(R.id.map_view1);
        container.removeAllViews();

    }


    @Override
    public void onRestart(){ //앱 재시작 , Pause됬다가 다시 시작할때
        super.onRestart();
        intent = getIntent();
        local = intent.getStringExtra("local");
        num = intent.getIntExtra("num",0);
        Intent restart = new Intent(MapTagging.this,MapTagging.class);
    //    Toast.makeText(getApplicationContext(), "restart :" + num + " , " +local , Toast.LENGTH_LONG).show(); //재시작됬을때 넘겨주는 값확인
        restart.putExtra("local",local);
        restart.putExtra("num",num);
        startActivity(restart);
    }


    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView arg0, int arg1,
                                                        String arg2) {
        // TODO Auto-generated method stub


    }


    @Override
    @Deprecated
    public void onCalloutBalloonOfPOIItemTouched(MapView arg0, MapPOIItem arg1) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView arg0, MapPOIItem arg1,
                                                 MapPOIItem.CalloutBalloonButtonType arg2) {
        // TODO Auto-generated method stub
        String a = arg1.getItemName();
        for (int i = 0; i < DataAll.size(); i++) {
            if (a.equals(DataAll.get(i).getContsName())) {
                Intent intent = new Intent(MapTagging.this, DetailActivity.class);

                intent.putExtra("CONTS", DataAll.get(i).getContsName());
                intent.putExtra("NEW", DataAll.get(i).getNewAddress());
                intent.putExtra("COORDX", DataAll.get(i).getCoordX());
                intent.putExtra("COORDY",DataAll.get(i).getCoordY());

                intent.putExtra("VALUE1",DataAll.get(i).getValue_01());
                intent.putExtra("VALUE2", DataAll.get(i).getValue_02());
                intent.putExtra("VALUE3", DataAll.get(i).getValue_03());
                intent.putExtra("VALUE4",DataAll.get(i).getValue_04());
                intent.putExtra("VALUE5",DataAll.get(i).getValue_05());
                intent.putExtra("VALUE6",DataAll.get(i).getValue_06());
                intent.putExtra("VALUE7",DataAll.get(i).getValue_07());
                intent.putExtra("VALUE8",DataAll.get(i).getValue_08());
                intent.putExtra("VALUE9",DataAll.get(i).getValue_09());
                intent.putExtra("VALUE10",DataAll.get(i).getValue_10());

                intent.putExtra("NAME1",DataAll.get(i).getName_01());
                intent.putExtra("NAME2",DataAll.get(i).getName_02());
                intent.putExtra("NAME3",DataAll.get(i).getName_03());
                intent.putExtra("NAME4",DataAll.get(i).getName_04());
                intent.putExtra("NAME5",DataAll.get(i).getName_05());
                intent.putExtra("NAME6",DataAll.get(i).getName_06());
                intent.putExtra("NAME7",DataAll.get(i).getName_07());
                intent.putExtra("NAME8",DataAll.get(i).getName_08());
                intent.putExtra("NAME9",DataAll.get(i).getName_09());
                intent.putExtra("NAME10",DataAll.get(i).getName_10());

                intent.putExtra("NUMBER",DataAll.get(i).getNumber());

                intent.putExtra("GUNAME",local); //지역구 넘기는부분
                intent.putExtra("num",num);

                intent.putExtra("covName",covName);

                startActivity(intent);

                break;
            }
        }

    }


    @Override
    public void onDraggablePOIItemMoved(MapView arg0, MapPOIItem arg1,
                                        MapPoint arg2) {
        // TODO Auto-generated method stub


    }


    @Override
    public void onPOIItemSelected(MapView arg0, MapPOIItem arg1) {
        // TODO Auto-generated method stub
        arg0.setMapCenterPoint(arg1.getMapPoint(), true);
        arg0.selectPOIItem(arg1, true);

        String a = arg1.getItemName(); // 터치한 이름.

        if(!a.equals("현재위치")) { //현재위치 마커는거름
            for (int i = 0; i < 30; i++) {
                if (a.equals(DataAll.get(i).getContsName())) {
                    Log.d("a.equals", "a : " + a + "Toilet.get(i).getContsName() : " + DataAll.get(i).getContsName());
                /*Toast.makeText(getApplicationContext(), DataAll.get(i).getNewAddress() + " , "
                        + DataAll.get(i).getOldAddress()+ " , " + DataAll.get(i).getCoordX() + " , " + DataAll.get(i).getCoordY() + " , "
                        +DataAll.get(i).getGuName() + " , " + DataAll.get(i).getContsName(), Toast.LENGTH_SHORT).show();*/
                    Toast.makeText(getApplicationContext(), "말풍선을 한번더 클릭하시면\n상세정보로 이동합니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        Log.d("TouchXY", "a : " + a);
        //Toast.makeText(getApplicationContext(), " Touch :" + a, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onMapViewCenterPointMoved(MapView arg0, MapPoint arg1) {
        // TODO Auto-generated method stub


    }


    @Override
    public void onMapViewDoubleTapped(MapView arg0, MapPoint arg1) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onMapViewDragEnded(MapView arg0, MapPoint arg1) {
        // TODO Auto-generated method stub


    }


    @Override
    public void onMapViewDragStarted(MapView arg0, MapPoint arg1) {
        // TODO Auto-generated method stub


    }


    @Override
    public void onMapViewInitialized(MapView arg0) {
        // TODO Auto-generated method stub


    }


    @Override
    public void onMapViewLongPressed(MapView arg0, MapPoint arg1) {
        // TODO Auto-generated method stub


    }


    @Override
    public void onMapViewMoveFinished(MapView arg0, MapPoint arg1) {
        // TODO Auto-generated method stub


    }


    @Override
    public void onMapViewSingleTapped(MapView arg0, MapPoint arg1) {
        // TODO Auto-generated method stub


    }


    @Override
    public void onMapViewZoomLevelChanged(MapView arg0, int arg1) {
        // TODO Auto-generated method stub


    }
/*
    public MapPoint.GeoCoordinate getMapPointGeoCoord(){

    }*/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override //뒤로가기 이벤트
    public  boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intentCoven = new Intent(MapTagging.this,ConvenActivity.class);
            intentCoven.putExtra("local",local); //구값을 편의시설로 다시넘겨줌
            startActivity(intentCoven);
            return true;
        }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.conven1) {
            // Handle the camera action
            onPause();
            intent = getIntent();
            local =intent.getStringExtra("local");
            Intent intent = new Intent(MapTagging.this,MapTagging.class);
            intent.putExtra("num",1);
            intent.putExtra("local",local);
            startActivity(intent);
        } else if (id == R.id.conven2) {
            onPause();
            intent = getIntent();
            local =intent.getStringExtra("local");
            Intent intent = new Intent(MapTagging.this,MapTagging.class);
            intent.putExtra("num",2);
            intent.putExtra("local",local);
            startActivity(intent);

        } else if (id == R.id.conven3) {
            onPause();
            intent = getIntent();
            local =intent.getStringExtra("local");
            Intent intent = new Intent(MapTagging.this,MapTagging.class);
            intent.putExtra("num",3);
            intent.putExtra("local",local);
            startActivity(intent);

        } else if (id == R.id.conven4) {
            onPause();
            intent = getIntent();
            local =intent.getStringExtra("local");
            Intent intent = new Intent(MapTagging.this,MapTagging.class);
            intent.putExtra("num",4);
            intent.putExtra("local",local);
            startActivity(intent);

        } else if (id == R.id.conven5) {
            onPause();
            intent = getIntent();
            local =intent.getStringExtra("local");
            Intent intent = new Intent(MapTagging.this,MapTagging.class);
            intent.putExtra("num",5);
            intent.putExtra("local",local);
            startActivity(intent);
        } else if (id == R.id.conven6) {
            onPause();
            intent = getIntent();
            local =intent.getStringExtra("local");
            Intent intent = new Intent(MapTagging.this,MapTagging.class);
            intent.putExtra("num",6);
            intent.putExtra("local",local);
            startActivity(intent);

        }  else if (id == R.id.conven7) {
            onPause();
            intent = getIntent();
            local =intent.getStringExtra("local");
            Intent intent = new Intent(MapTagging.this,MapTagging.class);
            intent.putExtra("num",7);
            intent.putExtra("local",local);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Uri uri = Uri.parse("mailto:kiozxcbnm@gmail.com"); //우리 이메일로 문의사항받기
            String[] ccs = {"secondEmail@gmail.com"}; //참조
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra(Intent.EXTRA_TEXT, "문의하실 내용을 입력하세요.");
            it.putExtra(Intent.EXTRA_CC, ccs);
            startActivity(it);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getGeoLocation() {
        StringBuffer mAddress = new StringBuffer();
        if (myLocation != null) {
            latPoint = myLocation.getLatitude();
            lngPoint = myLocation.getLongitude();
            try {
                // 위도,경도를 이용하여 현재 위치의 주소를 가져온다.
                List<Address> addresses;
                addresses = geoCoder.getFromLocation(latPoint, lngPoint, 1);
                for (Address addr : addresses) {
                    int index = addr.getMaxAddressLineIndex();
                    for (int i = 0; i <= index; i++) {
                        mAddress.append(addr.getAddressLine(i));
                        mAddress.append(" ");
                    }
                    mAddress.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("Address : "," " + mAddress);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        myLocation = location;
        getGeoLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
