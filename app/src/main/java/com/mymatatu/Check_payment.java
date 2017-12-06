package com.mymatatu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.VolleyEssentials.MySingleton;

import java.util.HashMap;
import java.util.Map;

public class Check_payment extends AppCompatActivity {
    private String acc_no, uname;
    private Button check;
    private CardView cv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment);
        cv = (CardView) findViewById(R.id.carview_toggle);
        iv = (ImageView) findViewById(R.id.imageView2);
        cv.setVisibility(View.INVISIBLE);
        Glide.with(this).load("https://www.stickleyonsecurity.com/images/loading.gif").into(iv);
        check = (Button) findViewById(R.id.card_refresh_button);
        Intent intent = getIntent();
        acc_no = intent.getStringExtra("account_no");
        uname = intent.getStringExtra("uname");
        String url = "http://astral.togglebits.in/testing/payment_check";
        StringRequest str_req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().compareToIgnoreCase("yes") == 0) {
                    Intent intent = new Intent(Check_payment.this, SyncContacts.class);
                    SaveSharedPreference.setUserName(Check_payment.this, uname);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("uname", uname);
                    startActivity(intent);
                } else {
                    cv.setVisibility(View.VISIBLE);
                    check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent1 = new Intent(Check_payment.this, Check_payment.class);
                            intent1.putExtra("uname", uname);
                            intent1.putExtra("account_no", acc_no);
                            startActivity(intent1);
                            finish();
                        }
                    });

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
                String credentials = "vivek:123456";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
            //    Log.d("auth",headers.toString());
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("account_no", acc_no);
                params.put("phone_no",SaveSharedPreference.getPhone(getApplicationContext()));
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(str_req);
    }
}
