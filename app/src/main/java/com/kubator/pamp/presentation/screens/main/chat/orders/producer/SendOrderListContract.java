package com.kubator.pamp.presentation.screens.main.chat.orders.producer;

import com.kubator.pamp.data.model.home.orders.PDFPreviewRequest;
import com.kubator.pamp.data.model.home.orders.SendPDFRequest;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;


/**
 * Created by shonliu on 12/29/17.
 */

public interface SendOrderListContract {

    interface View extends BaseView<Presenter> {
        void setBonPlanInfo(String _bonPlanInfo);

        void setProducer(String name);

        void openSendOrderListFlow(PDFPreviewRequest _pdfPreviewRequest, String _producerEmail, SendPDFRequest _sendPDFRequest);

        void setValidateButtonEnabled(boolean _isEnabled);

        void openCreateOrderPopUp();

        void chooseProducer();

        void setBonPlanInfoVisibility(boolean _isVisible);
    }

    interface Presenter extends BasePresenter {

        void setQuantity(double _quantity);

        void clickValider(String _id, String _dealId, double _quantity, String _producerEmail);

        void validateData(double _quantity, String _producerId);

        void clickToOpenCreateOrderPopUp();

        void clickToChooseProducer();
    }
}
