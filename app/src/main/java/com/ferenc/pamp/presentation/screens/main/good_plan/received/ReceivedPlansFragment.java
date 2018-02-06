package com.ferenc.pamp.presentation.screens.main.good_plan.received;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.domain.GoodDealRepository;
import com.ferenc.pamp.presentation.base.list.EndlessScrollListener;
import com.ferenc.pamp.presentation.base.refreshable.RefreshableFragment;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;
import com.ferenc.pamp.presentation.screens.main.chat.chat_relay.ReceivedRefreshRelay;
import com.ferenc.pamp.presentation.screens.main.good_plan.good_plan_adapter.GoodPlanAdapter;
import com.ferenc.pamp.presentation.screens.main.good_plan.received.re_diffuser.ReDiffuserActivity_;
import com.ferenc.pamp.presentation.screens.main.good_plan.received.receive_relay.ReceiveRelay;
import com.ferenc.pamp.presentation.screens.main.good_plan.warning_relay.WarningRelay;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */
@EFragment
public class ReceivedPlansFragment extends RefreshableFragment implements ReceivedPlansContract.View {
    @Override
    public void setPresenter(ReceivedPlansContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_received_plans;
    }

    @Override
    protected RefreshablePresenter getPresenter() {
        return mPresenter;
    }

    private ReceivedPlansContract.Presenter mPresenter;
    protected EndlessScrollListener mScrollListener;

    @ViewById(R.id.rvReceivedPlans_FRP)
    protected RecyclerView rvReceivedPlans;

    @Bean
    protected GoodDealRepository mGoodDealRepository;
    @Bean
    protected GoodPlanAdapter mGoodPlanAdapter;
    @Bean
    protected ReceiveRelay mReceiveRelay;
    @Bean
    protected WarningRelay mWarningRelay;

    @Bean
    protected ReceivedRefreshRelay mReceiveRefreshRelay;

    @StringRes(R.string.msg_share)
    protected String mShareMessage;

    @AfterInject
    @Override
    public void initPresenter() {
        new ReceivedPlansPresenter(this, mGoodDealRepository, mReceiveRelay, mReceiveRefreshRelay, mWarningRelay);
    }

    @AfterViews
    protected void initUI() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mScrollListener = new EndlessScrollListener(layoutManager, () -> {
            if (isRefreshing()) return false;
            mPresenter.loadNextPage();
            return true;
        });

        rvReceivedPlans.setLayoutManager(layoutManager);
        mGoodPlanAdapter.setAdapterItemType(Constants.ITEM_TYPE_RE_BROADCAST);
        rvReceivedPlans.setAdapter(mGoodPlanAdapter);
        rvReceivedPlans.addOnScrollListener(mScrollListener);

        RxView.clicks(btnPlaceholderAction1_VC)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> checkSendSMSPermission());

        mPresenter.subscribe();
    }

    @Override
    public void openReBroadcastFlow() {
        ReDiffuserActivity_.intent(mActivity).start();
    }

    @Override
    public void setReceivedGoodPlanList(List<GoodDealResponse> _receivedGoodPlansList) {
        mScrollListener.reset();
        mGoodPlanAdapter.setList(_receivedGoodPlansList);
    }

    @Override
    public void addReceivedGoodPlanList(List<GoodDealResponse> _receivedGoodPlansList) {
        mGoodPlanAdapter.addListDH(_receivedGoodPlansList);
    }

    @Override
    public void sharePlayStoreLincInSMS() {
        Intent it = new Intent(Intent.ACTION_SENDTO);
        it.setData(Uri.parse("smsto:"));
        it.putExtra("sms_body", mShareMessage);
        startActivity(it);
    }

    @Override
    public boolean isSendSMSPermissionNotGranted() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void checkSendSMSPermission() {
        if (isSendSMSPermissionNotGranted()) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, Constants.REQUEST_CODE_SEND_SMS);
        } else {
            mPresenter.sharePlayStoreLincInSMS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.sharePlayStoreLincInSMS();
            } else {
                ToastManager.showToast("Please, allow for PAMP access to send SMS.");
            }
        }
    }

    @Override
    public void showPlaceholder(Constants.PlaceholderType placeholderType) {
        super.showPlaceholder(placeholderType);
        if (placeholderType == Constants.PlaceholderType.EMPTY) {
            tvPlaceholderMessage_VC.setText(R.string.msg_empty_received_good_plans);
            ivPlaceholderImage_VC.setVisibility(View.INVISIBLE);
            btnPlaceholderAction1_VC.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
