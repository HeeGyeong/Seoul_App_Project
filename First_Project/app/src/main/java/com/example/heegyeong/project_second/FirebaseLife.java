package com.example.heegyeong.project_second;

import com.firebase.client.Firebase;

/**
 * Created by Heegyeong on 2016-11-17.
 */
public class FirebaseLife  extends android.app.Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
