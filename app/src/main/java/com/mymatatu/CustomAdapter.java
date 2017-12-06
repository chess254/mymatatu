package com.mymatatu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anonymous on 23-08-2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.myHolder> {

    private Context context;
    private ArrayList<TransferHistory> transferHistories;

    public CustomAdapter(Context context, ArrayList<TransferHistory> names) {
        this.context = context;
        this.transferHistories = names;
    }

    @Override
    public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myHolder(LayoutInflater.from(context).inflate(R.layout.transfer_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(myHolder holder, int position) {
        holder.mName.setText(transferHistories.get(position).text);
        if (transferHistories.get(position).wth_amount.contains("-"))
            holder.mName.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        else
            holder.mName.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        holder.mData.setText(transferHistories.get(position).data);
        holder.mDate.setText(transferHistories.get(position).wth_date);
        holder.mAmount.setText(transferHistories.get(position).wth_amount);
    }

    @Override
    public int getItemCount() {
        return transferHistories.size();
    }

    public class myHolder extends RecyclerView.ViewHolder {

        TextView mName, mData, mDate, mAmount;

        public myHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mAmount = (TextView) itemView.findViewById(R.id.amount);
            mData = (TextView) itemView.findViewById(R.id.data);
            mDate = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
