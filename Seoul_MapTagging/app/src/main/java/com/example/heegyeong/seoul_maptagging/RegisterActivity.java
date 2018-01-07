package com.example.heegyeong.seoul_maptagging;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.Iterator;

/**
 * Created by Heegyeong on 2017-11-22.
 */
public class RegisterActivity extends AppCompatActivity {

    private SharedPrefereneceUtil sharedPrefereneceUtil = new SharedPrefereneceUtil(this);

    private DatabaseReference databaseReference;
    private EditText userID;
    private ValueEventListener checkUserID = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            Iterator<DataSnapshot> child2 = dataSnapshot.getChildren().iterator();
            while (child.hasNext()) {

                if (child.next().getKey().equals(userID.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "존재하는 닉네임 입니다.", Toast.LENGTH_LONG).show();
                    databaseReference.removeEventListener(this);
                    return;
                }
            }
            while(child2.hasNext())
            {

                if(child2.next().getChildren().iterator().next().getKey().equals(number))
                {
                    Toast.makeText(getApplicationContext(), "해당 핸드폰 번호로 생성한 닉네임이 존재합니다.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            createUserID();

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        databaseReference  = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-base-2722d.firebaseio.com/user");

        userID = (EditText)findViewById(R.id.email);

        Intent intent2 = getIntent();
        String phoneNum = intent2.getStringExtra("phoneNum");

        number = phoneNum;

        Button check = (Button)findViewById(R.id.submit);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userID.getText().toString().length() > 1){
                    databaseReference.addListenerForSingleValueEvent(checkUserID);
                }else{
                    Toast.makeText(getApplicationContext(), "2글자 이상 입력해주세요.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    void createUserID()
    {
   //     Date date = new Date(System.currentTimeMillis());
        databaseReference.child(userID.getText().toString()).child(number).setValue(number);
        Toast.makeText(getApplicationContext(), "ID가 생성되었습니다.", Toast.LENGTH_SHORT).show();
        sharedPrefereneceUtil.putSharedPreference("userID", userID.getText().toString());
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);

    }

    @Override //뒤로가기 이벤트
    public  boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
