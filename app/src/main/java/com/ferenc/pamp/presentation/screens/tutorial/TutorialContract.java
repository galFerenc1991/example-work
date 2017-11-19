package com.ferenc.pamp.presentation.screens.tutorial;

import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;

/**
 * Created by
 * Ferenc on 2017.11.12..
 */

public interface TutorialContract {

    interface View extends BaseView<Presenter> {
        void openAuth();
    }

    interface Presenter extends BasePresenter {
        void skip();
    }
}
