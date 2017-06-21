package boomba.apps.android.ktmkotha.prefManager;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {

    public static final String PREF_NAME = "PREFERENCE";
    public static final String REG_ID = "REG_ID";

    public PrefManager() {
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getRegId(Context context) {
        return getSharedPreferences(context).getString(REG_ID, null);
    }

    public static void setRegId(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(REG_ID, newValue);
        editor.apply();
    }

 }

