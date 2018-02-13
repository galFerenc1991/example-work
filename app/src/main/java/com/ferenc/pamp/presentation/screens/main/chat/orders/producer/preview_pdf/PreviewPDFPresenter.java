package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.preview_pdf;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewRequest;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewResponse;
import com.ferenc.pamp.data.model.home.orders.SendPDFRequest;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ToastManager;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okio.BufferedSink;
import okio.Okio;



/**
 * Created by shonliu on 12/29/17.
 */

public class PreviewPDFPresenter implements PreviewPDFContract.Presenter {

    private PreviewPDFContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private PreviewPDFContract.Model mModel;
    private PDFPreviewRequest mPDFPreviewRequest;
    private PDFPreviewResponse mPDFPreviewResponse;
    private PreviewPDFActivity mActivity;
    private String mProducerEmail;
    private String mOrderId;
    private boolean isFromSendOrderListAct;
    private SendPDFRequest mSendPDFRequest;

    public PreviewPDFPresenter(PreviewPDFContract.View _view,
                               PreviewPDFContract.Model _model,
                               PDFPreviewRequest _PDFPreviewRequest,
                               PreviewPDFActivity _activity,
                               String _producerEmail,
                               String _orderId,
                               boolean _isFromOrderList,
                               SendPDFRequest _sendPDFRequest) {
        this.mView = _view;
        this.mModel = _model;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mPDFPreviewRequest = _PDFPreviewRequest;
        this.mActivity = _activity;
        this.mProducerEmail = _producerEmail;
        this.mOrderId = _orderId;
        this.isFromSendOrderListAct = _isFromOrderList;
        this.mSendPDFRequest = _sendPDFRequest;
        mView.setPresenter(this);
    }


    public void getPDFPreview() {
        mView.showProgress();
        mCompositeDisposable.add((isFromSendOrderListAct
                ? mModel.getPDFPreview(mPDFPreviewRequest.id, mPDFPreviewRequest)
                : mModel.getPDFPreview(mOrderId))
                .subscribe(pdfPreviewResponse -> {
                    mView.hideProgress(false);
                    mPDFPreviewResponse = pdfPreviewResponse;
                    mView.showPDFInWebView(getPDFPreviewResponse());
                    if (isFromSendOrderListAct) {
                        mView.showValiderButton();
                        mView.hideSendMyOrderButton();
                    } else {
                        mView.hideValiderButton();
                        mView.showSendMyOrderButton();
                    }

                }, throwableConsumer));
    }

    @Override
    public void subscribe() {
        getPDFPreview();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    private Consumer<Throwable> throwableConsumer = throwable -> {
        throwable.printStackTrace();
        if (throwable instanceof ConnectionLostException) {
            mView.hideProgress(true);
            ToastManager.showToast("Connection Lost");
        } else {
            mView.hideProgress(true);
            Log.d("PreviewPDF", "Error: "+ throwable.getMessage());
            ToastManager.showToast("Something went wrong");
        }
    };

    @Override
    public void clickedConfirm() {
        mView.showProgress();
        mCompositeDisposable.add(mModel.getFileByUrl("/" + getPDFPreviewResponse().file)
                .flatMap(responseBodyResponse -> Observable.create((ObservableOnSubscribe<File>) subscriber -> {
                    try {
                        String header = responseBodyResponse.headers().get("Content-Disposition");
//                        String fileName = header.replace("attachment; filename=", "");
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile(), "order_" + System.currentTimeMillis() + ".pdf");
                        BufferedSink sink = Okio.buffer(Okio.sink(file));
                        sink.writeAll(responseBodyResponse.body().source());
                        sink.close();
                        subscriber.onNext(file);
                        subscriber.onComplete();
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                })).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(file ->{
                    mView.hideProgress(false);
                    sharePDF(file);
                    if (isFromSendOrderListAct)
                        sendPDFToProducer();
                }, throwableConsumer));
    }

    @Override
    public PDFPreviewResponse getPDFPreviewResponse() {
        return mPDFPreviewResponse;
    }

    private void sharePDF(File _file) {
        Uri fileUri = FileProvider.getUriForFile(PampApp_.getInstance(), Constants.AUTHORITY, _file);
        String bodyText = "Important pdf";
        String subjectText = "PDF";



        Intent shareIntent = ShareCompat.IntentBuilder.from(mActivity)
                .setSubject(subjectText)
                .setText(bodyText)
                .setEmailTo(isFromSendOrderListAct ? new String[]{mProducerEmail} : new String[]{"someones.email@gmail.com"})
                .setType(Constants.MIME_TYPE_PDF)
                .addStream(fileUri)
                .getIntent();
        mActivity.startActivityForResult(shareIntent, Constants.REQUEST_CODE_ACTIVITY_SEND_PDF);
    }

    private void sendPDFToProducer() {
        mCompositeDisposable.add(mModel.sendPDFToProducer(mSendPDFRequest)
                .subscribe(object -> {
                    Log.d("sendPDFToProducer", "Success");
                }, throwableConsumer));
    }
}
