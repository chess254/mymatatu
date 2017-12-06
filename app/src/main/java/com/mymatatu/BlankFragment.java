package com.mymatatu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by anonymous on 28-07-2017.
 */

public class BlankFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.blank,container,false);
        android.app.FragmentManager fm = getFragmentManager();
        //fm.beginTransaction().replace(R.id.main_frame,new Route_Selection()).commit();

        return v;

    }
}
