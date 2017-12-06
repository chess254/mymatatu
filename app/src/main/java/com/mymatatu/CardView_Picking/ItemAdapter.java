package com.mymatatu.CardView_Picking;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mymatatu.CustomDataTypes.Item;
import com.mymatatu.R;
import com.mymatatu.SeatingArrangment1;
import com.mymatatu.SeatingArrangment14;
import com.mymatatu.SeatingArrangment2;
import com.mymatatu.SeatingArrangment3;
import com.mymatatu.Selection_List;

import java.util.ArrayList;

/**
 * Created by anonymous on 24-06-2017.
 */


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public Context context,ct;
    public ArrayList<Item> itemList;
    private static String id ;

    public ItemAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
        ct = context;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(v);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.tv1.setText(item.t1);
        holder.tv2.setText(item.t2);
        holder.tv3.setText(item.t3);
        holder.tv4.setText(item.t4);
        holder.tv5.setText(item.t5);
        holder.id.setText(String.valueOf(item.id));
        holder.totalseats.setText(String.valueOf(item.seats));
        holder.matatuid.setText(String.valueOf(item.m_id));
        holder.spac_id.setText(String.valueOf(item.sapc_id));
    }

    @Override
    public int getItemCount() {
        if(itemList != null)
        return itemList.size();

        return 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private Selection_List sl = new Selection_List();
        public CardView cvitem;
        public TextView tv1,tv2,tv3,tv4,tv5,id,totalseats,matatuid,spac_id;
        //private FragmentManager fm;
      /*  SeatingArrangment1 seatingarrangment1 = new SeatingArrangment1();
        SeatingArrangment2 seatingarrangment2 = new SeatingArrangment2();
        SeatingArrangment3 seatingarrangment3 = new SeatingArrangment3();*/
        SeatingArrangment14 seatingArrangment14 = new SeatingArrangment14();


        public ItemViewHolder(final View itemView) {
            super(itemView);
            cvitem = (CardView) itemView.findViewById(R.id.cvitem);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
            tv5 = (TextView) itemView.findViewById(R.id.tv5);
            id = (TextView) itemView.findViewById(R.id.invi);
            totalseats = (TextView) itemView.findViewById(R.id.inviseat);
            matatuid = (TextView) itemView.findViewById(R.id.invimatatuid);
            spac_id = (TextView) itemView.findViewById(R.id.invispac_id);
            cvitem.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  Bundle bundle = new Bundle();
                   bundle.putString("id",id.getText().toString());
                   bundle.putString("matatu_name",tv1.getText().toString());
                   bundle.putString("sacco_name",tv2.getText().toString());
                   bundle.putString("price",tv5.getText().toString());
                   bundle.putString("seats",totalseats.getText().toString());
                   bundle.putString("matatu_id",matatuid.getText().toString());
                   bundle.putString("sapc_id",spac_id.getText().toString());
                   bundle.putString("screen","selectionlist");
                   android.app.FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getFragmentManager();
                   /*seatingarrangment1.setArguments(bundle);
                   seatingarrangment2.setArguments(bundle);
                   seatingarrangment3.setArguments(bundle);*/
                   seatingArrangment14.setArguments(bundle);


                   fragmentManager.beginTransaction().replace(R.id.main_frame, seatingArrangment14)
                           .addToBackStack("Seating Arrangment").commit();

               }
           });



        }
    }


}
