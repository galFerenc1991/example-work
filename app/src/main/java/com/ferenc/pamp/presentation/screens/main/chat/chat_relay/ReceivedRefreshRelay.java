package com.ferenc.pamp.presentation.screens.main.chat.chat_relay;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import org.androidannotations.annotations.EBean;

/**
 * Created by
 * Ferenc on 2018.01.22..
 */


@EBean(scope = EBean.Scope.Singleton)
public class ReceivedRefreshRelay {

    public Relay<Boolean> receivedRefreshRelay = PublishRelay.create();
}
