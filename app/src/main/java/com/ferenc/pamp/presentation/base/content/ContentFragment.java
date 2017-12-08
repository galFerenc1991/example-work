package com.ferenc.pamp.presentation.base.content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BaseFragment;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

@EFragment
public abstract class ContentFragment extends BaseFragment implements ContentView {

    @ViewById
    protected ProgressBar pbMain_VC;
    @ViewById
    protected ProgressBar pbPagination_VC;

    @ViewById
    protected SwipeRefreshLayout swipeContainer_VC;
    @ViewById
    protected FrameLayout flContent_VC;
    @ViewById
    protected RelativeLayout rlPlaceholder_VC;

    @ViewById
    protected TextView tvPlaceholderMessage_VC;
    @ViewById
    protected ImageView ivPlaceholderImage_VC;
    @ViewById
    protected Button btnPlaceholderAction_VC;
    @ViewById
    protected ImageButton btnPlaceholderAction1_VC;

    private Snackbar snackbar;

    protected abstract int getLayoutRes();
    protected abstract BasePresenter getPresenter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.view_content, container, false);
        ViewGroup flContent = (ViewGroup) parent.findViewById(R.id.flContent_VC);
        View.inflate(getActivity(), getLayoutRes(), flContent);
        return parent;
    }

    @AfterViews
    protected void initSnackBar() {
        snackbar = Snackbar.make(flContent_VC.getChildAt(0), "", Snackbar.LENGTH_SHORT);
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);  // show multiple line
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(getPresenter() != null) getPresenter().unsubscribe();
    }

    @AfterViews
    protected void afterViews() {
        RxView.clicks(btnPlaceholderAction_VC)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> onPlaceholderAction());
        swipeContainer_VC.setEnabled(false);
        swipeContainer_VC.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.colorAccent));
    }

    protected void onPlaceholderAction() {
        getPresenter().subscribe();
    }

    @Override
    public void showErrorMessage(Constants.MessageType messageType) {
        showCustomMessage(getString(messageType.getMessageRes()), messageType.isDangerous());
    }

    @Override
    public void showCustomMessage(String msg, boolean isDangerous) {
        hideProgress();
        if(snackbar.isShown()) snackbar.dismiss();
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), isDangerous
                ? R.color.colorRed
                : R.color.colorPrimary));
        snackbar.setText(msg);
        snackbar.setDuration(2000);
        snackbar.show();
    }

    @Override
    public void showPlaceholder(Constants.PlaceholderType placeholderType) {
        hideKeyboard();
        dismissUI();
        rlPlaceholder_VC.setVisibility(View.VISIBLE);
        ivPlaceholderImage_VC.setImageResource(placeholderType.getIconRes());
        tvPlaceholderMessage_VC.setText(placeholderType.getMessageRes());
        btnPlaceholderAction_VC.setVisibility(placeholderType == Constants.PlaceholderType.EMPTY
                ? View.GONE
                : View.VISIBLE);
        btnPlaceholderAction1_VC.setVisibility(placeholderType == Constants.PlaceholderType.EMPTY
                ? View.GONE
                : View.VISIBLE);
    }

    @Override
    public void showProgressMain() {
        hideKeyboard();
        dismissUI();
        pbMain_VC.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressPagination() {
        pbPagination_VC.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbMain_VC.setVisibility(View.GONE);
        pbPagination_VC.setVisibility(View.GONE);
        flContent_VC.setVisibility(View.VISIBLE);
        rlPlaceholder_VC.setVisibility(View.GONE);
    }

    private void dismissUI() {
        pbMain_VC.setVisibility(View.GONE);
        pbPagination_VC.setVisibility(View.GONE);
        flContent_VC.setVisibility(View.GONE);
        rlPlaceholder_VC.setVisibility(View.GONE);
    }
}
