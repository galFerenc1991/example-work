package com.ferenc.pamp.presentation.screens.main.chat.orders;

import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;

/**
 * Created by
 * Ferenc on 2017.12.15..
 */

public interface OrderContract {

    interface View extends ContentView, BaseView<Presenter>{

    }
    interface Presenter extends BasePresenter{

    }

}
