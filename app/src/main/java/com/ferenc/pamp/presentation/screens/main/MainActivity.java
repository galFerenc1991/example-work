package com.ferenc.pamp.presentation.screens.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.screens.auth.AuthFragment_;
import com.ferenc.pamp.presentation.screens.main.home.HomeFragment_;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
    }

    protected void initFragment() {
        replaceFragment(HomeFragment_.builder().build());
    }

    @Override
    protected int getContainer() {
        return R.id.flContainer_AM;
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }
}
