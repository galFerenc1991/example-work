package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer;

import android.util.Log;

import com.ferenc.pamp.data.model.home.orders.Producer;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter.ProducerDH;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shonliu on 1/2/18.
 */

public class ChooseProducerPresenter implements ChooseProducerContract.Presenter {

    private ChooseProducerContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private ChooseProducerContract.Model mModel;
    private int mPage;
    private int mTotalPages = Integer.MAX_VALUE;

    public ChooseProducerPresenter(ChooseProducerContract.View _view, ChooseProducerContract.Model _model) {
        mView = _view;
        mModel = _model;
        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        getProducers(mPage, false);
    }


    private void getProducers(int _page, boolean isLoadMore) {

//        mView.showProgressPagination();

        mCompositeDisposable.add(mModel.getProducerList(_page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(producerListResponse -> {
                    Log.d("ChooseProducerPresenter", "getProducers successfully");
//                    mView.hideProgressPagination();

                    List<ProducerDH> producerDHS = new ArrayList<>();

                    for (Producer producer : producerListResponse.data)
                        producerDHS.add(new ProducerDH(producer.name));

                    if (!isLoadMore) {
                        mView.setProducerList(producerDHS);
                    }
                    else {
//                        mView.addProducerList();
                    }

                    mTotalPages = producerListResponse.meta.pages;
                    mPage++;
                }, e -> {
//                    mView.hideProgressPagination();

                    Log.d("ChooseProducerPresenter", "Error while getting new producers: " + e.getMessage());
                }));
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
