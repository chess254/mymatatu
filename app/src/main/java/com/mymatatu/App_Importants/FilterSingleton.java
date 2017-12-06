package com.mymatatu.App_Importants;

import android.content.Context;

import com.mymatatu.CustomDataTypes.Item;
import com.mymatatu.CustomDataTypes.Sacco_filter_item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by anonymous on 25-07-2017.
 */

public class FilterSingleton {

    private static FilterSingleton filterSingleton_instance;
    public ArrayList<Sacco_filter_item> allFilters;
    ArrayList<Integer> selected_saccos = new ArrayList<>();
    Context context;
    public ArrayList<Item> allItem;
    public ArrayList<Item> allItem_filter;
    public boolean asec = false, desc = false;

    private FilterSingleton(Context context) {
        this.context = context;
        allFilters = new ArrayList<>();
        selected_saccos = new ArrayList<>();
        allItem = new ArrayList<>();
        allItem_filter = new ArrayList<>();
    }

    public static FilterSingleton getInstance(Context context) {
        if (filterSingleton_instance == null) {
            filterSingleton_instance = new FilterSingleton(context);
        }
        return groupByValueSacco(filterSingleton_instance);
    }

    private static FilterSingleton groupByValueSacco(FilterSingleton filterSingleton) {
        Set<Sacco_filter_item> hs = new HashSet<>();
        hs.addAll(filterSingleton.allFilters);
        filterSingleton.allFilters.clear();
        filterSingleton.allFilters.addAll(hs);

        Collections.sort(filterSingleton.allFilters, new Comparator<Sacco_filter_item>() {
            public int compare(Sacco_filter_item s1, Sacco_filter_item s2) {
                // notice the cast to (Integer) to invoke compareTo
                return ((Integer) s1.id).compareTo(s2.id);
            }
        });

        return filterSingleton;
    }

    public ArrayList<Integer> allSelectedIds() {
        for (int i = 0; i < allFilters.size(); i++) {
            Sacco_filter_item s = allFilters.get(i);
            if (s.isFilterSelected) {
                selected_saccos.add(s.id);
            }
        }
        return selected_saccos;
    }


}
