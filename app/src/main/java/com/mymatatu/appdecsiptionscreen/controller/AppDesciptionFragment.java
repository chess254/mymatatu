package com.mymatatu.appdecsiptionscreen.controller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mymatatu.Global.AppConstant;
import com.mymatatu.R;
import com.mymatatu.appdecsiptionscreen.model.AppDesciptionModel;


public class AppDesciptionFragment extends Fragment {
    ImageView desciptionimageview;
    TextView boldtext, description;
    AppDesciptionModel appDesciptionModel;

    public AppDesciptionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppDesciptionFragment newInstance(AppDesciptionModel appDesciptionModel) {
        AppDesciptionFragment fragment = new AppDesciptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(AppConstant.APP_DECRIPTION_DATA, appDesciptionModel);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_desciption, container, false);
        appDesciptionModel = getArguments().getParcelable(AppConstant.APP_DECRIPTION_DATA);
        desciptionimageview = (ImageView) view.findViewById(R.id.desciptionimageview);
        boldtext = (TextView) view.findViewById(R.id.boldtext);
        description = (TextView) view.findViewById(R.id.description);
        desciptionimageview.setImageResource(appDesciptionModel.imageId);
        boldtext.setText(appDesciptionModel.highLightText);
        description.setText(appDesciptionModel.desciption);

        ///use this data as per requireemnt.....for making UI
        return view;
    }

}
