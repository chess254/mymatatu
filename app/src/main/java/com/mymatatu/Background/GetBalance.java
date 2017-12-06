package com.mymatatu.Background;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.AppConstants;
import com.mymatatu.Home;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.PaymentConfirm;
import com.mymatatu.R;
import com.mymatatu.VolleyEssentials.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anonymous on 26-07-2017.
 */

public class GetBalance {
    Activity c;
    Fragment f;
    public int balance;
    Home a;
    JSONObject jo = new JSONObject();
    AppConstants appConstants = new AppConstants();

    public GetBalance(Activity c) {
        this.c = c;
        this.f = f;

    }


    public void checkbalance(final Context c, final Fragment f) {

        final StringRequest balancecheckreq = new StringRequest(Request.Method.POST, appConstants.BASE_URL + "Passenger/showbalance"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.d("response balance", response);
                try {
                    JSONObject jo = new JSONObject(response);
                    if (jo.getBoolean("success")) {
                        JSONObject jo_new = jo.getJSONObject("balance");
                      //  Log.d("balance", String.valueOf(jo_new.getInt("matatu_user_wallet")));
                        SaveSharedPreference.setBalance(c, jo_new.getInt("matatu_user_wallet"));

                        if (f != null) {

                            if (f instanceof PaymentConfirm) {
                                ((PaymentConfirm) f).setdata(true);
                            }

                            a = (Home) c;
                            a.setBalance(String.valueOf(jo_new.getInt("matatu_user_wallet")));
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = c.getString(R.string.api_id) +
                        ":" + c.getString(R.string.api_password);
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone_login", SaveSharedPreference.getPhone(c));
                return params;
            }
        };
        MySingleton.getInstance(c).addToRequestQueue(balancecheckreq);
    }

}
