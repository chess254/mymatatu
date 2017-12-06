package com.mymatatu;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class RegisterActivity extends AppCompatActivity {
    GlobalProgressDialogue globalProgressDialogue;
    public static String TAG = RegisterActivity.class.getSimpleName();
    private Button register;
    AutoCompleteTextView county, city, rsm;
    private EditText name, phone, password/*, country, city*/, c_password, nextofkin;
    String name_s, phone_s, password_s, country_s, city_s, c_password_s, nextofkin_s;
    int countyint, cityint;
    private Dialog alertDialog;
    ImageView password_show, confirm_password_show;
    boolean password_show_flag = false, confirm_password_show_id = false;
    private TextView nameerror, phoneerror, passworderror, passwordconfirmerror, countyerror, cityerror, nextofkinerror;
    View v;
    TextView login_btn;
    String statusno = "", acc_no = "";
    RelativeLayout activity_main;
    ArrayList<CountryCityRSM> countyData, cityData, rsmData;
    ArrayList<String> county_string_list = new ArrayList<>();
    ArrayList<String> rsm_list = new ArrayList<>();
    ArrayList<String> citys_string_list = new ArrayList<>();
    private ArrayAdapter adapter, adaptercity, adapter3;
    private String countyId, cityId, rsmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        this.deleteDatabase("profile.db");
        name = (EditText) findViewById(R.id.name_in);
        phone = (EditText) findViewById(R.id.phone_in);
        login_btn = (TextView) findViewById(R.id.login_btn);
        password = (EditText) findViewById(R.id.password_in);
        c_password = (EditText) findViewById(R.id.confirm_password_in);
        nextofkin = (EditText) findViewById(R.id.nextofkin_in);
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        nameerror = (TextView) findViewById(R.id.nameerror);
        phoneerror = (TextView) findViewById(R.id.phoneerror);
        passworderror = (TextView) findViewById(R.id.passworderror);
        passwordconfirmerror = (TextView) findViewById(R.id.passwordcomfirmerror);
        countyerror = (TextView) findViewById(R.id.countyerror);
        cityerror = (TextView) findViewById(R.id.cityerror);
        nextofkinerror = (TextView) findViewById(R.id.nextofkinerror);
        register = (Button) findViewById(R.id.register_btn);
        password_show = (ImageView) findViewById(R.id.password_show_btn);
        confirm_password_show = (ImageView) findViewById(R.id.confirm_password_show_btn);
        ///Auto complete Code



       // county_string_list = rdb.getCounty();
       // citys_string_list = rdb.getCity();
        county = (AutoCompleteTextView) findViewById(R.id.etcounty);
        rsm = (AutoCompleteTextView) findViewById(R.id.rsm);
        adapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_1, county_string_list);
        county.setAdapter(adapter);
        county.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                county.showDropDown();
            }
        });
        city = (AutoCompleteTextView) findViewById(R.id.etcity);
        adaptercity = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_1, citys_string_list);
        city.setAdapter(adaptercity);
        city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                city.showDropDown();
            }
        });

        rsm_list.add("Junior manager");
        rsm_list.add("Senior manager");
        rsm_list.add("Supervisor");


        adapter3 = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_1, rsm_list);
        rsm.setAdapter(adapter3);
        rsm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                rsm.showDropDown();
            }
        });
        ///AutoComplete Done


        //Toggle Password Show
        password_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password_show_flag == false) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password_show_flag = true;
                    password_show.setImageResource(R.drawable.password_hide);
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password_show_flag = false;
                    password_show.setImageResource(R.drawable.password_show);
                }
            }
        });
        confirm_password_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirm_password_show_id == false) {
                    c_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirm_password_show_id = true;
                    confirm_password_show.setImageResource(R.drawable.password_hide);
                } else {
                    c_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirm_password_show_id = false;
                    confirm_password_show.setImageResource(R.drawable.password_show);
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                registerButtonPressed();
            }

        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, Login.class);
                startActivity(i);
            }
        });
        activity_main.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                CommonMethod.hideSoftKeyboard(RegisterActivity.this);
                return false;
            }
        });

        countycityrequest();

    }


    void registerButtonPressed() {
       /* name_s = name.getText().toString().trim();
        phone_s = phone.getText().toString().trim();
        country_s = county.getText().toString().trim();
        city_s = city.getText().toString().trim();
        password_s = password.getText().toString().trim();
        c_password_s = c_password.getText().toString().trim();
        nextofkin_s = nextofkin.getText().toString().trim();*/


        if (name.getText().toString().isEmpty())
            name.setError("Name Cannot be empty");
        else if (phone.getText().toString().isEmpty())
            phone.setError("Phone Cannot be empty");
        else if (password.getText().toString().isEmpty())
            password.setError("Password Cannot be empty");
        else if (password.getText().toString().length() < 6)
            password.setError("Password Has to be min 6 characters");
        else if (!c_password.getText().toString().equals(password.getText().toString()))
            c_password.setError("Both password is not same");
        else if (nextofkin.getText().toString().isEmpty())
            nextofkin.setError("Next Of Kin Cannot be empty");
        else if (county.getText().toString().isEmpty())
            county.setError("County Cannot be empty");
        else if (!county_string_list.contains(county.getText().toString()))
            county.setError("County is not present in our system");
        else if (city.getText().toString().isEmpty())
            city.setError("City Cannot be empty");
        else if (!citys_string_list.contains(city.getText().toString()))
            county.setError("City is not present in our system");
        else if (rsm.getText().toString().isEmpty())
            rsm.setError("RSM Cannot be empty");
        else if (!rsm_list.contains(rsm.getText().toString()))
            rsm.setError("RSM is not present in our system");
        else {
            if (Validation.checkconnection(this)) {
                countyId = findId(county.getText().toString(), countyData);
                cityId = findId(city.getText().toString(), cityData);
                rsmId = findId(rsm.getText().toString(), rsmData);
                sendRegisterRequest();

            } else {
                CommonMethod.showconnectiondialog(RegisterActivity.this, null);
            }



        /*if (Validation.checkEmptyfield(name_s)) {
            nameerror.setText("Name Cannot be empty");
            nameerror.setVisibility(View.VISIBLE);
        } else {
            nameerror.setVisibility(View.GONE);
            if (Validation.checkEmptyfield(phone_s)) {
                phoneerror.setText("Phone No Cannot be empty");
                phoneerror.setVisibility(View.VISIBLE);
            } else {
                phoneerror.setVisibility(View.GONE);
                if (Validation.checkEmptyfield(password_s)) {
                    passworderror.setText("Password Cannot be empty");
                    passworderror.setVisibility(View.VISIBLE);
                } else {
                    passworderror.setVisibility(View.GONE);
                    if (Validation.checkPassWordLenth(password_s)) {
                        passworderror.setText("Password Has to be min 6 characters");
                        passworderror.setVisibility(View.VISIBLE);
                    } else {
                        passworderror.setVisibility(View.GONE);
                        if (Validation.checkEmptyfield(c_password_s)) {
                            passwordconfirmerror.setText("Confirm Password Cannot be empty");
                            passwordconfirmerror.setVisibility(View.VISIBLE);
                        } else {
                            passwordconfirmerror.setVisibility(View.GONE);
                            if (!Validation.checkPassWordMatch(password_s, c_password_s)) {
                                passwordconfirmerror.setText("Confirm Password doesnot match");
                                passwordconfirmerror.setVisibility(View.VISIBLE);
                            } else {
                                passwordconfirmerror.setVisibility(View.GONE);
                                if (Validation.checkEmptyfield(nextofkin_s)) {
                                    nextofkinerror.setText("Next Of Kin Cannot be empty");
                                    nextofkinerror.setVisibility(View.VISIBLE);
                                } else {
                                    nextofkinerror.setVisibility(View.GONE);
                                    if (Validation.checkEmptyfield(country_s)) {
                                        countyerror.setText("County Cannot be empty");
                                        countyerror.setVisibility(View.VISIBLE);
                                    } else {
                                        countyerror.setVisibility(View.GONE);
                                        if (Validation.checkEmptyfield(city_s)) {
                                            cityerror.setText("City Cannot be empty");
                                            cityerror.setVisibility(View.VISIBLE);
                                        } else {
                                            cityerror.setVisibility(View.GONE);
                                            if (Validation.checkconnection(this)) {
                                                countyint = rdb.getCityId(country_s);
                                                cityint = rdb.getCityId(city_s);
                                                sendRegisterRequest();
                                                showDailogue();
                                            } else {
                                                CommonMethod.showconnectiondialog(RegisterActivity.this, null);
                                            }
                                        }

                                    }


                                }


                            }
                        }


                    }

                }
            }*/

        }


    }

    private String findId(String county_string, ArrayList<CountryCityRSM> countyData) {

        String id = "";
        for (int i = 0; i < countyData.size(); i++) {
            if (county_string.equals(countyData.get(i).name)) {
                id = countyData.get(i).id;
                break;
            }
        }
        return id;
    }


    void sendRegisterRequest() {
        showDailogue();
        String send_url = AppConstant.BASE_URL + "Passenger/addpassenger";
        StringRequest str_send = new StringRequest(Request.Method.POST, send_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Log.d("q", response);
                        dismissProgressDailogue();
                        try {
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = (JSONObject) ja.get(0);
                            acc_no = jo.getString("matatu_account_number");

                            if (acc_no.compareToIgnoreCase("1") == 0) {
                                //  Log.d("","1");
                                Snackbar snackbar = Snackbar.make(activity_main, "This number is already registered", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("LOGIN", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(RegisterActivity.this, Login.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                snackbar.show();

                            } else {
                                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.no_photo);
                                ByteArrayOutputStream boa = new ByteArrayOutputStream();
                                largeIcon.compress(Bitmap.CompressFormat.PNG, 0, boa);
                                byte[] imagearr = boa.toByteArray();
                                String encoded = Base64.encodeToString(imagearr, Base64.DEFAULT);
                                SaveSharedPreference.setPrefName(getApplicationContext(), name_s);
                                SaveSharedPreference.setPrefPhone(getApplicationContext(), phone_s);
                                SaveSharedPreference.setPrefNextofkin(getApplicationContext(), nextofkin_s);
                                SaveSharedPreference.setPrefCounty(getApplicationContext(), country_s);
                                SaveSharedPreference.setPrefCity(getApplicationContext(), city_s);
                                SaveSharedPreference.setPrefimage(getApplicationContext(), encoded);
                                SaveSharedPreference.setPrefPassword(getApplicationContext(), password_s);
                                SaveSharedPreference.setPrefUserAccNo(getApplicationContext(), acc_no);
//                                profile_data pd = new profile_data(getApplicationContext(), null, null, 1);
//                                pd.add(name_s, phone_s, country_s, city_s, imagearr);
                                showdailogbox(acc_no);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDailogue();
              //  Log.e("Volley Error", error.toString());
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
           //     Log.d("auth", headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("password", password.getText().toString());
                params.put("county", countyId);
                params.put("city", cityId);
                params.put("rsm", rsmId);
                params.put("next_of_kin", nextofkin.getText().toString());
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(str_send);

    }

    private void showdailogbox(final String acc) {
        alertDialog = new Dialog(RegisterActivity.this);
        alertDialog.setContentView(R.layout.register_dialog);
        final TextView account_no = (TextView) alertDialog.findViewById(R.id.account_dialog_disp);
        Button next = (Button) alertDialog.findViewById(R.id.button_next_register);
        alertDialog.setCancelable(false);
        account_no.setText("Account No : " + acc.trim());
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, Login.class);
                intent.putExtra("uname", phone_s);
                intent.putExtra("account_no", acc.trim());
                startActivity(intent);
                alertDialog.cancel();
            }
        });
        alertDialog.show();
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

    public void countycityrequest() {
        try {

            county_string_list.clear();
            citys_string_list.clear();
            rsm_list.clear();
            countyData = new ArrayList<>();
            rsmData = new ArrayList<>();
            cityData = new ArrayList<>();
            String response = SaveSharedPreference.getString(this, "all_county");
            JSONObject ja = new JSONObject(response);
            JSONArray countyArray = ja.getJSONArray("county");
            JSONArray cityArray = ja.getJSONArray("city");
            JSONArray rsmArray = ja.getJSONArray("rsm");
            if (countyArray.length() > 0) {
                for (int i = 0; i < countyArray.length(); i++) {
                    JSONObject jo_countycity = countyArray.getJSONObject(i);
                    countyData.add(new CountryCityRSM(jo_countycity.getString("id"), jo_countycity.getString("name"), jo_countycity.getString("id_c")));
                    county_string_list.add(jo_countycity.getString("name"));
                }
                adapter.notifyDataSetChanged();
            }

            if (cityArray.length() > 0) {
                for (int i = 0; i < cityArray.length(); i++) {
                    JSONObject jo_countycity = cityArray.getJSONObject(i);
                    cityData.add(new CountryCityRSM(jo_countycity.getString("id"), jo_countycity.getString("name"), jo_countycity.getString("id_c")));
                    citys_string_list.add(jo_countycity.getString("name"));
                }
                adaptercity.notifyDataSetChanged();
            }

            if (rsmArray.length() > 0) {
                for (int i = 0; i < rsmArray.length(); i++) {
                    JSONObject jo_countycity = rsmArray.getJSONObject(i);
                    rsmData.add(new CountryCityRSM(jo_countycity.getString("id"), jo_countycity.getString("name")));
                    rsm_list.add(jo_countycity.getString("name"));
                }
                adapter3.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
