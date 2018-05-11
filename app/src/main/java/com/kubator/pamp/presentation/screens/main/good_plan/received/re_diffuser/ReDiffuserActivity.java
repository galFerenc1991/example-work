package com.kubator.pamp.presentation.screens.main.good_plan.received.re_diffuser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.kubator.pamp.R;
import com.kubator.pamp.presentation.base.BaseActivity;
import com.kubator.pamp.presentation.screens.main.propose.ProposeFragment_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by
 * Ferenc on 2017.12.11..
 */
@EActivity(R.layout.activity_re_broadcast)
public class ReDiffuserActivity extends BaseActivity{

    @ViewById(R.id.toolbar_ARB)
    protected Toolbar mToolBar;

    @Override
    protected int getContainer() {
        return R.id.flContainer_ARB;
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolBar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            initFragment();
    }

    private void initFragment() {
        replaceFragment(ProposeFragment_.builder().isReBroadcastFlow(true).build());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
