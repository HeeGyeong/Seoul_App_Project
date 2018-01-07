package com.example.heegyeong.accessibilityservice;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Heegyeong on 2017-02-17.
 */
public class ScreenReceiver extends BroadcastReceiver {

    private KeyguardManager km = null;
    private KeyguardManager.KeyguardLock keyLock = null;
    private TelephonyManager telephonyManager = null;
    private boolean isPhoneIdle = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("test Log", "screen able test");
        Log.d("CheckingLog", "ScreenReceiver onReceive");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // 화면이 꺼졌을때, 꺼졌다는 Intent를 받아오기 위함.
            Log.d("test Log", "screen disable");
            Log.d("CheckingLog", "ScreenReceiver onReceive in ScreenOff");
            if (km == null)
                km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            if (keyLock == null)
                keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);

            if(telephonyManager == null){
                telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            }

            if(isPhoneIdle) {

                disableKeyguard();
                Log.d("test Log", "screen inable");
                Log.d("CheckingLog", "ScreenReceiver onReceive in isPhoneldle");
                Intent i = new Intent(context, InputPass.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("screenLock", true);
                context.startActivity(i);
                // 화면이 꺼졌을 때 MainActivity를 불러와서 화면을 켰을 때 바로 보이도록 구현
            }
        }
    }

    public void reenableKeyguard() {
        Log.d("CheckingLog", "ScreenReceiver reenableKeyguard");
        keyLock.reenableKeyguard();
    }
    public void disableKeyguard() {
        Log.d("CheckingLog", "ScreenReceiver disableKeyguard");
        keyLock.disableKeyguard();
    }

    private PhoneStateListener phoneListener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber){
            Log.d("CheckingLog", "ScreenReceiver PhoneStateListener");
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE :
                    isPhoneIdle = true;
                    break;

                case TelephonyManager.CALL_STATE_RINGING :
                    isPhoneIdle = false;
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK :
                    isPhoneIdle = false;
                    break;
            }
        }
    };
}
