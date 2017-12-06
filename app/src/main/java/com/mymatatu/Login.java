package com.mymatatu;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.CommonMethod;
import com.mymatatu.App_Importants.Validation;
import com.mymatatu.Global.AppConstant;
import com.mymatatu.Global.GlobalProgressDialogue;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.VolleyEssentials.MySingleton;
import com.mymatatu.model.CountryCityRSM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    GlobalProgressDialogue globalProgressDialogue;
    private EditText phoneno, password;
    ImageView password_show;
    boolean paswword_show_flag = false;
    private TextView error_tv;
    String phone_s, password_s;
    Button btn_login;
    View view;
    ArrayList<CountryCityRSM> countyData, cityData;
    TextView phoneerror, passworderror, register, forgot_pass, bannertext;
    RelativeLayout activity_login;
    JSONObject sendlogin;
    //RegionDatabaseHelper rdb;
    public static String TAG = Login.class.getSimpleName();
    private Dialog alertDialog;
    String acc_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        countycityrequest();
        getSupportActionBar().hide();
        // rdb = new RegionDatabaseHelper(this, null, null, 1);
        forgot_pass = (TextView) findViewById(R.id.forgot_password);
        activity_login = (RelativeLayout) findViewById(R.id.activity_login);
        register = (TextView) findViewById(R.id.register_btn);
        phoneerror = (TextView) findViewById(R.id.phoneloginerror);
        passworderror = (TextView) findViewById(R.id.passwordloginerror);
        view = new View(this);
        password_show = (ImageView) findViewById(R.id.password_show_btn);
        password_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paswword_show_flag == false) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    paswword_show_flag = true;
                    password_show.setImageResource(R.drawable.password_hide);
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    paswword_show_flag = false;
                    password_show.setImageResource(R.drawable.password_show);
                }
            }
        });
        sendlogin = new JSONObject();
        Intent intent = getIntent();
        String uname = intent.getStringExtra("uname");
        btn_login = (Button) findViewById(R.id.login);
        phoneno = (EditText) findViewById(R.id.phone_loginin);
        phoneno.setText(uname);
        password = (EditText) findViewById(R.id.password_loginin);
        activity_login.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                CommonMethod.hideSoftKeyboard(Login.this);
                return false;
            }
        });
        btn_login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgot_pass.setOnClickListener(this);

    }

    void loginbtnpressed() {
        if (Validation.checkEmptyfield(phone_s)) {
            phoneno.setError("Phone No cannot be empty");
            // phoneerror.setVisibility(View.VISIBLE);
        } else {
            phoneerror.setVisibility(View.GONE);
            if (Validation.checkEmptyfield(password_s)) {
                password.setError("Password No cannot be empty");
                // passworderror.setVisibility(View.VISIBLE);
            } else {
                passworderror.setVisibility(View.GONE);
                if (Validation.checkconnection(this)) {
                    showDailogue();
                    sendrequest();
                } else {
                    CommonMethod.showconnectiondialog(this, null);
                }
            }
        }

    }

    public void countycityrequest() {

        try {
            countyData = new ArrayList<>();
            cityData = new ArrayList<>();
            String response = SaveSharedPreference.getString(this, "all_county");
            JSONObject ja = new JSONObject(response);
            JSONArray countyArray = ja.getJSONArray("county");
            JSONArray cityArray = ja.getJSONArray("city");
            if (countyArray.length() > 0) {
                for (int i = 0; i < countyArray.length(); i++) {
                    JSONObject jo_countycity = countyArray.getJSONObject(i);
                    countyData.add(new CountryCityRSM(jo_countycity.getString("id"), jo_countycity.getString("name"), jo_countycity.getString("id_c")));

                }
            }

            if (cityArray.length() > 0) {
                for (int i = 0; i < cityArray.length(); i++) {
                    JSONObject jo_countycity = cityArray.getJSONObject(i);
                    cityData.add(new CountryCityRSM(jo_countycity.getString("id"), jo_countycity.getString("name"), jo_countycity.getString("id_c")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String findId(String county_string, ArrayList<CountryCityRSM> countyData) {

        String id = "";
        for (int i = 0; i < countyData.size(); i++) {
            if (county_string.equals(countyData.get(i).id)) {
                id = countyData.get(i).name;
                break;
            }
        }
        return id;
    }


    private void sendrequest() {
        String url = AppConstant.BASE_URL + "Login/checkdetails";
        StringRequest str = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Log.d("Response", response);
                dismissProgressDailogue();
                try {
                    JSONObject response_object = new JSONObject(response);
                    if (!response_object.getBoolean("success")) {
//                        Snackbar snackbar = Snackbar.make(activity_login, "This number is not registered", Snackbar.LENGTH_INDEFINITE)
//                                .setAction("Register", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        SaveSharedPreference.setPrefSyncContactFlag(getApplicationContext(), 1);
//                                        Intent intent = new Intent(Login.this, RegisterActivity.class);
//                                        startActivity(intent);
//                                    }
//                                });
//                        snackbar.show();
                        String message = response_object.getString("message");


                        if (!message.equals("Invalid phone number or password.")) {
                            showdailogbox(SaveSharedPreference.getPrefUserAccNo(Login.this), message);
                        } else
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    } else {
                        JSONArray ja = new JSONArray();
                        ja = response_object.getJSONArray("user_data");
                        JSONObject jo = (JSONObject) ja.get(0);
                        SaveSharedPreference.setUserName(getApplicationContext(), phone_s);
                        SaveSharedPreference.setPrefPhone(getApplicationContext(), phone_s);
                        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.no_photo);
                        ByteArrayOutputStream boa = new ByteArrayOutputStream();
                        largeIcon.compress(Bitmap.CompressFormat.PNG, 0, boa);
                        byte[] imagearr = boa.toByteArray();
                        String encoded = Base64.encodeToString(imagearr, Base64.DEFAULT);
                        SaveSharedPreference.setString(getApplicationContext(), "userAccount", jo.getString("matatu_account_number"));
                        SaveSharedPreference.setPrefName(getApplicationContext(), jo.getString("matatu_user_name"));
                        SaveSharedPreference.setPrefPhone(getApplicationContext(), phone_s);
                        SaveSharedPreference.setPrefNextofkin(getApplicationContext(), jo.getString("matatu_next_kin"));
                        //String county = rdb.getSpecific(jo.getInt("matatu_county"));
                        //String city = rdb.getSpecific(jo.getInt("matatu_city"));

                        SaveSharedPreference.setPrefCounty(getApplicationContext(), findId(jo.getInt("matatu_county") + "", countyData));
                        SaveSharedPreference.setPrefCity(getApplicationContext(), findId(jo.getInt("matatu_city") + "", cityData));
                        SaveSharedPreference.setPrefimage(getApplicationContext(), encoded);
                        SaveSharedPreference.setPrefPassword(getApplicationContext(), password_s);
                        Intent intent;
                        if (!response_object.getBoolean("sync")) {
                            intent = new Intent(Login.this, SyncContacts.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            intent = new Intent(Login.this, Home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error", error.toString());
                Toast.makeText(getApplicationContext(), "Server Error.\n Please Try Again later.", Toast.LENGTH_LONG).show();
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
                // Log.d("auth", headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone_login", phone_s);
                params.put("password_login", password_s);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                phone_s = phoneno.getText().toString().trim();
                password_s = password.getText().toString().trim();
                loginbtnpressed();
                break;
            case R.id.register_btn:
                Intent intent = new Intent(Login.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forgot_password:
                Intent intent1 = new Intent(Login.this, ForgotPasswordActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void showDailogue() {
        try {
            globalProgressDialogue = new GlobalProgressDialogue();
            globalProgressDialogue.show(getFragmentManager(), TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void dismissProgressDailogue() {
        try {
            if (globalProgressDialogue != null) {
                globalProgressDialogue.dismissAllowingStateLoss();
                globalProgressDialogue = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showdailogbox(final String acc, String message_s) {
        alertDialog = new Dialog(Login.this);
        alertDialog.setContentView(R.layout.register_dialog);
        TextView message = (TextView) alertDialog.findViewById(R.id.textView2);
        TextView hide = (TextView) alertDialog.findViewById(R.id.textView3);
        RelativeLayout rl = (RelativeLayout) alertDialog.findViewById(R.id.relativelayouthead);
        final TextView account_no = (TextView) alertDialog.findViewById(R.id.account_dialog_disp);
        hide.setVisibility(View.GONE);
        message.setText(message_s);
        Button next = (Button) alertDialog.findViewById(R.id.button_next_register);
        account_no.setText("Account No : " + acc.trim());
        next.setVisibility(View.GONE);
        alertDialog.show();
    }

}