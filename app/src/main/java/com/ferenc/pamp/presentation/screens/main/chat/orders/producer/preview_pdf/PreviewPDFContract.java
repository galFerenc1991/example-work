package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.preview_pdf;

import com.ferenc.pamp.data.model.home.orders.PDFPreviewRequest;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewResponse;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by shonliu on 12/29/17.
 */

public interface PreviewPDFContract {

    interface View extends BaseView<PreviewPDFContract.Presenter> {

        void showProgress();

        void hideProgress(boolean _closeActivity);

        void showPDFInWebView(PDFPreviewResponse pdfPreviewResponse);

        boolean isReedStoragePermissionNotGranted();

        void checkStoragePermission();

        void showValiderButton();
    }

    interface Presenter extends BasePresenter {

        void clickedConfirm();

        PDFPreviewResponse getPDFPreviewResponse();

        void getPDFPreview();

    }

    interface Model extends BaseModel {

        Observable<Response<ResponseBody>> getFileByUrl(String _url);

        Observable<PDFPreviewResponse> getPDFPreview(String _producerId, PDFPreviewRequest _requestBody);

    }

}
