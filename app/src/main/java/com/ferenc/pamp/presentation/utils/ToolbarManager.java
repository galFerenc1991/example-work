package com.ferenc.pamp.presentation.utils;

import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BaseActivity;

import org.androidannotations.annotations.EBean;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

@EBean
public class ToolbarManager {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private View.OnClickListener mNavigationClickListener;
    private float pxToolbarElevation;

    /**
     * Should be called after UI initialized
     */
    public void init(BaseActivity activity, Toolbar toolbar) {
        this.toolbar = toolbar;
        activity.setSupportActionBar(toolbar);
        actionBar = activity.getSupportActionBar();

        mNavigationClickListener = v -> activity.onBackPressed();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        Resources r = activity.getResources();
        pxToolbarElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
    }

    public void showHomeButton(boolean show) {
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(show);
        }
    }

    public void showHomeAsUp(boolean isShow) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isShow);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    public void setIconHome(int _resource) {
        if (actionBar != null)
            actionBar.setHomeAsUpIndicator(_resource);
    }

    public void hideToolbar(boolean isHide) {
        actionBar.hide();
    }

    public void showToolbar(boolean isShow) {
        actionBar.show();
    }


    public View.OnClickListener getNavigationClickListener(boolean need) {
        return need ? mNavigationClickListener : null;
    }

    public void setTitle(@StringRes int title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void setTitle(CharSequence title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void enableToolbarElevation(boolean isEnabled) {
        actionBar.setElevation(isEnabled ? pxToolbarElevation : 0);
    }

    public void enableCrossButton(boolean isEnabled) {
        if (actionBar != null) {
            if (isEnabled) {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_next_ic);
            } else {
                actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_next_ic);
            }
        }
    }

    public void closeActivityWhenBackArrowPressed(BaseActivity _activity) {
        toolbar.setNavigationOnClickListener(view -> {
            _activity.finish();
        });
    }

    public void superBackWhenBackArrowPressed(BaseActivity _activity, boolean isSuper) {
        toolbar.setNavigationOnClickListener(view -> {
            if (!isSuper)
            _activity.finish();
            else _activity.onBackPressed();
        });
    }

    public void displayToolbar(boolean isShown) {
        toolbar.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }

}
