package com.greemlock.ivApp.Objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {
    static final String PREF_IS_BLUETOOTH_CONNECTED= "is_bluetooth_connected";
    static final String PREF_USER_ID = "user_id";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setPrefIsBluetooth(Context ctx, boolean is_bt_connected)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_IS_BLUETOOTH_CONNECTED, is_bt_connected);
        editor.commit();
    }

    public static boolean getPrefIsBluetooth(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_IS_BLUETOOTH_CONNECTED, false);
    }

    public static void setPrefUserId(Context ctx, String user_id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, user_id);
        editor.commit();
    }

    public static String getPrefUserId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }
}
