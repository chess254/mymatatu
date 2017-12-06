package com.mymatatu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.mymatatu.VolleyEssentials.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Button next, next_otp, done;
    RelativeLayout part1, part2, part3, layout_forgetpassword;
    EditText phone, otp, password, confirm_password;
  //  TextView phoneerror, passworderror, confirmpassworderror, otperror;
   // String phone_s, otp_s, password_s, confirm_password_s;
    ImageView password_show, confirm_password_show;
    private boolean forgotFlag = false, passwordFlag = false;
    private static final String TAG = "Forgot";
    GlobalProgressDialogue globalProgressDialogue;
    boolean password_show_flag = false, confirm_password_show_id = false;
    String otp_reponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        this.setTitle("Forgot Password");
        getSupportActionBar().hide();
        layout_forgetpassword = (RelativeLayout) findViewById(R.id.layout_forgetpassword);
        part1 = (RelativeLayout) findViewById(R.id.forget_part1);
        part2 = (RelativeLayout) findViewById(R.id.forget_part2);
        part3 = (RelativeLayout) findViewById(R.id.forget_part3);
        next = (Button) findViewById(R.id.next);
        next_otp = (Button) findViewById(R.id.netx_otp);
        done = (Button) findViewById(R.id.forget_done);
        phone = (EditText) findViewById(R.id.phone_forget);
        otp = (EditText) findViewById(R.id.otp_forget);
        password = (EditText) findViewById(R.id.password_forget);
        confirm_password = (EditText) findViewById(R.id.confirm_password_forget);
       // phoneerror = (TextView) findViewById(R.id.phoneforgeterror);
       // otperror = (TextView) findViewById(R.id.optforgeterror);
       // passworderror = (TextView) findViewById(R.id.passworderror);
       // confirmpassworderror = (TextView) findViewById(R.id.confirmpassworderror);
        password_show = (ImageView) findViewById(R.id.password_show_btn);
        confirm_password_show = (ImageView) findViewById(R.id.confirm_password_show_btn);
        layout_forgetpassword.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                CommonMethod.hideSoftKeyboard(ForgotPasswordActivity.this);
                return false;
            }
        });
        next_otp.setOnClickListener(this);
        next.setOnClickListener(this);
        done.setOnClickListener(this);
        password_show.setOnClickListener(this);
        confirm_password_show.setOnClickListener(this);

        Selection.setSelection(phone.getText(), phone.getText().length());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (phone.getText().toString().isEmpty() || !Validation.checkphonenolength(phone.getText().toString()))
                    phone.setError("Phone number has to be 9 characters");
                else/* {
                    part1.setVisibility(View.GONE);
                    part2.setVisibility(View.VISIBLE);
                    forgotFlag = true;
                }
                //*/sendRequest_otp_call();

                /*phone_s = phone.getText().toString().trim();
                if (Validation.checkEmptyfield(phone_s)) {
                    phoneerror.setText("Phone no Cannot Be Empty");
                    phoneerror.setVisibility(View.VISIBLE);
                } else {
                    phoneerror.setVisibility(View.GONE);
                    if (Validation.checkphonenolength(phone_s)) {
                        //All True
                        sendRequest_otp_call();
                        part1.setVisibility(View.GONE);
                        part2.setVisibility(View.VISIBLE);
                    } else {
                        phoneerror.setText("Phone number has to be 9 characters");
                        phoneerror.setVisibility(View.VISIBLE);
                    }
                }*/

                break;
            case R.id.netx_otp:
                if (otp.getText().toString().isEmpty() || otp.getText().toString().length() != 6)
                    otp.setError("Enter 6 digit otp");
                else/* {

                    part2.setVisibility(View.GONE);
                    part3.setVisibility(View.VISIBLE);
                    passwordFlag = true;
                }//*/sendVerfy_otp_call();
                /*otp_s = otp.getText().toString().trim();
                if (otp_s.compareTo("123456") == 0) {
                    otperror.setVisibility(View.GONE);
                    part2.setVisibility(View.GONE);
                    part3.setVisibility(View.VISIBLE);
                } else {
                    otperror.setText("Otp doesnot match Please try again");
                    otperror.setVisibility(View.VISIBLE);
                }*/
                break;
            case R.id.forget_done:
                if (!password.getText().toString().equals(confirm_password.getText().toString())) {
                    confirm_password.setError("Password and confirm password is not same");
                    password.setError("Password and confirm password is not same");
                } else
                    changePasswordRequest();
              /*  confirm_password_s = confirm_password.getText().toString().trim();
                password_s = password.getText().toString().trim();
                if (Validation.checkEmptyfield(password_s)) {
                    passworderror.setText("Password Cannot be Empty");
                    passworderror.setVisibility(View.VISIBLE);
                } else {
                    passworderror.setVisibility(View.GONE);
                    if (Validation.checkPassWordLenth(password_s)) {
                        passworderror.setText("Your passsword needs to be alteast 6 Characters long");
                        passworderror.setVisibility(View.VISIBLE);
                    } else {
                        passworderror.setVisibility(View.GONE);
                        if (Validation.checkEmptyfield(confirm_password_s)) {
                            confirmpassworderror.setText("Confirm Password Cannot be Empty");
                            confirmpassworderror.setVisibility(View.VISIBLE);
                        } else {
                            confirmpassworderror.setVisibility(View.GONE);
                            if (Validation.checkPassWordMatch(password_s, confirm_password_s)) {
                                passworderror.setVisibility(View.GONE);
                                //True Condition

                                if (Validation.checkconnection(this)) {
                                    sendrequest_final_password();
                                } else {
                                    Intent intent = new Intent(ForgotPasswordActivity.this, No_internet1.class);
                                    startActivity(intent);
                                }
                            } else {
                                passworderror.setText("Password Doesnot Match");
                                passworderror.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }*/

                break;
            case R.id.password_show_btn:
                if (password_show_flag == false) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password_show_flag = true;
                    password_show.setImageResource(R.drawable.password_hide);
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password_show_flag = false;
                    password_show.setImageResource(R.drawable.password_show);
                }
                break;

            case R.id.confirm_password_show_btn:
                if (confirm_password_show_id == false) {
                    confirm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirm_password_show_id = true;
                    confirm_password_show.setImageResource(R.drawable.password_hide);
                } else {
                    confirm_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirm_password_show_id = false;
                    confirm_password_show.setImageResource(R.drawable.password_show);
                }
                break;
        }
    }

    private void sendrequest_final_password() {
        Intent intent = new Intent(ForgotPasswordActivity.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void sendRequest_otp_call() {

        showDailogue();
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.BASE_URL + "Matatuotp/generator"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDailogue();
                try {
                    JSONObject response_object = new JSONObject(response);
                    if (response_object.getBoolean("success")) {
                        forgotFlag = true;
                        part1.setVisibility(View.GONE);
                        part2.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, response_object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDailogue();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = ForgotPasswordActivity.this.getString(R.string.api_id) +
                        ":" + ForgotPasswordActivity.this.getString(R.string.api_password);
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
                //Log.d("auth", headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone_no", phone.getText().toString());
                return params;
            }


        };
        MySingleton.getInstance(this).addToRequestQueue(request);
        //return return_arr;

    }


    private void sendVerfy_otp_call() {

        showDailogue();
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.BASE_URL + "Matatuotp/checkotp"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDailogue();
                try {
                    JSONObject response_object = new JSONObject(response);
                    if (response_object.getBoolean("success")) {
                        passwordFlag = true;
                        part2.setVisibility(View.GONE);
                        part3.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, response_object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDailogue();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = ForgotPasswordActivity.this.getString(R.string.api_id) +
                        ":" + ForgotPasswordActivity.this.getString(R.string.api_password);
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
              //  Log.d("auth", headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone_no", phone.getText().toString());
                params.put("otp", otp.getText().toString());
                return params;
            }


        };
        MySingleton.getInstance(this).addToRequestQueue(request);
        //return return_arr;

    }


    private void changePasswordRequest() {

        showDailogue();
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.BASE_URL + "Matatuotp/passwordchange"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDailogue();
                try {
                    JSONObject response_object = new JSONObject(response);
                    if (response_object.getBoolean("success")) {
                        Toast.makeText(ForgotPasswordActivity.this, response_object.getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, response_object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDailogue();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = ForgotPasswordActivity.this.getString(R.string.api_id) +
                        ":" + ForgotPasswordActivity.this.getString(R.string.api_password);
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
               // Log.d("auth", headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone_no", phone.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }


        };
        MySingleton.getInstance(this).addToRequestQueue(request);
        //return return_arr;

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

    @Override
    public void onBackPressed() {
        if (passwordFlag) {
            part2.setVisibility(View.VISIBLE);
            part3.setVisibility(View.GONE);
            passwordFlag = false;
        } else if (forgotFlag) {
            part1.setVisibility(View.VISIBLE);
            part2.setVisibility(View.GONE);
            forgotFlag = false;
        } else
            super.onBackPressed();
    }
}
