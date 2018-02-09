package com.ferenc.pamp.presentation.base;

import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.screens.main.chat.participants.participants_list.ParticipantsListFragment_;
import com.ferenc.pamp.presentation.screens.main.propose.share.ShareFragment_;
import com.ferenc.pamp.presentation.utils.ToolbarManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SystemService;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

@EActivity
public abstract class BaseActivity extends AppCompatActivity {


    private boolean doubleBackToExitPressedOnce = false;

    @SystemService
    protected InputMethodManager inputMethodManager;
    @Bean
    protected ToolbarManager toolbarManager;

    @IdRes
    protected abstract int getContainer();
    protected abstract Toolbar getToolbar();

    @AfterViews
    public void initToolbar() {
        toolbarManager.init(this, getToolbar());

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if(getSupportFragmentManager().getBackStackEntryCount() > 1) {
                toolbarManager.showHomeButton(true);
            }
        });
    }

    public void replaceFragment(BaseFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(getContainer(), fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    public void replaceFragmentClearBackstack(BaseFragment fragment) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        replaceFragment(fragment);
    }

    public void replaceFragmentWithOutBackstack(BaseFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(getContainer(), fragment)
                .commit();
    }

    public void replaceFragmentAllowingStateLoss(BaseFragment fragment) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(getContainer(), fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            if (doubleBackToExitPressedOnce) {
                finish();
            } else {
                doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        }

//        if (doubleBackToExitPressedOnce) {
//            if(getSupportFragmentManager().getBackStackEntryCount() == 1) finish();
//            else super.onBackPressed();
//            return;
//        }
//
//        doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideKeyboard();
    }

    protected void hideKeyboard() {
        if (getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public ToolbarManager getToolbarManager() {
        return toolbarManager;
    }
}
