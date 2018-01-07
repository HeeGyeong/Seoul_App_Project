package com.example.heegyeong.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends RuntimePermission {

    String delete = "delete";

    private static final int FIRST_VIEW_SELECT = 1;

    String[] setLockApp = new String[30];
    String[] saveRoute = new String[30];

    String[] setName = new String[30];
    String[] saveLockName = new String[30];

    String[] tempName = new String[30];
    String[] tempPath = new String[30];
    int[] tempIndex = new int[30];
    boolean[] reLock = new boolean[30];

    boolean toggleCheck;

    ToggleButton ScreenLockcheck;

    ImageView imgIcon;
    
    int LockCount = 0;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyData> myDataset;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private static final int REQUEST_PERMISSION = 10;   // 런타임 퍼미션

    String phoneCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        /// 폰 번호 가져오는데 사용
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        final String phoneNum = telephonyManager.getLine1Number();
        phoneCount = SharedPreferenceUtil.getData(this, "phoneCount");

        if(phoneNum != null)
            SharedPreferenceUtil.putData(this, "phoneNum", phoneNum);
        else
            Toast.makeText(this,"Can not load phone number..",Toast.LENGTH_SHORT).show();

        if(phoneCount == null){
            phoneCount = "0";
            SharedPreferenceUtil.putData(this, "phoneCount", phoneCount);
        }
*/
        requestAppPermissions(new String[]{
                        android.Manifest.permission.READ_SMS,
                        android.Manifest.permission.SEND_SMS,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CALL_PHONE,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.READ_PHONE_STATE},
                R.string.msg, REQUEST_PERMISSION);

        Log.d("CheckLog","start activity");
        // 접근성 권한이 없으면 접근성 권한 설정하는 다이얼로그 띄워주는 부분
        if (!checkAccessibilityPermissions()) {
            Log.d("CheckLog", "check permission");
            setAccessibilityPermissions();
        }


        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#db7093"));
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        imgIcon = (ImageView)findViewById(R.id.mainIcon);
        ScreenLockcheck = (ToggleButton)findViewById(R.id.setScreenLock);

        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        for(int i =1 ; i<30 ; i++){
            saveRoute[i] = prefs.getString("setLockApp"+i, null);// 값이 없을시 null을 넣음
            saveLockName[i] = prefs.getString("setLockAppName"+i, null);
        }

        //toggleCheck = prefs.getBoolean("check", false);
        toggleCheck = SharedPreferenceUtil.getDataBoolean(this, "check");
        ScreenLockcheck.setChecked(toggleCheck);
        if(toggleCheck == false){
            ScreenLockcheck.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_round_on));
        }else if(toggleCheck == true){
            ScreenLockcheck.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_round));
        }

        for(int i = 1 ; i<30 ; i++){
            tempIndex[i] = 0;
            tempPath[i] = null;
            tempName[i] = null;
            reLock[i] = false;
        }

        Intent intent2 = getIntent();
        for(int i = 0 ; i<30 ; i++){       // AppLockSetting 에서 넘어온 것인지 확인하기 위해 index 0을 포함한다.
            setLockApp[i] = intent2.getStringExtra("setLockApp" + i);   // 넘어온 값이 없으면 null을 return  한다.N
            setName[i] = intent2.getStringExtra("setLockAppName"+i);
        }

        if(setLockApp[0] != null){  // 넘어온 값이 있으면.
            for(int i =1 ; i<30 ; i++){
                // 넘어온 값으로 새롭게 덮어 쓴 후에
                saveRoute[i] = setLockApp[i];
                saveLockName[i] = setName[i];
                Intent intent = new Intent(this, AccessibilityService.class);
                for(int j=1; j<30; j++) { // 그 값을 던진다.
                    SharedPreferenceUtil.putData(this, "path" + j, saveRoute[j]);
                }
                startService(intent);
            }
        } else{ // 넘어온 값이 없으면
            Intent intent = new Intent(this, AccessibilityService.class);
            for(int i=1; i<30; i++) { // 저장되어있던 값을 던진다.
                SharedPreferenceUtil.putData(this, "path" + i, saveRoute[i]);
            }
            startService(intent);
        }

        prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for(int i =1  ; i<30 ; i++){
            editor.putString("setLockApp" + i, saveRoute[i]);    // 최신 값을 다시 저장한다.
            editor.putString("setLockAppName" + i, saveLockName[i]);
        }
        editor.commit();

        ////////////////////////////////////////////
        // RecyclerView 생성
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();
        mAdapter = new RecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        // Item 생성
        for(int i = 0 ; i <30 ; i++){
            if(saveRoute[i] != null){
                if(!(saveRoute[i].equals("delete"))){
                    // adapter.addItem(saveRoute[i],saveLockName[i]);
                        myDataset.add(new MyData(saveLockName[i],saveRoute[i], R.drawable.ic_security_24dp));

                    LockCount++;
                }
            }
        }

        if(LockCount == 0){
            // App Lock List가 하나도 없을때 나타나는 이미지. 구해서 바꿀 것.
            mRecyclerView.setBackgroundResource(R.drawable.ic_close_24dp);
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // get item
                MyData item = myDataset.get(position);
                String titleStr = item.getTitle().trim();
                String decsStr = item.getPack().trim();
                int pos = position;
           //     Toast.makeText(MainActivity.this, decsStr + "을 선택. posiotion : "+ pos + " title : " + titleStr, Toast.LENGTH_SHORT).show();

                for (int i = pos+1; i < 30; i++) {
                    if(saveRoute[i] != null && tempIndex[i] == 0){
                        //Toast.makeText(MainActivity.this, decsStr + "으아악", Toast.LENGTH_SHORT).show();
                        if(saveRoute[i].equals(decsStr)){
                            Toast.makeText(MainActivity.this, titleStr + "의 잠금을 해제합니다.", Toast.LENGTH_SHORT).show();

                            // 재 잠금을 위하여 temp에 저장해둠.
                            tempIndex[i] = i;
                            reLock[i] = true;
                            tempPath[i] = saveRoute[i];
                            tempName[i] = saveLockName[i];

                            saveRoute[i] = null;
                            setLockApp[i] = null;
                            saveLockName[i] = null;
                            setName[i] = null;

                            item.setIcon(R.drawable.ic_close_24dp);

                            break;
                        }
                    } else if(saveRoute[i] == null && tempIndex[i] != 0){
                        if(reLock[i] == true){
                            Toast.makeText(MainActivity.this, tempName[i] + "의 잠금을 설정합니다.", Toast.LENGTH_SHORT).show();

                            saveRoute[i] = tempPath[i];
                            setLockApp[i] = tempPath[i];
                            saveLockName[i] = tempName[i];
                            setName[i] = tempName[i];

                            tempIndex[i] = 0;
                            reLock[i] = false;
                            tempPath[i] = null;
                            tempName[i] = null;

                            item.setIcon(R.drawable.ic_security_24dp);

                            break;
                        }
                    }
                    // Toast.makeText(MainActivity.this, saveRoute[i] + "의 잠금을 해제합니다.", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(MainActivity.this, titleStr + "의 잠금을 해제합니다.", Toast.LENGTH_SHORT).show();
                }

                SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                for(int i =1  ; i<30 ; i++){
                    editor.putString("setLockApp" + i, saveRoute[i]);    // 최신 값을 다시 저장한다.
                    editor.putString("setLockAppName"+i, saveLockName[i]);
                }
                editor.commit();

                prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                for(int i =1 ; i<30 ; i++){
                    saveRoute[i] = prefs.getString("setLockApp"+i, null);// 값이 없을시 null을 넣음
                    saveLockName[i] = prefs.getString("setLockAppName"+i, null);
                }

                Intent intent = new Intent(MainActivity.this, AccessibilityService.class);
                for(int i=1; i<30; i++) // 저장되어있던 값을 던진다.
                    SharedPreferenceUtil.putData(MainActivity.this, "path" + i , saveRoute[i]);
                startService(intent);

                mAdapter.notifyDataSetChanged();
            }

        }));

        Button passSettingBtn = (Button)findViewById(R.id.passSetBtn);
        passSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 비밀번호 설정 버튼을 누르면 비밀번호를 설정하는 Acitivity로 이동한다.
                // 해당 Acitivty 에서 설정 > 똑같은 Acitivty에서 intent값을 사용하여 재 확인
                // > 비밀번호 입력하는 Acitivty로 intent를 던져서 저장한 후에, 그 값과 비교. 맞으면 finish. 틀리면 Toast.
                // 일정 횟수 이상 틀리면 사진 찍기 + 어플 종료.
                showDialog(FIRST_VIEW_SELECT);
            }
        });


        Button setLockApp = (Button)findViewById(R.id.setLockApp);
        setLockApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AppLockSetting.class);
                for (int i = 1; i<30 ; i++){
                    intent.putExtra("setLockApp"+i, saveRoute[i]);
                    intent.putExtra("setLockAppName"+i, saveLockName[i]);
                }
                startActivity(intent);
            }
        });

        //locationCheck
        Button otherSetting = (Button)findViewById(R.id.otherSetting);
        otherSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, OtherSetting.class);
                startActivity(intent);
            }
        });

    }



    public void ScreenLockClick(View v){
        // 토글 on, off 값 어플에 저장해야 한다.
        boolean on = ((ToggleButton) v).isChecked();

        if(on){
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_round));
            Intent intent = new Intent(this, ScreenLockService.class);
            Toast.makeText(getApplicationContext(), "Screen Lock On", Toast.LENGTH_SHORT).show();
            startService(intent);
        }else {
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_button_round_on));
            Intent intent = new Intent(this, ScreenLockService.class);
            Toast.makeText(getApplicationContext(), "Screen Lock Off", Toast.LENGTH_SHORT).show();
            stopService(intent);
        }
    }

    // 접근성 권한이 있는지 없는지 확인하는 부분
    // 있으면 true, 없으면 false
    public boolean checkAccessibilityPermissions() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);

        // getEnabledAccessibilityServiceList는 현재 접근성 권한을 가진 리스트를 가져오게 된다
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.DEFAULT);

        for (int i = 0; i < list.size(); i++) {
            AccessibilityServiceInfo info = list.get(i);

            // 접근성 권한을 가진 앱의 패키지 네임과 패키지 네임이 같으면 현재앱이 접근성 권한을 가지고 있다고 판단함
            if (info.getResolveInfo().serviceInfo.packageName.equals(getApplication().getPackageName())) {
                Log.d("CheckLog","permission on");
                return true;
            }
        }
        Log.d("CheckLog", "permission off");
        return false;
    }

    // 접근성 설정화면으로 넘겨주는 부분
    public void setAccessibilityPermissions() {
        AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
        Log.d("CheckLog","entered permission dialog");
        gsDialog.setTitle("접근성 권한 설정");
        gsDialog.setMessage("접근성 권한을 필요로 합니다");
        gsDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 설정화면으로 보내는 부분
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                // 동일한 역할
                //Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                //startActivityForResult(intent, 0);
                return;
            }
        }).create().show();
    }



    @Override
    public void onPermissionsGranted(int requsetCode) {
        // 권한 설정 전부다 완료 되면 실행시에 뜨는 부분
        // Toast.makeText(getApplicationContext(), "Permission granted",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final CharSequence[] conven = {"New Password Setting", "Delete Password"};
        final CharSequence[] name = {"New Password Setting", "Delete Password"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password Setting...");
        builder.setItems(conven, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (conven[item] == name[0]) {
                    Toast.makeText(getApplicationContext(), "비밀 번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), InputPass.class);
                    intent.putExtra("newSetting", true);
                    startActivity(intent);
                } else if (conven[item] == name[1]) {
                    Intent intent = new Intent(getApplicationContext(), InputPass.class);
                    intent.putExtra("delete", true);
                    startActivity(intent);
                }
            }
        });
        AlertDialog alert = builder.create();
        return alert;
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.heegyeong.accessibilityservice/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        //SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
       // SharedPreferences.Editor editor = prefs.edit();

        ToggleButton ScreenLockCheck = (ToggleButton)findViewById(R.id.setScreenLock);
        SharedPreferenceUtil.putDataBoolean(this, "check", ScreenLockCheck.isChecked());
        //editor.putBoolean("check", ScreenLockCheck.isChecked());
        //editor.commit();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.heegyeong.accessibilityservice/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();

    }
}

