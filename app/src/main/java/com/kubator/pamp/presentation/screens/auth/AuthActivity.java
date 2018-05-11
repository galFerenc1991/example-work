package com.kubator.pamp.presentation.screens.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.kubator.pamp.R;
import com.kubator.pamp.presentation.base.BaseActivity;

import org.androidannotations.annotations.EActivity;

/**
 * Created by
 * Ferenc on 2017.11.13..
 */
@EActivity(R.layout.activity_auth)
public class AuthActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            initFragment();
    }

    protected void initFragment() {
        replaceFragment(AuthFragment_.builder().build());
    }

    @Override
    protected int getContainer() {
        return R.id.flContainer_AA;
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }
}
