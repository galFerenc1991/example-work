package com.ferenc.pamp.presentation.screens.main.chat;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.domain.GoodDealRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.custom.SettingsActivity;
import com.ferenc.pamp.presentation.custom.SettingsActivity_;
import com.ferenc.pamp.presentation.screens.main.chat.chat_relay.ProposeRefreshRelay;
import com.ferenc.pamp.presentation.screens.main.chat.chat_relay.ReceivedRefreshRelay;
import com.ferenc.pamp.presentation.screens.main.chat.participants.ParticipantsActivity_;
import com.ferenc.pamp.presentation.screens.main.good_plan.proposed.propose_relay.ProposeRelay;
import com.ferenc.pamp.presentation.screens.main.good_plan.received.re_diffuser.ReDiffuserActivity_;
import com.ferenc.pamp.presentation.screens.main.good_plan.received.receive_relay.ReceiveRelay;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.SignedUserManager;
import com.jakewharton.rxbinding2.view.RxView;


import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.ferenc.pamp.presentation.utils.Constants.REQUEST_CODE_SETTINGS_ACTIVITY;

/**
 * Created by shonliu on 12/13/17.
 */

@EActivity(R.layout.activity_chat)
public class ChatActivity extends BaseActivity implements ChatContract.View {

    private ChatContract.Presenter mPresenter;

    @Extra
    protected int fromWhere;

    @Extra
    protected String mDealId;

    @ViewById(R.id.ivBack_AC)
    protected ImageView ivBack;

    @ViewById(R.id.llBonPlanParticipants_AC)
    protected LinearLayout llBonPlanParticipants;

    @ViewById(R.id.ivShare_AC)
    protected ImageView ivShareBonPlan;

    @ViewById(R.id.ivSettings_AC)
    protected ImageView ivSettings;

    @ViewById(R.id.tvParticipants_AC)
    protected TextView tvParticipants;

    @ViewById(R.id.pbDealLoad_AC)
    protected ProgressBar pbDealLoad;

    @ViewById(R.id.tvToolBarName_AC)
    protected TextView tvToolBarName;

    @Bean
    protected GoodDealManager mGoodDealManager;

    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;

    @Bean
    protected GoodDealRepository mGoodDealRepository;

    @Bean
    protected ProposeRefreshRelay mProposeRefreshRelay;
    @Bean
    protected ReceivedRefreshRelay mReceiveRefreshRelay;

    @Bean
    protected SignedUserManager mSignedUserManager;


    @AfterViews
    protected void initFragment() {
        initClickListeners();

//        mPresenter.setParticipants();

//        switch (fromWhere) {
//            case Constants.ITEM_TYPE_RE_BROADCAST:
//                ivShareBonPlan.setVisibility(View.VISIBLE);
//                break;
//            case Constants.ITEM_TYPE_REUSE:
//                ivSettings.setVisibility(View.VISIBLE);
//                break;
//            default:
//                throw new RuntimeException("ChatActivity :: initFragment [Can find fromWhere]");
//        }
//        replaceFragment(ChatFragment_.builder().fromWhere(fromWhere).build());

        mPresenter.subscribe();
    }

    @Override
    public void initFromWhere(int _fromWhere) {
        if (fromWhere == 0) {
            fromWhere = _fromWhere;
        }
        initMenuButton();
    }

    private void initMenuButton() {
        switch (fromWhere) {
            case Constants.ITEM_TYPE_RE_BROADCAST:
                ivShareBonPlan.setVisibility(View.VISIBLE);
                break;
            case Constants.ITEM_TYPE_REUSE:
                ivSettings.setVisibility(View.VISIBLE);
                break;
            default:
                throw new RuntimeException("ChatActivity :: initFragment [Can find fromWhere]");
        }
    }

    @Override
    public void showProgress() {
        pbDealLoad.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbDealLoad.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(String _title) {
        tvToolBarName.setText(_title);
    }

    @Override
    public void showChat() {
        replaceFragment(ChatFragment_.builder().fromWhere(fromWhere).build());
    }

    private void initClickListeners() {
        RxView.clicks(ivBack)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> onBackPressed());

        RxView.clicks(ivShareBonPlan)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedShare());

        RxView.clicks(ivSettings)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedSettings());

        RxView.clicks(llBonPlanParticipants)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedParticipants());
    }

    @Override
    public void hideSettings() {
        ivSettings.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideShareButton() {
        ivShareBonPlan.setVisibility(View.INVISIBLE);
    }

    @AfterInject
    @Override
    public void initPresenter() {
        new ChatPresenter(this, mGoodDealManager, mGoodDealResponseManager, mGoodDealRepository, mDealId, mSignedUserManager);
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void shareGoodDeal() {
        ReDiffuserActivity_.intent(this).start();
    }

    @Override
    public void openSettingsDialog() {
        SettingsActivity_.intent(this).startForResult(Constants.REQUEST_CODE_SETTINGS_ACTIVITY);
    }

    @OnActivityResult(Constants.REQUEST_CODE_SETTINGS_ACTIVITY)
    protected void settingsActionResult(int resultCode, Intent data) {
        if (getSupportFragmentManager().getFragments() == null)
            return;

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(Constants.REQUEST_CODE_SETTINGS_ACTIVITY, resultCode, data);
        }
    }

    @Override
    public void showParticipants() {
        ParticipantsActivity_.intent(this).fromWhere(fromWhere).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
    }

    @Override
    public void setParticipants(String participants) {
        tvParticipants.setText(participants);
    }

    @Override
    protected int getContainer() {
        return R.id.fragmentContainer;
    }

    @Override
    protected Toolbar getToolbar() {
        return new Toolbar(this);
    }

    @Override
    public void onBackPressed() {
        mReceiveRefreshRelay.receivedRefreshRelay.accept(true);
        mProposeRefreshRelay.proposeRefreshRelay.accept(true);

//        switch (fromWhere) {
//            case Constants.ITEM_TYPE_RE_BROADCAST:
//                mReceiveRefreshRelay.receivedRefreshRelay.accept(true);
//                break;
//            case Constants.ITEM_TYPE_REUSE:
//                mProposeRefreshRelay.proposeRefreshRelay.accept(true);
//                break;
//        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
