package com.mymatatu.App_Importants;

import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.VolleyEssentials.MySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anonymous on 31-07-2017.
 */

public class SeatingQuery {
    int size;
    Context c;
    String matatuid;

    public SeatingQuery(Context c,int size,String matatuid) {
        this.size =size;
        this.c =c;
        this.matatuid = matatuid;
    }

    int sendarr[] = new int[size];

    public int[] sendseatingrequest(){
        StringRequest str = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "vivek:123456";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
               // Log.d("auth",headers.toString());
                return headers;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("matatu_id",matatuid);
                return params;
            }
        };

        MySingleton.getInstance(c).addToRequestQueue(str);


        return sendarr;
    }
}
