package com.mymatatu.Background;

import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.AppConstants;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.R;
import com.mymatatu.VolleyEssentials.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anonymous on 08-08-2017.
 */

public class CancelBooking {
    Context c;

    public CancelBooking(Context c) {
        this.c = c;
    }

    public void sendCancelRequest(){
        StringRequest str = new StringRequest(Request.Method.POST, AppConstants.BASE_URL + "Matatu/deletetemporary",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject response_object = new JSONObject(response);
                            if(response_object.getBoolean("success")){
                                //Toast.makeText(c,response_object.getString("message"),Toast.LENGTH_LONG).show();
                            }else {
                               // Toast.makeText(c,)
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = c.getString(R.string.api_id)+":"+c.getString(R.string.api_password);
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
               // Log.d("auth",headers.toString());
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone_no", SaveSharedPreference.getPhone(c));
                return params;
            }
        };

        MySingleton.getInstance(c).addToRequestQueue(str);
    }
}
