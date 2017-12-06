package com.mymatatu.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mymatatu.CustomDataTypes.Profile;

/**
 * Created by anonymous on 21-07-2017.
 */

public class profile_data extends SQLiteOpenHelper {

    private static final int DATABSAE_VERSION = 1;
    private static final String dbname = "profile.db";
    private static final String tablename = "profile";
    private static final String c1 = "name";
    private static final String c2 = "phone";
    private static final String c3 = "county";
    private static final String c4 = "city";
    private static final String c5 = "image";
    public profile_data(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, DATABSAE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + tablename + "("+
                c1 + " TEXT," +
                c2 + " TEXT," +
                c3 + " TEXT," +
                c4 + " TEXT," +
                c5 + " BLOB);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ tablename);
        onCreate(db);
    }

    public void add (String name,String phone, String county,String city , byte[] image){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put(c1,name);
        cv.put(c2,phone);
        cv.put(c3,county);
        cv.put(c4,city);
        cv.put(c5,image);
        database.insert( tablename, null, cv );
        //Log.d("Adding","profile");
    }

    public Profile readdata (){

        SQLiteDatabase database = this.getReadableDatabase();
        Profile p = new Profile();
        String query = "SELECT * FROM "+tablename;
        Cursor c = database.rawQuery(query,null);
        if(c.moveToFirst()) {
            p.name = c.getString(c.getColumnIndex(c1));
            p.phone = c.getString(c.getColumnIndex(c2));
            p.county = c.getString(c.getColumnIndex(c3));
            p.city = c.getString(c.getColumnIndex(c4));
            p.imagear = c.getBlob(c.getColumnIndex(c5));
        }else
        {
            p.name = "John Doe";
            p.phone = "+254-";
            p.county = "County";
            p.city = "City";
            p.name = "John Doe";
        }

        return p;
    }
}
