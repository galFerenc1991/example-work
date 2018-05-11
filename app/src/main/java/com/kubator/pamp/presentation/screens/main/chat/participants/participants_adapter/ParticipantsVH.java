package com.kubator.pamp.presentation.screens.main.chat.participants.participants_adapter;

import android.view.View;
import android.widget.TextView;

import com.kubator.pamp.R;
import com.michenko.simpleadapter.OnCardClickListener;
import com.michenko.simpleadapter.RecyclerVH;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shonliu on 12/22/17.
 */

public class ParticipantsVH extends RecyclerVH<ParticipantsDH> {


    private CircleImageView civParticipantAvatar;
    private TextView tvParticipantName;

    public ParticipantsVH(View itemView) {
        super(itemView);
        civParticipantAvatar = itemView.findViewById(R.id.civParticipantAvatar_IP);
        tvParticipantName = itemView.findViewById(R.id.tvParticipantName_IP);
    }

    @Override
    public void setListeners(OnCardClickListener listener) {

    }

    @Override
    public void bindData(ParticipantsDH data) {

        Picasso.with(data.getContext())
                .load(data.getUser().getAvatarUrl())
                .placeholder(R.drawable.ic_userpic)
                .error(R.drawable.ic_userpic)
                .into(civParticipantAvatar);
        tvParticipantName.setText(data.getUser().getFullName());
    }
}
