package com.mymatatu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class No_internet1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet_page1);
        Intent Activityname = getIntent();
        final String name = Activityname.getStringExtra("ActivityName");
       //Snackbar sn = Snackbar.make(getWindow().getDecorView().getRootView(),"Please Connect to the Internet and try again",Snackbar.LENGTH_INDEFINITE);

       // sn.show();
    }
}
