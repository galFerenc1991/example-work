package com.kubator.pamp.presentation.screens.main.good_plan.received.receive_relay;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import org.androidannotations.annotations.EBean;

/**
 * Created by
 * Ferenc on 2017.12.11..
 */
@EBean(scope = EBean.Scope.Singleton)

public class ReceiveRelay {

    public Relay<Boolean> receiveRelay = PublishRelay.create();

}
