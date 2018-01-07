package com.example.heegyeong.seoul_maptagging;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Heegyeong on 2017-11-25.
 */
public class LoginActivity extends RuntimePermission {

    private SharedPrefereneceUtil sharedPrefereneceUtil = new SharedPrefereneceUtil(this);

    private DatabaseReference databaseReference;
    EditText checkId;
    Button login;
    Button register;
    Button gstlogin;

    int indexCount = 0;
    int searchCount = 0;

    private static final int REQUEST_PERMISSION = 10;   // 런타임 퍼미션
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestAppPermissions(new String[]{
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.CALL_PHONE,
                        android.Manifest.permission.READ_SMS
                        },
                R.string.msg, REQUEST_PERMISSION);




        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        checkId = (EditText)findViewById(R.id.checkId);

        login =(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
                final String phoneNum = telephonyManager.getLine1Number();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                        Iterator<DataSnapshot> child2 = dataSnapshot.getChildren().iterator();
                        indexCount = 0;
                        searchCount = 0;
                        while(child.hasNext())
                        {
                            indexCount++;
                            if(child.next().getKey().equals(checkId.getText().toString()))
                            {
                                while(child2.hasNext())
                                {
                                    searchCount++;
                                    if(child2.next().getChildren().iterator().next().getKey().equals(phoneNum))
                                    {
                                        if(searchCount == indexCount){
                                            Toast.makeText(getApplicationContext(), "로그인!", Toast.LENGTH_LONG).show();
                                            sharedPrefereneceUtil.putSharedPreference("userID", checkId.getText().toString());
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);

                                        }else{
                                            Toast.makeText(getApplicationContext(), "아이디가 맞지 않습니다. ", Toast.LENGTH_LONG).show();
                                        }
                                        return;
                                    }
                                }

                                return;
                            }

                        }
                        Toast.makeText(getApplicationContext(),"존재하지 않는 아이디입니다.",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "첫 방문을 환영합니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "본 어플리케이션에서 사용할 닉네임을 정해주세요.", Toast.LENGTH_SHORT).show();
                TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
                final String phoneNum = telephonyManager.getLine1Number();

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("phoneNum", phoneNum);
                startActivity(intent);
            }
        });

        gstlogin = (Button)findViewById(R.id.gstlogin);
        gstlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  sharedPrefereneceUtil.putSharedPreference("userID", "guest");
                TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
                final String phoneNum = telephonyManager.getLine1Number();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("phoneNum", phoneNum);
                intent.putExtra("guestID","guestID");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requsetCode) {
        // 권한 설정 전부다 완료 되면 실행시에 뜨는 부분
    }
}