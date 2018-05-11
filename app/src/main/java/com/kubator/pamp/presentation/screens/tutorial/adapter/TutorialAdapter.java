package com.kubator.pamp.presentation.screens.tutorial.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kubator.pamp.R;

/**
 * Created by
 * Ferenc on 2017.11.12..
 */

public class TutorialAdapter extends PagerAdapter {

    private static final int PAGE_COUNT = 3;

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = null;
        switch (position) {
            case 0:
                view =  inflater.inflate(R.layout.view_tutorial_first_page, container, false);
                break;
            case 1:
                view = inflater.inflate(R.layout.view_tutorial_second, container, false);
                break;
            case 2:
                view = inflater.inflate(R.layout.view_tutorial_third, container, false);
                break;
//            default:
//                return new RuntimeException("tutorial adapter");
        }
container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
