package com.kubator.pamp.presentation.screens.main.propose.share.adapter;

import android.view.View;
import android.widget.TextView;

import com.kubator.pamp.R;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public class ContactTypeHeaderVH extends RecyclerVH<ContactDH> {
    private TextView tvContactTypeHeader;

    public ContactTypeHeaderVH(View itemView) {
        super(itemView);
        tvContactTypeHeader = findView(R.id.tvContactTypeHeader_ICTH);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(ContactDH data) {
        tvContactTypeHeader.setText(data.getHeader());
    }
}
