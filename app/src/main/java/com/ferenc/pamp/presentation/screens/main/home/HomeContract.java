package com.ferenc.pamp.presentation.screens.main.home;

import com.ferenc.pamp.data.model.auth.SignUpResponse;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.11.17..
 */

public interface HomeContract {
    interface View extends ContentView , BaseView<Presenter>{
        void openAuthScreen();
    }

    interface Presenter extends BasePresenter{
        void signOut();
    }

    interface Model extends BaseModel{
        Observable<SignUpResponse> signOut();

    }
}
