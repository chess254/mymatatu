package com.mymatatu.appdecsiptionscreen.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mymatatu.Login_SharedPrefrences.SaveSharedPreference;
import com.mymatatu.R;
import com.mymatatu.RegisterActivity;
import com.mymatatu.appdecsiptionscreen.model.AppDesciptionModel;
import com.mymatatu.appdecsiptionscreen.model.AppDescriptionProvider;

import java.util.ArrayList;

/**
 * Created by S.V. on 8/1/2017.
 */

public class AppDescriptionActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    TextView skip, next, indicator1, indicator2, indicator3, indicator4;
    int selectedPosition;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_description);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        skip = (TextView) findViewById(R.id.skip);
        next = (TextView) findViewById(R.id.next);
        indicator1 = (TextView) findViewById(R.id.indicator1);
        indicator2 = (TextView) findViewById(R.id.indicator2);
        indicator3 = (TextView) findViewById(R.id.indicator3);
        indicator4 = (TextView) findViewById(R.id.indicator4);
        next.setOnClickListener(this);
        skip.setOnClickListener(this);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                selectedPosition = position;
                if (position == 3) {
                    next.setText("Login");
                } else {
                    next.setText("Next");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.next && selectedPosition < 3) {
            selectedPosition = selectedPosition + 1;
            viewPager.setCurrentItem(selectedPosition);
            return;
        }

        Intent intent = new Intent(this, RegisterActivity.class);
        SaveSharedPreference.setPrefAppDescription(getApplicationContext(), true);
        startActivity(intent);
        finish();
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        AppDescriptionProvider appDescriptionProvider = new AppDescriptionProvider();
        ArrayList<AppDesciptionModel> allData = appDescriptionProvider.allAppdescription();
        int pagerCount = allData.size();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            AppDesciptionFragment appDesciptionFragment = new AppDesciptionFragment().newInstance(allData.get(position));
            return appDesciptionFragment;
        }

        @Override
        public int getCount() {
            return pagerCount;
        }
    }


    void setIndicator(int position) {
        if (position == 0) {
            indicator1.setBackgroundResource(R.drawable.selected_dot);
            indicator2.setBackgroundResource(R.drawable.default_dot);
            indicator3.setBackgroundResource(R.drawable.default_dot);
            indicator4.setBackgroundResource(R.drawable.default_dot);
        } else if (position == 1) {

            indicator1.setBackgroundResource(R.drawable.default_dot);
            indicator2.setBackgroundResource(R.drawable.selected_dot);
            indicator3.setBackgroundResource(R.drawable.default_dot);
            indicator4.setBackgroundResource(R.drawable.default_dot);

        } else if (position == 2) {
            indicator1.setBackgroundResource(R.drawable.default_dot);
            indicator2.setBackgroundResource(R.drawable.default_dot);
            indicator3.setBackgroundResource(R.drawable.selected_dot);
            indicator4.setBackgroundResource(R.drawable.default_dot);

        } else if (position == 3) {

            indicator1.setBackgroundResource(R.drawable.default_dot);
            indicator2.setBackgroundResource(R.drawable.default_dot);
            indicator3.setBackgroundResource(R.drawable.default_dot);
            indicator4.setBackgroundResource(R.drawable.selected_dot);

        }


    }
}