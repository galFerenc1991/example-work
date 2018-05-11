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

public class ContactHeaderVH extends RecyclerVH<ContactDH> {

    private TextView tvHeader;

    public ContactHeaderVH(View itemView) {
        super(itemView);
        tvHeader = findView(R.id.tvHeader_ICONH);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(ContactDH data) {
        tvHeader.setText(data.getHeader());
    }

}
