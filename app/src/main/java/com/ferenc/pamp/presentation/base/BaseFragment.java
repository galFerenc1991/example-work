package com.ferenc.pamp.presentation.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SystemService;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

@EFragment
public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;

    @SystemService
    protected InputMethodManager inputMethodManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
        setHasOptionsMenu(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideKeyboard();
    }

    protected void hideKeyboard() {
        if(getView() != null) {
            inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    protected void disableUI(boolean disable) {
        if (getView() != null && getView() instanceof ViewGroup) {
            setEnabled((ViewGroup) getView(), !disable);
        }
    }

    protected void setEnabled(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                setEnabled((ViewGroup) view, enabled);
            }
        }
    }
}

