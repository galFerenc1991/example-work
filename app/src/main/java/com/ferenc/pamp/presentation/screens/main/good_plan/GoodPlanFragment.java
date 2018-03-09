package com.ferenc.pamp.presentation.screens.main.good_plan;

import android.support.design.widget.TabLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RelativeLayout;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.tabs.ContentTabsFragment;
import com.ferenc.pamp.presentation.base.tabs.TabPagerAdapter;
import com.ferenc.pamp.presentation.screens.main.good_plan.proposed.ProposedPlansFragment_;
import com.ferenc.pamp.presentation.screens.main.good_plan.received.ReceivedPlansFragment_;
import com.ferenc.pamp.presentation.screens.main.good_plan.warning_relay.WarningRelay;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.17..
 */

@EFragment
public class GoodPlanFragment extends ContentTabsFragment {

    @ViewById(R.id.rlTitle_FCT)
    protected RelativeLayout rlTitle;
    @StringRes(R.string.title_received_plans)
    protected String receivedTabTitle;
    @StringRes(R.string.title_proposed_plans)
    protected String proposedTabTitle;

    private CompositeDisposable mCompositeDisposable;

    @Bean
    protected WarningRelay mWarningRelay;

    @Override
    public void addFragmentsToAdapter(TabPagerAdapter adapter) {
        adapter.addFragment(ReceivedPlansFragment_.builder().build(), receivedTabTitle);
        adapter.addFragment(ProposedPlansFragment_.builder().build(), proposedTabTitle);
    }

    @AfterViews
    public void configViewPager() {
        mCompositeDisposable = new CompositeDisposable();
        rlTitle.setVisibility(View.GONE);
        vpContent.setPagingEnabled(false);
        vpContent.setCurrentItem(1);
        setWarningBadge();
    }

    private void setWarningBadge() {
        TabLayout.Tab proposeTab = tlTabs.getTabAt(1);
        mCompositeDisposable.add(mWarningRelay.proposeRelay.subscribe(aBoolean -> {
            if (aBoolean) {
                Spannable text = new SpannableString("Bons plans proposé  ●");
                text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorWarning)), 20, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                proposeTab.setText(text);

            } else proposeTab.setText(proposedTabTitle);

        }));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_content_tabs;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
