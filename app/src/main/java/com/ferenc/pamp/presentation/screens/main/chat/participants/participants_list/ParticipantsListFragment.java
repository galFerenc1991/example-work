package com.ferenc.pamp.presentation.screens.main.chat.participants.participants_list;



import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BaseFragment;
import com.ferenc.pamp.presentation.screens.main.chat.participants.participants_adapter.ParticipantsAdapter;
import com.ferenc.pamp.presentation.screens.main.chat.participants.participants_adapter.ParticipantsDH;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by shonliu on 12/22/17.
 */
@EFragment(R.layout.fragment_participants_list)
public class ParticipantsListFragment extends BaseFragment implements ParticipantsListContract.View {

    @ViewById(R.id.rvParticipants_FPL)
    protected RecyclerView rvParticipants;

    @Bean
    protected ParticipantsAdapter mParticipantsAdapter;

    @Bean
    protected GoodDealResponseManager goodDealResponseManager;

    private ParticipantsListContract.Presenter mPresenter;


    @AfterInject
    @Override
    public void initPresenter() {
        new ParticipantsListPresenter(this, goodDealResponseManager, getContext());
    }

    @AfterViews
    protected void initUI() {
        rvParticipants.setLayoutManager(new LinearLayoutManager(getContext()));
        rvParticipants.setAdapter(mParticipantsAdapter);

        mPresenter.subscribe();
    }

    @Override
    public void setPresenter(ParticipantsListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setParticipantsList(List<ParticipantsDH> _list) {
        mParticipantsAdapter.setListDH(_list);
    }
}
