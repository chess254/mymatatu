package com.mymatatu;

import android.app.Fragment;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mymatatu.Background.GetBalance;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by anonymous on 20-07-2017.
 */

public class Payment_done extends Fragment {
    GetBalance getBalance;
    private JSONArray ja;
    String date_s;
    private String booking_id,to_s,from_s,sacco_name_s,matatu_no_s,seats_s="",seat_no_s,price_s,total_cost_s,insurance_s;
    TextView to,from,sacco_name,matatu_no,seats,seat_no,total_cost,insurance,bookingid,date;
    //turnacated table

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.payment_done,container,false);
        getActivity().setTitle("Payment Done");
        getBalance = new GetBalance(getActivity());
        getBalance.checkbalance(getActivity(),Payment_done.this);
        Bundle bundle = getArguments();
        Date currdate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        date_s = format.format(currdate);

        try {
            ja = new JSONArray(bundle.getString("json_data_final"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        init(v);
        parseJson();
        setData();
        //add to db
//        Booking_transaction_db bdb = new Booking_transaction_db(getActivity(),null,null,1);
//        bdb.addtotable1(booking_id,from_s,to_s,sacco_name_s,matatu_no_s,seats_s,seat_no_s,total_cost_s,date,insurance_s);
//        bdb.addtotable2(booking_id,from_s,to_s,sacco_name_s,matatu_no_s,seats_s,seat_no_s,total_cost_s,date,insurance_s);
        return v;
    }

    private void setData() {
        date.setText(date_s);
        bookingid.setText(booking_id);
        from.setText(from_s);
        to.setText(to_s);
        sacco_name.setText(sacco_name_s);
        matatu_no.setText(matatu_no_s);
        seats.setText(seats_s);
        seat_no.setText(seat_no_s);
        total_cost.setText(total_cost_s);
        insurance.setText(insurance_s);

    }

    private void init(View v) {
        date = (TextView) v.findViewById(R.id.date_data);
        bookingid = (TextView) v.findViewById(R.id.pd_booking_id_data);
        from = (TextView) v.findViewById(R.id.pd_from_data);
        to = (TextView) v.findViewById(R.id.pd_to_data);
        sacco_name = (TextView) v.findViewById(R.id.pd_sacco_name_data);
        matatu_no = (TextView) v.findViewById(R.id.pd_matatu_no_data);
        seats = (TextView) v.findViewById(R.id.pd_totalseats_data);
        seat_no = (TextView) v.findViewById(R.id.pd_seat_nos_data);
        total_cost = (TextView) v.findViewById(R.id.pd_total_cost_data);
        insurance = (TextView) v.findViewById(R.id.pd_insurance);
    }

    private void parseJson() {
        JSONObject jo = new JSONObject();
        try {

            jo = ja.getJSONObject(0);
            booking_id = jo.getString("booking_id");
            to_s = jo.getString("to");
            from_s = jo.getString("from");
            sacco_name_s = jo.getString("sacco_name");
            matatu_no_s = jo.getString("matatu_number");
            seats_s = jo.getString("seats_total");
            seat_no_s = jo.getString("seats_no");
            total_cost_s = jo.getString("total_cost");
            insurance_s = jo.getString("insurance");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
