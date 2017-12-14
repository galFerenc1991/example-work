package com.ferenc.pamp.presentation.screens.main.good_plan.good_plan_adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.ferenc.pamp.PampApp;
import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.data.api.RestConst;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.base.models.GoodDeal;
import com.ferenc.pamp.presentation.screens.main.chat.ChatActivity_;
import com.ferenc.pamp.presentation.screens.main.good_plan.proposed.propose_relay.ProposeRelay;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 * Ferenc on 2017.11.22..
 */
@EBean
public class GoodPlanAdapter extends RecyclerSwipeAdapter<GoodPlanAdapter.SimpleViewHolder> {

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        public ImageView ivProfileImage;
        public TextView tvGoodPlanWith;
        public TextView tvProductName;
        public TextView tvOrderStatus;
        public TextView tvPlanStatus;
        public TextView tvTimeBeforeClosing;
        public ImageView ivReuseIndicator;

        public RelativeLayout rlReuse;
        public RelativeLayout rlReBroadcast;
        private RelativeLayout rlRootLayout;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivAvatar_LIDP);
            tvGoodPlanWith = (TextView) itemView.findViewById(R.id.tvGoodPlanWith_LIDP);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName_LIDP);
            tvOrderStatus = (TextView) itemView.findViewById(R.id.tvOrderStatus_LIDP);
            tvPlanStatus = (TextView) itemView.findViewById(R.id.tvPlanStatus_LIDP);
            tvTimeBeforeClosing = (TextView) itemView.findViewById(R.id.tvTimeBeforeClosing_LIDP);
            ivReuseIndicator = (ImageView) itemView.findViewById(R.id.ivReuseIndicator_LIDP);
            rlReuse = (RelativeLayout) itemView.findViewById(R.id.rlReuse_VR);
            rlReBroadcast = (RelativeLayout) itemView.findViewById(R.id.rlReBroadcast_VRB);
            rlRootLayout = (RelativeLayout) itemView.findViewById(R.id.rlRootLayout_LIGP);


            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            itemView.setOnClickListener(view -> {
            });
        }
    }

    @Bean
    protected ProposeRelay mProposeRelay;
    @Bean
    protected GoodDealManager mGoodDealManager;
    private List<GoodDealResponse> listGD = new ArrayList<>();
    private int mGoodPlanItemType;
    @RootContext
    protected Context context;

    public GoodPlanAdapter() {

    }

    public void setAdapterItemType(int _goodPlanItemType) {
        mGoodPlanItemType = _goodPlanItemType;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (mGoodPlanItemType == Constants.ITEM_TYPE_RE_BROADCAST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_re_broadcast_swipe, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reuse_swipe, parent, false);

        }
        return new SimpleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(SimpleViewHolder viewHolder, int position) {
        GoodDealResponse goodDealResponse = listGD.get(position);
        Picasso.with(PampApp_.getInstance())
                .load(RestConst.BASE_URL + goodDealResponse.contributor.avatar)
                .placeholder(R.drawable.ic_userpic)
                .error(R.drawable.ic_userpic)
                .fit()
                .centerCrop()
                .into(viewHolder.ivProfileImage);
        viewHolder.tvGoodPlanWith.setText(goodDealResponse.title);
        viewHolder.tvProductName.setText(goodDealResponse.product);
//        if (goodDealResponse.recipients.isEmpty()) {
//            viewHolder.tvOrderStatus.setVisibility(View.INVISIBLE);
//        } else {
//            viewHolder.tvOrderStatus.setVisibility(View.VISIBLE);
//        }
        if (goodDealResponse.isResend) {
            viewHolder.ivReuseIndicator.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivReuseIndicator.setVisibility(View.INVISIBLE);
        }

        if (goodDealResponse.state.equals("progress")) {
            long timeBeforeClose = goodDealResponse.closingDate - System.currentTimeMillis();
            long timeBeforeCloseInHours = timeBeforeClose / (60 * 60 * 1000);
            long days = timeBeforeCloseInHours / 24;
            long hours = timeBeforeCloseInHours - days * 24;

            viewHolder.tvPlanStatus.setVisibility(View.GONE);
            viewHolder.tvTimeBeforeClosing.setVisibility(View.VISIBLE);
            String time = "j" + days + ". et " + hours + ".";
            viewHolder.tvTimeBeforeClosing.setText(time);
        } else if (goodDealResponse.state.equals("closed")) {
            viewHolder.tvPlanStatus.setVisibility(View.GONE);
            viewHolder.tvTimeBeforeClosing.setVisibility(View.VISIBLE);
            viewHolder.tvTimeBeforeClosing.setText(goodDealResponse.state);
        } else {
            viewHolder.tvPlanStatus.setVisibility(View.VISIBLE);
            viewHolder.tvTimeBeforeClosing.setVisibility(View.GONE);
            viewHolder.tvPlanStatus.setText(goodDealResponse.state);
        }

        if (mGoodPlanItemType == Constants.ITEM_TYPE_RE_BROADCAST) {
            viewHolder.rlReBroadcast.setOnClickListener(view -> {

            });
        } else {
            viewHolder.rlReuse.setOnClickListener(view -> {
                mGoodDealManager.saveGoodDeal(getGoodDealFromItem(goodDealResponse));
                mProposeRelay.proposeRelay.accept(true);
            });
        }

        viewHolder.rlRootLayout.setOnClickListener(view ->
                ChatActivity_
                        .intent(context)
                        .fromWhere(mGoodPlanItemType)
                        .goodDealResponse(goodDealResponse)
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .start());

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        mItemManger.bindView(viewHolder.itemView, position);

    }

    private GoodDealRequest getGoodDealFromItem(GoodDealResponse _goodDealResponse) {
//        GoodDealRequest goodDealForReBroadcast = mGoodDealManager.getGoodDeal();
//        goodDealForReBroadcast.product = _goodDealResponse.product;
//        goodDealForReBroadcast.description = _goodDealResponse.description;
//        goodDealForReBroadcast.price = _goodDealResponse.price;
//        goodDealForReBroadcast.unit = _goodDealResponse.unit;
//        goodDealForReBroadcast.quantity = _goodDealResponse.quantity;
//        goodDealForReBroadcast.closingDate = _goodDealResponse.closingDate;

        GoodDealRequest goodDealRequest = new GoodDealRequest.Builder()
                .setProduct(_goodDealResponse.product)
                .setDescription(_goodDealResponse.description)
                .setPrice(_goodDealResponse.price)
                .setUnit(_goodDealResponse.unit)
                .setQuantity(_goodDealResponse.quantity)
                .setClosingDate(_goodDealResponse.closingDate)
                .setDeliveryAddress(_goodDealResponse.deliveryAddress)
                .setDeliveryStartDate(_goodDealResponse.deliveryStartDate)
                .setDeliveryEndDate(_goodDealResponse.deliveryEndDate)
                .build();
        return goodDealRequest;
    }

    public void setList(@NonNull List<GoodDealResponse> list) {
        listGD.clear();
        listGD.addAll(list);
        notifyDataSetChanged();
    }

    public void addListDH(List<GoodDealResponse> list) {
        int oldSize = listGD.size();
        listGD.addAll(list);
        notifyItemRangeInserted(oldSize, listGD.size());
    }

    @Override
    public int getItemCount() {
        return listGD.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}
