package com.kubator.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_update_producer;

import com.kubator.pamp.data.model.home.orders.Producer;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;
import com.jakewharton.rxrelay2.PublishRelay;


import io.reactivex.Observable;

/**
 * Created by shonliu on 1/2/18.
 */

public interface CreateUpdateProducerContract {

    interface View extends BaseView<Presenter> {

        boolean validerData();

        void enableValidateBtn(boolean _isEnabled);

        void finish();

        void finishActivityWithResult(Producer producer);

        void setProducerData(Producer _producer);

        void showProgress();

        void hideProgress();

        void openAutocompletePlaceScreen();
    }

    interface Presenter extends BasePresenter {

        PublishRelay<Boolean> validateFields();

        void createUpdateProducer(String _name,
                                  String _email,
                                  String _phone,
                                  String _address,
                                  String _description);

        boolean validateData(String _name,
                             String _email,
                             String _phone,
                             String _address,
                             String _description);

        void clickOnAddress();
    }

    interface Model {
        Observable<Producer> createProducer(Producer _producer);

        Observable<Producer> updateProducer(Producer _producer);
    }
}
