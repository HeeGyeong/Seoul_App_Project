package com.example.heegyeong.accessibilityservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Heegyeong on 2017-02-25.
 */
public class OtherSetting extends AppCompatActivity {

    private GpsInfo gps;


    String[] number = new String[4];
    EditText number1;
    EditText number2;
    EditText number3;
    EditText number4;   /// Save for sedning MSG
    String[] saveNumber = new String[4];

    String[] index = new String[2];
    EditText gMail;
    EditText password;
    String[] saveIndex = new String[2];

    EditText QText;
    EditText AText;

    String[] QAText = new String[2];
    String[] saveQAText = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_setting);

        number1 = (EditText)findViewById(R.id.number1);
        number2 = (EditText)findViewById(R.id.number2);
        number3 = (EditText)findViewById(R.id.number3);
        number4 = (EditText)findViewById(R.id.sendMSG);


        gMail = (EditText)findViewById(R.id.gmail);
        password = (EditText)findViewById(R.id.pwd);

        QText = (EditText)findViewById(R.id.QuesText);
        AText = (EditText)findViewById(R.id.AnswText);
        /////////////////////////////////////////////////////////////////////////
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        for(int i = 0; i < 4 ; i ++){
            saveNumber[i] = prefs.getString("saveNumber"+i, null);// 값이 없을시 null을 넣음
        }

        if(saveNumber[0] != null)
            number1.setText(saveNumber[0]);
        if(saveNumber[1] != null)
            number2.setText(saveNumber[1]);
        if(saveNumber[2] != null)
            number3.setText(saveNumber[2]);
        if(saveNumber[3] != null)
            number4.setText(saveNumber[3]);
        /////////////////////////////////////
        for(int i = 0; i < 2 ; i ++){
            saveIndex[i] = prefs.getString("saveIndex"+i, null);// 값이 없을시 null을 넣음
        }

        if(saveIndex[0] != null)
            gMail.setText(saveIndex[0]);
        if(saveIndex[1] != null)
            password.setText(saveIndex[1]);
        //////////////////////////////////////
        saveQAText[0] = SharedPreferenceUtil.getData(this, "QText");
        saveQAText[1] = SharedPreferenceUtil.getData(this, "AText");
        if(saveQAText[0] != null)
            QText.setText(saveQAText[0]);
        if(saveQAText[1] != null)
            AText.setText(saveQAText[1]);
        ///////////////////////////////////////

        number1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("saveNumber"+0, number1.getText().toString());
                editor.commit();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });

        number2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("saveNumber"+1, number2.getText().toString());
                editor.commit();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });

        number3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("saveNumber"+2, number3.getText().toString());
                editor.commit();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });

        number4.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("saveNumber"+3, number4.getText().toString());
                editor.commit();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });
        ///////////////////
        gMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("saveIndex"+0, gMail.getText().toString());
                SharedPreferenceUtil.putData(OtherSetting.this, "Gmail",  gMail.getText().toString());
                editor.commit();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("saveIndex"+1, password.getText().toString());
                SharedPreferenceUtil.putData(OtherSetting.this, "GmailPwd", password.getText().toString());
                editor.commit();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });

        QText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                SharedPreferenceUtil.putData(OtherSetting.this, "QText", QText.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });

        AText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                SharedPreferenceUtil.putData(OtherSetting.this, "AText", AText.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });
        /////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////

        //locationCheck
        ImageButton locationCheck = (ImageButton)findViewById(R.id.locationCheck);
        locationCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps = new GpsInfo(OtherSetting.this);
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    String locationName = getAddress(OtherSetting.this, latitude, longitude);
                  /*  Toast.makeText(
                            getApplicationContext(),
                            "위치 정보 제공 승인 - \n위도: " + latitude + "\n경도: " + longitude, Toast.LENGTH_LONG).show();*/
                    Toast.makeText(
                            getApplicationContext(), "현재 위치 : \n"+locationName, Toast.LENGTH_LONG).show();
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });

        ImageButton Save = (ImageButton)findViewById(R.id.Save);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number[0] = number1.getText().toString();
                number[1] = number2.getText().toString();
                number[2] = number3.getText().toString();
                number[3] = number4.getText().toString();

                index[0] = gMail.getText().toString();
                index[1] = password.getText().toString();

                QAText[0] = QText.getText().toString();
                QAText[1] = AText.getText().toString();

                Intent intent = new Intent(OtherSetting.this, InputPass.class);
                intent.putExtra("setting", true);
                intent.putExtra("setting2", true);
                for (int i = 0; i < 4; i++) {
                    intent.putExtra("saveNumber" + i, number[i]);
                }
                for (int i = 0; i < 2; i++) {
                    intent.putExtra("saveIndex" + i, index[i]);
                }

                Log.d("NumberLog", "onClick" + number[0] + " : " + number[1] + " : " + number[2]);
                Toast.makeText(OtherSetting.this, "설정이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
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
}
