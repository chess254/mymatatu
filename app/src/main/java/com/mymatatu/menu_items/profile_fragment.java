package com.mymatatu.menu_items;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mymatatu.ChangePasswordFragment;
import com.mymatatu.CustomDataTypes.Profile;
import com.mymatatu.Home;
import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.R;

/**
 * Created by anonymous on 07-07-2017.
 */

public class profile_fragment extends Fragment{
    private android.app.FragmentManager fm;
    private String name_s,phone_s,county_s,city_s,nextofkin_s,image_s,password_s;
    private Bitmap image;
    private Profile p;
    private TextView name,phone,county,city,nextofkin,password;
    private ImageView profile_pic;
    private BitmapFactory.Options options;
    private byte[] imagearr;
    TextView changepassword;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_menuitem_fragment,container,false);

        getActivity().setTitle("Profile");
        Home ho = (Home) getActivity();
        if(ho.timerflag == true){
            ho.endTimer();
        }
       init(v);
        getdata();
            setData();
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.main_frame,new ChangePasswordFragment())
                        .addToBackStack("changepassword").commit();
            }
        });
            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.edit_fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag", 1);
                    bundle.putString("name",name_s);
                    bundle.putString("phone",phone_s);
                    bundle.putString("county",county_s);
                    bundle.putString("city",city_s);
                    profile_fragment_edit pfe = new profile_fragment_edit();
                    pfe.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.main_frame, pfe)
                            .addToBackStack("profile_edit").commit();
                }
            });

        return v;
    }


    private void init(View v) {
        p=new Profile();
        fm = getFragmentManager();
        name = (TextView) v.findViewById(R.id.name_data);
        password = (TextView) v.findViewById(R.id.password_data) ;
        phone = (TextView) v.findViewById(R.id.phone_edit_profile);
        county = (TextView) v.findViewById(R.id.county_data);
        city = (TextView) v.findViewById(R.id.city_data);
        changepassword = (TextView) v.findViewById(R.id.changepassword) ;
        nextofkin = (TextView) v.findViewById(R.id.nextofkin_data);
        profile_pic = (ImageView) v.findViewById(R.id.app_bar_image);
        options = new BitmapFactory.Options();

    }
    private  void setData(){
        name.setText(name_s);
        phone.setText(phone_s);
        county.setText(county_s);
        city.setText(city_s);
        profile_pic.setImageBitmap(image);
        nextofkin.setText(nextofkin_s);
        password.setText(password_s);
    }
    private void getdata(){
        if(SaveSharedPreference.getName(getActivity()) != null){
            name_s = SaveSharedPreference.getName(getActivity());
        }else {
            name_s = "John Doe";
        }
            if(SaveSharedPreference.getPhone(getActivity()) != null){
                phone_s = SaveSharedPreference.getPhone(getActivity());
            }else {
                phone_s = "John Doe";
            }
                if(SaveSharedPreference.getPrefCounty(getActivity()) != null){
                    county_s = SaveSharedPreference.getPrefCounty(getActivity());
                }else {
                    county_s = "John Doe";
                }
                    if(SaveSharedPreference.getPrefCity(getActivity()) != null){
                        city_s = SaveSharedPreference.getPrefCity(getActivity());
                    }else {
                        city_s = "John Doe";
                    }
                        if(SaveSharedPreference.getPrefNextofkin(getActivity()) != null){
                            nextofkin_s = SaveSharedPreference.getPrefNextofkin(getActivity());
                        }else {
                            nextofkin_s = "John Doe";
                        }

                            if(SaveSharedPreference.getPrefImage(getActivity()) != null){
                                image_s = SaveSharedPreference.getPrefImage(getActivity());
                                imagearr =  Base64.decode(image_s, Base64.DEFAULT);
                                image = BitmapFactory.decodeByteArray(imagearr, 0, imagearr.length);
                                password_s = SaveSharedPreference.getPrefPassword(getActivity());
                            }else {
                               //Else for image
                            }

    }
}
