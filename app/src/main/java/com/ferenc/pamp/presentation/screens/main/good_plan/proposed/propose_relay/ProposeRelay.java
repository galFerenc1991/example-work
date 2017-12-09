package com.ferenc.pamp.presentation.screens.main.good_plan.proposed.propose_relay;

import com.jakewharton.rxrelay2.BehaviorRelay;

import org.androidannotations.annotations.EBean;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.09..
 */

@EBean(scope = EBean.Scope.Singleton)
public class ProposeRelay {

    public BehaviorRelay<Boolean> proposeRelay = BehaviorRelay.create();
}
