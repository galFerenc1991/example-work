package com.ferenc.pamp.presentation.screens.main.profile.edit_profile.bank_card;

import android.support.v7.widget.Toolbar;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card.AddCardFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

/**
 * Created by
 * Ferenc on 2018.01.10..
 */
@EActivity(R.layout.activity_add_bank_card)
public class AddBankCardActivity extends BaseActivity {

    @ViewById(R.id.toolbar_AABC)
    protected Toolbar toolbar;

    @StringRes(R.string.title_add_card)
    protected String titleCreateCard;

    @Override
    protected int getContainer() {
        return R.id.flContainer_AABC;
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @AfterViews
    protected void onCreate() {

        initFragment();

        initBar();

    }

    protected void initFragment() {
        replaceFragment(AddCardFragment_.builder().withEditProfile(true).build());
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    private void initBar() {
        toolbarManager.setTitle(titleCreateCard);
        toolbarManager.showHomeAsUp(true);
        toolbarManager.closeActivityWhenBackArrowPressed(this);
        toolbarManager.setIconHome(R.drawable.ic_arrow_back_green);
    }

}
