package com.mymatatu.CustomDataTypes;

import java.io.Serializable;

/**
 * Created by anonymous on 13-07-2017.
 */

public class Sacco_filter_item implements Serializable  {

    public int id;
    public String s_name;
    public boolean isFilterSelected = false;

    @Override
    public boolean equals(Object obj) {
        if (obj != null || obj instanceof Sacco_filter_item) {
            Sacco_filter_item c = (Sacco_filter_item) obj;
            if (id == c.id && s_name.equals(c.s_name))
                return true;
        }
        return false;
    }

    public int hashCode() {
        // System.out.println("In hashcode");
        int hashcode = 0;
        hashcode = id;
        hashcode += s_name.hashCode();
        return hashcode;
    }

}
