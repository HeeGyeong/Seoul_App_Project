package com.example.heegyeong.accessibilityservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Heegyeong on 2017-03-08.
 */
public class SearchPassQA extends AppCompatActivity {

    String saveQText;
    String saveAText;

    TextView Question;
    EditText Answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_pass_qa);

        saveQText = SharedPreferenceUtil.getData(this, "QText");
        saveAText = SharedPreferenceUtil.getData(this, "AText");

        Question = (TextView)findViewById(R.id.Qtext);
        if(saveQText != null)
            Question.setText(saveQText);
        else
            Question.setText("질문이 설정되지 않았습니다.");

        Answer = (EditText)findViewById(R.id.Atext);


        Button resetpwd = (Button)findViewById(R.id.resetpwd);
        resetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enterA = Answer.getText().toString();

                if(saveAText.equals(enterA)){
                    Intent intent = new Intent(SearchPassQA.this, PassSetting.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "질문에 대한 답이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchPassQA.this, InputPass.class);
        startActivity(intent);
    }
}
