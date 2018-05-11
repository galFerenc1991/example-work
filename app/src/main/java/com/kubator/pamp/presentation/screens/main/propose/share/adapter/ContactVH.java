package com.kubator.pamp.presentation.screens.main.propose.share.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kubator.pamp.R;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public class ContactVH extends RecyclerVH<ContactDH> {

    private ImageView ivContactPicture;
    private TextView tvContactName;
    private TextView tvContactNumber;
    private CheckBox cbContactSelect;

    public ContactVH(View itemView) {
        super(itemView);
        ivContactPicture = findView(R.id.ivUserAvatar_ICONT);
        tvContactName = findView(R.id.tvContactName_ICONT);
        tvContactNumber = findView(R.id.tvContactNumber_ICONT);
        cbContactSelect = findView(R.id.cbContactSelected_ICONT);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {
        itemView.setOnClickListener(view -> listener.onClick(itemView, getAdapterPosition(), getItemViewType()));
    }

    @Override
    public void bindData(ContactDH data) {
        tvContactName.setText(data.getUserContact().getName());
        tvContactNumber.setText(data.getUserContact().getPhoneNumber());
        cbContactSelect.setChecked(data.isSelected());
        cbContactSelect.setOnClickListener(view -> {
            if (data.isSelected()) {
                data.setSelected(false);
            } else {
                data.setSelected(true);
            }
        });
    }
}
