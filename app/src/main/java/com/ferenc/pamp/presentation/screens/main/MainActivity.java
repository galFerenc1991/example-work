package com.ferenc.pamp.presentation.screens.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.GoodDealRepository;
import com.ferenc.pamp.domain.UserRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.screens.main.chat.ChatActivity_;
import com.ferenc.pamp.presentation.screens.main.chat.chat_relay.ReceivedRefreshRelay;
import com.ferenc.pamp.presentation.screens.main.good_plan.GoodPlanFragment_;
import com.ferenc.pamp.presentation.screens.main.good_plan.proposed.propose_relay.ProposeRelay;
import com.ferenc.pamp.presentation.screens.main.profile.ProfileFragment_;
import com.ferenc.pamp.presentation.screens.main.propose.ProposeFragment_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;

    @Bean
    protected GoodDealRepository mGoodDealRepository;

    @Bean
    protected ProposeRelay mProposeRelay;

    @Bean
    protected ReceivedRefreshRelay mReceiveRefreshRelay;

    @Bean
    protected GoodDealManager mGoodDealManager;

    @Bean
    protected UserRepository mUserRepository;

    @ViewById(R.id.toolbar_AM)
    protected Toolbar mToolBar;
    @ViewById(R.id.tlBottomBar)
    protected TabLayout tlBottomBar;

    @Extra
    protected String mDealId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            initFragment();
        mGoodDealManager.clearGoodDeal();

        FirebaseInstanceId.getInstance().getToken();
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int success = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (success != ConnectionResult.SUCCESS) {
            googleApiAvailability.makeGooglePlayServicesAvailable(this);
        }
    }

    private void initFragment() {
        replaceFragment(GoodPlanFragment_.builder().build());
    }

    @Override
    public void openChat(String _id) {
        ChatActivity_.intent(this).mDealId(_id).start();
    }

    @Override
    public void refreshReceivedList() {
        mReceiveRefreshRelay.receivedRefreshRelay.accept(true);
    }

    @AfterInject
    @Override
    public void initPresenter() {
        new MainPresenter(this, mGoodDealRepository, mProposeRelay, mUserRepository);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @AfterViews
    protected void initBottomBar() {
        mPresenter.subscribe();

        FirebaseDynamicLinks
                .getInstance().getDynamicLink(this.getIntent())
                .addOnSuccessListener(pendingDynamicLinkData -> {
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        String id = "";
                        if (deepLink.getQueryParameter("id") != null) {
                            id = deepLink.getQueryParameter("id");
                        } else {
                            try {
                                id = new URI(deepLink.getQueryParameter("link"))
                                        .getQuery()
                                        .replace("id","")
                                        .replace("=","");
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }

                        mPresenter.connectGoodDeal(id);

                    }
                });

        TabLayout.Tab tab1 = tlBottomBar.newTab();
        tab1.setIcon(R.drawable.selector_bb_tab1);
        tab1.setText("bon plan");

        TabLayout.Tab tab2 = tlBottomBar.newTab();
        tab2.setIcon(R.drawable.selector_bb_tab2);
        tab2.setText("proposer");

        TabLayout.Tab tab3 = tlBottomBar.newTab();
        tab3.setIcon(R.drawable.selector_bb_tab3);
        tab3.setText("compte");

        tlBottomBar.addTab(tab1);
        tlBottomBar.addTab(tab2);
        tlBottomBar.addTab(tab3);
        tlBottomBar.getTabAt(0).select();
        tlBottomBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragmentClearBackstack(GoodPlanFragment_.builder().build());
                        break;
                    case 1:
                        replaceFragmentClearBackstack(ProposeFragment_.builder().isReBroadcastFlow(false).build());
                        break;
                    case 2:
                        replaceFragmentClearBackstack(ProfileFragment_.builder().build());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (mDealId != null) {
            openChat(mDealId);
        }
    }

    @Override
    public void openProposedFlow() {
        tlBottomBar.getTabAt(1).select();
    }

    @Override
    protected int getContainer() {
        return R.id.flContainer_AM;
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolBar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
        mGoodDealManager.clearGoodDeal();
    }
}
