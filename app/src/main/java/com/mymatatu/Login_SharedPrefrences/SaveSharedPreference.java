package com.mymatatu.Login_SharedPrefrences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by anonymous on 30-06-2017.
 */

public class SaveSharedPreference {
    static final String PREF_USER_NAME= "username";
    static final String PREF_USER_ACC_NO ="account_no";
    static final String PREF_USER_BALANCE = "balance";
    static final String PREF_SYNC_CONTACT_FLAG = "sync_contact_flag";
    static final String PREF_SOURCE= "source";
    static final String PREF_DESTINATION = "destination";
    static final String PREF_PHONE = "phone";
    static final String PREF_NAME = "name";
    static final String PREF_NEXTOFKIN = "nextofkin";
    static final String PREF_COUNTY = "county";
    static final String PREF_CITY = "city";
    static final String PREF_IMAGE = "image";
    static final String PREF_PASSWORD = "password";
    static final String PREF_APP_DESCRIPTION = "app_description_flag";
    static final String PREF_ACCOUNT_NO = "account_no";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static void setString(Context ctx, String key,String userName )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(key, userName);
        editor.commit();
    }
    public static void setBalance (Context ctx, int balance){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_USER_BALANCE, balance );
        editor.commit();
    }



    public static void setPrefSyncContactFlag(Context ctx, int flag){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_SYNC_CONTACT_FLAG, flag);
        editor.commit();
    }

    public static void setSource(Context ctx, String source )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_SOURCE, source);
        editor.commit();
    }

    public static void setDestination (Context ctx, String destination )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_DESTINATION, destination);
        editor.commit();
    }

    public static void setPrefName (Context ctx, String name )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_NAME, name);
        editor.commit();
    }
    public static void setPrefPhone (Context ctx, String phone )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_PHONE, phone);
        editor.commit();
    }
    public static void setPrefNextofkin (Context ctx, String nextofkin )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_NEXTOFKIN, nextofkin);
        editor.commit();
    }
    public static void setPrefCounty (Context ctx, String county )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_COUNTY, county);
        editor.commit();
    }
    public static void setPrefCity (Context ctx, String city )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_CITY, city);
        editor.commit();
    }
    public static void setPrefimage (Context ctx, String iamge )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_IMAGE, iamge);
        editor.commit();
    }
    public static void setPrefPassword (Context ctx, String password )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_PASSWORD, password);
        editor.commit();
    }
    public static void setPrefUserAccNo (Context ctx, String accno )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ACC_NO, accno);
        editor.commit();
    }
    public static void setPrefAppDescription (Context ctx, boolean flag )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_APP_DESCRIPTION, flag);
        editor.commit();
    }

    public static void setPrefUserAccNo (Context ctx, boolean flag )
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_APP_DESCRIPTION, flag);
        editor.commit();
    }

    public static String getString(Context ctx,String key)
    {
        return getSharedPreferences(ctx).getString(key, "");
    }

    public static boolean getPrefAppDescription(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_APP_DESCRIPTION, false);
    }

    public static String getPrefUserAccNo(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_ACC_NO, "");
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static int getBalance (Context ctx)
    {
        return getSharedPreferences(ctx).getInt(PREF_USER_BALANCE ,0);
    }

    public static int getPrefSyncContactFlag (Context ctx)
    {
        return getSharedPreferences(ctx).getInt(PREF_SYNC_CONTACT_FLAG ,0);
    }
    public static String getSource(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_SOURCE, "");
    }

    public static String getDestination (Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_DESTINATION, "");
    }

    public static String getName (Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_NAME, "");
    }
    public static String getPhone (Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_PHONE, "");
    }
    public static String getPrefNextofkin (Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_NEXTOFKIN, "");
    }public static String getPrefCounty (Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_COUNTY, "");
    }
    public static String getPrefCity (Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_CITY, "");
    }
    public static String getPrefImage (Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_IMAGE, "");
    }
    public static String getPrefPassword (Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_PASSWORD, "");
    }



    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_USER_NAME); //clear all stored data
        editor.remove(PREF_USER_BALANCE);
        editor.commit();

    }
    public static void clearSourceDestination(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_DESTINATION); //clear all stored data
        editor.remove(PREF_SOURCE);
        editor.commit();

    }



}
