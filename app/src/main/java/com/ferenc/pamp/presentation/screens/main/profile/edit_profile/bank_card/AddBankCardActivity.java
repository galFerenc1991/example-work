package com.ferenc.pamp.presentation.screens.main.profile.edit_profile.bank_card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card.AddCardFragment_;
import org.androidannotations.annotations.EActivity;

/**
 * Created by
 * Ferenc on 2018.01.10..
 */
@EActivity(R.layout.activity_add_bank_card)
public class AddBankCardActivity extends BaseActivity {

    @Override
    protected int getContainer() {
        return R.id.flContainer_AABC;
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            initFragment();
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
}
