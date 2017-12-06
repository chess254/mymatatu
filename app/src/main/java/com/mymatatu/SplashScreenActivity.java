package com.mymatatu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.Validation;
import com.mymatatu.Global.AppConstant;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.VolleyEssentials.MySingleton;
import com.mymatatu.appdecsiptionscreen.controller.AppDescriptionActivity;
import com.mymatatu.model.CountryCityRSM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    // RegionDatabaseHelper rdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screeb);
        // rdb = new RegionDatabaseHelper(this,null,null,1);
        getSupportActionBar().hide();
        countycityrequest();
        // this.deleteDatabase("region.db");
        //this.deleteDatabase("mymatatucontacts.db");
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (Validation.checkconnection(getApplicationContext())) {
                    if (check_login()) {
                        Intent i = new Intent(SplashScreenActivity.this, Home.class);
                        i.putExtra("screen", "splashscreen");
                        startActivity(i);
                    } else {
                        if (SaveSharedPreference.getPrefAppDescription(getApplicationContext())) {
                            Intent i = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SplashScreenActivity.this, AppDescriptionActivity.class);
                            startActivity(i);
                        }
                    }
                } else {
                    // CommonMethod.showconnectiondialog(SplashScreenActivity.this);
                    Toast.makeText(SplashScreenActivity.this, "Your not Connected to the Internet", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SplashScreenActivity.this, No_internet1.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    boolean check_login() {
        if (SaveSharedPreference.getUserName(this).length() != 0 && SaveSharedPreference.getPrefSyncContactFlag(this) == 1) {
            return true;
        }
        return false;
    }

    public void countycityrequest() {

        String url2 = AppConstant.BASE_URL + "Routes/locations";
        StringRequest str = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SaveSharedPreference.setString(SplashScreenActivity.this, "all_county", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = getApplicationContext().getString(R.string.api_id) +
                        ":" + getApplicationContext().getString(R.string.api_password);
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
            //    Log.d("auth", headers.toString());
                return headers;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(str);
    }
}

