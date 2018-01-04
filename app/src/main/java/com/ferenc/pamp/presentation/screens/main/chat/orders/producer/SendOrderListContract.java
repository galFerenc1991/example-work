package com.ferenc.pamp.presentation.screens.main.chat.orders.producer;

import com.ferenc.pamp.data.model.home.orders.PDFPreviewRequest;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;


/**
 * Created by shonliu on 12/29/17.
 */

public interface SendOrderListContract {

    interface View extends BaseView<Presenter> {
        void setBonPlanInfo(String _bonPlanInfo);

        void setProducer(String name);

        void openSendOrderListFlow(PDFPreviewRequest _pdfPreviewRequest, String _producerEmail);

        void setValidateButtonEnabled(boolean _isEnabled);

        void openCreateOrderPopUp();

        void chooseProducer();
    }

    interface Presenter extends BasePresenter {

        boolean setQuantity(int _quantity);

        void clickValider(String _id, String _dealId, int _quantity, String _producerEmail);

        void validateData(int _quantity, String _producerId);

        void clickToOpenCreateOrderPopUp();

        void clickToChooseProducer();
    }
}
