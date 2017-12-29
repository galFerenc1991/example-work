package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.save_card;

import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;

/**
 * Created by
 * Ferenc on 2017.12.25..
 */

public interface SaveCardContract {
    interface View extends ContentView, BaseView<Presenter> {
        void openSuccessCreatedCardScreen();
    }

    interface Presenter extends BasePresenter {
        void clickedValidate();

        void setSaveCardInProfile(boolean isSave);
    }
}
