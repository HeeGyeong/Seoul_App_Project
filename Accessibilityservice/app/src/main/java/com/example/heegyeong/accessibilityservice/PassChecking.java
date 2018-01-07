package com.example.heegyeong.accessibilityservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Heegyeong on 2017-02-15.
 */
public class PassChecking extends AppCompatActivity {

    String pass = null;
    EditText passText;
    String pwdCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_setting);
        passText = (EditText) findViewById(R.id.password);
        passText.setHint("다시 한번 입력해주세요.");

        Intent intent = getIntent();
        pwdCheck = intent.getStringExtra("pwd");

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
                    if(passText.length() == 4){
                        String pwd = passText.getText().toString();
                        if(pwd.equals(pwdCheck)){
                            Toast.makeText(getApplicationContext(),"비밀번호 설정이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PassChecking.this, InputPass.class);
                            intent.putExtra("pwd", pwd);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),"비밀번호를 잘못 입력하셨습니다.",Toast.LENGTH_SHORT).show();
                            pass = null;
                            passText.setText("");
                        }
                    }
                }
                break;
            case R.id.butC :
                pass = null;
                passText.setText("");
                break;
            case R.id.butin :
                String pwd = passText.getText().toString();
                if(pwd.equals(pwdCheck)){
                    Toast.makeText(getApplicationContext(),"비밀번호 설정이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PassChecking.this, InputPass.class);
                    intent.putExtra("pwd", pwd);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"비밀번호를 잘못 입력하셨습니다.",Toast.LENGTH_SHORT).show();
                    pass = null;
                    passText.setText("");
                }
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