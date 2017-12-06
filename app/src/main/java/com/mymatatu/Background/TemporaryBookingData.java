package com.mymatatu.Background;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.AppConstants;

import java.util.ArrayList;

/**
 * Created by anonymous on 08-08-2017.
 */

public class TemporaryBookingData {
    Context c;
    int m_id;

    public TemporaryBookingData(Context c,int m_id) {
        this.c = c;
        this.m_id = m_id;
    }

    public ArrayList<Integer> temporaryBookingData(){
        ArrayList<Integer> send_arr = new ArrayList<>();
        StringRequest str = new StringRequest(Request.Method.POST, AppConstants.BASE_URL + "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });



        return send_arr;
    }
}
