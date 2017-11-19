package com.ferenc.pamp.presentation.screens.splash;

import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;

/**
 * Created by
 * Ferenc on 2017.11.10..
 */

public interface SplashContract {

    interface View extends BaseView<Presenter>{
        void startTutorial();
        void openAuth();
        void openMain();
    }
    interface Presenter extends BasePresenter{

    }
}
