package com.ferenc.pamp.presentation.screens.main.good_plan.received;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.swipe.util.Attributes;
import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.screens.main.good_plan.proposed.ProposedPlansContract;
import com.ferenc.pamp.presentation.screens.main.good_plan.received.adapter.GoodPlanAdapter;
import com.ferenc.pamp.presentation.utils.Constants;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */
@EFragment
public class ReceivedPlansFragment extends ContentFragment implements ReceivedPlansContract.View {
    @Override
    public void setPresenter(ReceivedPlansContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_received_plans;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private ReceivedPlansContract.Presenter mPresenter;

    @ViewById(R.id.rvReceivedPlans_FRP)
    protected RecyclerView rvReceivedPlans;


    protected GoodPlanAdapter mGoodPlanAdapter;

    private ArrayList<String> mDataSet;

    @AfterInject
    @Override
    public void initPresenter() {
        new ReceivedPlansPresenter(this);
    }

    @AfterViews
    protected void initUI() {
        rvReceivedPlans.setLayoutManager(new LinearLayoutManager(getContext()));

        String[] adapterData = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
        mDataSet = new ArrayList<String>(Arrays.asList(adapterData));
        mGoodPlanAdapter = new GoodPlanAdapter(getContext(), mDataSet);
//        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
        rvReceivedPlans.setAdapter(mGoodPlanAdapter);

        // mPresenter.subscribe();
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
