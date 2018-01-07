package com.example.heegyeong.seoul_maptagging;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Heegyeong on 2017-11-08.
 */
public class BookmarkActivity extends AppCompatActivity {
    int loc; // 즐겨찾기선택한 지역설정변수
    private SharedPrefereneceUtil sharedPrefereneceUtil = new SharedPrefereneceUtil(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        //라디오 그룹
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                switch (checkedId) {
                    case R.id.loc1:
                        loc = 1;
                        break;
                    case R.id.loc2:
                        loc = 2;
                        break;
                    case R.id.loc3:
                        loc = 3;
                        break;
                    case R.id.loc4:
                        loc = 4;
                        break;
                    case R.id.loc5:
                        loc = 5;
                        break;
                    case R.id.loc6:
                        loc = 6;
                        break;
                    case R.id.loc7:
                        loc = 7;
                        break;
                    case R.id.loc8:
                        loc = 8;
                        break;
                    case R.id.loc9:
                        loc = 9;
                        break;
                    case R.id.loc10:
                        loc = 10;
                        break;
                    case R.id.loc11:
                        loc = 11;
                        break;
                    case R.id.loc12:
                        loc = 12;
                        break;
                    case R.id.loc13:
                        loc = 13;
                        break;
                    case R.id.loc14:
                        loc = 14;
                        break;
                    case R.id.loc15:
                        loc = 15;
                        break;
                    case R.id.loc16:
                        loc = 16;
                        break;
                    case R.id.loc17:
                        loc = 17;
                        break;
                    case R.id.loc18:
                        loc = 18;
                        break;
                    case R.id.loc19:
                        loc = 19;
                        break;
                    case R.id.loc20:
                        loc = 20;
                        break;
                    case R.id.loc21:
                        loc = 21;
                        break;
                    case R.id.loc22:
                        loc = 22;
                        break;
                    case R.id.loc23:
                        loc = 23;
                        break;
                    case R.id.loc24:
                        loc = 24;
                        break;
                    case R.id.loc25:
                        loc = 25;
                        break;
                }
                sharedPrefereneceUtil.putSharedPreference("loc", loc);
                Toast.makeText(getApplicationContext(), "설정이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BookmarkActivity.this, MainActivity.class);
                startActivity(intent);
                // Toast.makeText(getApplicationContext(), loc + "저장", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public  boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent1 = new Intent(BookmarkActivity.this,MainActivity.class);
            startActivity(intent1);
            return true;
        }
        return false;}

}
