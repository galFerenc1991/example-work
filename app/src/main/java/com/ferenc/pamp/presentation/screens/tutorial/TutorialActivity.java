package com.ferenc.pamp.presentation.screens.tutorial;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.screens.auth.AuthActivity_;
import com.ferenc.pamp.presentation.screens.tutorial.adapter.TutorialAdapter;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.SharedPrefManager_;
import com.jakewharton.rxbinding2.view.RxView;
import com.rd.PageIndicatorView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.10..
 */
@EActivity(R.layout.activity_tutorial)
public class TutorialActivity extends AppCompatActivity implements TutorialContract.View {

    private TutorialContract.Presenter mPresenter;
    private PagerAdapter mPagerAdapter;

    @ViewById(R.id.vpTutorialContainer_AT)
    protected ViewPager vpTutorialContainer;
    @ViewById(R.id.btnStartUsing_AT)
    protected Button btnStartUsing;
    @ViewById(R.id.indicator_AT)
    protected PageIndicatorView indicator;

    @Pref
    protected SharedPrefManager_ mSharedPrefManager;

    @AfterInject
    @Override
    public void initPresenter() {
        new TutorialPresenter(this, mSharedPrefManager);
        mPagerAdapter = new TutorialAdapter();
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(btnStartUsing)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.skip());
        vpTutorialContainer.setAdapter(mPagerAdapter);
        indicator.setViewPager(vpTutorialContainer);
        vpTutorialContainer.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    btnStartUsing.setVisibility(View.VISIBLE);
                } else {
                    btnStartUsing.setVisibility(View.INVISIBLE);
                }
            }
        });
        mPresenter.subscribe();
    }

    @Override
    public void setPresenter(TutorialContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void openAuth() {
        AuthActivity_.intent(this).start();
    }
}
