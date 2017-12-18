package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.domain.ChatRepository;
import com.ferenc.pamp.presentation.utils.SocketUtil;
import com.ferenc.pamp.presentation.base.list.EndlessScrollListener;
import com.ferenc.pamp.presentation.base.refreshable.RefreshableFragment;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessengerAdapter;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.SignedUserManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by shonliu on 12/12/17.
 */
@EFragment
public class MessengerFragment extends RefreshableFragment implements MessengerContract.View {

    @Bean
    protected MessengerAdapter mMessengerAdapter;

    @Bean
    protected ChatRepository mChatRepository;

    @Bean
    protected SignedUserManager signedUserManager;

    @Bean
    protected SocketUtil socketUtil;


    @FragmentArg
    protected GoodDealResponse goodDealResponse;

    private MessengerContract.Presenter mPresenter;

    protected EndlessScrollListener mScrollListener;

    @ViewById(R.id.rvMessages_FChM)
    protected RecyclerView rvMessages;

    @ViewById(R.id.etInputText_FChM)
    protected EditText etInputText;

    @ViewById(R.id.ivAddImg_FChM)
    protected ImageView ivAddImg;

    @ViewById(R.id.rlSendMsg_FChM)
    protected RelativeLayout rlSendMsg;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_chat_messenger;
    }

    @Override
    protected RefreshablePresenter getPresenter() {
        return null;
    }

    @AfterInject
    @Override
    public void initPresenter() {
        new MessengerPresenter(this, mChatRepository, goodDealResponse, signedUserManager.getCurrentUser(), socketUtil, PampApp_.getInstance());
    }

    @AfterViews
    protected void initUI() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setReverseLayout(true);
        rvMessages.setLayoutManager(llm);
        rvMessages.setAdapter(mMessengerAdapter);
        mPresenter.subscribe();

        RxView.clicks(ivAddImg)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> addImage());

        RxView.clicks(rlSendMsg)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> sendMessage());

    }

    @Override
    public void setPresenter(MessengerContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void openMessengerFragment() {

    }

    @Override
    public void setMessagesList(List<MessagesDH> _list) {
        mMessengerAdapter.setListDH(_list);
    }

    @Override
    public void addItem(List<MessagesDH> _list) {
//        mMessengerAdapter.addListDH(_list);
        mMessengerAdapter.insertItem(_list.get(0), 0);
        if (rvMessages != null) {
            rvMessages.scrollToPosition(0);
        }
    }

    @Override
    public void sendMessage() {
        if (!etInputText.getText().toString().trim().equals("")) {
            MessageResponse messageResponse = new MessageResponse();

            messageResponse.user = signedUserManager.getCurrentUser();
            messageResponse.text = etInputText.getText().toString().trim();
            mPresenter.sendMessage(messageResponse);
            etInputText.setText("");
        }
    }

    @Override
    public void addImage() {
        //TODO : init avatar manager(with CAMERA parameter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socketUtil.socketDisconnect();
    }
}
