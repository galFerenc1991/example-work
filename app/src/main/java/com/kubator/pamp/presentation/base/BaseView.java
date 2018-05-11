package com.kubator.pamp.presentation.base;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

public interface BaseView<T extends BasePresenter> {
    void initPresenter();
    void setPresenter(T presenter);
}
