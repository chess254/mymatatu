package com.mymatatu.App_Importants;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mymatatu.R;


/**
 * Created by anonymous on 31-07-2017.
 */

public class ViewPagereAdapter extends PagerAdapter {

    private int[] images = {
            R.drawable.android_authority,
            R.drawable.android_authority,
            R.drawable.android_authority,
            R.drawable.android_authority,
            R.drawable.android_authority,
    };

    private String[] title = {
            "Halts",
            "IMG 2",
            "IMG 3",
            "IMG 4",
            "IMG 5",
    };

    Context context;
    LayoutInflater layoutInflater;

    public ViewPagereAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View item = layoutInflater.inflate(R.layout.layout, null);

        ImageView view = (ImageView) item.findViewById(R.id.imageView);

        view.setImageResource(images[position]);

        container.addView(item);

        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
