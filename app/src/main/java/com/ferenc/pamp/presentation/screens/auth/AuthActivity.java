package com.ferenc.pamp.presentation.screens.auth;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BaseActivity;

import org.androidannotations.annotations.EActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
