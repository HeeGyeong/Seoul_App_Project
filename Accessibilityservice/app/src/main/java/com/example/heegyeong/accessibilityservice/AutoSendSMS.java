package com.example.heegyeong.accessibilityservice;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
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
 * Created by Heegyeong on 2017-03-07.
 */
public class AutoSendSMS extends AppCompatActivity {

    // GPSTracker class
    private GpsInfo gps;

    ////
    double latitude;
    double longitude;
    String locationName;

    String Gmail = "";
    String GmailPwd = "";

    String smsText;

    String receiveNumber = "";

    String latitudeSave;
    String longitudeSave;
    String locationNameSave;

    Boolean samePosition = false;
    Boolean upDatePosition = false;

    Boolean BTCheck = false;
    int BTLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_sms);


        Intent intent2 = getIntent();
        receiveNumber = intent2.getStringExtra("receiveNumber");

        Gmail = SharedPreferenceUtil.getData(this, "Gmail");
        GmailPwd = SharedPreferenceUtil.getData(this, "GmailPwd");

        // 이 값이 맨 처음 문자를 보냈을 때 저장된 위치여야 한다.
        latitudeSave = SharedPreferenceUtil.getData(AutoSendSMS.this, "latitudeLast");
        longitudeSave = SharedPreferenceUtil.getData(AutoSendSMS.this, "longitudeLast");
        locationNameSave = SharedPreferenceUtil.getData(AutoSendSMS.this, "locationNameLast");

        samePosition = intent2.getBooleanExtra("samePosition", false);
        upDatePosition = intent2.getBooleanExtra("upDatePosition", false);

        BTCheck =  intent2.getBooleanExtra("BTCheck", false);

        GpsInfo gps;
        gps = new GpsInfo(AutoSendSMS.this);

            // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            locationName = getAddress(AutoSendSMS.this, latitude, longitude);

            if(latitude == 0.0 && longitude == 0.0){    // 값을 제대로 불러오지 못했을 경우, 한번더 불러온다.
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                locationName = getAddress(AutoSendSMS.this, latitude, longitude);
            }

            smsText =  "위도: " + Double.toString(latitude) + "\n경도: " + Double.toString(longitude)
                    + "\n주소: " + locationName;

            if(samePosition == true){
                if(latitudeSave != null){
                    if(Double.valueOf(latitudeSave).doubleValue() != latitude
                            || Double.valueOf(longitudeSave).doubleValue() != longitude) {   // 변화가 있으면 이동함.
                        sendSMS(receiveNumber, smsText);
                    }else{  //  이동하지 않음.
                        sendSMS(receiveNumber, "이동하지 않았습니다.");
                    }
                }else{
                    sendSMS(receiveNumber, "기준 위치를 알 수 없습니다.");
                }

            }else{
                if(BTCheck == true){
                    BTLevel =  getBatteryPercentage(this);
                    smsText= "배터리 잔량은 "+BTLevel+"% 입니다.";
                    sendSMS(receiveNumber, smsText);
                }else{
                    sendSMS(receiveNumber, smsText);
                }
            }

            SharedPreferenceUtil.putData(AutoSendSMS.this, "latitudeLast",  String.valueOf(latitude));
            SharedPreferenceUtil.putData(AutoSendSMS.this, "longitudeLast", String.valueOf(longitude));
            SharedPreferenceUtil.putData(AutoSendSMS.this, "locationNameLast", locationName);


        } else {
            //gps.showSettingsAlert();
            Toast.makeText(getApplicationContext(),"위치 제공을 승인하지 않았습니다.", Toast.LENGTH_LONG).show();
            sendSMS(receiveNumber, "위치 제공을 승인하지 않았습니다.");
        }



        if(samePosition == false){
            if(upDatePosition == false){
                if(BTCheck == false){
                    Intent intent = new Intent(AutoSendSMS.this, CameraStart.class);
                    startActivity(intent);

                    /// Location 제공 승인을 하지 않으면 이 부분에서 에러 발생.
                    if(Gmail != null){   // gmail 로그인을 했을 경우.
                        if((gps.isGetLocation())){
                            sendEmail(smsText);
                        } else{
                            sendEmail("위치 정보를 제공하지 않는 상태입니다.");
                        }

                    } else{
                        Log.d("MailLog", "saveIndex[] : " + Gmail + " : " + GmailPwd);
                        Toast.makeText(getApplicationContext(),"GMail 로그인을 하지 않았습니다.", Toast.LENGTH_LONG).show();
                    }

                    // PHP Server로 phoneNumber, LocationName Throws.
                    // phoneCount 부분은 테스트하기 위해서 사용. Picture 부분 테이블 지울 때 삭제할 것.
                    String phone = SharedPreferenceUtil.getData(getApplicationContext(), "phoneNum");
                    String phoneCount = SharedPreferenceUtil.getData(getApplicationContext(), "phoneCount");
                    int count = Integer.parseInt(phoneCount);
                    insertToDatabase(phone, locationName, "MainApp Test : "+ (++count));

                    Intent PHPIntent = new Intent(AutoSendSMS.this, PHPConnection.class);
                    startActivity(PHPIntent);
                }
            }
        }

        finish();
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

    private void sendEmail(String text) {
        //Getting content for email

        //String email = "dbgmlrud4590@gmail.com";
        // 수신자는 로그인한 본인 한정.
        String subject = "현재 핸드폰의 위치입니다.";
        String message = text;

        //Creating SendMail object
        SendMail sm = new SendMail(this, Gmail, subject, message, Gmail, GmailPwd);

        //Executing sendmail to send email
        sm.execute();
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
                    return new String("Exception this class: " + e.getMessage());
                }
            }
        }

        InsertData task = new InsertData();
        task.execute(phone,address,picture);
    }

    public static int getBatteryPercentage(Context context) {
        Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;
        return (int)(batteryPct * 100);
    }
}
