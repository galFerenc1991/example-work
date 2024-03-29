package com.kubator.pamp.presentation.screens.main.chat;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kubator.pamp.R;
import com.kubator.pamp.domain.GoodDealRepository;
import com.kubator.pamp.presentation.base.BaseActivity;
import com.kubator.pamp.presentation.custom.SettingsActivity_;
import com.kubator.pamp.presentation.screens.main.MainActivity_;
import com.kubator.pamp.presentation.screens.main.chat.chat_relay.ProposeRefreshRelay;
import com.kubator.pamp.presentation.screens.main.chat.chat_relay.ReceivedRefreshRelay;
import com.kubator.pamp.presentation.screens.main.chat.participants.ParticipantsActivity_;
import com.kubator.pamp.presentation.screens.main.good_plan.received.re_diffuser.ReDiffuserActivity_;
import com.kubator.pamp.presentation.utils.Constants;
import com.kubator.pamp.presentation.utils.GoodDealManager;
import com.kubator.pamp.presentation.utils.GoodDealResponseManager;
import com.kubator.pamp.presentation.utils.SignedUserManager;
import com.jakewharton.rxbinding2.view.RxView;


import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;


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
    public void hideParticipantTextView() {
        tvParticipants.setVisibility(View.GONE);
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

    @Override
    public void showSharePopUp() {
        View dialogViewTitle = LayoutInflater.from(this)
                .inflate(R.layout.view_resend_pop_up_title, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setCustomTitle(dialogViewTitle)
                .setView(R.layout.view_resend_pop_up_message)
                .setPositiveButton(R.string.button_ok, (dialog, which) -> {
                    mPresenter.clickedShare();
                })
                .setNegativeButton(R.string.button_cancel, (dialogInterface, i) -> {
                })
                .setCancelable(false)
                .create()
                .show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getSupportFragmentManager().getFragments() == null)
            return;

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showParticipants(boolean isDealClosed) {
        ParticipantsActivity_.intent(this).fromWhere(fromWhere).isDealClosed(isDealClosed).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
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
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
