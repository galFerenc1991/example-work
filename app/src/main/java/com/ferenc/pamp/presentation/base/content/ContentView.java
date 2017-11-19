package com.ferenc.pamp.presentation.base.content;

import com.ferenc.pamp.presentation.utils.Constants;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

public interface ContentView {
    void showProgressMain();

    void showProgressPagination();

    void hideProgress();

    void showErrorMessage(Constants.MessageType messageType);

    void showCustomMessage(String msg, boolean isDangerous);

    void showPlaceholder(Constants.PlaceholderType placeholderType);
}
