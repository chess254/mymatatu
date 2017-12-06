package com.mymatatu.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anonymous on 19-07-2017.
 */

public class Filterdb extends SQLiteOpenHelper {
    private static final int DATABSAE_VERSION = 1;
    private static final String DATABASE_NAME = "filter_values.db";
    public static String TABLE_NAME = "filter";
    public static String COLUMN_ID = "_id";
    public static String COLUMN_PRODUCTNAME ="ids";

    public Filterdb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABSAE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +"("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCTNAME + " INTEGER "
                +");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    //Adding Values
    public void adds (Integer no){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME,no);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME,null , values);
        //Log.d("Add","done");
    }

    public void dels (Integer id){
        SQLiteDatabase db = getWritableDatabase();
        // db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_PRODUCTNAME + " =\"" + productname + " \";");
        db.execSQL("delete from "+TABLE_NAME+" where "+COLUMN_PRODUCTNAME + "=" + id);
       // Log.d("DOne","del");

    }

    public Integer[] priting(){
        String string="";
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db =getReadableDatabase();

        //
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        Integer a[] = new Integer[c.getCount()];
        int i=0;
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME))!= null){
                a[i] = c.getInt(c.getColumnIndex(COLUMN_PRODUCTNAME));
                i++;
                c.moveToNext();
            }
        }
        c.close();


        return a;
    }

}
