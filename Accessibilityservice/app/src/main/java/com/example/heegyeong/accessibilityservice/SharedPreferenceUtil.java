package com.example.heegyeong.accessibilityservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Heegyeong on 2017-03-03.
 */
public class SharedPreferenceUtil {
    public static void putData
            (Context context, String key, String value)
    {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public static String getData
            (Context context, String key)
    {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString(key, null);
    }


    public static void putDataBoolean
            (Context context, String key, Boolean value)
    {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getDataBoolean
            (Context context, String key)
    {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getBoolean(key, false);
    }
}
