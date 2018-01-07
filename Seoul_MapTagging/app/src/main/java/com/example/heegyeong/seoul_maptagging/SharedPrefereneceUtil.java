package com.example.heegyeong.seoul_maptagging;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Heegyeong on 2017-11-08.
 */
public class SharedPrefereneceUtil {

    static final String PREF_NAME = "com.shared.pref";
    static Context mContext;

    public SharedPrefereneceUtil(Context c) { mContext = c; }

    public void putSharedPreference(String key, int value) {
        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getSharedPreference(String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getInt(key, value);
    }

    public void putSharedPreference(String key, String value) {
        SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSharedPreference(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getString(key, value);
    }
}
