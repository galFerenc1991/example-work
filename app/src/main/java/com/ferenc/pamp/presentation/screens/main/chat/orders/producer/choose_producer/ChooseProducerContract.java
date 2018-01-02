package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer;

import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.home.orders.Producer;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter.ProducerDH;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by shonliu on 1/2/18.
 */

public interface ChooseProducerContract {

    interface View extends BaseView<Presenter> {

        void setProducerList(List<ProducerDH> _list);
    }

    interface Presenter extends BasePresenter {

    }

    interface Model {
        Observable<ListResponse<Producer>> getProducerList(int _page);
    }
}
