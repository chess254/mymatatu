package com.mymatatu.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mymatatu.CustomDataTypes.Bookingdata;

import java.util.ArrayList;

/**
 * Created by anonymous on 20-07-2017.
 */

public class Booking_transaction_db extends SQLiteOpenHelper {
    private static final int DATABSAE_VERSION = 1;
    private static final String dbname = "bookinghistory.db";
    private static final String tablename1 = "bookings";
    private static final String tablename2 = "bookinghistory";
    public static String TABLE_NAME_RECENTSEARCHES = "recentsearches";
    private static final String tb1_c1 = "_ids";
    private static final String tb1_c2 = "from_s";
    private static final String tb1_c3 = "to_s";
    private static final String tb1_c4 = "sacco_name";
    private static final String tb1_c5 = "matatu_name";
    private static final String tb1_c6 = "total_seats";
    private static final String tb1_c7 = "seats_number";
    private static final String tb1_c8 = "total_cost";
    private static final String tb1_c9 = "Date";
    private static final String tb1_c10 = "Insurance";
    public static String RECENTSEARCHES_id = "_id";
    public static String RECENTSEARCHES_to = "source_rs";
    public static String RECENTSEARCHES_from = "destination_rs";
    public static String RECENTSEARCHES_date = "Date_rs";


    public Booking_transaction_db(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, DATABSAE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + tablename1 +"("
                + tb1_c1 + " TEXT , "
                + tb1_c2 + " TEXT, "
                + tb1_c3 + " TEXT, "
                + tb1_c4 + " TEXT, "
                + tb1_c5 + " TEXT, "
                + tb1_c6 + " TEXT, "
                + tb1_c7 + " TEXT, "
                + tb1_c8 + " TEXT, "
                + tb1_c9 + " TEXT, "
                + tb1_c10 + " TEXT "
                +");";
        String query2 = "CREATE TABLE " + tablename2 +"("
                + tb1_c1 + " TEXT , "
                + tb1_c2 + " TEXT, "
                + tb1_c3 + " TEXT, "
                + tb1_c4 + " TEXT, "
                + tb1_c5 + " TEXT, "
                + tb1_c6 + " TEXT, "
                + tb1_c7 + " TEXT, "
                + tb1_c8 + " TEXT, "
                + tb1_c9 + " TEXT, "
                + tb1_c10 + " TEXT "
                +");";
        String query3 = "CREATE TABLE " + TABLE_NAME_RECENTSEARCHES +"("
                + RECENTSEARCHES_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RECENTSEARCHES_from + " TEXT, "
                + RECENTSEARCHES_to + " TEXT, "
                + RECENTSEARCHES_date + " TEXT "
                +");";


        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ tablename1);
        db.execSQL("DROP TABLE IF EXISTS "+ tablename2);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_RECENTSEARCHES);
        onCreate(db);
    }

    public void addtotable1(String booking_id,String from,String to,String s_name,String m_name,
                            String total_seats,String s_number,String total_cost,String date,String insurance){
        ContentValues values = new ContentValues();
        values.put(tb1_c1,booking_id);
        values.put(tb1_c2,from);
        values.put(tb1_c3,to);
        values.put(tb1_c4,s_name);
        values.put(tb1_c5,m_name);
        values.put(tb1_c6,total_seats);
        values.put(tb1_c7,s_number);
        values.put(tb1_c8,total_cost);
        values.put(tb1_c9,date);
        values.put(tb1_c10,insurance);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(tablename1,null,values);
      //  Log.d("Add","done1");
    }

    public void addtotable2(String booking_id ,String from,String to,String s_name,String m_name
            ,String total_seats,String s_number,String total_cost,String date,String insurance){
        ContentValues values = new ContentValues();
        values.put(tb1_c1,booking_id);
        values.put(tb1_c2,from);
        values.put(tb1_c3,to);
        values.put(tb1_c4,s_name);
        values.put(tb1_c5,m_name);
        values.put(tb1_c6,total_seats);
        values.put(tb1_c7,s_number);
        values.put(tb1_c8,total_cost);
        values.put(tb1_c9,date);
        values.put(tb1_c10,insurance);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(tablename2,null,values);
      //  Log.d("Add",values.toString());
    }

    public void turnctable1(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tablename1,null,null);
       // Log.d("del","done");
    }

    public ArrayList<Bookingdata> showtable2(){
        ArrayList<Bookingdata> returnlist = new ArrayList<>();
        String query = "SELECT * FROM "+tablename2;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query,null);
        if(c == null){
           // Log.d("table","empty");
        }else {

            c.moveToFirst();
            while (!c.isAfterLast()) {
                if (c.getString(c.getColumnIndex(tb1_c2)) != null) {
                    Bookingdata bditem = new Bookingdata();
                    bditem.id = c.getString(c.getColumnIndex(tb1_c1));
                   // Log.d("bookingid", String.valueOf(bditem.id));
                    bditem.from_s = c.getString(c.getColumnIndex(tb1_c2));
                    bditem.to_s = c.getString(c.getColumnIndex(tb1_c3));
                    bditem.sacco_name = c.getString(c.getColumnIndex(tb1_c4));
                    bditem.matatu_name = c.getString(c.getColumnIndex(tb1_c5));
                    bditem.total_seats = c.getString(c.getColumnIndex(tb1_c6));
                    bditem.seats = c.getString(c.getColumnIndex(tb1_c7));
                    bditem.total_cost = c.getString(c.getColumnIndex(tb1_c8));
                    bditem.date = c.getString(c.getColumnIndex(tb1_c9));
                    bditem.insurance = c.getString(c.getColumnIndex(tb1_c10));
                    c.moveToNext();
                    returnlist.add(bditem);
                }

            }
        }
        c.close();
        return returnlist;
    }

    public void addrecentsearches (String from,String to, String date){
        ContentValues values = new ContentValues();
        values.put(RECENTSEARCHES_date,date);
        values.put(RECENTSEARCHES_from,from);
        values.put(RECENTSEARCHES_to,to);
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME_RECENTSEARCHES;
        Cursor c = db.rawQuery(query,null);
        if(c.getCount() > 5){
            c.moveToFirst();
            String query_del = "DELETE FROM "+TABLE_NAME_RECENTSEARCHES +" WHERE "
                    +RECENTSEARCHES_id +" = "+ c.getInt(c.getColumnIndex(RECENTSEARCHES_id));
            db.execSQL(query_del);
        }
        db.insert(TABLE_NAME_RECENTSEARCHES,null , values);
        //Log.d("ADD_RS","DONE");
    }

    public ArrayList<String> print_recentsearches (){
        ArrayList<String> final_send = new ArrayList<>();
        String query = "SELECT * FROM "+TABLE_NAME_RECENTSEARCHES;
        SQLiteDatabase db =getReadableDatabase();
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String final_string = "";
            if (c.getString(c.getColumnIndex(RECENTSEARCHES_id)) != null) {
                final_string = c.getString(c.getColumnIndex(RECENTSEARCHES_from)) + ","
                        + c.getString(c.getColumnIndex(RECENTSEARCHES_to)) + ","
                        + c.getString(c.getColumnIndex(RECENTSEARCHES_date));
            }
            final_send.add(final_string);
            c.moveToNext();
        }

        return final_send;
    }
}
