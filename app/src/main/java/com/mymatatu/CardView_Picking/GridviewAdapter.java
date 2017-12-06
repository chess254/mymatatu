package com.mymatatu.CardView_Picking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mymatatu.App_Importants.FilterSingleton;
import com.mymatatu.CustomDataTypes.Sacco_filter_item;
import com.mymatatu.DataBaseHelper.Filterdb;

import java.util.ArrayList;

/**
 * Created by anonymous on 17-07-2017.
 */

public class GridviewAdapter extends BaseAdapter {
    public int ids[];
    public String names[];
    Filterdb fdb;
    private Context c;
    private CheckBox cb[];
    private LayoutInflater inflater;
    public ArrayList<Integer> selected = new ArrayList<>();
    public ArrayList<Integer> selected_old = new ArrayList<>();
    //    public ArrayList<Sacco_filter_item> sacco_items = new ArrayList<>();
    FilterSingleton filterSingleton;

    public GridviewAdapter(Context c, ArrayList<Sacco_filter_item> list) {
        this.c = c;
        filterSingleton = FilterSingleton.getInstance(c);
        cb = new CheckBox[filterSingleton.allFilters.size()];
//        this.sacco_items = filterSingleton.allFilters;
    }


    @Override
    public int getCount() {
        return filterSingleton.allFilters.size();
    }

    @Override
    public Object getItem(int position) {
        return filterSingleton.allFilters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //fdb = new Filterdb(c,null,null,1);
        final Sacco_filter_item sf1 = filterSingleton.allFilters.get(position);
        cb[position] = new CheckBox(c);
        cb[position].setId(sf1.id);
        cb[position].setText(sf1.s_name);
        if (sf1.isFilterSelected) {
            cb[position].setChecked(true);
        } else {
            cb[position].setChecked(false);
        }

        cb[position].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    filterSingleton.allFilters.get(position).isFilterSelected = true;
                else filterSingleton.allFilters.get(position).isFilterSelected = false;

            }
        });

        return cb[position];
    }

}
