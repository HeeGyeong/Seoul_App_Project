package com.example.heegyeong.accessibilityservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Heegyeong on 2017-02-14.
 */
public class PassSetting extends AppCompatActivity {

    String pass = null;
    EditText passText;
    String phoneCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_setting);
        passText = (EditText)findViewById(R.id.password);
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
                if(pass == null) {
                    pass = tmp;
                    passText.setText(pass);
                }else{
                    pass = pass + tmp;
                    passText.setText(pass);
                    if(passText.length() == 4){ //  4자리를 입력하면 자동으로 넘어갈 수 있도록 변경.
                        String pwd = passText.getText().toString();
                        Toast.makeText(getApplicationContext(), "다시한번 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PassSetting.this, PassChecking.class);
                        intent.putExtra("pwd", pwd);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.butC :
                pass = null;
                passText.setText("");
                break;
            case R.id.butin :
                String pwd = passText.getText().toString();
                Toast.makeText(getApplicationContext(), "다시한번 입력해 주세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PassSetting.this, PassChecking.class);
                intent.putExtra("pwd", pwd);
                startActivity(intent);
                break;
            default :
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 누르면 어플 종료됨. 따라서, MainAcitivty를 띄우기 위한 작업.
        super.onBackPressed();
    }

}
