package com.ferenc.pamp.presentation.screens.main;

import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.domain.UserRepository;
import com.ferenc.pamp.presentation.screens.main.good_plan.proposed.propose_relay.ProposeRelay;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.google.firebase.iid.FirebaseInstanceId;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.12.07..
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private MainContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;
    private ProposeRelay mProposeRelay;
    private UserRepository mUserRepository;

    public MainPresenter(MainContract.View _view, MainContract.Model _model, ProposeRelay _proposeRelay, UserRepository _userRepository) {
        this.mView = _view;
        this.mModel = _model;
        this.mProposeRelay = _proposeRelay;
        this.mUserRepository = _userRepository;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mCompositeDisposable.add(mUserRepository.setNotifToken(new TokenRequest(FirebaseInstanceId.getInstance().getToken()))
        .subscribe(generalMessageResponse -> {
//            ToastManager.showToast("Token saved");
        }, throwable -> {
            ToastManager.showToast("Firebase token save error:" + throwable.getMessage());
        }));
        mProposeRelay.proposeRelay.subscribe(aBoolean -> {
            mView.openProposedFlow();
        });
    }

    @Override
    public void connectGoodDeal(String _id) {
        mCompositeDisposable.add(mModel.connectGoodDeal(_id)
                .subscribe(connectGoodDealResponse -> {
                    ToastManager.showToast(" GoodDeal : " + _id + "connected! \nif you don't see it in received list, please do refresh");
                }, throwable -> {

                }));
    }

    @Override
    public void unsubscribe() {
mCompositeDisposable.clear();
    }
}
