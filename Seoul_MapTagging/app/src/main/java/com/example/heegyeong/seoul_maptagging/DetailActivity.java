package com.example.heegyeong.seoul_maptagging;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.HashMap;

/**
 * Created by Heegyeong on 2017-09-29.
 */
public class DetailActivity extends AppCompatActivity implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener,MapView.POIItemEventListener{

    String Conts = null;

    String[] name = new String[10];
    String[] value = new String[10];
    //////////////////////

    private HashMap<Integer, ClipData.Item> mTagItemMap = new HashMap<Integer, ClipData.Item>();


    // 이부분 추가
    final Firebase mRoofRef = new Firebase("https://fir-base-2722d.firebaseio.com/detail/messages");
   // final Firebase mRoofRef = new Firebase("https://projectdatebase.firebaseio.com/product/detail");

    ListView mListview;
    //

    String userID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //// Daum Map Api 를 사용하여 Map을 설정.
        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("20f431566f4df1a182333ecc61ea87ce");
        mapView.setMapViewEventListener(this);

        LinearLayout container = (LinearLayout) findViewById(R.id.map_view);
        container.addView(mapView);

        /////
        final EditText opinion = (EditText)findViewById(R.id.product_opinion);
        /////

        /////////////////////////////////////////////////////
        /// User ID를 사용하여 댓글을 남기기 위함. SharedPreference를 사용.
        SharedPrefereneceUtil sharedPrefereneceUtil = new SharedPrefereneceUtil(this);
        userID = sharedPrefereneceUtil.getSharedPreference("userID",null);
        ///////////////////////////////////////////


        ///
        Firebase.setAndroidContext(this);
        mListview = (ListView)findViewById(R.id.listview);
        Intent intent2 = getIntent();
        final String SaveGuName = intent2.getStringExtra("GUNAME");
        final String SaveCovName = intent2.getStringExtra("covName");
        final Firebase massagesRef = mRoofRef.child(SaveGuName).child(SaveCovName);
        ///


        TextView Conts_name = (TextView) findViewById(R.id.conts_name);//addr_new
        TextView addr_new = (TextView) findViewById(R.id.addr_new);
        TextView number = (TextView) findViewById(R.id.number);
        TextView comment = (TextView) findViewById(R.id.Comment);

        TextView[] Value = new TextView[10];
        TextView[] Name = new TextView[10];


        Value[0] = (TextView) findViewById(R.id.value_01);
        Value[1] = (TextView) findViewById(R.id.value_02);
        Value[2] = (TextView) findViewById(R.id.value_03);
        Value[3] = (TextView) findViewById(R.id.value_04);
        Value[4] = (TextView) findViewById(R.id.value_05);
        Value[5] = (TextView) findViewById(R.id.value_06);
        Value[6] = (TextView) findViewById(R.id.value_07);
        Value[7] = (TextView) findViewById(R.id.value_08);
        Value[8] = (TextView) findViewById(R.id.value_09);
        Value[9] = (TextView) findViewById(R.id.value_10);

        Name[0] = (TextView) findViewById(R.id.name_01);
        Name[1] = (TextView) findViewById(R.id.name_02);
        Name[2] = (TextView) findViewById(R.id.name_03);
        Name[3] = (TextView) findViewById(R.id.name_04);
        Name[4] = (TextView) findViewById(R.id.name_05);
        Name[5] = (TextView) findViewById(R.id.name_06);
        Name[6] = (TextView) findViewById(R.id.name_07);
        Name[7] = (TextView) findViewById(R.id.name_08);
        Name[8] = (TextView) findViewById(R.id.name_09);
        Name[9] = (TextView) findViewById(R.id.name_10);

        Intent intent = getIntent();
        Conts = intent.getStringExtra("CONTS");
        String addNew = intent.getStringExtra("NEW");
        String COORDX = intent.getStringExtra("COORDX");
        String COORDY = intent.getStringExtra("COORDY");
        final String NUMBER = intent.getStringExtra("NUMBER");
        int num = intent.getIntExtra("num",0);


        Conts_name.setText(Conts);
        addr_new.setText(addNew);
        number.setText(NUMBER);
        if(NUMBER == null){
            comment.setText("등록된 전화번호가 없습니다.");
        }

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(Conts);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Float.parseFloat(COORDY), Float.parseFloat(COORDX)));

        if(num==1) {
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
            marker.setCustomImageResourceId(R.drawable.custom_toilet_none);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomSelectedImageResourceId(R.drawable.custom_toilet_selec);

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

        //marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        //marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.


        mapView.addPOIItem(marker);
        ClipData.Item item = mTagItemMap.get(marker.getTag());
        mTagItemMap.put(marker.getTag(), item);

        for(int i =1;i <11;i++){
            value[i-1] = intent.getStringExtra("VALUE"+ i);
            name[i-1] = intent.getStringExtra("NAME"+ i);
            if(value[i-1] == null || value[i-1].length() == 1){
                Value[i-1].setEnabled(false);
                Value[i-1].setHeight(1);
                Value[i-1].setTextSize(1);
                Name[i-1].setEnabled(false);
                Name[i-1].setHeight(1);
                Name[i-1].setTextSize(1);
            }else{
                Value[i-1].setText(value[i-1]);
                Name[i-1].setText(name[i - 1]+ " : ");
            }
        }

        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView 클릭될 시 할 코드작성
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + NUMBER));
                startActivity(callIntent);

            }
        });

        ////////////////////////////////////////////////////////////////////////////////////

        final FirebaseListAdapter<String> adapter =
                new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1, massagesRef.child(Conts)) {
                    @Override
                    protected void populateView(View view, String s, int i) {
                        TextView textview = (TextView)view.findViewById(android.R.id.text1);
                        textview.setText(s);

                    }
                };
        mListview.setAdapter(adapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //       Toast.makeText(getApplicationContext(), mListview.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                String chck = mListview.getItemAtPosition(position).toString();
                // 입력된 번호와 자신의 번호가 같으면 댓글을 삭제한다.
                if (chck.contains(userID)) {
                    adapter.getRef(position).removeValue();
                }
            }
        });

        Button addButton = (Button)findViewById(R.id.btn_input_opinion) ;
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(userID == null){
                    Toast.makeText(getApplicationContext(), "게스트는 댓글기능이 제한됩니다.", Toast.LENGTH_SHORT).show();
                }else{
                    if( opinion.getText().toString().length() >0){
                        massagesRef.child(Conts).push().setValue(userID + " : " + opinion.getText().toString());
                        opinion.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "댓글을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Action send를 사용해서 댓글 남기기 기능 추가.
        opinion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if(userID == null) {
                        Toast.makeText(getApplicationContext(), "게스트는 댓글기능이 제한됩니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        if(opinion.getText().toString().length() >0){
                            //////////////////////////////////////////////////////////////////////////////////////////
                            massagesRef.child(Conts).push().setValue(userID + " : " + opinion.getText().toString());
                            opinion.setText("");
                        } else {
                            Toast.makeText(getApplicationContext(),"댓글을 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return handled;
            }

        });


    }


    @Override
    public void onPause(){
        super.onPause();
        LinearLayout container = (LinearLayout) findViewById(R.id.map_view);
        container.removeAllViews();
        finish();
    }

    @Override //뒤로가기 이벤트
    public  boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            String GuName = intent.getStringExtra("GUNAME");
            int num = intent.getIntExtra("num",1);
            Log.d("LOCAL","=="+GuName+"===");
            Intent intent1 = new Intent(DetailActivity.this,MapTagging.class);
            intent1.putExtra("local",GuName);
            intent1.putExtra("num",num);
            startActivity(intent1);
            return true;
        }
        return false;
    }
    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}

