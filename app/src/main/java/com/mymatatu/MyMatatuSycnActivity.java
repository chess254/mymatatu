package com.mymatatu;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyMatatuSycnActivity extends Activity {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> contacts = new ArrayList<>();
    private String n="" , c="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_matatu_sycn);

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            names.add(name);
            n+=name+"\n";
            contacts.add(phoneNumber);
            c+=contacts+"\n";
        }

        ((TextView)findViewById(R.id.name)).setText(n);
        ((TextView)findViewById(R.id.number)).setText(c);
        Toast.makeText(getApplicationContext(),names.size()+" "+contacts.size(),Toast.LENGTH_LONG).show();
        phones.close();
    }
}
