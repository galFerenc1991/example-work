package com.ferenc.pamp.presentation.screens.main.profile;

import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.AuthRepository;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.screens.auth.AuthActivity_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.12.04..
 */
@EFragment
public class ProfileFragment extends ContentFragment implements ProfileContract.View {

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_profile;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private ProfileContract.Presenter mPresenter;

    @ViewById(R.id.tvName_FP)
    protected TextView tvName;
    @ViewById(R.id.rlProfile_information_FP)
    protected RelativeLayout rlProfileInformation;
    @ViewById(R.id.rlMyGoodPlans_FP)
    protected RelativeLayout rlMyGoodPlans;
    @ViewById(R.id.rlAbout_FP)
    protected RelativeLayout rlAbout;
    @ViewById(R.id.rlShare_FP)
    protected RelativeLayout rlShare;
    @ViewById(R.id.rlConnect_FP)
    protected RelativeLayout rlConnect;
    @ViewById(R.id.rlLogOut_FP)
    protected RelativeLayout rlLogOut;

    @Bean
    protected AuthRepository mAuthRepository;


    @AfterInject
    @Override
    public void initPresenter() {
        new ProfilePresenter(this, mAuthRepository);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(rlLogOut)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedLogOut());
    }

    @Override
    public void logOut() {
        AuthActivity_.intent(this)
                .flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                .start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
