package com.example.heegyeong.project_second;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Heegyeong on 2016-09-06.
 */
public class Email_Layout extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_layout);

        //email 보내기에 사용될 edittext
        final EditText toEdit = (EditText)findViewById(R.id.email_address);
        final EditText subjectEdit = (EditText)findViewById(R.id.email_subject);
        final EditText messageEdit = (EditText)findViewById(R.id.email_message);

        Button b = (Button)findViewById(R.id.email_send);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String to = toEdit.getText().toString();
                String subject = subjectEdit.getText().toString();
                String message = messageEdit.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"fhskf94kr@naver.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "이메일 클라이언트 선택하기 :"));
            }
        });

    }
}
