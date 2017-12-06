package com.mymatatu;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Base64;
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
 * Created by anonymous on 08-08-2017.
 */

public class SeatingArrangment14 extends Fragment implements View.OnClickListener {

    private TextView seat_info;
    GlobalProgressDialogue globalProgressDialogue;
    Button cancel, done;
    String print = "";
    String s_id, matatu_name, sacco_name, price, m_id, sapc_id, total_seats;
    ArrayList<Integer> seatno;
    ToggleButton tb[] = new ToggleButton[14];
    int counter = 0, type;
    public TextView tv1;
    long timeleft;
    String screen = "";
    android.app.FragmentManager fm;
    CountDownTimer ct;
    TextView headername, headerroute, headerseats, headercost;
    int i[];
    int temp[];
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
            R.drawable.gseat11,
            R.drawable.gseat12,
            R.drawable.gseat13,
            R.drawable.gseat14};

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
            R.drawable.wseat11,
            R.drawable.wseat12,
            R.drawable.wseat13,
            R.drawable.wseat14};
    /*Collapseable View*/
//    private int[] images = {
//            R.drawable.android_authority,
//            R.drawable.android_authority,
//            R.drawable.android_authority,
//            R.drawable.android_authority,
//            R.drawable.android_authority,
//    };
//
//    private String[] title = {
//            "IMG 1",
//            "IMG 2",
//            "IMG 3",
//            "IMG 4",
//            "IMG 5",
//    };
    private static final String TAG = "MainActivity";
    private Button mButton1, mButton2, mButton3;
    private BottomSheetBehavior mBottomSheetBehavior1, mBottomSheetBehavior2;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }


    private View getSettterMatatu(String name, LayoutInflater inflater, final ViewGroup container) {
        View v;
        switch (total_seats) {
            case "14":
                v = inflater.inflate(R.layout.seating_model14, container, false);
                break;
            case "11":
                v = inflater.inflate(R.layout.seating_model1, container, false);
                break;
            case "7":
                v = inflater.inflate(R.layout.seating_model2, container, false);
                break;
            default:
                v = inflater.inflate(R.layout.seating_model3, container, false);
        }
        return v;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        total_seats = bundle.getString("seats");
        temp = new int[Integer.parseInt(total_seats)];
        i = new int[Integer.parseInt(total_seats)];

        View v = getSettterMatatu(total_seats, inflater, container);


        seatno = new ArrayList<>();
        //Get All the data
        screen = bundle.getString("screen");
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
        tv1 = (TextView) v.findViewById(R.id.timer);
        String headerroute_s = SaveSharedPreference.getSource(getActivity()) +
                " to " +
                SaveSharedPreference.getDestination(getActivity());
        headername.setText("Matatu Number : " + matatu_name);
        headerroute.setText(headerroute_s);
        headerseats.setText("Available Seats : " + total_seats);
        headercost.setText("Cost : " + price + " KSh.");
        showDailogue();
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

    void settemparr() {
        seatno.clear();
        for (int j = 0; j < Integer.parseInt(total_seats); j++) {
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

    private int[] sendintiailrequest() {
        final int return_arr[] = new int[14];
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
               // Log.d("auth", headers.toString());
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
        tv1.setText("TIME LEFT : " + milliseconds + " seconds");

    }


    @Override
    public void onClick(View v) {


        int seatPosition = Integer.parseInt(v.getTag().toString());

        if (Validation.checkconnection(getActivity())) {

            sendrequest(seatPosition, !tb[seatPosition - 1].isChecked() ? 1 : 0);

        } else
            CommonMethod.showconnectiondialog(getActivity(), this);



       /* switch (v.getId()) {
            case R.id.seat1:
                if (!tb[0].isChecked()) {
                    tb[0].setBackgroundResource(R.drawable.gseat1);
                    tb[0].setChecked(false);
                    counter++;
                    seatno.add(1);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();
                    sendrequest(1,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });


                } else {
                    tb[0].setBackgroundResource(R.drawable.wseat1);
                    tb[0].setChecked(true);
                    counter--;
                    remove(1);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(1,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });

                }
                break;
            case R.id.seat2:
                if (!tb[1].isChecked()) {
                    tb[1].setBackgroundResource(R.drawable.gseat2);
                    tb[1].setChecked(false);
                    counter++;
                    seatno.add(2);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(2,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[1].setBackgroundResource(R.drawable.wseat2);
                    tb[1].setChecked(true);
                    counter--;
                    remove(2);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(2,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat3:
                if (!tb[2].isChecked()) {
                    tb[2].setBackgroundResource(R.drawable.gseat3);
                    tb[2].setChecked(false);
                    counter++;
                    seatno.add(3);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(3,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[2].setBackgroundResource(R.drawable.wseat3);
                    tb[2].setChecked(true);
                    counter--;
                    remove(3);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(3,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat4:
                if (!tb[3].isChecked()) {
                    tb[3].setBackgroundResource(R.drawable.gseat4);
                    tb[3].setChecked(false);
                    counter++;
                    seatno.add(4);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(4,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[3].setBackgroundResource(R.drawable.wseat4);
                    tb[3].setChecked(true);
                    counter--;
                    remove(4);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(4,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat5:
                if (!tb[4].isChecked()) {
                    tb[4].setBackgroundResource(R.drawable.gseat5);
                    tb[4].setChecked(false);
                    counter++;
                    seatno.add(5);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(5,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[4].setBackgroundResource(R.drawable.wseat5);
                    tb[4].setChecked(true);
                    counter--;
                    remove(5);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(5,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat6:
                if (!tb[5].isChecked()) {
                    tb[5].setBackgroundResource(R.drawable.gseat6);
                    tb[5].setChecked(false);
                    counter++;
                    seatno.add(6);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(6,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[5].setBackgroundResource(R.drawable.wseat6);
                    tb[5].setChecked(true);
                    counter--;
                    remove(6);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();
                    sendrequest(6,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat7:
                if (!tb[6].isChecked()) {
                    tb[6].setBackgroundResource(R.drawable.gseat7);
                    tb[6].setChecked(false);
                    counter++;
                    seatno.add(7);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(7,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[6].setBackgroundResource(R.drawable.wseat7);
                    tb[6].setChecked(true);
                    counter--;
                    remove(7);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(7,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat8:
                if (!tb[7].isChecked()) {
                    tb[7].setBackgroundResource(R.drawable.gseat8);
                    tb[7].setChecked(false);
                    counter++;
                    seatno.add(8);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(8,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[7].setBackgroundResource(R.drawable.wseat8);
                    tb[7].setChecked(true);
                    counter--;
                    remove(8);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(8,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat9:
                if (!tb[8].isChecked()) {
                    tb[8].setBackgroundResource(R.drawable.gseat9);
                    tb[8].setChecked(false);
                    counter++;
                    seatno.add(9);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(9,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[8].setBackgroundResource(R.drawable.wseat9);
                    tb[8].setChecked(true);
                    counter--;
                    remove(9);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(9,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat10:
                if (!tb[9].isChecked()) {
                    tb[9].setBackgroundResource(R.drawable.gseat10);
                    tb[9].setChecked(false);
                    counter++;
                    seatno.add(10);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(10,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[9].setBackgroundResource(R.drawable.wseat10);
                    tb[9].setChecked(true);
                    counter--;
                    remove(10);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(10,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat11:
                if (!tb[10].isChecked()) {
                    tb[10].setBackgroundResource(R.drawable.gseat11);
                    tb[10].setChecked(false);
                    counter++;
                    seatno.add(11);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();
                    sendrequest(11,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[10].setBackgroundResource(R.drawable.wseat11);
                    tb[10].setChecked(true);
                    counter--;
                    remove(11);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();
                    sendrequest(11,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat12:
                if (!tb[11].isChecked()) {
                    tb[11].setBackgroundResource(R.drawable.gseat12);
                    tb[11].setChecked(false);
                    counter++;
                    seatno.add(12);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(12,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[11].setBackgroundResource(R.drawable.wseat12);
                    tb[11].setChecked(true);
                    counter--;
                    remove(12);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(12,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat13:
                if (!tb[12].isChecked()) {
                    tb[12].setBackgroundResource(R.drawable.gseat13);
                    tb[12].setChecked(false);
                    counter++;
                    seatno.add(13);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(13,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[12].setBackgroundResource(R.drawable.wseat13);
                    tb[12].setChecked(true);
                    counter--;
                    remove(13);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();
                    sendrequest(13,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;
            case R.id.seat14:
                if (!tb[13].isChecked()) {
                    tb[13].setBackgroundResource(R.drawable.gseat14);
                    tb[13].setChecked(false);
                    counter++;
                    seatno.add(14);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(14,1);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[13].setBackgroundResource(R.drawable.wseat14);
                    tb[13].setChecked(true);
                    counter--;
                    remove(14);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    showDailogue();

                    sendrequest(14,0);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;

        }*/
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
        tb[11] = (ToggleButton) v.findViewById(R.id.seat12);
        tb[12] = (ToggleButton) v.findViewById(R.id.seat13);
        tb[13] = (ToggleButton) v.findViewById(R.id.seat14);

    }


    void sendrequest(final int n, final int func) {
        showDailogue();
        StringRequest request = new StringRequest(Request.Method.POST, AppConstant.BASE_URL + "Matatu/temporarydata",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Log.d("seats", response);
                        dismissProgressDailogue();
                        try {
                            JSONObject respose_object = new JSONObject(response);
                            if (respose_object.getBoolean("success")) {

                                Toast.makeText(getActivity(), respose_object.getString("message"), Toast.LENGTH_LONG).show();
                                if (!tb[n - 1].isChecked()) {
                                    tb[n - 1].setBackgroundResource(selected[n - 1]);
                                    tb[n - 1].setChecked(false);
                                    counter++;
                                    seatno.add(n);
                                    print = makestring(seatno);
                                    seat_info.setText(print);
                                } else {
                                    tb[n - 1].setBackgroundResource(unSelected[n - 1]);
                                    tb[n - 1].setChecked(true);
                                    counter--;
                                    remove(n);
                                    print = makestring(seatno);
                                    seat_info.setText(print);
                                }
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
              //  Log.d("auth", headers.toString());
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
