package com.mymatatu.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by anonymous on 17-07-2017.
 */

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABSAE_VERSION = 1;
    private static final String DATABASE_NAME = "mymatatu.db";
    public static String TABLE_NAME = "contacts";
    public static String COLUMN_ID = "_id";
    public static String COLUMN_NAME = "name";
    public static String COLUMN_PRODUCTNAME ="contact";
    public static String Filter_Table_Name = "filters";
    public static String Filter_Column_Name = "filter_on_ids";


    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABSAE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +"("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT "
                + COLUMN_PRODUCTNAME + " TEXT "
                +");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    //Adding Values
    public void addContacts (String name ,String no){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,name);
        values.put(COLUMN_PRODUCTNAME,no);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME,null , values);
       // Log.d("Add","done");
    }

    public ArrayList<String> priting(){
        ArrayList<String> send = new ArrayList<>();
        String string="";
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db =getReadableDatabase();

        //
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME))!= null){
                send.add(c.getString(c.getColumnIndex(COLUMN_NAME)) + " " +c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME)));
                c.moveToNext();
            }
        }
        c.close();


        return send;
    }

    public ArrayList<String> get_numbers(){
        ArrayList<String> send = new ArrayList<>();
        String string="";
        String query = "SELECT "+COLUMN_PRODUCTNAME+" FROM "+TABLE_NAME;
        SQLiteDatabase db =getReadableDatabase();

        //
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME))!= null){
                send.add(c.getString(c.getColumnIndex(COLUMN_NAME)) + " " +c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME)));
                c.moveToNext();
            }
        }
        c.close();


        return send;
    }

}
