package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.data.api.RestConst;
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


    public GoodDealDiffusionVH(View itemView) {
        super(itemView);
        civInterlocutorAvatar = (CircleImageView) itemView.findViewById(R.id.civInterlocutorAvatar_IMGDD);
        civMyAvatar = (CircleImageView) itemView.findViewById(R.id.civMyAvatar_IMGDD);
        cvDealBackground = (CardView) itemView.findViewById(R.id.cvDealBackground_IMGDD);
        tvDealAuthorName = (TextView) itemView.findViewById(R.id.tvDealAuthorName_IMGDD);
        tvDealDescription = (TextView) itemView.findViewById(R.id.tvDealDescription_IMGDD);
        tvDealPriceDescription = (TextView) itemView.findViewById(R.id.tvDealPriceDescription_IMGDD);
        tvDealAmountItems = (TextView) itemView.findViewById(R.id.tvDealAmountItems_IMGDD);
        tvDealStartDate = (TextView) itemView.findViewById(R.id.tvDealStartDate_IMGDD);
        tvDealEndDate = (TextView) itemView.findViewById(R.id.tvDealEndDate_IMGDD);
        tvDealLocation = (TextView) itemView.findViewById(R.id.tvDealLocation_IMGDD);

    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(MessagesDH data) {

        if (data.getGoodDealResponse().contributor.id.equals(data.getMyUser().getId())) {
            Picasso.with(PampApp_.getInstance())
                    .load(data.getMyUser().getAvatarUrl())
                    .placeholder(R.drawable.ic_userpic)
                    .error(R.drawable.ic_userpic)
                    .into(civMyAvatar);
            civMyAvatar.setVisibility(View.VISIBLE);
            cvDealBackground.setCardBackgroundColor(PampApp_.getInstance().getResources().getColor(R.color.msgMyGoodDealDiffusionColor));
            tvDealAuthorName.setText(data.getMyUser().getFirstName());
        } else {
            Picasso.with(PampApp_.getInstance())
                    .load(data.getGoodDealResponse().contributor.getAvatar())
                    .placeholder(R.drawable.ic_userpic)
                    .error(R.drawable.ic_userpic)
                    .into(civInterlocutorAvatar);
            civInterlocutorAvatar.setVisibility(View.VISIBLE);
            cvDealBackground.setCardBackgroundColor(PampApp_.getInstance().getResources().getColor(R.color.msgGoodDealDiffusionColorOther));
            tvDealAuthorName.setText(data.getGoodDealResponse().contributor.firstName);
        }


        tvDealDescription.setText(data.getMessageResponse().text);
        tvDealPriceDescription.setText(data.getGoodDealResponse().description);
        tvDealAmountItems.setText(data.getGoodDealResponse().unit);
        tvDealStartDate.setText(new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.FRANCE).format(new Date(data.getGoodDealResponse().deliveryStartDate)));
        tvDealEndDate.setText(new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.FRANCE).format(new Date(data.getGoodDealResponse().deliveryEndDate)));
        tvDealLocation.setText(data.getGoodDealResponse().deliveryAddress);

    }
}
