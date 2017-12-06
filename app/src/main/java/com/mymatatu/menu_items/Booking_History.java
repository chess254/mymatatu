package com.mymatatu.menu_items;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.CommonMethod;
import com.mymatatu.App_Importants.Validation;
import com.mymatatu.CardView_Picking.booking_rv_adapter;
import com.mymatatu.CustomDataTypes.Bookingdata;
import com.mymatatu.DataBaseHelper.Booking_transaction_db;
import com.mymatatu.Global.AppConstant;
import com.mymatatu.Global.GlobalProgressDialogue;
import com.mymatatu.Home;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.R;
import com.mymatatu.VolleyEssentials.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by anonymous on 20-07-2017.
 */

public class Booking_History extends Fragment {
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutmanager;
   private RecyclerView.Adapter adapter;
    private ArrayList<Bookingdata> data;
    SwipeRefreshLayout swipeRefreshLayout;
    private GlobalProgressDialogue globalProgressDialogue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Home ho = (Home) getActivity();
        if (ho.timerflag == true) {
            ho.endTimer();
        }
        View v = inflater.inflate(R.layout.booking_history_menuitem, container, false);
        getActivity().setTitle("My Bookings");

        data = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        rv = (RecyclerView) v.findViewById(R.id.booking_rv);
        callAPi();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data = new ArrayList<>();
                callAPi();
            }
        });

//        bdb = new Booking_transaction_db(getActivity(),null,null,1);
//        data = bdb.showtable2();
//

        return v;
    }

    public void callAPi() {
        if (Validation.checkconnection(getActivity()))
            sendRequest();
        else
            CommonMethod.showconnectiondialog(getActivity(), Booking_History.this);
    }

    void sendRequest() {
        showDailogue();
        StringRequest str = new StringRequest(Request.Method.POST, AppConstant.BASE_URL + "Passenger/bookinghistory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo_response = new JSONObject(response);
                    JSONArray ja_data = new JSONArray();
                    dismissProgressDailogue();
                    if (jo_response.getBoolean("success")) {
                      //  Log.d("response", jo_response.toString());
                        ja_data = jo_response.getJSONArray("all_data");
                        for (int i = 0; i < ja_data.length(); i++) {
                            Bookingdata bd = new Bookingdata();
                            JSONObject jo = new JSONObject();
                            jo = (JSONObject) ja_data.get(i);
                            bd.date = jo.getString("Travel_date");
                            bd.id = jo.getString("Book_id");
                            bd.to_s = jo.getString("destination");
                            bd.from_s = jo.getString("source");
                            bd.sacco_name = jo.getString("sacco_name");
                            bd.matatu_name = jo.getString("matatu_name");
                            bd.seats = jo.getString("seat_booked_id");
                            bd.total_seats = jo.getString("total_seats_booked");
                            bd.total_cost = jo.getString("total_amount");
                            if (jo.getString("passenger_insurance").trim().compareToIgnoreCase("1") == 0) {
                                bd.insurance = "Yes";
                            } else {
                                bd.insurance = "No";
                            }
                            data.add(bd);

                            layoutmanager = new LinearLayoutManager(getActivity());
                            rv.setLayoutManager(layoutmanager);
                            Collections.reverse(data);
                            adapter = new booking_rv_adapter(getActivity(), data);
                            rv.setAdapter(adapter);
                        }
                    } else {
                        Toast.makeText(getActivity(), jo_response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDailogue();
                Toast.makeText(getActivity(), "Server Error : " + error.toString(), Toast.LENGTH_SHORT).show();
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
            //    Log.d("auth", headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone_no", SaveSharedPreference.getPhone(getActivity()));
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(str);
        ;
        swipeRefreshLayout.setRefreshing(false);

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

}
