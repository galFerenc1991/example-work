package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;


import com.ferenc.pamp.R;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shonliu on 12/13/17.
 */

public class DefaultMessageVH extends RecyclerVH<MessagesDH> {


    private CircleImageView civInterlocutorAvatar;
    private CircleImageView civMyAvatar;
    private TextView tvMsgAuthorName;
    private TextView tvMsgText;
    private CardView cvMsgBackground;


    DefaultMessageVH(View itemView) {
        super(itemView);
        civInterlocutorAvatar   = itemView.findViewById(R.id.civInterlocutorAvatar_IMD);
        civMyAvatar             = itemView.findViewById(R.id.civMyAvatar_IMD);
        tvMsgAuthorName         = itemView.findViewById(R.id.tvMsgAuthorName_IMD);
        tvMsgText               = itemView.findViewById(R.id.tvMsgText_IMD);
        cvMsgBackground         = itemView.findViewById(R.id.cvMsgBackground_IMD);

    }

    @Override
    public void setListeners(OnCardClickListener listener) {


    }

    @Override
    public void bindData(MessagesDH data) {
        MessageResponse messageResponse = data.getMessageResponse();
        Context context = data.getContext();
        User user = data.getMyUser();



        if (messageResponse.user.getId().equals(user.getId())) {

            Picasso.with(context)
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.ic_userpic)
                    .error(R.drawable.ic_userpic)
                    .into(civMyAvatar);

            civMyAvatar             .setVisibility(View.VISIBLE);
            civInterlocutorAvatar   .setVisibility(View.GONE);
            cvMsgBackground         .setCardBackgroundColor(context.getResources().getColor(R.color.msgMyGoodDealDiffusionColor));
            tvMsgAuthorName         .setText(user.getFirstName());

        } else {

            Picasso.with(context)
                    .load(messageResponse.user.getAvatarUrl())
                    .placeholder(R.drawable.ic_userpic)
                    .error(R.drawable.ic_userpic)
                    .into(civInterlocutorAvatar);

            civInterlocutorAvatar   .setVisibility(View.VISIBLE);
            civMyAvatar             .setVisibility(View.GONE);
            cvMsgBackground         .setCardBackgroundColor(context.getResources().getColor(R.color.msgGoodDealDiffusionColorOther));
            tvMsgAuthorName         .setText(messageResponse.user.getFirstName());

        }
        tvMsgText.setText(messageResponse.text);
    }
}
