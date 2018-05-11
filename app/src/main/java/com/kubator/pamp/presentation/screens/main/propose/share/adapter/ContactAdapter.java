package com.kubator.pamp.presentation.screens.main.propose.share.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.kubator.pamp.R;
import com.michenko.simpleadapter.RecyclerAdapter;
import com.michenko.simpleadapter.RecyclerVH;

import org.androidannotations.annotations.EBean;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

@EBean
public class ContactAdapter extends RecyclerAdapter<ContactDH> {

    public static final int TYPE_HEADER = 1;
    public static final int TYPE_CONTACT_HEADER = 2;
    public static final int TYPE_ITEM = 3;

    @NonNull
    @Override
    protected RecyclerVH<ContactDH> createVH(View view, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new ContactHeaderVH(view);
            case TYPE_CONTACT_HEADER:
                return new ContactTypeHeaderVH(view);
            case TYPE_ITEM:
                return new ContactVH(view);
            default:
                throw new RuntimeException("ContactAdapter :: createVH [Can find such view type]");
        }
    }

    @Override
    protected int getLayoutRes(int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return R.layout.item_contact_header;
            case TYPE_CONTACT_HEADER:
                return R.layout.item_contact_type_header;

            case TYPE_ITEM:
                return R.layout.item_contact;
            default:
                throw new RuntimeException("ContactAdapter :: getLayoutRes [Can find such view type]");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }
}
