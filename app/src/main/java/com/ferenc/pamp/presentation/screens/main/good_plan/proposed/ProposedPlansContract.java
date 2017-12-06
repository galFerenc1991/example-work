package com.ferenc.pamp.presentation.screens.main.good_plan.proposed;

import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */

public interface ProposedPlansContract {
    interface View extends ContentView, BaseView<Presenter> {
        void openProposerFragment();
    }

    interface Presenter extends BasePresenter {
        void openProposerFragment();

    }

    interface Model extends BaseModel {

    }
}
