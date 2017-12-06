package com.mymatatu;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mymatatu.App_Importants.CommonMethod;
import com.mymatatu.App_Importants.Validation;
import com.mymatatu.Background.CancelBooking;
import com.mymatatu.Background.GetBalance;
import com.mymatatu.CardView_Picking.recent_booking_rv_adapter;
import com.mymatatu.CustomDataTypes.Bookingdata;
import com.mymatatu.DataBaseHelper.Booking_transaction_db;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.model.CountryCityRSM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import at.markushi.ui.CircleButton;

/**
 * Created by anonymous on 03-07-2017.
 */

public class Route_Selection extends Fragment implements View.OnClickListener {

    private AutoCompleteTextView source, dest;
    private TextView error;
    int s, d;
    CircleButton searchbtn;
    String source_s, dest_s;
    ImageView toggle;
    RecyclerView rv;
    private Booking_transaction_db bdb;
    private ArrayList<Bookingdata> data1 = new ArrayList<>();
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<Bookingdata> data_final = new ArrayList<>();
    private RecyclerView.LayoutManager layoutmanager;
    private RecyclerView.Adapter adapter_rv;
    private ArrayList<String> area;
    //RegionDatabaseHelper rdb;
    GetBalance getBalance;
    CancelBooking cb;
    private ArrayAdapter adapter, adapter1;
    private ArrayList<CountryCityRSM> cityData;

    //    Dialog nointernetdialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.route_layout_fragment, container, false);
        getActivity().setTitle("Search A Matatu");
        cb = new CancelBooking(getActivity());
        cb.sendCancelRequest();
        getBalance = new GetBalance(getActivity());
        getBalance.checkbalance(getActivity(), Route_Selection.this);
        //rdb = new RegionDatabaseHelper(getActivity(), null, null, 1);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("screen").equals("recentsearches")) {
                source.setText(bundle.getString("source"));
                dest.setText(bundle.getString("destination"));
            }
        }
       /* Home ho = (Home) getActivity();
        ho.hidekeyboardontouch();
        if (ho.timerflag == true) {
            ho.endTimer();
        }
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);*/


        searchbtn = (CircleButton) rootview.findViewById(R.id.route_done);
        toggle = (ImageView) rootview.findViewById(R.id.image_toggle);
        source = (AutoCompleteTextView) rootview.findViewById(R.id.autoCompleteTextView);
        dest = (AutoCompleteTextView) rootview.findViewById(R.id.autoCompleteTextView2);
        error = (TextView) rootview.findViewById(R.id.error_route_selection);
        area = new ArrayList<>();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, area);
        source.setAdapter(adapter);
        adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, area);
        dest.setAdapter(adapter1);
        searchbtn.setOnClickListener(this);
        toggle.setOnClickListener(this);


        //REcycler View part
        rv = (RecyclerView) rootview.findViewById(R.id.rv_route_layout);
        bdb = new Booking_transaction_db(getActivity(), null, null, 1);
        data1 = bdb.showtable2();
        data = bdb.print_recentsearches();
        data_final = converter(data1);
        layoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        rv.setLayoutManager(layoutmanager);
        adapter_rv = new recent_booking_rv_adapter(getActivity(), data);
        rv.setAdapter(adapter_rv);

        countycityrequest();

        return rootview;
    }

    private ArrayList<Bookingdata> converter(ArrayList<Bookingdata> data) {
        ArrayList<Bookingdata> return_data = new ArrayList<>();

        return return_data;
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.route_done:
                source_s = source.getText().toString().trim();
                dest_s = dest.getText().toString().trim();
             //   Log.d("Ids", String.valueOf(s) + "  " + String.valueOf(d));
                if (Validation.checkEmptyfield(source_s)) {
                    error.setText("Source Cannot be Empty");
                    error.setVisibility(View.VISIBLE);
                } else {
                    error.setVisibility(View.GONE);
                    if (Validation.checkEmptyfield(dest_s)) {
                        error.setText("Destination Cannot be Empty");
                        error.setVisibility(View.VISIBLE);
                    } else {
                        error.setVisibility(View.GONE);
                        if (Validation.checkroutesame(source_s, dest_s)) {
                            error.setText("Source and Destination Cannot be Same");
                            error.setVisibility(View.VISIBLE);
                        } else {
                            error.setVisibility(View.GONE);
                            Date currdate = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            String date = format.format(currdate);
                            bdb.addrecentsearches(source_s, dest_s, date);
                            if (Validation.checkconnection(getActivity())) {

                                s =Integer.parseInt(findId(source_s,cityData));
                                d =Integer.parseInt(findId(dest_s,cityData));
                                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                sendrequest();
                            } else {
                                CommonMethod.showconnectiondialog(getActivity(), null);
                            }
                        }
                    }
                }
                break;
            case R.id.image_toggle:
                RotateAnimation rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(300);
                toggle.startAnimation(rotate);
                source_s = source.getText().toString().trim();
                dest_s = dest.getText().toString().trim();
                dest.animate().alpha(0.0f);
                source.animate().alpha(0.0f);
                dest.setText(source_s);
                source.setText(dest_s);
                dest.animate().alpha(0.8f).setDuration(500);
                source.animate().alpha(0.8f).setDuration(500);
                dest.clearFocus();
                source.clearFocus();
                break;
        }

    }

    private void sendrequest() {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle bundle = new Bundle();
        bundle.putString("source", source.getText().toString());
        bundle.putString("destination", dest.getText().toString());
        bundle.putInt("s", s);
        bundle.putInt("d", d);
        bundle.putString("screen", "route");
        SaveSharedPreference.setSource(getActivity(), source_s);
        SaveSharedPreference.setDestination(getActivity(), dest_s);
        android.app.FragmentManager fm = getFragmentManager();
        Selection_List selection_list = new Selection_List();
        selection_list.setArguments(bundle);
        fm.beginTransaction().replace(R.id.main_frame, selection_list)
                .addToBackStack("Selection List").commit();


    }

    public void countycityrequest() {
        try {
            area.clear();
            cityData = new ArrayList<>();
            String response = SaveSharedPreference.getString(getActivity(), "all_county");
            JSONObject ja = new JSONObject(response);

            JSONArray cityArray = ja.getJSONArray("city");
            if (cityArray.length() > 0) {
                for (int i = 0; i < cityArray.length(); i++) {
                    JSONObject jo_countycity = cityArray.getJSONObject(i);
                    cityData.add(new CountryCityRSM(jo_countycity.getString("id"), jo_countycity.getString("name"), jo_countycity.getString("id_c")));
                    area.add(jo_countycity.getString("name"));
                }
                adapter.notifyDataSetChanged();
                adapter1.notifyDataSetChanged();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
