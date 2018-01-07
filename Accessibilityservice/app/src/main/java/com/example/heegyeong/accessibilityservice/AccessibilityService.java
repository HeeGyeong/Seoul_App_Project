package com.example.heegyeong.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * Created by Heegyeong on 2017-02-12.
 */
/// ANR 에러가 발생하는 서비스 아마도 여기인듯.. 이 부분 수정해 볼 것.
    // 따로 이부분만 실행해선 오류가 안날 수 있음. > 일단 부분부분 짤라서 실행해 볼 것.
public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
    String TAG = "AccessibilityService Log";

    int index = 1;
    String[] route = new String[30];
    String[] saveRoute = new String[30];

    /* ANR 오류 해결을 위해서, SharedPreperence를 저장하는 UtilClass를 사용.
        Intent를 사용하기 위해서 선언한 onStartCommand Class를 삭제하고, 안에 있는 내용을
        onAccessibilityEvent에 옮겨서 사용.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // saveRoute[0] 은 항상 자기 자신으로 설정되어있음. 따라서 route는 route[1]부터 저장.
        // 계속 응답 오류가 나서, try-catch를 사용해서 NullPointerException에 대한 오류를 잡아냄.
        try{
            for(int i = 1; i < 30 ; i++) { // intent로 넘어온 값을 route 배열에 저장
                String nullCheck =  SharedPreferenceUtil.getData(this, "path" + i);
                if(nullCheck != null){
                    route[i] = nullCheck;
                } else{
                }
                //Log.d("CheckLog","path"+i+" : " + route[i]);
            }
        }catch(NullPointerException ex){
            Toast.makeText(this,"NullPointerException",Toast.LENGTH_SHORT).show();
        }


        for(int i =0; i<30; i++){  // SharedPreference로 저장된 값을 saveRoute 배열에 불러옴.
            saveRoute[i] =  SharedPreferenceUtil.getData(this, "path" + i);
        }

        return startId;
    }*/

    // 이벤트가 발생할때마다 실행되는 부분
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        Log.e(TAG, "Catch Event : " + event.toString());

        Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
        Log.e(TAG, "Catch Event TEXT : " + event.getText());
        Log.e(TAG, "Catch Event ContentDescription  : " + event.getContentDescription());
        Log.e(TAG, "Catch Event getSource : " + event.getSource());
        Log.e(TAG, "=========================================================================");

        Log.d("CheckLog", "Called event");
        Log.d("CheckLog", event.getPackageName() + "");
        Log.d("CheckLog", index+"");

        try{
            for(int i = 1; i < 30 ; i++) { // intent로 넘어온 값을 route 배열에 저장
                String nullCheck =  SharedPreferenceUtil.getData(this, "path" + i);
                if(nullCheck != null){
                    route[i] = nullCheck;
                } else{
                }
            }
        }catch(NullPointerException ex){
            Toast.makeText(this,"NullPointerException",Toast.LENGTH_SHORT).show();
        }


        // SharedPreferenceUtil.putData(this, "path" + 0, "com.example.heegyeong.accessibilityservice");
        SharedPreferenceUtil.putData(this, "path" + 0, "com.android.settings");
        for(int i =0; i<30; i++){  // SharedPreference로 저장된 값을 saveRoute 배열에 불러옴.
            saveRoute[i] =  SharedPreferenceUtil.getData(this, "path" + i);
        }

        try{
            for(int i = 0 ; i < 30 ; i++){
                // Acitivty 에서 service 로 패키지 파일에 대한 String 을 넘겨서 저장해야 함.
                if(event.getPackageName().toString().equals(saveRoute[i])){
                    Log.d("CheckLog", event.getPackageName()+"");
                    // Service 에서 Intent를 통하여 Activity를 불러낼수 없음. 따라서 오류 발생.
                    //Intent intent = new Intent(this, MainActivity.class);
                    //startActivity(intent);
                    //////////////////////////////
                    if(index == 1){
                        index += 1;
                        Intent intent = new Intent(this, InputPass.class);
                        PendingIntent p = PendingIntent.getActivity(this, 0, intent, 0);
                        try {
                            p.send();
                            // 잠금 푸는 Acitivity에서 비밀번호를 풀면 index 값을 3으로 증가
                            // Index 초기화를 할 때, idnex가 3이 아니라면 초기화를 하지 않음.
                            // 문제 1. 잠금 푸는 Acitivty로 index 값을 어떻게 넘기는지 모름
                            // 문제 2. index를 증가 시킨 후에 넘기기 때문에, 비밀번호를 풀지 않아도 다시 잠금이 안생김.
                            // 문제 3. MainAcitivty에서 AccessibitilyService로 값을 넘길 수 있어야 함.

                            // 문제 해결 :. 뒤로가기 버튼 누를 시에, Index를 1로 설정하는 화면(홈)으로
                            // 이동하게 만들어 index를 초기화 시킨다.
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }
                    ///////////////////////////////////////////////
                }
            }
        }catch(NullPointerException ne){
            // Toast.makeText(this,"AccessibilityService NullPointException",Toast.LENGTH_SHORT).show();
        }catch(Exception ex){
            // Toast.makeText(this,"AccessibilityService Exception",Toast.LENGTH_SHORT).show();
        }

        if(event.getPackageName() != null){
            if(/*   최근 사용앱으로 넘어가도 index 초기화 하는것은 너무 비효율 적.
                event.getPackageName().toString().equals("com.android.systemui") ||*/
                // 여기서 Null이 발생해서 오류가 남. Null일때도 잡을 수 있도록 설정해야함.
                    event.getPackageName().toString().equals("com.sec.android.app.launcher")){
                index = 1;
                // 앱 마다 index를 설정 > Map을 사용. 혹은 배열을 사용하여 각 App마다의 index값을 주기.
                Log.d("CheckLog","index reset");
            }
        }else{
            Log.d("nullCheckLog","event.getPackageName() : "+event.getPackageName());
        }

    }

    // 접근성 권한을 가지고, 연결이 되면 호출되는 함수
    public void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        Log.d("CheckLog","entered permission. and connected");

        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK; // 전체 이벤트 가져오기
        info.feedbackType = AccessibilityServiceInfo.DEFAULT | AccessibilityServiceInfo.FEEDBACK_HAPTIC;
        info.notificationTimeout = 100; // millisecond

        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub
        Log.e("TEST", "OnInterrupt");
    }
}

