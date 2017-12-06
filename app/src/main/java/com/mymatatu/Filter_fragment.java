package com.mymatatu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

import com.mymatatu.App_Importants.FilterSingleton;
import com.mymatatu.CardView_Picking.GridviewAdapter;
import com.mymatatu.CustomDataTypes.Item;
import com.mymatatu.CustomDataTypes.Sacco_filter_item;

import java.util.ArrayList;

/**
 * Created by anonymous on 12-07-2017.
 */

public class Filter_fragment extends Fragment {
    android.app.FragmentManager fm;
    private ListView listview_filter;
    private GridView gridview_filter;
    private ListAdapter filter_adapter;
    String s = "";
    private Button done, clear;
    final String TAG = this.getClass().getSimpleName();
    ArrayList<Sacco_filter_item> itemlist = new ArrayList<>();
    ArrayList<Integer> selected_saccos = new ArrayList<>();
    ArrayList<Integer> selected_saccos_old = new ArrayList<>();
    FilterSingleton filterSingleton;
    RadioButton asc, desc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_fragment, container, false);
        Bundle bundle = getArguments();
        done = (Button) v.findViewById(R.id.filter_done_btn);
        clear = (Button) v.findViewById(R.id.filter_clear_button);
        gridview_filter = (GridView) v.findViewById(R.id.gridview);
        asc = (RadioButton) v.findViewById(R.id.price_ascending);
        desc = (RadioButton) v.findViewById(R.id.price_descending);
        filterSingleton = FilterSingleton.getInstance(getActivity());
        final GridviewAdapter gva = new GridviewAdapter(getActivity(), filterSingleton.allFilters);
        gridview_filter.setAdapter(gva);
        setcheckedradiobutton();
        asc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (asc.isChecked()) {
                    filterSingleton.asec = true;
                    filterSingleton.desc = false;
                    setcheckedradiobutton();
                } else {
                    filterSingleton.asec = false;
                    filterSingleton.desc = true;
                    setcheckedradiobutton();
                }
            }
        });
        desc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (desc.isChecked()) {
                    filterSingleton.desc = true;
                    filterSingleton.asec = false;
                    setcheckedradiobutton();
                } else {
                    filterSingleton.desc = false;
                    filterSingleton.asec = true;
                    setcheckedradiobutton();
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("screen", "filter");
//                bundle.putIntegerArrayList("selected",selected_saccos);


                if (filterSingleton.allItem_filter != null)
                    filterSingleton.allItem_filter.clear();
                for (int j = 0; j < filterSingleton.allItem.size(); j++) {
                    for (int i = 0; i < filterSingleton.allFilters.size(); i++)
                        if (filterSingleton.allItem.get(j).id == filterSingleton.allFilters.get(i).id && filterSingleton.allFilters.get(i).isFilterSelected) {
                            filterSingleton.allItem_filter.add(filterSingleton.allItem.get(j));
                        }
                }


                Selection_List sl = new Selection_List();
                sl.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.main_frame, sl)
                        .addToBackStack("filter").commit();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //filterSingleton.allFilters = new ArrayList<>();
                for (int i = 0; i < filterSingleton.allFilters.size(); i++) {
                    filterSingleton.allFilters.get(i).isFilterSelected = false;
                }
                Bundle bundle = new Bundle();
                bundle.putString("screen", "filter");
                filterSingleton.allItem_filter = new ArrayList<Item>();
                gridview_filter.setAdapter(gva);
                filterSingleton.asec = false;
                filterSingleton.desc = false;
                Selection_List sl = new Selection_List();
                sl.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.main_frame, sl)
                        .addToBackStack("filter").commit();
            }
        });
        return v;
    }

    private void setcheckedradiobutton() {
        if (filterSingleton.asec == true) {
            asc.setChecked(true);
        } else {
            asc.setChecked(false);
        }
        if (filterSingleton.desc == false) {
            desc.setChecked(false);
        } else {
            desc.setChecked(true);
        }
    }
}
