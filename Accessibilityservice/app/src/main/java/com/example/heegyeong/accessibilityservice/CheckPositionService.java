package com.example.heegyeong.accessibilityservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Heegyeong on 2017-04-01.
 */
public class CheckPositionService extends Service {

    String SMSNumber;
    int index;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {    // 최초 생성되었을 때 한번 실행
        super.onCreate();
        index = 0;
        SMSNumber = SharedPreferenceUtil.getData(this, "SMSNumber");
        Log.d("myTask", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // 백그라운드에서 실행되는 동작들이 들어감

        Log.d("myTask", "onStartCommand()");
        final Timer timer = new Timer();

        TimerTask myTask = new TimerTask() {
            public void run() {
                Log.d("myTask", "TimerTask()");
                index++;

                Intent i = new Intent(CheckPositionService.this, AutoSendSMS.class);
                i.putExtra("receiveNumber", SMSNumber);
                i.putExtra("upDatePosition", true);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                if(index >= 5){
                    index = 0;
                    timer.cancel();
                }

            }
        };

        timer.schedule(myTask, 1000, 60000); // 1초후 첫실행, 1분 마다 계속실행

        // return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        // 서비스 종료시에 실행되는 함수
        Log.d("CheckingLog", "ScreenLockService onDestroy");
        super.onDestroy();
    }
}