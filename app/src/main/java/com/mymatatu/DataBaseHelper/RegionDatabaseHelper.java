package com.mymatatu.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by anonymous on 29-07-2017.
 */

public class RegionDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABSAE_VERSION = 1;
    private static final String DATABASE_NAME = "region.db";
    public static String TABLE_NAME = "countycity";
    public static String COLUMN_ID = "city_id";
    public static String COLUMN_NAME = "name";
    public static String COLUMN_COUNTY_ID = "county_id";




    public RegionDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABSAE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME +"("
                + COLUMN_ID + " INTEGER , "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_COUNTY_ID + " INTEGER "
                +");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public void addCityCounty(int id,String name,int id_c){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,id);
        values.put(COLUMN_NAME,name);
        values.put(COLUMN_COUNTY_ID,id_c);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        Log.d("id",String.valueOf(id));
        Log.d("name",String.valueOf(name));
        Log.d("id_c",String.valueOf(id_c));
    }
    public  ArrayList<String> getCounty (){
        ArrayList<String> send = new ArrayList<>();
        String getCounty_query = "SELECT * FROM " + TABLE_NAME
                +" WHERE "+COLUMN_COUNTY_ID + " = 0";
        SQLiteDatabase db =getReadableDatabase();
        Cursor c = db.rawQuery(getCounty_query,null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null){
                send.add(c.getString(c.getColumnIndex(COLUMN_NAME)));
            }
            c.moveToNext();
        }
        return send;
    }

    public  ArrayList<String> getCity (){
        ArrayList<String> send = new ArrayList<>();
        String getCounty_query = "SELECT * FROM " + TABLE_NAME
                +" WHERE "+COLUMN_COUNTY_ID + " > 0";
        SQLiteDatabase db =getReadableDatabase();
        Cursor c = db.rawQuery(getCounty_query,null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null){
                send.add(c.getString(c.getColumnIndex(COLUMN_NAME)));
            }
            c.moveToNext();
        }
        return send;
    }

    public  ArrayList<String> getCityspecific (String s){
        ArrayList<String> send = new ArrayList<>();
        String getCounty_query = "SELECT * FROM " + TABLE_NAME
                +" WHERE "+COLUMN_NAME + " =  '" +s+ "'";
        SQLiteDatabase db =getReadableDatabase();
        Cursor c = db.rawQuery(getCounty_query,null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null){
                send.add(c.getString(c.getColumnIndex(COLUMN_NAME)));
            }
            c.moveToNext();
        }
        return send;
    }

    public String getSpecific (int s){
        String returns = "";
        String getCounty_query = "SELECT * FROM " + TABLE_NAME
                +" WHERE "+COLUMN_ID + " = " +s;
        SQLiteDatabase db =getReadableDatabase();
        Cursor c = db.rawQuery(getCounty_query,null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null){
                returns = c.getString(c.getColumnIndex(COLUMN_NAME));
            }
            c.moveToNext();
        }

        return returns;
    }

    public int getCityId (String s){
        int id = 999 ;
        String getCounty_query = "SELECT * FROM " + TABLE_NAME
                +" WHERE "+COLUMN_NAME + " = '" +s+"'";
        SQLiteDatabase db =getReadableDatabase();
        Cursor c = db.rawQuery(getCounty_query,null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null){
                id = c.getInt(c.getColumnIndex(COLUMN_ID));
            }
            c.moveToNext();
        }
        return id;
    }
}
