package com.example.heegyeong.accessibilityservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

/**
 * Created by Heegyeong on 2017-02-15.
 */
public class InputPass extends AppCompatActivity {

    String pass = null;
    EditText passText;
    String pwdCheck = null;
    String savePwd = null;
    int Count = 0;

    String[] number = new String[4];
    String[] saveNumber = new String[4];
    boolean numberSetting = false;

    String[] index = new String[2];
    String[] saveIndex = new String[2];
    boolean gmailSetting = false;

    // GPSTracker class
    private GpsInfo gps;

    ////
    double latitude;
    double longitude;
    String locationName;

    String saveSmsText;
    String smsText;

    boolean newSetting = false;
    boolean delete = false;

    boolean screen = false;
    ImageView lockImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_pass);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        Log.d("test Log", "Screen Lock Start");
        // 기존 잠금화면 보다 위에 띄우고 | 기본 잠금 화면을 지워라

        passText = (EditText) findViewById(R.id.password);
        lockImage = (ImageView) findViewById(R.id.lockImage);


        Intent intent = getIntent();
        pwdCheck = intent.getStringExtra("pwd");
        ///////////////////////////////////////////////////////////
        newSetting = intent.getBooleanExtra("newSetting", false);
        delete = intent.getBooleanExtra("delete", false);
        //////////////////////////////////////////////////////////
        screen = intent.getBooleanExtra("screenLock", false);
        //////////////////////////////////////////////////////////
        // 잠금 종류에 따른 아이콘 변경.
        if(screen == true){
            lockImage.setImageResource(R.drawable.screen_lock2);
        }else{
            lockImage.setImageResource(R.drawable.app_lock1);
        }
        //////////////////////////////////////////////////////////

        numberSetting = intent.getBooleanExtra("setting", false);
        Log.d("NumberLog", "numberSetting" + numberSetting);
        for(int i =0; i<4; i++){
            number[i] = intent.getStringExtra("saveNumber"+i);
            Log.d("NumberLog", "saving number check" + number[i]);
        }

        gmailSetting = intent.getBooleanExtra("setting2", false);
        Log.d("MailLog", "gmail" + numberSetting);
        for(int i =0; i<2; i++){
            index[i] = intent.getStringExtra("saveIndex"+i);
            Log.d("MailLog", "saving gmail check" + index[i]);
        }




        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        savePwd = prefs.getString("password", null);// 값이 없을시 null을 넣음

        for(int i =0; i<4; i++){
            saveNumber[i] = prefs.getString("saveNumber"+i, null);// 값이 없을시 null을 넣음
            Log.d("NumberLog", "saved number check" + saveNumber[i]);
        }

        for(int i =0; i<2; i++){
            saveIndex[i] = prefs.getString("saveIndex"+i, null);// 값이 없을시 null을 넣음
            Log.d("MailLog", "saved gmail check" + saveIndex[i]);
        }

        // 넘겨진 값이 있으면, passSetting > passChecking > InputPass로 온 것.
        if(pwdCheck == null)        // 넘겨진 값이 없으면.
            pwdCheck = savePwd;

        if(numberSetting == false){ // otherSetting으로 부터 넘겨온 intent가 아니면.
            for(int i =0; i<4; i++)
                number[i] = saveNumber[i];
        } else if(numberSetting == true){   // otherSetting으로 넘겨온 intent 이면.
            prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            for(int i =0; i<4; i++){
                editor.putString("saveNumber"+i, number[i]);
                Log.d("NumberLog", "new saving numberSave" + number[i]);
                Log.d("NumberLog", "new saving saveNumber" + saveNumber[i]);
            }
            editor.commit();
        }

        if(gmailSetting == false){ // GMailLogin으로 부터 넘겨온 intent가 아니면.
            for(int i =0; i<2; i++)
                index[i] = saveIndex[i];
        } else if(gmailSetting == true){   // GMailLogin으로 부터 넘겨온 intent 이면.
            prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            for(int i =0; i<2; i++){
                editor.putString("saveIndex"+i, index[i]);
                Log.d("MailLog", "new saving MailSave" + index[i]);
                Log.d("MailLog", "new saving saveMail" + saveIndex[i]);
            }
            editor.commit();
        }

        // delete가 들어올 경우, 값을 삭제해야 한다. 따라서, 값을 저장해선 안된다.
        // 값 저장
        if(pwdCheck!=null){
            if(delete == false) {
                prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("password", pwdCheck);
                editor.commit();
            }
        }

        if(pwdCheck == null && savePwd == null && delete == false){
            Toast.makeText(this, "비밀번호를 설정해 주세요.", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(InputPass.this, PassSetting.class);
            startActivity(intent2);
            finish();
        }else if(pwdCheck == null && savePwd == null && delete == true){
            Toast.makeText(this, "비밀번호가 설정되어있지 않습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }else if(delete == true){
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }

        Button enterBtn = (Button)findViewById(R.id.butin);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = passText.getText().toString();
                if(pwd.equals(pwdCheck)){
                    // Toast.makeText(getApplicationContext(), "로 그 인 ! ", Toast.LENGTH_SHORT).show();
                    if(delete == true && newSetting == false){
                       // Toast.makeText(getApplicationContext(), "비밀번호 삭제 중입니다..", Toast.LENGTH_SHORT).show();
                        savePwd = null;
                        pwdCheck = null;
                        SharedPreferences prefss = getSharedPreferences("PrefName", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefss.edit();
                        editor.putString("password", pwdCheck);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "비밀번호 삭제 완료.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else if(newSetting == true && delete == false){
                        Intent intent2 = new Intent(getApplicationContext(), PassSetting.class);
                        startActivity(intent2);
                        finish();
                    }
                    finish();
                }else {
                    if(++Count < 6)
                        Toast.makeText(getApplicationContext(), Count + "회 비밀번호를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show();
                    pass = null;
                    passText.setText("");
                    if(Count == 5){
                        gps = new GpsInfo(InputPass.this);
                        // GPS 사용유무 가져오기
                        if (gps.isGetLocation()) {

                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();
                            locationName = getAddress(InputPass.this, latitude, longitude);

                            if(latitude == 0.0 && longitude == 0.0){    // 값을 제대로 불러오지 못했을 경우, 한번더 불러온다.
                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();
                                locationName = getAddress(InputPass.this, latitude, longitude);
                            }

                            if(saveNumber[3] != null && saveNumber[3].length() > 2){  // 문자 글자 제한이 넘어가면 문자 전송을 하지 못함.
                                saveSmsText = saveNumber[3];
                            } else if(saveNumber[3].length() < 2){
                                saveSmsText = "핸드폰을 도난당했습니다. \n현재 핸드폰의 위치는 다음과 같습니다.";
                            } else{
                                saveSmsText = "핸드폰을 도난당했습니다. \n현재 핸드폰의 위치는 다음과 같습니다.";
                            }

                            smsText =  "위도: " + Double.toString(latitude) + "\n경도: " + Double.toString(longitude)
                                    + "\n주소: " + locationName;

                            if(saveNumber[0] != null){
                                if (saveNumber[0].length()>0 && smsText.length()>0){
                                    sendSMS(saveNumber[0], saveSmsText);
                                    sendSMS(saveNumber[0], smsText);
                                }
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),"번호가 저장되지 않았습니다.", Toast.LENGTH_LONG).show();
                            }

                            if(saveNumber[1] != null){
                                if (saveNumber[1].length()>0 && smsText.length()>0){
                                    sendSMS(saveNumber[0], saveSmsText);
                                    sendSMS(saveNumber[0], smsText);
                                }
                            }
                            if(saveNumber[2] != null){
                                if (saveNumber[2].length()>0 && smsText.length()>0){
                                    sendSMS(saveNumber[0], saveSmsText);
                                    sendSMS(saveNumber[0], smsText);
                                }
                            }

                        } else {
                            //gps.showSettingsAlert();
                            Toast.makeText(getApplicationContext(),"위치 제공을 승인하지 않았습니다.", Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(InputPass.this, CameraStart.class);
                        startActivity(intent);

                        /// Location 제공 승인을 하지 않으면 이 부분에서 에러 발생.
                        if(saveIndex[0] != null){   // gmail 로그인을 했을 경우.
                            if((gps.isGetLocation())){
                                sendEmail(smsText);
                            } else{
                                sendEmail("위치 정보를 제공하지 않는 상태입니다.");
                            }

                        } else{
                            Log.d("MailLog","saveIndex[] : " + saveIndex[0] + " : " + saveIndex[1]);
                            Toast.makeText(getApplicationContext(),"GMail 로그인을 하지 않았습니다.", Toast.LENGTH_LONG).show();
                        }

                        // PHP Server로 phoneNumber, LocationName Throws.
                        // phoneCount 부분은 테스트하기 위해서 사용. Picture 부분 테이블 지울 때 삭제할 것.
                        String phone = SharedPreferenceUtil.getData(getApplicationContext(), "phoneNum");
                        String phoneCount = SharedPreferenceUtil.getData(getApplicationContext(), "phoneCount");
                        int count = Integer.parseInt(phoneCount);
                        insertToDatabase(phone, locationName, "MainApp Test : "+ (++count));

                        Intent PHPIntent = new Intent(InputPass.this, PHPConnection.class);
                        startActivity(PHPIntent);

                    }
                    if(Count > 5){
                        // 홈으로 이동.
                        Toast.makeText(getApplicationContext(), "어플을 종료합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                }
            }
        });


        Button lostPwd = (Button)findViewById(R.id.lostPwd);
        lostPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"질문 답변으로 찾기", "이메일로 찾기"};
                AlertDialog.Builder builder = new AlertDialog.Builder(InputPass.this);     // 여기서 this는 Activity의 this
                builder.setTitle("비밀번호 찾기")        // 제목 설정
                        .setItems(items, new DialogInterface.OnClickListener() {    // 목록 클릭시 설정
                            public void onClick(DialogInterface dialog, int index) {
                                if(items[index].equals("질문 답변으로 찾기")){
                                    Intent intent = new Intent(InputPass.this, SearchPassQA.class);
                                    startActivity(intent);
                                    finish();
                                }else if(items[index].equals("이메일로 찾기")){
                                    Intent intent = new Intent(InputPass.this, SearchPassMail.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
            }
        });
    }

    private void sendEmail(String text) {
        //Getting content for email

        //String email = "dbgmlrud4590@gmail.com";
        // 수신자는 로그인한 본인 한정.
        String subject = "현재 핸드폰의 위치입니다.";
        String message = text;

        //Creating SendMail object
        SendMail sm = new SendMail(this, saveIndex[0], subject, message, saveIndex[0], saveIndex[1]);
        // this, gmail-id, Title, text, gmail-id, password

        //Executing sendmail to send email
        sm.execute();
    }


    public void passEnter(View v){
        switch(v.getId()){
            case R.id.but0 :
            case R.id.but1 :
            case R.id.but2 :
            case R.id.but3 :
            case R.id.but4 :
            case R.id.but5 :
            case R.id.but6 :
            case R.id.but7 :
            case R.id.but8 :
            case R.id.but9 :
                String tmp = ((Button)v).getText().toString();
                if(pass == null) {  // 입력된게 없으면
                    pass = tmp;
                    passText.setText(pass);
                }else{  // 입력된게 있으면.
                    pass = pass + tmp;
                    passText.setText(pass);
                }
                break;
            case R.id.butC :
                pass = null;
                passText.setText("");
                break;
            default :
                break;
        }
    }

    // BACK KEY 방지
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK :
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // Home, MultiTasking Key 눌렀을 때 호출됨.
    @Override
    protected void onUserLeaveHint() {
        Log.d("HomeKeyLog", "Home Button Touch");
        super.onUserLeaveHint();
    }

    public void sendSMS(String smsNumber, String smsText){
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(getApplicationContext(), "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(getApplicationContext(), "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(getApplicationContext(), "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }

    public static String getAddress(Context mContext,double lat, double lng) {
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address = null;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress  = currentLocationAddress;
                }
            }
        } catch (IOException e) {
            Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return nowAddress;
    }

    private void insertToDatabase(String phone, String address, String picture){

        class InsertData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String phone = (String)params[0];
                    String address = (String)params[1];
                    String picture = (String)params[2];
                    String link="http://seu920.cafe24.com/data_insert.php";

                    String data  = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
                    data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");
                    data += "&" + URLEncoder.encode("picture", "UTF-8") + "=" + URLEncoder.encode(picture, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        InsertData task = new InsertData();
        task.execute(phone,address,picture);
    }

}
