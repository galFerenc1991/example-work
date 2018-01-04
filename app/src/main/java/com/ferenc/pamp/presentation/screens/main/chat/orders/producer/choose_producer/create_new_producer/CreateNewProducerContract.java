package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_new_producer;

import com.ferenc.pamp.data.model.home.orders.Producer;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.jakewharton.rxrelay2.PublishRelay;


import io.reactivex.Observable;

/**
 * Created by shonliu on 1/2/18.
 */

public interface CreateNewProducerContract {

    interface View extends BaseView<Presenter> {

        boolean validerData();

        void enableValidateBtn(boolean _isEnabled);

        void finish();

        void finishActivityWithResult(Producer producer);
    }

    interface Presenter extends BasePresenter {

        PublishRelay<Boolean> validateFields();

        void createProducer( String _name,
                 String _email,
                 String _phone,
                 String _address,
                 String _description);
    }

    interface Model {
        Observable<Producer> createProducer(Producer _producer);
    }
}
