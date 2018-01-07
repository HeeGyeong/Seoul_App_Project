package com.example.heegyeong.accessibilityservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

/**
 * Created by Heegyeong on 2017-03-08.
 */
public class SearchPassMail extends AppCompatActivity {

    TextView searchMail;
    EditText enterCode;
    String Gmail;

    private static char[] chars;
    String saveChars;
    Boolean sendMail = true;

    static {
        StringBuilder buffer = new StringBuilder();
        for(char ch = '0'; ch <= '9'; ++ch)
            buffer.append(ch);
        for(char ch = 'a'; ch <= 'z'; ++ch)
            buffer.append(ch);
        for(char ch = 'A'; ch <= 'Z'; ++ch)
            buffer.append(ch);

        chars = buffer.toString().toCharArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_pass_mail);

        String id = SharedPreferenceUtil.getData(this, "Gmail");

        searchMail = (TextView)findViewById(R.id.searchMail);
        if(id == null) {
            id = "저장된 G-Mail 메일 주소가 없습니다.";
            sendMail = false;
        }
        searchMail.setText(id);

        Gmail = SharedPreferenceUtil.getData(this, "Gmail");

        enterCode = (EditText)findViewById(R.id.enterCode);

        saveChars = randomString(10);

        Button sendCode = (Button)findViewById(R.id.sendCode);
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferenceUtil.putData(getApplicationContext(), "saveChars", saveChars);
                if(sendMail == true){
                    Toast.makeText(SearchPassMail.this, "보안코드를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                    sendEmail(saveChars);
                } else if(sendMail == false){
                    Toast.makeText(SearchPassMail.this, "메일을 보낼 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button resetting = (Button)findViewById(R.id.resetting);
        resetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp = enterCode.getText().toString();
                String code = SharedPreferenceUtil.getData(getApplicationContext(), "saveChars");
                if(temp.equals(code)){
                    Intent intent = new Intent(getApplicationContext(), PassSetting.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(),"보안 코드값이 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmail(String text) {
        //Getting content for email

        //String email = "dbgmlrud4590@gmail.com";
        // 수신자는 로그인한 본인 한정.
        String subject = "비밀번호 수정 보안 코드입니다.";
        String message = text;
        String gmailID = SharedPreferenceUtil.getData(this, "Gmail");
        String pwd = SharedPreferenceUtil.getData(this, "GmailPwd");

        //Creating SendMail object
        SendMail sm = new SendMail(this, gmailID , subject, message, gmailID, pwd,1);
        // this, gmail-id, Title, text, gmail-id, password

        //Executing sendmail to send email
        sm.execute();
    }

    public static String randomString(int length){
        if(length < 1)
            throw new IllegalArgumentException("Length < 1 : "+ length);

        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for(int i =0; i< length ; i++){
            randomString.append(chars[random.nextInt(chars.length)]);
        }
        return randomString.toString();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchPassMail.this, InputPass.class);
        startActivity(intent);
    }
}
