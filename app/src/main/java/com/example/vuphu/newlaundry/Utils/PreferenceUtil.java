package com.example.vuphu.newlaundry.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.JsonReader;

import com.apollographql.apollo.api.ResponseReader;
import com.example.vuphu.newlaundry.GetCustomerQuery;
import com.example.vuphu.newlaundry.Order.OBBranch;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.stetho.inspector.network.PrettyPrinterDisplayType.JSON;

public class PreferenceUtil {
    public static final String IS_FIRST_OPEN = "IS_FIRST_OPEN";
    public static final String WELCOME = "WELCOME";
    public static final String PREFERENCE = "CUSTOMER";
    private static final String AUTH_TOKEN_TIME = "AUTH_TOKEN_TIME";
    private static final String AUTH_TOKEN = "AUTH_TOKEN";
    private static final String SET_UP_INFO = "SET_UP_INFO";
    private static final String CURRENT_USER = "CURRENT_USER";
    private static final String LIST_ORDER = "LIST_ORDER";
    private static final String LIST_BRANCH = "LIST_BRANCH";
    private static final String ID_USER = "ID_USER";


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
        return sharedPref.getString(AUTH_TOKEN, null);
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
        return  sharedPref.getBoolean(SET_UP_INFO, true);
    }

    public static void setCurrentUser(@NonNull Context context, GetCustomerQuery.CustomerById currentUser){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        editor.putString(CURRENT_USER, gson.toJson(currentUser));
        editor.apply();
    }

    public static GetCustomerQuery.CustomerById getCurrentUser (@NonNull Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        String currentUser = sharedPref.getString(CURRENT_USER, "");
        if (currentUser == ""){
            return null;
        }
        else {
            Gson gson = new Gson();
            return gson.fromJson(currentUser, GetCustomerQuery.CustomerById.class);
        }
    }

    public static ArrayList<OBOrderDetail> getListOrderDetail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        ArrayList<OBOrderDetail> listOrder = new ArrayList<>();
        String serializedObject = sharedPreferences.getString(LIST_ORDER, null);
        if (serializedObject != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<OBOrderDetail>>(){}.getType();
            listOrder = gson.fromJson(serializedObject, type);
        }
        return listOrder;
    }

    public static void setListOrderDetail(ArrayList<OBOrderDetail> list, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        editor.putString(LIST_ORDER, gson.toJson(list));
        editor.apply();
    }

    public static void setListBranch(ArrayList<OBBranch> listBranch, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        editor.putString(LIST_BRANCH, gson.toJson(listBranch));
        editor.apply();
    }

    public static ArrayList<OBBranch> getListBranch(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        ArrayList<OBBranch> listBranch = new ArrayList<>();
        String serializedObject = sharedPreferences.getString(LIST_BRANCH, null);
        if (serializedObject != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<OBBranch>>(){}.getType();
            listBranch = gson.fromJson(serializedObject, type);
        }
        return listBranch;
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }



    public static boolean checkKeyExist(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    public static void setIdUser(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ID_USER, id);
        editor.apply();
    }

    public static String getIdUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ID_USER, "");
    }

    public static void removeOrderList(Context context) {
        removeKey(context, LIST_ORDER);
    }

    public static void setIsFirstOpen(Context context, boolean isFirstOpen){
        SharedPreferences sharedPreferences = context.getSharedPreferences(WELCOME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_FIRST_OPEN, isFirstOpen);
        editor.apply();
    }

    public static Boolean isFirstOpen(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WELCOME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_FIRST_OPEN, true);
    }

}
