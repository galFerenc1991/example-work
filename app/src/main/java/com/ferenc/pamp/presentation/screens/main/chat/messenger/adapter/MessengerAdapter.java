package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactHeaderVH;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactTypeHeaderVH;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactVH;
import com.ferenc.pamp.presentation.utils.Constants;
import com.michenko.simpleadapter.RecyclerAdapter;
import com.michenko.simpleadapter.RecyclerVH;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shonliu on 12/13/17.
 */
@EBean
public class MessengerAdapter extends RecyclerAdapter<MessagesDH> {

    @RootContext
    protected Context context;

    private List<MessageResponse> messageResponseList = new ArrayList<>();

    @NonNull
    @Override
    protected RecyclerVH<MessagesDH> createVH(View view, int viewType) {
        switch (viewType) {
            case Constants.DEFAULT_MSG_GROUP_TYPE:
                return new DefaultMessageVH(view);

            case Constants.M1_MSG_GROUP_TYPE:
                return new GoodDealDiffusionVH(view);

            case Constants.M2_M3_M4_MSG_GROUP_TYPE:
                return new OrderingStateVH(view);

            case Constants.M5_M6_M9_M12_MSG_GROUP_TYPE:
                return new GoodDealStateVH(view);

            case Constants.M8_M10_MSG_GROUP_TYPE:
                return new GoodDealEndingVH(view);

            case Constants.M11_1_M11_2_M11_3_MSG_GROUP_TYPE:
                return new GoodDealConfirmationVH(view);

            default:
                throw new RuntimeException("MessengerAdapter :: createVH [Can find such view type]");
        }
    }

    @Override
    protected int getLayoutRes(int viewType) {
        switch (viewType) {
            case Constants.DEFAULT_MSG_GROUP_TYPE:
                return R.layout.item_message_default;

            case Constants.M1_MSG_GROUP_TYPE:
                return R.layout.item_message_good_deal_diffusion;

            case Constants.M2_M3_M4_MSG_GROUP_TYPE:
                return R.layout.item_message_ordering_state;

            case Constants.M5_M6_M9_M12_MSG_GROUP_TYPE:
                return R.layout.item_message_good_deal_state;

            case Constants.M8_M10_MSG_GROUP_TYPE:
                return R.layout.item_message_good_deal_ending;

            case Constants.M11_1_M11_2_M11_3_MSG_GROUP_TYPE:
                return R.layout.item_message_good_deal_confirmation;

            default:
                throw new RuntimeException("MessengerAdapter :: getLayoutRes [Can find such view type]");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getMsgGroupType();
    }



}
