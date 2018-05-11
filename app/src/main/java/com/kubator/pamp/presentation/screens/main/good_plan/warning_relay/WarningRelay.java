package com.kubator.pamp.presentation.screens.main.good_plan.warning_relay;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import org.androidannotations.annotations.EBean;

/**
 * Created by
 * Ferenc on 2018.01.31..
 */

@EBean(scope = EBean.Scope.Singleton)
public class WarningRelay {
    public Relay<Boolean> proposeRelay = PublishRelay.create();
}
