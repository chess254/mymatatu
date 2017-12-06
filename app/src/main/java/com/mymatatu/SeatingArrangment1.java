package com.mymatatu;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mymatatu.App_Importants.CommonMethod;
import com.mymatatu.App_Importants.Validation;
import com.mymatatu.App_Importants.ViewPagereAdapter;
import com.mymatatu.Global.AppConstant;
import com.mymatatu.Global.GlobalProgressDialogue;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.VolleyEssentials.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anonymous on 04-07-2017.
 */

public class SeatingArrangment1 extends Fragment implements View.OnClickListener {
    private TextView seat_info;
    Button cancel, done;
    String print = "";
    String s_id, matatu_name, sacco_name, price, m_id, sapc_id, total_seats;
    ArrayList<Integer> seatno;
    ToggleButton tb[] = new ToggleButton[11];
    int counter = 0, type;
    public TextView tv1;
    TextView headername, headerroute, headerseats, headercost;
    long timeleft;
    String screen = "";
    android.app.FragmentManager fm;
    CountDownTimer ct;
    int i[] = new int[11];
    int temp[] = new int[11];
    private int selected[] = {R.drawable.gseat1,
            R.drawable.gseat2,
            R.drawable.gseat3,
            R.drawable.gseat4,
            R.drawable.gseat5,
            R.drawable.gseat6,
            R.drawable.gseat7,
            R.drawable.gseat8,
            R.drawable.gseat9,
            R.drawable.gseat10,
            R.drawable.gseat11};

    private int unSelected[] = {R.drawable.wseat1,
            R.drawable.wseat2,
            R.drawable.wseat3,
            R.drawable.wseat4,
            R.drawable.wseat5,
            R.drawable.wseat6,
            R.drawable.wseat7,
            R.drawable.wseat8,
            R.drawable.wseat9,
            R.drawable.wseat10,
            R.drawable.wseat11};

    private static final String TAG = "SeatingArragments";
    GlobalProgressDialogue globalProgressDialogue;
    private Button mButton1, mButton2, mButton3;
    private BottomSheetBehavior mBottomSheetBehavior1, mBottomSheetBehavior2;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.seating_model1, container, false);
        Bundle bundle = getArguments();
        seatno = new ArrayList<>();

        //Get All the data

        screen = bundle.getString("screen");
        Log.i("String", screen);
        getActivity().setTitle("Select Your Seat");
        seat_info = (TextView) v.findViewById(R.id.seat_info);
        String id = bundle.getString("id");
        init(v);
        s_id = bundle.getString("id");
        matatu_name = bundle.getString("matatu_name");
        sacco_name = bundle.getString("sacco_name");
        price = bundle.getString("price");
        m_id = bundle.getString("matatu_id");
        sapc_id = bundle.getString("sapc_id");
        total_seats = bundle.getString("seats");
        tv1 = (TextView) v.findViewById(R.id.timer);
        String headerroute_s = SaveSharedPreference.getSource(getActivity()) +
                " to " +
                SaveSharedPreference.getDestination(getActivity());
        headername.setText("Matatu Number : " + matatu_name);
        headerroute.setText(headerroute_s);
        headerseats.setText("Available Seats : " + total_seats);
        headercost.setText("Cost : " + price + " KSh.");
        //showDailogue();
        if (Validation.checkconnection(getActivity())) {
            sendintiailrequest();
            //Timer part

            Home ho = (Home) getActivity();
            if (ho.timerflag == false) {
                ho.startTimer();
            } else {
                ho.endTimer();
            }

        /*Collapsable*/

            ViewPager demo_pager = (ViewPager) v.findViewById(R.id.demo_pager);

            TabLayout demo = (TabLayout) v.findViewById(R.id.demo);

            demo_pager.setAdapter(new ViewPagereAdapter(getActivity()));

            demo.setupWithViewPager(demo_pager);

            final View bottomSheet2 = v.findViewById(R.id.bottom_sheet2);
            mBottomSheetBehavior2 = BottomSheetBehavior.from(bottomSheet2);
            mBottomSheetBehavior2.setHideable(false);
//        mBottomSheetBehavior2.setPeekHeight( 40);
            mBottomSheetBehavior2.setPeekHeight((int) getResources().getDimension(R.dimen.tab_height_android));
            mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_SETTLING);


            mBottomSheetBehavior2.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    mButton2.setText(R.string.button2_peek);
                    } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                    mButton2.setText(R.string.button2_hide);
                    } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    mButton2.setText(R.string.button2);
                    }
                }

                @Override
                public void onSlide(View bottomSheet, float slideOffset) {
                }
            });
        } else {
            CommonMethod.showconnectiondialog(getActivity(), this);
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });

        return v;

    }

    public void setarr() {
        for (int j = 0; j < i.length; j++) {
            if (i[j] != 0) {
                tb[j].setClickable(false);
                tb[j].setEnabled(false);
                tb[j].setBackgroundResource(R.drawable.seat_layout_screen_nor_bkd);
                tb[j].setOnClickListener((View.OnClickListener) this);
            } else {
                tb[j].setClickable(true);
                tb[j].setChecked(true);
                tb[j].setOnClickListener((View.OnClickListener) this);
            }
        }
    }

    void settemparr() {
        seatno.clear();
        for (int j = 0; j < temp.length; j++) {
            if (temp[j] != 0) {
                tb[j].setClickable(true);
                tb[j].setEnabled(true);
                tb[j].setBackgroundResource(selected[j]);
                tb[j].setOnClickListener((View.OnClickListener) this);
                seatno.add(j + 1);
                print = makestring(seatno);
                seat_info.setText(print);
            } else {
                tb[j].setOnClickListener((View.OnClickListener) this);
            }
        }
    }

    public void tempseat() {

    }

    private int[] sendintiailrequest() {
        final int return_arr[] = new int[11];
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.BASE_URL + "Matatu/showseats"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDailogue();
                try {
                    JSONObject response_object = new JSONObject(response);
                    JSONArray ja = new JSONArray();
                    JSONObject new_jo = new JSONObject();
                    JSONObject ja_temp = new JSONObject();
                    if (response_object.getBoolean("success")) {
                        new_jo = response_object.getJSONObject("user_data");
                        ja = new_jo.getJSONArray("orig_data");
                        JSONObject jo = (JSONObject) ja.get(0);

                        for (int j = 0; j < jo.length(); j++) {
                            String s = "matatu_seat" + (j + 1);
                            i[j] = Integer.parseInt(jo.getString(s.trim()));
                        }
                        setarr();
                        if (new_jo.getJSONObject("new_temp") != null) {
                            ja_temp = new_jo.getJSONObject("new_temp");
                            for (int j = 0; j < jo.length(); j++) {
                                String s = "matatu_seat" + (j + 1);
                                temp[j] = Integer.parseInt(ja_temp.getString(s.trim()));
                            }
                            settemparr();
                        }


                    } else {
                        Toast.makeText(getActivity(), response_object.getString("message"), Toast.LENGTH_LONG);
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
                String credentials = getActivity().getString(R.string.api_id) +
                        ":" + getActivity().getString(R.string.api_password);
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
                Log.d("auth", headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("m_id", m_id);
                params.put("sapc_id", sapc_id);
                params.put("total_seat", total_seats);
                params.put("phone_no", SaveSharedPreference.getPhone(getActivity()));
                return params;
            }


        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
        return return_arr;
    }

    public void timerShow(long milliseconds) {
        tv1.setText("Time Left : " + milliseconds + " seconds");

    }


    @Override
    public void onClick(View v) {

        int seatPosition = Integer.parseInt(v.getTag().toString());

        if (Validation.checkconnection(getActivity())) {

            if (!tb[seatPosition - 1].isChecked()) {
                tb[seatPosition - 1].setBackgroundResource(selected[seatPosition - 1]);
                tb[seatPosition - 1].setChecked(false);
                counter++;
                seatno.add(seatPosition);
                print = makestring(seatno);
                seat_info.setText(print);
                showDailogue();
                sendrequest(seatPosition, 1);
            } else {
                tb[seatPosition - 1].setBackgroundResource(unSelected[seatPosition - 1]);
                tb[seatPosition - 1].setChecked(true);
                counter--;
                remove(seatPosition);
                print = makestring(seatno);
                seat_info.setText(print);
                showDailogue();
                sendrequest(seatPosition, 0);

            }
        } else
            CommonMethod.showconnectiondialog(getActivity(), this);
    }


    private void remove(int i) {
        for (int j = 0; j < seatno.size(); j++) {
            if (seatno.get(j) == i) {
                seatno.remove(j);
            }
        }
    }


    public void change() {
        Bundle bundle = new Bundle();
        //bundle.putString("name","s2");
        bundle.putInt("count", counter);
        bundle.putLong("timer", timeleft);
        bundle.putIntegerArrayList("seatnos", seatno);
        bundle.putString("id", s_id);
        bundle.putString("matatu_name", matatu_name);
        bundle.putString("sacco_name", sacco_name);
        bundle.putString("price", price);
        bundle.putString("screen", "seating");
        bundle.putString("m_id", m_id);
        bundle.putString("sacp_id", sapc_id);
        PaymentConfirm pf = new PaymentConfirm();
        pf.setArguments(bundle);
        //ct.onFinish();
        //ct.cancel();
        if (seatno.size() > 0) {
            fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.main_frame, pf).addToBackStack("Payment Confirm").commit();
        } else {
            Toast.makeText(getActivity(), "No Seats are Selected", Toast.LENGTH_LONG).show();
        }
    }

    public String makestring(ArrayList<Integer> a) {
        String final_s = "";
        Collections.sort(a);
        for (int i = 0; i < a.size(); i++) {

            final_s = final_s + a.get(i).toString() + ",";
        }
        if (final_s.trim().endsWith(",") || final_s.trim().endsWith("]")) {
            final_s = final_s.substring(0, final_s.length() - 1);
        }
        if (final_s.trim().startsWith("[")) {
            final_s = final_s.substring(1, final_s.length());
        }
        return final_s;
    }

    public void init(View v) {

        headername = (TextView) v.findViewById(R.id.seatingmatatuname);
        headerroute = (TextView) v.findViewById(R.id.seatingroutename);
        headerseats = (TextView) v.findViewById(R.id.seatingseatsavailalbe);
        headercost = (TextView) v.findViewById(R.id.seatingcost);
        done = (Button) v.findViewById(R.id.confirm_booking);
        tb[0] = (ToggleButton) v.findViewById(R.id.seat1);
        tb[1] = (ToggleButton) v.findViewById(R.id.seat2);
        tb[2] = (ToggleButton) v.findViewById(R.id.seat3);
        tb[3] = (ToggleButton) v.findViewById(R.id.seat4);
        tb[4] = (ToggleButton) v.findViewById(R.id.seat5);
        tb[5] = (ToggleButton) v.findViewById(R.id.seat6);
        tb[6] = (ToggleButton) v.findViewById(R.id.seat7);
        tb[7] = (ToggleButton) v.findViewById(R.id.seat8);
        tb[8] = (ToggleButton) v.findViewById(R.id.seat9);
        tb[9] = (ToggleButton) v.findViewById(R.id.seat10);
        tb[10] = (ToggleButton) v.findViewById(R.id.seat11);
    }


    void sendrequest(final int n, final int func) {

        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.BASE_URL + "Matatu/temporarydata",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("seats", response);
                        dismissProgressDailogue();
                        try {
                            JSONObject respose_object = new JSONObject(response);
                            if (respose_object.getBoolean("success")) {
                                Toast.makeText(getActivity(), respose_object.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                tb[n - 1].setClickable(false);
                                tb[n - 1].setEnabled(false);
                                tb[n - 1].setBackgroundResource(R.drawable.seat_layout_screen_nor_bkd);
                                Toast.makeText(getActivity(), respose_object.getString("message"), Toast.LENGTH_LONG).show();
                                remove(n);
                                print = makestring(seatno);
                                seat_info.setText(print);
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
                String credentials = getActivity().getString(R.string.api_id) +
                        ":" + getActivity().getString(R.string.api_password);
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
                Log.d("auth", headers.toString());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("seat_no", String.valueOf(n));
                params.put("status", String.valueOf(func));
                params.put("m_id", m_id);
                params.put("phone_no", SaveSharedPreference.getPhone(getActivity()));
                return params;
            }

            ;


        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
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
