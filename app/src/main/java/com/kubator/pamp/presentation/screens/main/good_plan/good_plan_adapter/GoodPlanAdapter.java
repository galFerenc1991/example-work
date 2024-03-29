package com.kubator.pamp.presentation.screens.main.good_plan.good_plan_adapter;

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
import com.kubator.pamp.PampApp_;
import com.kubator.pamp.R;
import com.kubator.pamp.data.api.RestConst;
import com.kubator.pamp.data.model.home.good_deal.GoodDealRequest;
import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.kubator.pamp.presentation.screens.main.chat.ChatActivity_;
import com.kubator.pamp.presentation.screens.main.good_plan.proposed.propose_relay.ProposeRelay;
import com.kubator.pamp.presentation.screens.main.good_plan.received.receive_relay.ReceiveRelay;
import com.kubator.pamp.presentation.utils.Constants;
import com.kubator.pamp.presentation.utils.GoodDealManager;
import com.kubator.pamp.presentation.utils.GoodDealResponseManager;
import com.kubator.pamp.presentation.utils.RoundedTransformation;
import com.kubator.pamp.presentation.utils.SignedUserManager;
import com.kubator.pamp.presentation.utils.ToastManager;
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
        private ImageView ivNotif;

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
            ivNotif = itemView.findViewById(R.id.ivNotif_LIDP);


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
    @Bean
    protected SignedUserManager mSignedUserManager;

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
        viewHolder.tvGoodPlanWith.setText(goodDealResponse.owner.getFirstName());
        viewHolder.tvProductName.setText(goodDealResponse.product);
        if (goodDealResponse.isResend) {
            viewHolder.ivReuseIndicator.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivReuseIndicator.setVisibility(View.INVISIBLE);
        }

        if (goodDealResponse.hasOrders) viewHolder.tvOrderStatus.setVisibility(View.VISIBLE);
        else viewHolder.tvOrderStatus.setVisibility(View.GONE);

        if (mSignedUserManager.getCurrentUser().getId().equals(goodDealResponse.owner.getId())) {
            viewHolder.tvOrderStatus.setText(R.string.my_order_status);
        }

        String state = goodDealResponse.state;
        switch (state) {
            case Constants.STATE_PROGRESS:
                long timeBeforeClose = goodDealResponse.closingDate - System.currentTimeMillis();
                long oneHourInMilliseconds = 60 * 60 * 1000;
                long oneMinuteInMilliseconds = 60 * 1000;
                long timeBeforeCloseInHours = timeBeforeClose / oneHourInMilliseconds;
                long days = timeBeforeCloseInHours / 24;
                long hours = timeBeforeCloseInHours - days * 24;
                long minute = timeBeforeClose / oneMinuteInMilliseconds;

                viewHolder.tvPlanStatus.setVisibility(View.GONE);
                viewHolder.tvTimeBeforeClosing.setVisibility(View.VISIBLE);
                String time;
                if (days == 0 && hours == 0) {
                    time = minute + "m";
                } else {
                    time = days + "j. et " + hours + "h.";
                }
                viewHolder.tvTimeBeforeClosing.setText(time);
                break;
            case Constants.STATE_CLOSED:
                viewHolder.tvPlanStatus.setVisibility(View.GONE);
                viewHolder.tvTimeBeforeClosing.setVisibility(View.VISIBLE);
                viewHolder.tvTimeBeforeClosing.setText(Constants.STATE_CLOSED_FRANCE);
                break;
            case Constants.STATE_CANCELED:
                viewHolder.tvPlanStatus.setVisibility(View.VISIBLE);
                viewHolder.tvTimeBeforeClosing.setVisibility(View.GONE);
                viewHolder.tvPlanStatus.setText(Constants.STATE_CANCELED_FRANCE);
                viewHolder.tvPlanStatus.setTextColor(context.getResources().getColor(R.color.textColorRed));
                break;
            case Constants.STATE_CONFIRM:
                viewHolder.tvPlanStatus.setVisibility(View.VISIBLE);
                viewHolder.tvTimeBeforeClosing.setVisibility(View.GONE);
                viewHolder.tvPlanStatus.setText(Constants.STATE_CONFIRM_FRANCE);
                viewHolder.tvPlanStatus.setTextColor(context.getResources().getColor(R.color.textColorGreen));
                break;
        }

        if (goodDealResponse.getAttention() == null) viewHolder.ivNotif.setVisibility(View.GONE);
        else viewHolder.ivNotif.setVisibility(View.VISIBLE);

        if (mGoodPlanItemType == Constants.ITEM_TYPE_RE_BROADCAST) {
            viewHolder.rlReBroadcast.setOnClickListener(view -> {
                if (goodDealResponse.state.equals(Constants.STATE_PROGRESS)) {
                    if (!goodDealResponse.isResend) {
                        mGoodDealManager.saveGoodDeal(getGoodDealFromReSend(goodDealResponse));
                        mReceiveRelay.receiveRelay.accept(true);
                    } else ToastManager.showToast("Vous avez re-diffusé le bon plan");
                } else ToastManager.showToast("You can not resend CLOSED GOOD DEAL!");
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
//                            .fromWhere(mGoodPlanItemType)
                            .mDealId(goodDealResponse.id)
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
                .setDeliveryStartDate(_goodDealResponse.deliveryStartDate)
                .setDeliveryEndDate(_goodDealResponse.deliveryEndDate)
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
