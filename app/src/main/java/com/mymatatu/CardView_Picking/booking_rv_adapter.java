package com.mymatatu.CardView_Picking;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mymatatu.CustomDataTypes.Bookingdata;
import com.mymatatu.R;

import java.util.ArrayList;

/**
 * Created by anonymous on 20-07-2017.
 */

public class booking_rv_adapter extends RecyclerView.Adapter<booking_rv_adapter.ViewHolder>{
    ArrayList<Bookingdata> data = new ArrayList<>();
    Context c;
    public booking_rv_adapter(Context c, ArrayList<Bookingdata> data) {
        this.data = data;
        this.c =c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.booking_cardview,parent,false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bookingdata bdata = data.get(position);
        holder.date.setText(bdata.date);
        holder.id.setText(bdata.id);
        holder.from.setText(bdata.from_s);
        holder.to.setText(bdata.to_s);
        holder.sacco_name.setText(bdata.sacco_name);
        holder.matatu_no.setText(bdata.matatu_name);
        holder.seats.setText(bdata.seats);
        holder.seat_no.setText(bdata.total_seats);
        holder.total_cost.setText(bdata.total_cost);
        holder.insurance.setText(bdata.insurance);
    }

    @Override
    public int getItemCount() {
        if(data !=null){
            return data.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView to,from,sacco_name,matatu_no,seats,seat_no,total_cost,date,id,insurance;
        public CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
             cv= (CardView) itemView.findViewById(R.id.booking_rv_cv);
            id = (TextView) itemView.findViewById(R.id.pd_booking_id_data);
            from = (TextView) itemView.findViewById(R.id.pd_from_data);
            to = (TextView) itemView.findViewById(R.id.pd_to_data);
            sacco_name = (TextView) itemView.findViewById(R.id.pd_sacco_name_data);
            matatu_no = (TextView) itemView.findViewById(R.id.pd_matatu_no_data);
            seats = (TextView) itemView.findViewById(R.id.pd_totalseats_data);
            seat_no = (TextView) itemView.findViewById(R.id.pd_seat_nos_data);
            total_cost = (TextView) itemView.findViewById(R.id.pd_total_cost_data);
            date = (TextView) itemView.findViewById(R.id.date_data);
            insurance = (TextView) itemView.findViewById(R.id.pd_insurance);

        }
    }


}

