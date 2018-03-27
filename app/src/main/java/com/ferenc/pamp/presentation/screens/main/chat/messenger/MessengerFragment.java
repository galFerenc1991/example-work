package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import android.app.Activity;
import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.ChatRepository;
import com.ferenc.pamp.domain.SocketRepository;
import com.ferenc.pamp.domain.GoodDealRepository;
import com.ferenc.pamp.presentation.base.list.EndlessScrollListener;
import com.ferenc.pamp.presentation.base.refreshable.RefreshableFragment;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;
import com.ferenc.pamp.presentation.custom.end_flow_screen.EndFlowActivity_;
import com.ferenc.pamp.presentation.custom.end_flow_screen.EndFlowOrderActivity_;
import com.ferenc.pamp.presentation.screens.main.chat.ChatActivity;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.create_order_pop_up.CreateOrderPopUpActivity_;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.PaymentActivity_;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessengerAdapter;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.SendOrderListActivity_;
import com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_date.DeliveryDateActivity_;
import com.ferenc.pamp.presentation.utils.AvatarManager;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.DateManager;

import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.SignedUserManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * Created by shonliu on 12/12/17.
 */
@EFragment
public class MessengerFragment extends RefreshableFragment implements MessengerContract.View {
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_chat_messenger;
    }

    @Override
    protected RefreshablePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(MessengerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private MessengerContract.Presenter mPresenter;

    protected EndlessScrollListener mScrollListener;

    private Context mContext;

    private final static int START_POSITION = 0;

    @Bean
    protected MessengerAdapter mMessengerAdapter;
    @Bean
    protected ChatRepository mChatRepository;
    @Bean
    protected GoodDealRepository mGoodDealRepository;
    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;
    @Bean
    protected SocketRepository mSocketRepository;
    @Bean
    protected SignedUserManager signedUserManager;
    @Bean
    protected AvatarManager avatarManager;

    @Bean
    protected GoodDealManager mGoodDealManager;

    @ViewById(R.id.rvMessages_FChM)
    protected RecyclerView rvMessages;

    @ViewById(R.id.etInputText_FChM)
    protected EditText etInputText;

    @ViewById(R.id.ivAddImg_FChM)
    protected ImageView ivAddImg;

    @ViewById(R.id.rlSendMsg_FChM)
    protected RelativeLayout rlSendMsg;
    @ViewById(R.id.btnOrder_FCM)
    protected Button btnOrder;
    @ViewById(R.id.pbPagination_FCM)
    protected ProgressBar pbPaginationProgress;

    @StringRes(R.string.button_cancel_deal)
    protected String mCancelDeal;
    @StringRes(R.string.button_not_cancel_deal)
    protected String mNotCancelDeal;
    @StringRes(R.string.button_order)
    protected String mButtonOrderText;
    @StringRes(R.string.button_change_order)
    protected String mButtonChangeOrderText;


    @AfterInject
    @Override
    public void initPresenter() {
        mContext = PampApp_.getInstance();
        new MessengerPresenter(this,
                mChatRepository, mGoodDealRepository,
                mSocketRepository, signedUserManager,
                mContext, mGoodDealResponseManager, mGoodDealManager);
        avatarManager.attach(mActivity);

    }

    @AfterViews
    protected void initUI() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setReverseLayout(true);
        mScrollListener = new EndlessScrollListener(llm, () -> {
            if (isRefreshing()) return false;
            mPresenter.loadNextPage();
            return true;
        });

        rvMessages.setLayoutManager(llm);
        rvMessages.setAdapter(mMessengerAdapter);
        rvMessages.addOnScrollListener(mScrollListener);
        mPresenter.subscribe();

        RxView.clicks(btnOrder)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedCreateOrder());

        RxView.clicks(ivAddImg)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.selectImage());

        RxView.clicks(rlSendMsg)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.sendMessage(etInputText.getText().toString().trim()));

    }

    @Override
    public void initCreateOrderButton(boolean _isHaveOrder) {
        btnOrder.setVisibility(View.VISIBLE);
        btnOrder.setText(_isHaveOrder ? mButtonChangeOrderText : mButtonOrderText);
    }

    @OnActivityResult(Constants.REQUEST_CODE_SETTINGS_ACTIVITY)
    protected void settingsActionResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (data.getStringExtra(Constants.KEY_SETTINGS)) {
                case Constants.KEY_SEND_ORDERS:
                    mPresenter.sendOrders();
                    break;
                case Constants.KEY_CHANGE_CLOSE_DATE:
                    mPresenter.changeCloseDateAction();
                    break;
                case Constants.KEY_CHANGE_DELIVERY_DATE:
                    mPresenter.changeDeliveryDateAction();
                    break;
                case Constants.KEY_CANCEL_GOOD_DEAL:
                    mPresenter.cancelDealAction();
                    break;
            }
        }
    }

    @Override
    public void openCreateOrderPopUp() {
        CreateOrderPopUpActivity_.intent(mActivity).isSendOrderListFlow(false)
                .startForResult(Constants.REQUEST_CODE_CREATE_ORDER_POP_UP_ACTIVITY);
    }

    @OnActivityResult(Constants.REQUEST_CODE_CREATE_ORDER_POP_UP_ACTIVITY)
    protected void createOrderResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mPresenter.initCreateOrderButton();
            mPresenter.resultQuantity(data.getDoubleExtra(Constants.KEY_PRODUCT_QUANTITY, -1));
        }
    }

    @Override
    public void openDeleteOrderScreen() {
        EndFlowOrderActivity_
                .intent(mActivity)
                .mIsCreatedFlow(false)
                .start();
    }

    @Override
    public void openCreateOrderFlow(double _quantity) {
        PaymentActivity_
                .intent(mActivity)
                .extra(Constants.KEY_PRODUCT_QUANTITY, _quantity)
                .startForResult(Constants.REQUEST_CODE_ACTIVITY_END_FLOW_ACTIVITY);
    }

    @Override
    public void openSendOrderListFlow() {
        SendOrderListActivity_
                .intent(mActivity)
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .start();
    }

    @Override
    public void selectImage() {
        checkCameraPermission();
    }

    @OnActivityResult(Constants.REQUEST_CODE_GET_IMAGE)
    protected void handleImage(int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            avatarManager.handleFullsizeImage(Constants.REQUEST_CODE_CROP_IMAGE, resultCode, data, true);
    }


    @OnActivityResult(Constants.REQUEST_CODE_CROP_IMAGE)
    protected void cropImage(int resultCode) {
        if (resultCode == RESULT_OK)
            mPresenter.sendImage(avatarManager.getCroppedFile());
    }

    @Override
    public boolean isCameraPermissionNotGranted() {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void checkCameraPermission() {
        if (isCameraPermissionNotGranted()) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, Constants.REQUEST_CODE_CAMERA);
        } else {
            avatarManager.getImageOnlyFromCamera(Constants.REQUEST_CODE_GET_IMAGE);
        }
    }

    @Override
    public void hideOrderBtn() {
        btnOrder.setVisibility(View.GONE);
        ChatActivity chatActivity = (ChatActivity) getActivity();
        chatActivity.hideShareButton();
    }

    @Override
    public List<MessagesDH> getMessagesDHs() {
        return mMessengerAdapter.getListDH();
    }

    @Override
    public void updateItem(int _position) {
        mMessengerAdapter.updateItem(_position);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                avatarManager.getImageOnlyFromCamera(Constants.REQUEST_CODE_GET_IMAGE);
            } else {
                ToastManager.showToast("Please, allow for PAMP access to Camera.");
            }
        }
    }

    @OnActivityResult(Constants.REQUEST_CODE_ACTIVITY_END_FLOW_ACTIVITY)
    protected void createOrderFlowResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mPresenter.initCreateOrderButton();
            EndFlowOrderActivity_.intent(this).mIsCreatedFlow(true).start();
        }
    }

    @Override
    public void openCloseDatePicker(Calendar _calendar, long _startDeliveryDate) {
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(Locale.FRANCE);

        Calendar result = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity, R.style.DialogTheme, (view, hourOfDay, minute) -> {
            result.set(Calendar.HOUR_OF_DAY, hourOfDay);
            result.set(Calendar.MINUTE, minute);
            if (DateManager.isBeforeNow(result)) {
                showErrorMessage(Constants.MessageType.SELECT_FUTURE_DATE);
            } else {
                mPresenter.setChangedCloseDate(result);
            }
        }, _calendar.get(Calendar.HOUR_OF_DAY), _calendar.get(Calendar.MINUTE), true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.DialogTheme, (view, year, month, dayOfMonth) -> {
            result.set(year, month, dayOfMonth);
            timePickerDialog.show();
        }, _calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH), _calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(_startDeliveryDate);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.setTitle(R.string.close_date_dialog_title);
        datePickerDialog.show();
    }

    @Override
    public void openDeliveryDateScreen(long _currentStartDeliveryDate, long _currentEndDeliveryDate) {
        DeliveryDateActivity_.intent(mActivity)
                .extra(Constants.KEY_IS_REBROADCAST, false)
                .mStartDeliveryDate(_currentStartDeliveryDate)
                .mEndDeliveryDate(_currentEndDeliveryDate)
                .startForResult(Constants.REQUEST_CODE_ACTIVITY_DELIVERY_DATE);
    }

    @OnActivityResult(Constants.REQUEST_CODE_ACTIVITY_DELIVERY_DATE)
    void resultDate(int resultCode, @OnActivityResult.Extra(value = Constants.KEY_START_DATE_RESULT) String _startDate
            , @OnActivityResult.Extra(value = Constants.KEY_END_DATE_RESULT) String _endDate) {
        if (resultCode == Activity.RESULT_OK) {
            mPresenter.setChangedDeliveryDate(_startDate, _endDate);
        }
    }

    @Override
    public void openCloseGoodDealPopUp() {
        View dialogViewTitle = LayoutInflater.from(getContext())
                .inflate(R.layout.view_clancel_good_deal_pop_up_title, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.DialogTheme);
        builder.setCustomTitle(dialogViewTitle)
                .setView(R.layout.view_cancel_dial_pop_up_message)
                .setPositiveButton(mCancelDeal, (dialog, which) -> {
                    mPresenter.cancelDeal();
                })
                .setNegativeButton(mNotCancelDeal, (dialogInterface, i) -> {
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void openDeliveryDateChangedNotif(String _title) {
        View dialogViewTitle = LayoutInflater.from(getContext())
                .inflate(R.layout.view_title_notification_pop_up, null, false);
        TextView textView = dialogViewTitle.findViewById(R.id.tvTitle);
        textView.setText(_title);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.DialogTheme);
        builder.setCustomTitle(dialogViewTitle)
                .setView(R.layout.view_delivery_date_changed_pop_up_message)
                .setPositiveButton(R.string.button_ok, (dialog, which) -> {
                    mPresenter.changeDeliveryDateAction();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void openEndFlowScreen() {
        EndFlowActivity_
                .intent(mActivity)
                .mFlow(Constants.NOT_CREATE_FLOW)
                .fromWhere(Constants.ITEM_TYPE_REUSE)
                .mGoodDealResponse(mGoodDealResponseManager.getGoodDealResponse())
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .start();
    }

    @Override
    public void openMessengerFragment() {

    }

    @Override
    public void setMessagesList(List<MessagesDH> _list) {
        mScrollListener.reset();
        mMessengerAdapter.setListDH(_list);
    }

    @Override
    public void changeItem(MessagesDH _item, int _position) {
        mMessengerAdapter.changeItem(_item, _position);
    }

    @Override
    public void addMessagesList(List<MessagesDH> _list) {
        mMessengerAdapter.addListDH(_list);
    }

    @Override
    public void addItem(List<MessagesDH> _list) {
        mMessengerAdapter.insertItem(_list.get(START_POSITION), START_POSITION);
    }

    @Override
    public void clearInputText() {
        etInputText.setText("");
    }

    @Override
    public void changeRecyclerViewLayoutParams(boolean _isChange) {
        ViewGroup.LayoutParams params = rvMessages.getLayoutParams();
        params.height = _isChange ? WRAP_CONTENT : MATCH_PARENT;
        rvMessages.setLayoutParams(params);
    }

    @Override
    public void togglePaginationProgress(boolean visibility) {
        pbPaginationProgress.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void scrollToStart() {
        if (rvMessages != null) {
            rvMessages.scrollToPosition(START_POSITION);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
