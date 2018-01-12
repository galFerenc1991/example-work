package com.ferenc.pamp.presentation.screens.main.profile.edit_profile.change_password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BaseActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by
 * Ferenc on 2018.01.10..
 */

@EActivity(R.layout.activity_change_password)
public class ChangePasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            initFragment();
    }

    protected void initFragment() {
        replaceFragment(ChangePasswordFragment_.builder().build());
    }

    @Override
    protected int getContainer() {
        return R.id.flContainer_ACP;
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @Click(R.id.ivBack_AP)
    protected void backPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }
}
