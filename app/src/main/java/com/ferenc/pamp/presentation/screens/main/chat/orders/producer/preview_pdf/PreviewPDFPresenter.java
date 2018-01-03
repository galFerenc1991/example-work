package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.preview_pdf;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ShareCompat;

import com.ferenc.pamp.data.api.Rest;
import com.ferenc.pamp.data.api.RestConst;
import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewRequest;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewResponse;
import com.ferenc.pamp.presentation.utils.ToastManager;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;


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

    public PreviewPDFPresenter(PreviewPDFContract.View _view, PreviewPDFContract.Model _model, PDFPreviewRequest _PDFPreviewRequest, PreviewPDFActivity _activity) {
        this.mView = _view;
        this.mModel = _model;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mPDFPreviewRequest = _PDFPreviewRequest;
        this.mActivity = _activity;
        mView.setPresenter(this);
    }

    public void getPDFPreview() {
        mView.showProgress();
        mCompositeDisposable.add(mModel.getPDFPreview(mPDFPreviewRequest.id, mPDFPreviewRequest)
                .subscribe(pdfPreviewResponse -> {
                    mView.hideProgress(false);
                    mPDFPreviewResponse = pdfPreviewResponse;
                    mView.showPDFInWebView(getPDFPreviewResponse());
                }, throwableConsumer));
    }

    @Override
    public void subscribe() {

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
            ToastManager.showToast("Something went wrong");
        }
    };

    @Override
    public void clickedConfirm() {
        mView.showProgress();
        mCompositeDisposable.add(mModel.getFileByUrl(RestConst.BASE_URL + "/" + getPDFPreviewResponse().file)
                .flatMap(responseBodyResponse -> Observable.create((ObservableOnSubscribe<File>) subscriber -> {
                    try {
//                        String header = responseBodyResponse.headers().get("Content-Disposition");
                        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "orders_list.pdf");
                        BufferedSink sink = Okio.buffer(Okio.sink(file));
                        sink.writeAll(responseBodyResponse.body().source());
                        sink.close();
                        subscriber.onNext(file);
                        subscriber.onComplete();
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }


                })).subscribe(file ->{
                    mView.hideProgress(false);
                    sharePDF(file);
                }, throwableConsumer));
    }

    @Override
    public PDFPreviewResponse getPDFPreviewResponse() {
        return mPDFPreviewResponse;
    }

    private void sharePDF(File _file) {
        Uri fileUri = Uri.fromFile(_file);
        String bodyText = "Body Text That Will Be Shared";
        String subjectText = "Subject Text That Will Be Shared";
        Intent shareIntent = ShareCompat.IntentBuilder.from(mActivity)
                .setSubject(subjectText)
                .setText(bodyText)
                .setType("application/pdf")
                .addStream(fileUri)
                .getIntent();
        mActivity.startActivity(shareIntent);
    }
}
