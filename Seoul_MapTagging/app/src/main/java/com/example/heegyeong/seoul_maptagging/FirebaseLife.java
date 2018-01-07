package com.example.heegyeong.seoul_maptagging;

import com.firebase.client.Firebase;

/**
 * Created by Heegyeong on 2017-10-23.
 */
public class FirebaseLife extends android.app.Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
