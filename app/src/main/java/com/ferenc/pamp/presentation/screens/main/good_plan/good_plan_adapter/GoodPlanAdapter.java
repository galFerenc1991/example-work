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
import com.ferenc.pamp.presentation.screens.main.good_plan.received.receive_relay.ReceiveRelay;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.RoundedTransformation;
import com.ferenc.pamp.presentation.utils.ToastManager;
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

    @RootContext
    protected Context context;
    @Bean
    protected ProposeRelay mProposeRelay;
    @Bean
    protected ReceiveRelay mReceiveRelay;
    @Bean
    protected GoodDealManager mGoodDealManager;
    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;

    private List<GoodDealResponse> listGD = new ArrayList<>();
    private int mGoodPlanItemType;

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
                .load(RestConst.BASE_URL + "/" + goodDealResponse.owner.getAvatar())
                .placeholder(R.drawable.ic_userpic)
                .error(R.drawable.ic_userpic)
                .transform(new RoundedTransformation(200, 0))
                .fit()
                .centerCrop()
                .into(viewHolder.ivProfileImage);
        viewHolder.tvGoodPlanWith.setText(goodDealResponse.title);
        viewHolder.tvProductName.setText(goodDealResponse.product);
        if (goodDealResponse.isResend) {
            viewHolder.ivReuseIndicator.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivReuseIndicator.setVisibility(View.INVISIBLE);
        }

        String state = goodDealResponse.state;
        switch (state) {
            case Constants.STATE_PROGRESS:
                long timeBeforeClose = goodDealResponse.closingDate - System.currentTimeMillis();
                long timeBeforeCloseInHours = timeBeforeClose / (60 * 60 * 1000);
                long days = timeBeforeCloseInHours / 24;
                long hours = timeBeforeCloseInHours - days * 24;

                viewHolder.tvPlanStatus.setVisibility(View.GONE);
                viewHolder.tvTimeBeforeClosing.setVisibility(View.VISIBLE);
                String time = "j" + days + ". et " + hours + ".";
                viewHolder.tvTimeBeforeClosing.setText(time);
                break;
            case Constants.STATE_CLOSED:
                viewHolder.tvPlanStatus.setVisibility(View.GONE);
                viewHolder.tvTimeBeforeClosing.setVisibility(View.VISIBLE);
                viewHolder.tvTimeBeforeClosing.setText(goodDealResponse.state);
                break;
            case Constants.STATE_CANCELED:
                viewHolder.tvPlanStatus.setVisibility(View.VISIBLE);
                viewHolder.tvTimeBeforeClosing.setVisibility(View.GONE);
                viewHolder.tvPlanStatus.setText(goodDealResponse.state);
                viewHolder.tvPlanStatus.setTextColor(context.getResources().getColor(R.color.textColorRed));
                break;
            case Constants.STATE_CONFIRM:
                viewHolder.tvPlanStatus.setVisibility(View.VISIBLE);
                viewHolder.tvTimeBeforeClosing.setVisibility(View.GONE);
                viewHolder.tvPlanStatus.setText(goodDealResponse.state);
                viewHolder.tvPlanStatus.setTextColor(context.getResources().getColor(R.color.textColorGreen));
                break;
        }

        if (mGoodPlanItemType == Constants.ITEM_TYPE_RE_BROADCAST) {
            viewHolder.rlReBroadcast.setOnClickListener(view -> {
                if (goodDealResponse.state.equals(Constants.STATE_PROGRESS)) {
                    mGoodDealManager.saveGoodDeal(getGoodDealFromReSend(goodDealResponse));
                    mReceiveRelay.receiveRelay.accept(true);
                } else ToastManager.showToast("You can not resend CLOSED GOOD DEAL!!!");
            });
        } else {
            viewHolder.rlReuse.setOnClickListener(view -> {
                mGoodDealManager.saveGoodDeal(getGoodDealFromReUse(goodDealResponse));
                mProposeRelay.proposeRelay.accept(true);
            });
        }

        viewHolder.rlRootLayout.setOnClickListener(view -> {
                    mGoodDealManager.saveGoodDeal(getGoodDealFromItem(goodDealResponse));
                    mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                    ChatActivity_
                            .intent(context)
                            .fromWhere(mGoodPlanItemType)
                            .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .start();
                }
        );

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        mItemManger.bindView(viewHolder.itemView, position);

    }

    private GoodDealRequest getGoodDealFromItem(GoodDealResponse _goodDealResponse) {
        return new GoodDealRequest.Builder()
                .setID(_goodDealResponse.id)
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
    }

    private GoodDealRequest getGoodDealFromReUse(GoodDealResponse _goodDealResponse) {
        return new GoodDealRequest.Builder()
                .setID(_goodDealResponse.id)
                .setProduct(_goodDealResponse.product)
                .setDescription(_goodDealResponse.description)
                .setPrice(_goodDealResponse.price)
                .setUnit(_goodDealResponse.unit)
                .setQuantity(_goodDealResponse.quantity)
                .setDeliveryAddress(_goodDealResponse.deliveryAddress)
                .build();
    }

    private GoodDealRequest getGoodDealFromReSend(GoodDealResponse _goodDealResponse) {
        return new GoodDealRequest.Builder()
                .setID(_goodDealResponse.id)
                .setProduct(_goodDealResponse.product)
                .setDescription(_goodDealResponse.description)
                .setPrice(_goodDealResponse.price)
                .setUnit(_goodDealResponse.unit)
                .setQuantity(_goodDealResponse.quantity)
                .setClosingDate(_goodDealResponse.closingDate)
                .build();
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
