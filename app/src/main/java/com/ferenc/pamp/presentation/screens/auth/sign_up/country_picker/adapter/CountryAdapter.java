package com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.ferenc.pamp.R;
import com.michenko.simpleadapter.RecyclerAdapter;
import com.michenko.simpleadapter.RecyclerVH;

import org.androidannotations.annotations.EBean;

/**
 * Created by
 * Ferenc on 2017.11.20..
 */
@EBean
public class CountryAdapter extends RecyclerAdapter<CountryDH> {

    private static final int TYPE_ITEM = 2;
    private static final int TYPE_HEADER = 1;

    @NonNull
    @Override
    protected RecyclerVH<CountryDH> createVH(View view, int viewType) {
        if (viewType == TYPE_ITEM)
            return new CountryVH(view);
        return new CountryHeaderVH(view);
    }

    @Override
    protected int getLayoutRes(int viewType) {
        if (viewType == TYPE_ITEM)
            return R.layout.item_country;
        return R.layout.item_country_header;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).header != null)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }
}
