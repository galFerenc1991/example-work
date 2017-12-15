package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.domain.ChatRepository;
import com.ferenc.pamp.presentation.base.list.EndlessScrollListener;
import com.ferenc.pamp.presentation.base.refreshable.RefreshableFragment;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;
import com.ferenc.pamp.presentation.screens.main.chat.ChatActivity;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessengerAdapter;
import com.ferenc.pamp.presentation.screens.main.good_plan.received.ReceivedPlansContract;
import com.ferenc.pamp.presentation.utils.SignedUserManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

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


    @FragmentArg
    protected GoodDealResponse goodDealResponse;

    private MessengerContract.Presenter mPresenter;

    protected EndlessScrollListener mScrollListener;

    @ViewById(R.id.rvMessages_FChM)
    protected RecyclerView rvMessages;

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
        new MessengerPresenter(this, mChatRepository, goodDealResponse, signedUserManager.getCurrentUser());
    }

    @AfterViews
    protected void initUI() {
        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMessages.setAdapter(mMessengerAdapter);

        mPresenter.subscribe();
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
        mMessengerAdapter.addListDH(_list);
    }
}
