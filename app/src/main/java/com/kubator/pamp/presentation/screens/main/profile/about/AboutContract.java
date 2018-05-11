package com.kubator.pamp.presentation.screens.main.profile.about;

import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by shonliu on 1/25/18.
 */

public interface AboutContract {

    interface View extends BaseView<Presenter> {

        void showProgressBar();

        void hideProgressBar();

        void initClickListeners();

        void initBar();

        void showHTML(String html);

    }

    interface Presenter extends BasePresenter {

        void clickCGU();

        void clickRules();

        void clickAboutUs();
    }

    interface Model {
        Observable<ResponseBody> getCGU();

        Observable<ResponseBody> getRules();

        Observable<ResponseBody> getAboutUs();

    }
}
