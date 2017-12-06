package com.mymatatu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.AppConstants;
import com.mymatatu.App_Importants.CommonMethod;
import com.mymatatu.App_Importants.Validation;
import com.mymatatu.Background.CancelBooking;
import com.mymatatu.Background.GetBalance;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.VolleyEssentials.MySingleton;
import com.mymatatu.model.CountryCityRSM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.DKGRAY;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.RED;

/**
 * Created by anonymous on 05-07-2017.
 */

public class PaymentConfirm extends Fragment {
    GetBalance getBalance;
    int total_seats_no;
    long timeleft;
    private Button cancel, done;
    TextView tv;
    int s, d;
    private TextView to, from, sacco_name, matatu_no, seats, seat_no, price, total_cost, wallet_balance, error;
    String id, to_s, from_s, sacco_name_s, matatu_no_s, seats_s = "", seat_no_s, price_s, total_cost_s, wallet_balance_s, m_id, sapc_id;
    CheckBox insurance;
    boolean insurance_flag = false;
    public CountDownTimer ct;
    private ArrayList<Integer> seatnos = new ArrayList<>();
    private JSONArray ja_send = new JSONArray();
    private JSONObject jo_send = new JSONObject();
    android.app.FragmentManager fm;
    private ArrayList<CountryCityRSM> countyData, cityData;
    Home ho;
    String screen = "";
    private Dialog alertDialog;
    private Dialog confirmpaymentDialog;
    CancelBooking cb;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.payment_confirm_fragment, container, false);
        getActivity().setTitle("Confirm Your Payment");
        countycityrequest();
        getBalance = new GetBalance(getActivity());
        getBalance.checkbalance(getActivity(), PaymentConfirm.this);
        cb = new CancelBooking(getActivity());
        Bundle bundle = getArguments();
        ho = (Home) getActivity();
        //RegionDatabaseHelper rdb = new RegionDatabaseHelper(getActivity(), null, null, 1);
        //PaymentConfirm paymentConfirmobject = new PaymentConfirm();
        done = (Button) v.findViewById(R.id.confirm);
        cancel = (Button) v.findViewById(R.id.cancel);
        timeleft = bundle.getLong("timer");
        if (bundle.getString("screen") != null && !bundle.getString("screen").isEmpty()) {
            screen = bundle.getString("screen");
        } else {
            screen = "no";
        }
        if (!screen.equals("seating")) {
            fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.main_frame, new Route_Selection()).commit();
        } else {
            //Initialization
            init(v);


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ct.onFinish();
                    cb.sendCancelRequest();
                    ho.endTimer();
                    Intent intent = new Intent(getActivity(), Home.class);
                    startActivity(intent);
                }
            });

            //finalize data
            id = bundle.getString("id");
            seatnos = bundle.getIntegerArrayList("seatnos");
            total_seats_no = seatnos.size();
            from_s = SaveSharedPreference.getSource(getActivity());
            to_s = SaveSharedPreference.getDestination(getActivity());
            matatu_no_s = bundle.getString("matatu_name");
            sacco_name_s = bundle.getString("sacco_name");
            price_s = bundle.getString("price");
            m_id = bundle.getString("m_id");
            sapc_id = bundle.getString("sacp_id");
            s = Integer.parseInt(findId(from_s, cityData)); //rdb.getCityId(from_s.trim());
            d = Integer.parseInt(findId(to_s, cityData));// rdb.getCityId(to_s.trim());
            seat_no_s = String.valueOf(total_seats_no);
            for (int i = 0; i < seatnos.size(); i++) {
                seats_s = seats_s + seatnos.get(i).toString() + "\t,";
            }
            if (seats_s.endsWith(",")) {
                seats_s = seats_s.substring(0, seats_s.length() - 1);
            }
            total_cost_s = String.valueOf(Integer.parseInt(price_s.trim()) * total_seats_no);
            if (Validation.checkcost(SaveSharedPreference.getBalance(getActivity()), Integer.parseInt(total_cost_s.trim()))) {
                setdata(true);
            } else {
                wallet_balance_s = String.valueOf(SaveSharedPreference.getBalance(getActivity()));
                setdata(false);
            }
            insurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (insurance.isChecked()) {
                        total_cost_s = String.valueOf(Integer.parseInt(total_cost_s) + (70 * seatnos.size()));
                        insurance_flag = true;
                        if (Validation.checkcost(SaveSharedPreference.getBalance(getActivity()), Integer.parseInt(total_cost_s.trim()))) {
                            setdata(true);
                        } else {
                            wallet_balance_s = String.valueOf(SaveSharedPreference.getBalance(getActivity()));
                            setdata(false);
                        }

                    } else {
                        total_cost_s = String.valueOf(Integer.parseInt(total_cost_s) - (70 * seatnos.size()));
                        insurance_flag = false;
                        setdata(true);

                    }
                }
            });
            //Send the data
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Make the server call here to send the data
                    //send ja_send

                    if (!Validation.checkcost(SaveSharedPreference.getBalance(getActivity()), Integer.parseInt(total_cost_s.trim()))) {
                        wallet_balance_s = String.valueOf(SaveSharedPreference.getBalance(getActivity()));
                        showdailogbox();

                    } else {
                        wallet_balance_s = String.valueOf(SaveSharedPreference.getBalance(getActivity()));
                        showalertdialog(getActivity());
                    }

                }


            });


        }
        return v;
    }

    public void countycityrequest() {

        try {
            countyData = new ArrayList<>();
            cityData = new ArrayList<>();
            String response = SaveSharedPreference.getString(getActivity(), "all_county");
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
            if (county_string.equals(countyData.get(i).name)) {
                id = countyData.get(i).id;
                break;
            }
        }
        return id;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            ct.cancel();
        }
    }

    private void sendrequest() {
        StringRequest request = new StringRequest(Request.Method.POST, AppConstants.BASE_URL + "Passenger/bookpassengerseat",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("seats", response);
                        try {
                            JSONObject respose_object = new JSONObject(response);
                            if (respose_object.getBoolean("success")) {
                                id = respose_object.getString("booking_id");
                                if (insurance_flag == true) {

                                }
                                makejson();
                                fm = getFragmentManager();
                                Bundle bundle = new Bundle();
                                bundle.putString("json_data_final", ja_send.toString());
                                Payment_done pd = new Payment_done();
                                pd.setArguments(bundle);
                                fm.beginTransaction().replace(R.id.main_frame, pd)
                                        .addToBackStack("payment-done").commit();
                            } else {
                                Toast.makeText(getActivity(), respose_object.getString("message"), Toast.LENGTH_LONG).show();
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
                String credentials = getActivity().getString(R.string.api_id) +
                        ":" + getActivity().getString(R.string.api_password);
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
                params.put("from", String.valueOf(s));
                params.put("to", String.valueOf(d));
                params.put("sacco_name", sacco_name_s);
                params.put("matatu_number", matatu_no_s);
                params.put("seats_total", String.valueOf(total_seats_no));
                params.put("seats_no", seats_s);
                params.put("price", price_s);
                params.put("total_cost", total_cost_s);
                params.put("m_id", m_id);
                params.put("sacp_id", sapc_id);
                params.put("phone", SaveSharedPreference.getPhone(getActivity()));
                params.put("s_id", id);
                if (insurance_flag == true) {
                    params.put("insurance_value", String.valueOf(1));
                } else {
                    params.put("insurance_value", String.valueOf(0));
                }
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    private void makejson() {

        try {
            jo_send.put("booking_id", id);
            jo_send.put("to", to_s);
            jo_send.put("from", from_s);
            jo_send.put("sacco_name", sacco_name_s);
            jo_send.put("matatu_number", matatu_no_s);
            jo_send.put("seats_total", seats_s);
            jo_send.put("seats_no", seat_no_s);
            jo_send.put("price", price_s);
            jo_send.put("total_cost", total_cost_s);
            jo_send.put("m_id", m_id);
            jo_send.put("sacp_id", sapc_id);
            jo_send.put("phone_no", SaveSharedPreference.getPhone(getActivity()));
            jo_send.put("s_id", id);
            if (insurance_flag == true) {
                jo_send.put("insurance", "Yes");
                jo_send.put("insurance_value", 1);
            } else {
                jo_send.put("insurance", "No");
                jo_send.put("insurance_value", 0);
            }
            ja_send.put(jo_send);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setdata(boolean flag) {
        to.setText(to_s);
        from.setText(from_s);
        sacco_name.setText(sacco_name_s);
        matatu_no.setText(matatu_no_s);
        seats.setText(seats_s);
        seat_no.setText(seat_no_s);
        price.setText(price_s);
        total_cost.setText(total_cost_s);
        wallet_balance_s = String.valueOf(SaveSharedPreference.getBalance(getActivity()));
        wallet_balance.setText(wallet_balance_s);
        if (Integer.parseInt(total_cost.getText().toString()) < Integer.parseInt(wallet_balance_s)) {
            wallet_balance.setTextColor(DKGRAY);
        } else {
            wallet_balance.setTextColor(RED);
        }
    }


    //Handling BAck pressed Events
    public void backbuttonpressed() {
        ct.onFinish();
        ct.cancel();
    }

    public void init(View v) {
        insurance = (CheckBox) v.findViewById(R.id.insurance_checkbox);
        to = (TextView) v.findViewById(R.id.to_data);
        from = (TextView) v.findViewById(R.id.from_data);
        sacco_name = (TextView) v.findViewById(R.id.sacco_name_data);
        matatu_no = (TextView) v.findViewById(R.id.matatu_no_data);
        seats = (TextView) v.findViewById(R.id.total_seats_no_data);
        seat_no = (TextView) v.findViewById(R.id.seat_no_data);
        price = (TextView) v.findViewById(R.id.price_data);
        total_cost = (TextView) v.findViewById(R.id.total_cost_data);
        wallet_balance = (TextView) v.findViewById(R.id.wallet_balance_data);
        tv = (TextView) v.findViewById(R.id.timer);

        done = (Button) v.findViewById(R.id.confirm);
    }

    public void timerShow(long milliseconds) {
        tv.setText("TIME LEFT : " + milliseconds + " seconds");

    }

    private void showdailogbox() {
        alertDialog = new Dialog(getActivity());
        alertDialog.setContentView(R.layout.refil_wallet_dialog);
        final TextView account_no = (TextView) alertDialog.findViewById(R.id.account_dialog_disp);
        TextView amount = (TextView) alertDialog.findViewById(R.id.amount_dialog_disp);
        int remaining = Integer.parseInt(total_cost_s) - SaveSharedPreference.getBalance(getActivity());
        amount.setText("Please Refill Your wallet for " + remaining + "KSh");
        Button next = (Button) alertDialog.findViewById(R.id.button_next_register);
        account_no.setText("Account No : " + SaveSharedPreference.getString(getActivity(),"userAccount"));
        next.setText("Refresh");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBalance = new GetBalance(getActivity());
                getBalance.checkbalance(getActivity(), PaymentConfirm.this);//200mil
                //setdata(true);
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void showconfirmdialog() {
        confirmpaymentDialog = new Dialog(getActivity());
        confirmpaymentDialog.setContentView(R.layout.paymentconfirm_dialog);
        Button confirm = (Button) confirmpaymentDialog.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makejson();
                ho.endTimer();
                sendrequest();
                confirmpaymentDialog.cancel();
            }
        });
        confirmpaymentDialog.show();

    }


    private void showalertdialog(Context context) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Payment")
                .setMessage("Are your sure you want to proceed ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (Validation.checkconnection(getActivity())) {
                            makejson();
                            ho.endTimer();
                            sendrequest();
                        } else {
                            CommonMethod.showconnectiondialog(getActivity(), PaymentConfirm.this);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .show();
    }

}
