package com.ferenc.pamp.presentation.screens.main.chat.participants.participants_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.ferenc.pamp.R;
import com.michenko.simpleadapter.RecyclerAdapter;
import com.michenko.simpleadapter.RecyclerVH;

import org.androidannotations.annotations.EBean;

/**
 * Created by shonliu on 12/22/17.
 */
@EBean
public class ParticipantsAdapter extends RecyclerAdapter<ParticipantsDH> {

    @NonNull
    @Override
    protected RecyclerVH<ParticipantsDH> createVH(View view, int viewType) {
        return new ParticipantsVH(view);
    }

    @Override
    protected int getLayoutRes(int viewType) {
        return R.layout.item_participant;
    }
}
