package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.data.api.RestConst;
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
        civInterlocutorAvatar = (CircleImageView) itemView.findViewById(R.id.civInterlocutorAvatar_IMD);
        civMyAvatar = (CircleImageView) itemView.findViewById(R.id.civMyAvatar_IMD);
        tvMsgAuthorName = (TextView) itemView.findViewById(R.id.tvMsgAuthorName_IMD);
        tvMsgText = (TextView) itemView.findViewById(R.id.tvMsgText_IMD);
        cvMsgBackground = (CardView) itemView.findViewById(R.id.cvMsgBackground_IMD);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {


    }

    @Override
    public void bindData(MessagesDH data) {
        if (data.getMessageResponse().user.getId().equals(data.getMyUser().getId())) {
            Picasso.with(PampApp_.getInstance())
                    .load(data.getMyUser().getAvatarUrl())
                    .placeholder(R.drawable.ic_userpic)
                    .error(R.drawable.ic_userpic)
                    .into(civMyAvatar);
            civMyAvatar.setVisibility(View.VISIBLE);
            civInterlocutorAvatar.setVisibility(View.GONE);
            cvMsgBackground.setCardBackgroundColor(PampApp_.getInstance().getResources().getColor(R.color.msgMyGoodDealDiffusionColor));
            tvMsgAuthorName.setText(data.getMyUser().getFirstName());
        } else {
            Picasso.with(PampApp_.getInstance())
                    .load(data.getMessageResponse().user.getAvatarUrl())
                    .placeholder(R.drawable.ic_userpic)
                    .error(R.drawable.ic_userpic)
                    .into(civInterlocutorAvatar);
            civInterlocutorAvatar.setVisibility(View.VISIBLE);
            civMyAvatar.setVisibility(View.GONE);
            cvMsgBackground.setCardBackgroundColor(PampApp_.getInstance().getResources().getColor(R.color.msgGoodDealDiffusionColorOther));
            tvMsgAuthorName.setText(data.getMessageResponse().user.getFirstName());

        }
        tvMsgText.setText(data.getMessageResponse().text);
    }
}
