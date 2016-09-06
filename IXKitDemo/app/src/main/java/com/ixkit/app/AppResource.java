package com.ixkit.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by icoco on 8/25/16.
 */
public class AppResource {

    /**
     *
     */
    public static final String SETTING_INFO = "SETTINGInfo";

    public static String getConfig(Context context, String key,
                                   String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(SETTING_INFO,
                0);
        String value = settings.getString(key, defaultValue);
        return value;

    }

    public static void setConfig(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(SETTING_INFO,
                0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(key, value);

        editor.commit();

    }

}
