package com.ferenc.pamp.presentation.screens.main.chat.participants;


import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.screens.main.chat.participants.participants_adapter.ParticipantsAdapter;
import com.ferenc.pamp.presentation.screens.main.chat.participants.participants_list.ParticipantsListFragment_;
import com.ferenc.pamp.presentation.screens.main.propose.share.ShareFragment_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

/**
 * Created by shonliu on 12/22/17.
 */
@EActivity(R.layout.activity_participants)
public class ParticipantsActivity extends BaseActivity {


    @Extra
    protected int fromWhere;

    @StringRes(R.string.title_participants_list)
    protected String mTitleParticipantsList;

    @StringRes(R.string.title_add_participants)
    protected String mTitleAddParticipants;

    @ViewById(R.id.tvToolBarName_AP)
    protected TextView tvToolbarTitle;

    @ViewById(R.id.btnAjouter_AP)
    protected Button btnAjouter;

    @ViewById(R.id.ivBack_AP)
    protected ImageView ivBack;

    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;

    @Bean
    protected ParticipantsAdapter mParticipantsAdapter;

    @AfterViews
    protected void initUI() {
        initClickListener();
        replaceParticipantsFragment(fromWhere);
    }

    @Override
    protected int getContainer() {
        return R.id.fragmentContainer_AP;
    }

    @Override
    protected Toolbar getToolbar() {
        return new Toolbar(this);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer_AP) instanceof ShareFragment_)
            replaceParticipantsFragment(fromWhere);
        else
            finish();
    }

    private void initClickListener() {
        RxView.clicks(btnAjouter)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> replaceShareFragment());

        RxView.clicks(ivBack)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> onBackPressed());
    }

    private void replaceShareFragment() {
        setToolbarTitle(mTitleAddParticipants);
        btnAjouter.setVisibility(View.GONE);
        replaceFragmentClearBackstack(ShareFragment_.builder().isReBroadcastFlow(false).isUpdateGoodDeal(true).build());
    }

    private void replaceParticipantsFragment(int fromWhere) {
        if (fromWhere == Constants.ITEM_TYPE_REUSE) {
            btnAjouter.setVisibility(View.VISIBLE);
        }
        setToolbarTitle(mTitleParticipantsList);
        replaceFragmentClearBackstack(ParticipantsListFragment_.builder().build());
    }

    private void setToolbarTitle(String toolbarTitle) {
        tvToolbarTitle.setText(toolbarTitle);
    }


}
