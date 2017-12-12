package com.ferenc.pamp.presentation.screens.main.good_plan.proposed.propose_relay;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import org.androidannotations.annotations.EBean;


/**
 * Created by
 * Ferenc on 2017.12.09..
 */

@EBean(scope = EBean.Scope.Singleton)
public class ProposeRelay {

    public Relay<Boolean> proposeRelay = PublishRelay.create();
}
