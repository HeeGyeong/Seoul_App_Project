package com.example.heegyeong.accessibilityservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Heegyeong on 2017-03-07.
 */
public class SMSReceiver extends BroadcastReceiver {

    AudioManager audio;
    static MediaPlayer music;

    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        // 수신한 액션을 이 onReceive메소드에서 처리하게 됩니다

        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {

        }
        */
        music = MediaPlayer.create(context, R.raw.konan);
        // 무한 반복을 하지 않는다.
        // music.setLooping(true);

        // SMS 메시지를 파싱.
        Bundle bundle = intent.getExtras();
        String str = ""; // 출력할 문자열 저장. ""으로 저장 : dump값 들어가는 것 방지.
        String num = "";
        if (bundle != null) { // 수신된 내용이 있으면
            // 실제 메세지는 Object타입의 배열에 PDU 형식으로 저장된다고 함.
            Object [] pdus = (Object[])bundle.get("pdus");

            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += msgs[i].getOriginatingAddress()  // 폰 번호.
                        + " : " +
                        msgs[i].getMessageBody().toString(); // 문자 수신내용
                num += msgs[i].getOriginatingAddress();
            }
           // Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        }
        // Activity를 하나 실행시키고, 거기서 메일 보내는 것과 함수를 작성. 바로 함수를 보내고
        // finish를 통해 activity는 종료시키도록 구현하면 됨.
        if(str.contains("지금 어디니?")){
            Intent i = new Intent(context, AutoSendSMS.class);
            i.putExtra("receiveNumber", num);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        if(str.contains("노래 시작")){
            //getSystemService를 그냥 쓰면 안된다. 해당 Activity는 BroadCastReceiver 이다.
            //getSystemService는 Context를 상속받은 컴포넌트에서만 사용이 가능한데, BroadCastReceiver는
            //Context를 따로 상속받지 않는다. 따라서, context를 앞에 붙여서 사용해야 한다.

            audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audio.setStreamVolume(AudioManager.STREAM_RING,
                    (int) (audio.getStreamMaxVolume(AudioManager.STREAM_RING) * 1.0), AudioManager.FLAG_PLAY_SOUND);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC,
                    (int) (audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 1.0), AudioManager.FLAG_PLAY_SOUND);

            music.start();
        }

        if(str.contains("노래 그만")){
            audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC,
                    (int) (audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)*0.0), AudioManager.FLAG_PLAY_SOUND);
            audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            music.stop();
            try {
                music.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            music.seekTo(0);
            music.release();
        }

        if(str.contains("이동했니?")){
            Intent i = new Intent(context, AutoSendSMS.class);
            i.putExtra("receiveNumber", num);
            i.putExtra("samePosition", true);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        if(str.contains("건강하니?")){
            Intent BTIntent = new Intent(context, AutoSendSMS.class);
            BTIntent.putExtra("receiveNumber", num);
            BTIntent.putExtra("BTCheck", true);
            BTIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(BTIntent);
        }

        if(str.contains("위치 계속")){
            Intent positionIntent = new Intent(context, CheckPositionService.class);
            context.startService(positionIntent);
            SharedPreferenceUtil.putDataBoolean(context, "SMS", true);
            SharedPreferenceUtil.putData(context, "SMSNumber", num);
        }

        if(str.contains("사진 보내")){
            Intent PHPIntent = new Intent(context, PHPConnection.class);
            PHPIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(PHPIntent);
        }




    }

}
