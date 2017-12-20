package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shonliu on 12/13/17.
 */

public class GoodDealDiffusionVH extends RecyclerVH<MessagesDH> {


    private CircleImageView civInterlocutorAvatar;
    private CircleImageView civMyAvatar;

    private CardView cvDealBackground;
    private TextView tvDealAuthorName;
    private TextView tvDealDescription;
    private TextView tvDealPriceDescription;
    private TextView tvDealAmountItems;
    private TextView tvDealStartDate;
    private TextView tvDealEndDate;
    private TextView tvDealLocation;


    GoodDealDiffusionVH(View itemView) {
        super(itemView);
        civInterlocutorAvatar = itemView.findViewById(R.id.civInterlocutorAvatar_IMGDD);
        civMyAvatar = itemView.findViewById(R.id.civMyAvatar_IMGDD);
        cvDealBackground = itemView.findViewById(R.id.cvDealBackground_IMGDD);
        tvDealAuthorName = itemView.findViewById(R.id.tvDealAuthorName_IMGDD);
        tvDealDescription = itemView.findViewById(R.id.tvDealDescription_IMGDD);
        tvDealPriceDescription = itemView.findViewById(R.id.tvDealPriceDescription_IMGDD);
        tvDealAmountItems = itemView.findViewById(R.id.tvDealAmountItems_IMGDD);
        tvDealStartDate = itemView.findViewById(R.id.tvDealStartDate_IMGDD);
        tvDealEndDate = itemView.findViewById(R.id.tvDealEndDate_IMGDD);
        tvDealLocation = itemView.findViewById(R.id.tvDealLocation_IMGDD);

    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(MessagesDH data) {

        GoodDealResponse goodDealResponse = data.getGoodDealResponse();
        MessageResponse messageResponse = data.getMessageResponse();
        Context context = data.getContext();
        User user = data.getMyUser();
        String deliveryStartDate = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.FRANCE).format(new Date(goodDealResponse.deliveryStartDate));
        String deliveryEndDate = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.FRANCE).format(new Date(goodDealResponse.deliveryEndDate));

        if (goodDealResponse.owner.getId().equals(user.getId())) {
            Picasso.with(context)
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.ic_userpic)
                    .error(R.drawable.ic_userpic)
                    .into(civMyAvatar);
            civMyAvatar.setVisibility(View.VISIBLE);
            cvDealBackground.setCardBackgroundColor(context.getResources().getColor(R.color.msgMyGoodDealDiffusionColor));
            tvDealAuthorName.setText(user.getFirstName());
        } else {
            Picasso.with(context)
                    .load(goodDealResponse.owner.getAvatarUrl())
                    .placeholder(R.drawable.ic_userpic)
                    .error(R.drawable.ic_userpic)
                    .into(civInterlocutorAvatar);
            civInterlocutorAvatar.setVisibility(View.VISIBLE);
            cvDealBackground.setCardBackgroundColor(context.getResources().getColor(R.color.msgGoodDealDiffusionColorOther));
            tvDealAuthorName.setText(goodDealResponse.owner.getFirstName());
        }


        tvDealDescription.setText(messageResponse.text);
        tvDealPriceDescription.setText(goodDealResponse.description);
        tvDealAmountItems.setText(goodDealResponse.unit);
        tvDealStartDate.setText(deliveryStartDate);
        tvDealEndDate.setText(deliveryEndDate);
        tvDealLocation.setText(goodDealResponse.deliveryAddress);

    }
}
