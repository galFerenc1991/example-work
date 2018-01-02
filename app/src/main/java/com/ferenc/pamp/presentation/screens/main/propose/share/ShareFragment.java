package com.ferenc.pamp.presentation.screens.main.propose.share;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.domain.GoodDealRepository;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.custom.end_flow_screen.EndFlowActivity_;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactAdapter;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactDH;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ContactManager;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.27..
 */
@EFragment
public class ShareFragment extends ContentFragment implements ShareContract.View {

    @Override
    public void setPresenter(ShareContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_share_good_plan;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private ShareContract.Presenter mPresenter;
    private GoodDealResponse mGoodDealResponse;

    @ViewById(R.id.rvContactList_FSGP)
    protected RecyclerView rvContactList;
    @ViewById(R.id.btnShare_FSGP)
    protected Button btnShare;

    @StringRes(R.string.button_ok)
    protected String mOk;

    @Bean
    protected GoodDealRepository mGoodDealRepository;
    @Bean
    protected ContactAdapter mContactAdapter;
    @Bean
    protected GoodDealManager mGoodDealManager;
    @Bean
    protected ContactManager mContactManager;

    @FragmentArg
    protected boolean isReBroadcastFlow;

    @FragmentArg
    protected boolean isUpdateGoodDeal;

    @AfterInject
    @Override
    public void initPresenter() {
        new SharePresenter(this, mGoodDealRepository, mGoodDealManager, isReBroadcastFlow, isUpdateGoodDeal, mContactManager);

        mContactAdapter.setOnCardClickListener((view, position, viewType) ->
                mPresenter.selectItem(mContactAdapter.getItem(position), position));
    }

    @AfterViews
    protected void initUI() {
        initStyle();
        RxView.clicks(btnShare)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> checkSendSMSPermission());

        rvContactList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContactList.setAdapter(mContactAdapter);

        checkReedContactsPermission();
    }

    private void initStyle() {
        if (isReBroadcastFlow) {
            btnShare.setBackground(getResources().getDrawable(R.drawable.bg_confirm_button_yellow));
            mActivity.setTheme(R.style.ReBroadcastTheme);
        }
    }

    @Override
    public void sendSmsWith(Uri _dynamicLink
            , List<String> _selectedContacts
            , GoodDealResponse _goodDealResponse) {
        ArrayList<String> selectedContacts = new ArrayList<>(_selectedContacts);
        mGoodDealResponse = _goodDealResponse;

        Uri uri = Uri.parse(Constants.KEY_SENDTO_SMS + TextUtils.join(", ", selectedContacts));
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra(Constants.KEY_SMS_BODY, R.string.msg_send_good_deal + _dynamicLink.toString());
        startActivityForResult(it, Constants.REQUEST_CODE_SEND_SMS_DONE);


//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage(TextUtils.join(", ", selectedContacts),null, R.string.msg_send_good_deal + _dynamicLink.toString(),null, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SEND_SMS_DONE) {
            if (!isUpdateGoodDeal) {
                EndFlowActivity_
                        .intent(this)
                        .mIsCreatedFlow(true)
                        .fromWhere(Constants.ITEM_TYPE_REUSE)
                        .mGoodDealResponse(mGoodDealResponse)
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .start();
            } else {
                getActivity().finish();
            }
        }
    }

    @Override
    public void setContactAdapterList(List<ContactDH> _list) {
        mContactAdapter.setListDH(_list);
    }

    @Override
    public void updateItem(ContactDH item, int position) {
        mContactAdapter.changeItem(item, position);
    }

    @Override
    public boolean isReedContactsPermissionNotGranted() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void checkReedContactsPermission() {
        if (isReedContactsPermissionNotGranted()) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, Constants.REQUEST_CODE_REED_CONTACTS);
        } else {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE_REED_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.subscribe();
            } else {
                ToastManager.showToast("Please, allow for PAMP access to address book.");
            }
        }
        if (requestCode == Constants.REQUEST_CODE_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.share(mContactAdapter.getListDH());
            } else {
                ToastManager.showToast("Please, allow for PAMP access to send SMS.");
            }
        }
    }

    @Override
    public boolean isSendSMSPermissionNotGranted() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void checkSendSMSPermission() {
        if (isSendSMSPermissionNotGranted()) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, Constants.REQUEST_CODE_SEND_SMS);
        } else {
            mPresenter.share(mContactAdapter.getListDH());
        }
    }

    @Override
    public void openVerificationErrorPopUP() {
        View dialogViewTitle = LayoutInflater.from(getContext())
                .inflate(R.layout.view_good_deal_verification_error_dialog_title, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.DialogTheme);
        builder.setCustomTitle(dialogViewTitle)
                .setView(R.layout.view_good_deal_verification_error_dialog)
                .setPositiveButton(mOk, (dialog, which) -> {
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
