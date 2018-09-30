package com.example.vuphu.newlaundry.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class PreferenceUtil {
    public static final String PREFERENCE = "CUSTOMER";
    private static final String AUTH_TOKEN_TIME = "AUTH_TOKEN_TIME";
    private static final String AUTH_TOKEN = "AUTH_TOKEN";
    private static final String SET_UP_INFO = "SET_UP_INFO";

    public static Long getLastCheckedAuthTokenTime(@NonNull Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sharedPref.getLong(AUTH_TOKEN_TIME, 0);
    }

    public static void setLastCheckedAuthTokenTime(@NonNull Context context, @NonNull Long tokenCheckTime) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(AUTH_TOKEN_TIME, tokenCheckTime);
        editor.apply();
    }

    public static String getAuthToken(@NonNull Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sharedPref.getString(AUTH_TOKEN, "");
    }

    public static void setAuthToken(@NonNull Context context, @NonNull String authToken) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(AUTH_TOKEN, authToken);
        editor.apply();
    }

    public static void setPreferenceNull (@NonNull Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
        editor.apply();

    }

    public static void setSetUpInfo (@NonNull Context context, boolean bo){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SET_UP_INFO, bo);
        editor.apply();
    }
    public static boolean getSetUpInfo (@NonNull Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return  sharedPref.getBoolean(SET_UP_INFO, false);
    }

}
