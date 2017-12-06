package com.mymatatu.CardView_Picking;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mymatatu.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by anonymous on 24-07-2017.
 */

public class recent_booking_rv_adapter extends RecyclerView.Adapter<booking_rv_adapter.ViewHolder> {

    ArrayList<String> data = new ArrayList<>();
    Context c;

    public recent_booking_rv_adapter(Context c, ArrayList<String> data) {
        this.data = data;
        this.c = c;
    }

    @Override
    public booking_rv_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.booking_history_rv_item, parent, false);

        booking_rv_adapter.ViewHolder viewHolder = new booking_rv_adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(booking_rv_adapter.ViewHolder holder, int position) {
        String main = data.get(position);
        StringTokenizer st = new StringTokenizer(main,",");
        while(st.hasMoreTokens()){
            holder.from.setText(st.nextToken());
            holder.to.setText(st.nextToken());
            holder.date.setText(st.nextToken());
        }
//
//        holder.from.setText(bdata.from_s);
//        holder.to.setText(bdata.to_s);
//        holder.sacco_name.setText(bdata.sacco_name);
//        holder.matatu_no.setText(bdata.matatu_name);
//        holder.seats.setText(bdata.total_seats);
//        holder.seat_no.setText(bdata.seats);
//        holder.total_cost.setText(bdata.total_cost);
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView to, from, sacco_name, matatu_no, seats, seat_no, total_cost, date;
        public CardView cv;


        public ViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.booking_rv_cv_recent);
            from = (TextView) itemView.findViewById(R.id.pd_from_data);
            to = (TextView) itemView.findViewById(R.id.pd_to_data);
            date = (TextView) itemView.findViewById(R.id.date_data);
            cv.setOnClickListener(this);
//            cv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   Log.d("ha","ha");
//                    /*Bundle bundle = new Bundle();
//                    bundle.putString("screen","recentsearches");
//                    bundle.putString("source",from.getText().toString().trim());
//                    bundle.putString("destination",to.getText().toString().trim());
//                    Route_Selection rs = new Route_Selection();
//                    rs.setArguments(bundle);
//                    android.app.FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.main_frame,rs)
//                            .addToBackStack("recentsearches").commit();*/
//                }
//            });

        }


        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.booking_rv_cv_recent:
                   // Log.d("pressed","yes");

                    break;
            }
        }
    }
}
