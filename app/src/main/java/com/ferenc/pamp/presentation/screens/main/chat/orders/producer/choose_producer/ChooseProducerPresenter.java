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
    private String mProducer;
    private int selectedPos;
    private int mPage;
    private int mTotalPages = Integer.MAX_VALUE;
    private List<ProducerDH> producerDHS;

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

                    producerDHS = new ArrayList<>();

                    for (Producer producer : producerListResponse.data)
                        producerDHS.add(new ProducerDH(producer.producerId, producer.name));

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

    @Override
    public void selectItem(ProducerDH item, int position) {
        mView.updateItem(new ProducerDH(producerDHS.get(selectedPos).getProducerId(), producerDHS.get(selectedPos).getProducer()), selectedPos);

        selectedPos = position;
        mView.updateItem(new ProducerDH(item.getProducerId(),item.getProducer(), true), selectedPos);
        mView.enabledValider();
    }

    @Override
    public Producer getSelectedProducer() {
        return new Producer(producerDHS.get(selectedPos).getProducer(), producerDHS.get(selectedPos).getProducerId());
    }

    @Override
    public void clickedValide() {
        mView.finishActivityWithResult();
    }

    @Override
    public void addNewProducer(String _producerName, String _producerId) {
        mView.addItemToList(_producerName,_producerId);
    }
}
