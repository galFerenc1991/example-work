package com.ferenc.pamp.presentation.screens.main.chat.orders.producer;

import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;


/**
 * Created by shonliu on 12/29/17.
 */

public interface SendOrderListContract {

    interface View extends BaseView<Presenter> {
        void setBonPlanInfo(String _bonPlanInfo);
    }

    interface Presenter extends BasePresenter {

        boolean setQuantity(int _quantity);

    }
}
