package com.ferenc.pamp.presentation.screens.main.propose.share;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

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

    static Uri createdShort;
    private boolean isUp;
    private Animation slideOut;
    private Animation slideIn;

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
    private boolean isResentFlow = false;

    @ViewById(R.id.rvContactList_FSGP)
    protected RecyclerView rvContactList;
    @ViewById(R.id.btnShare_FSGP)
    protected Button btnShare;
    @ViewById(R.id.etSearch_FSGP)
    protected EditText etSearch;
    @ViewById(R.id.ivCancelSearch_FSGP)
    protected ImageView ivCancelSearch;
    @ViewById(R.id.cvSearchBlock_FSGP)
    protected CardView cvSearchBlock;

    @StringRes(R.string.button_ok)
    protected String mOk;

    @Bean
    protected GoodDealRepository mGoodDealRepository;
    @Bean
    protected ContactAdapter mContactAdapter;
    @Bean
    protected GoodDealManager mGoodDealManager;
    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;
    @Bean
    protected ContactManager mContactManager;

    @FragmentArg
    protected boolean isReBroadcastFlow;

    @FragmentArg
    protected boolean isUpdateGoodDeal;

    @AfterInject
    @Override
    public void initPresenter() {
        new SharePresenter(this, mGoodDealRepository, mGoodDealManager, mGoodDealResponseManager, isReBroadcastFlow, isUpdateGoodDeal, mContactManager);
        mContactAdapter.setOnCardClickListener((view, position, viewType) ->
                mPresenter.selectItem(mContactAdapter.getItem(position), position));
    }

    @AfterViews
    protected void initUI() {

        checkReedContactsPermission();

        initStyle();
        initListeners();
        initAdapter();
        initAnimations();

    }

    private void initStyle() {
        if (isReBroadcastFlow) {
            btnShare.setBackground(getResources().getDrawable(R.drawable.bg_confirm_button_yellow));
            mActivity.setTheme(R.style.ReBroadcastTheme);
        }
    }

    //    @Override
    public void sendSmsWith(Uri _dynamicLink
            , List<String> _selectedContacts
            , GoodDealResponse _goodDealResponse) {
        ArrayList<String> selectedContacts = new ArrayList<>(_selectedContacts);
        mGoodDealResponse = _goodDealResponse;

        Uri uri = Uri.parse(Constants.KEY_SENDTO_SMS + TextUtils.join(", ", selectedContacts));
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);

        String textToSMSIntent = getString(R.string.msg_send_good_deal_without_account) + "\n" + _dynamicLink.toString();

        it.putExtra(Constants.KEY_SMS_BODY, textToSMSIntent);
        startActivityForResult(it, Constants.REQUEST_CODE_SEND_SMS_DONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SEND_SMS_DONE) {
            if (!isUpdateGoodDeal) {
                EndFlowActivity_
                        .intent(this)
                        .mFlow(isReBroadcastFlow ? Constants.RESENT_FLOW : Constants.CREATE_FLOW)
//                        .mIsCreatedFlow(true)
                        .fromWhere(Constants.ITEM_TYPE_REUSE)
                        .mGoodDealResponse(mGoodDealResponse)
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .start();
            } else {
                //лишив на случай кіть захочуть обратно вернути...
//                EndFlowActivity_
//                        .intent(this)
//                        .mFlow(Constants.ADD_PARTICIPANT_FLOW)
//                        .fromWhere(Constants.ITEM_TYPE_REUSE)
//                        .mGoodDealResponse(mGoodDealResponse)
//                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        .start();

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
    public void openResendVerificationErrorPopUP() {
        View dialogViewTitle = LayoutInflater.from(getContext())
                .inflate(R.layout.view_good_deal_verification_error_dialog_title, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.DialogTheme);
        builder.setCustomTitle(dialogViewTitle)
                .setView(R.layout.view_good_deal_resend_verification_error_dialog  )
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

    public void getShortLink(List<String> _selectedContacts, GoodDealResponse _goodDealResponse) {
        showProgressMain();
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(getDynamicLink(_goodDealResponse.id))
                .setDynamicLinkDomain(PampApp_.getInstance().getString(R.string.app_code) + ".app.goo.gl")
                .buildShortDynamicLink()
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {

                        createdShort = task.getResult().getShortLink();
                        sendSmsWith(createdShort, _selectedContacts, _goodDealResponse);

                    } else hideProgress();

                });

    }

    public static Uri getDynamicLink(String _id) {
        String appCode = PampApp_.getInstance().getString(R.string.app_code);

        Uri.Builder builder = new Uri.Builder()
                .scheme("https")
                .authority(appCode + ".app.goo.gl")
                .path("/")
                .appendQueryParameter("link", "http://pampconnect.com/?id=" + _id)
                .appendQueryParameter("apn", "com.ferenc.pamp")
                .appendQueryParameter("ibi", "com.1kubator.pamp");

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(builder.build())
                .setDynamicLinkDomain(appCode + ".app.goo.gl")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        return dynamicLink.getUri();
    }

    private void initListeners() {

        RxView.clicks(btnShare)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> checkSendSMSPermission());

        RxView.clicks(ivCancelSearch)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    etSearch.setText("");
                    mPresenter.search("");
                    ivCancelSearch.setVisibility(View.GONE);
                });

        RxTextView.editorActionEvents(etSearch).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEARCH) {
                mPresenter.search(etSearch.getText().toString().trim());
                hideKeyboard();
            }
        });

        RxTextView.textChangeEvents(etSearch).subscribe(textChangeEvents -> {
            if (!textChangeEvents.text().toString().equals("")) {
                ivCancelSearch.setVisibility(View.VISIBLE);
                mPresenter.search(etSearch.getText().toString().trim());
            } else {
                mPresenter.search("");
                ivCancelSearch.setVisibility(View.GONE);
            }
        });

        rvContactList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    isUp = true;
                    Log.i("Recycler view", "scrolling up");
                } else {
                    isUp = false;
                    Log.i("Recycler view", "scrolling down");
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    animateSearchBlock();
                    Log.i("Recycler view ", "SCROLL_STATE_FLING");
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    Log.i("Recycler view ", "SCROLL_STATE_TOUCH_SCROLL");
                } else {
                    animateSearchBlock();
                    Log.i("Recycler view ", "SCROLL_STATE_STOPED");
                }
            }
        });
    }

    private void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvContactList.setLayoutManager(llm);
        rvContactList.setAdapter(mContactAdapter);
    }

    private void initAnimations() {
        slideOut = AnimationUtils.loadAnimation(mActivity, R.anim.slide_out);
        slideIn = AnimationUtils.loadAnimation(mActivity, R.anim.slide_in);
    }

    private void animateSearchBlock() {
        if (cvSearchBlock != null) {
            if (isUp) {
                if (cvSearchBlock.getVisibility() == View.VISIBLE)
                    cvSearchBlock.startAnimation(slideOut);
                cvSearchBlock.setVisibility(View.GONE);
            } else {
                if (cvSearchBlock.getVisibility() == View.GONE)
                    cvSearchBlock.startAnimation(slideIn);
                cvSearchBlock.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgress();
    }
}
