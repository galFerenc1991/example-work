package com.kubator.pamp.presentation.screens.main.chat.orders.producer.preview_pdf;

import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.kubator.pamp.data.model.home.orders.PDFPreviewRequest;
import com.kubator.pamp.data.model.home.orders.PDFPreviewResponse;
import com.kubator.pamp.data.model.home.orders.SendPDFRequest;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by shonliu on 12/29/17.
 */

public interface PreviewPDFContract {

    interface View extends BaseView<PreviewPDFContract.Presenter> {

        void showProgress();

        void hideProgress(boolean hide);

        void showPDFInWebView(PDFPreviewResponse pdfPreviewResponse);

        boolean isReedStoragePermissionNotGranted();

        void checkStoragePermission();

        void showValiderButton();

        void hideValiderButton();

        void showSendMyOrderButton();

        void hideSendMyOrderButton();
    }

    interface Presenter extends BasePresenter {

        void clickedConfirm();

        PDFPreviewResponse getPDFPreviewResponse();

        void getPDFPreview();

    }

    interface Model extends BaseModel {

        Observable<Response<ResponseBody>> getFileByUrl(String _url);

        Observable<PDFPreviewResponse> getPDFPreview(String _producerId, PDFPreviewRequest _requestBody);

        Observable<PDFPreviewResponse> getPDFPreview(String _orderId);

        Observable<Object> sendPDFToProducer(SendPDFRequest _requestBody);

        Observable<GoodDealResponse> getDialId(String _dealId);

    }

}
