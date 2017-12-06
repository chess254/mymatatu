package com.mymatatu;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mymatatu.App_Importants.ViewPagereAdapter;

import java.util.ArrayList;

/**
 * Created by anonymous on 12-07-2017.
 */

public class SeatingArrangment3 extends Fragment implements View.OnClickListener {
    private TextView seat_info;
    Button cancel,done;
    String print = "";
    String s_id, matatu_name, sacco_name, price;
    ArrayList<Integer> seatno ;
    ToggleButton tb[] = new ToggleButton[7];
    int counter = 0, type;
    public TextView tv1;
    long timeleft;
    String screen="";
    android.app.FragmentManager fm;
    CountDownTimer ct;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.seating_model3,container,false);
        Bundle bundle = getArguments();
        screen = bundle.getString("screen");
        getActivity().setTitle("Select Your Seat");
        seatno = new ArrayList<>();
        seat_info = (TextView) v.findViewById(R.id.seat_info);
        String id = bundle.getString("id");
        init(v);
        int i[] = {1,1,0,0,0,0,1};

        //Timer part

        Home ho = (Home) getActivity();
        if(ho.timerflag == false){
            ho.startTimer();
        }else{
            ho.endTimer();
        }

        s_id = bundle.getString("id");
        matatu_name = bundle.getString("matatu_name");
        sacco_name = bundle.getString("sacco_name");
        price = bundle.getString("price");
        tv1 = (TextView) v.findViewById(R.id.timer);
        for(int j=0;j< i.length;j++){
            if(i[j] == 1){
                tb[j].setClickable(false);
                tb[j].setEnabled(false);
                tb[j].setBackgroundResource(R.drawable.seat_layout_screen_nor_bkd);
                tb[j].setOnClickListener((View.OnClickListener) this);
            }
            else{
                tb[j].setClickable(true);
                tb[j].setChecked(true);
                tb[j].setOnClickListener((View.OnClickListener) this);
            }
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

        return v;
    }

    public  void timerShow(long milliseconds){
        tv1.setText("TIME LEFT : " + milliseconds + " seconds");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seat1:
                if (!tb[0].isChecked()) {
                    tb[0].setBackgroundResource(R.drawable.seat_layout_screen_nor_std);
                    tb[0].setChecked(false);
                    counter++;
                    seatno.add(1);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });


                } else {
                    tb[0].setBackgroundResource(R.drawable.seat_layout_screen_nor_avl);
                    tb[0].setChecked(true);
                    counter--;
                    remove(1);
                    print = makestring(seatno);
                    seat_info.setText(print);
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
                    tb[1].setBackgroundResource(R.drawable.seat_layout_screen_nor_std);
                    tb[1].setChecked(false);
                    counter++;
                    seatno.add(2);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[1].setBackgroundResource(R.drawable.seat_layout_screen_nor_avl);
                    tb[1].setChecked(true);
                    counter--;
                    remove(2);
                    print = makestring(seatno);
                    seat_info.setText(print);
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
                    tb[2].setBackgroundResource(R.drawable.seat_layout_screen_nor_std);
                    tb[2].setChecked(false);
                    counter++;
                    seatno.add(3);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[2].setBackgroundResource(R.drawable.seat_layout_screen_nor_avl);
                    tb[2].setChecked(true);
                    counter--;
                    remove(3);
                    print = makestring(seatno);
                    seat_info.setText(print);
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
                    tb[3].setBackgroundResource(R.drawable.seat_layout_screen_nor_std);
                    tb[3].setChecked(false);
                    counter++;
                    seatno.add(4);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[3].setBackgroundResource(R.drawable.seat_layout_screen_nor_avl);
                    tb[3].setChecked(true);
                    counter--;
                    remove(4);
                    print = makestring(seatno);
                    seat_info.setText(print);
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
                    tb[4].setBackgroundResource(R.drawable.seat_layout_screen_nor_std);
                    tb[4].setChecked(false);
                    counter++;
                    seatno.add(5);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[4].setBackgroundResource(R.drawable.seat_layout_screen_nor_avl);
                    tb[4].setChecked(true);
                    counter--;
                    remove(5);
                    print = makestring(seatno);
                    seat_info.setText(print);
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
                    tb[5].setBackgroundResource(R.drawable.seat_layout_screen_nor_std);
                    tb[5].setChecked(false);
                    counter++;
                    seatno.add(6);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[5].setBackgroundResource(R.drawable.seat_layout_screen_nor_avl);
                    tb[5].setChecked(true);
                    counter--;
                    remove(6);
                    print = makestring(seatno);
                    seat_info.setText(print);
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
                    tb[6].setBackgroundResource(R.drawable.seat_layout_screen_nor_std);
                    tb[6].setChecked(false);
                    counter++;
                    seatno.add(7);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                } else {
                    tb[6].setBackgroundResource(R.drawable.seat_layout_screen_nor_avl);
                    tb[6].setChecked(true);
                    counter--;
                    remove(7);
                    print = makestring(seatno);
                    seat_info.setText(print);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            change();
                        }
                    });
                }
                break;

        }
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
        bundle.putString("screen","seating");
        PaymentConfirm pf = new PaymentConfirm();
        pf.setArguments(bundle);
        //ct.onFinish();
        //ct.cancel();
        fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.main_frame, pf).addToBackStack("Payment Confirm").commit();
    }

    public String makestring(ArrayList<Integer> a) {
        String final_s = "";
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

    private void init(View v) {
        done = (Button) v.findViewById(R.id.confirm_booking);
        tb[0] = (ToggleButton) v.findViewById(R.id.seat1);
        tb[1] = (ToggleButton) v.findViewById(R.id.seat2);
        tb[2] = (ToggleButton) v.findViewById(R.id.seat3);
        tb[3] = (ToggleButton) v.findViewById(R.id.seat4);
        tb[4] = (ToggleButton) v.findViewById(R.id.seat5);
        tb[5] = (ToggleButton) v.findViewById(R.id.seat6);
        tb[6] = (ToggleButton) v.findViewById(R.id.seat7);
    }


}
