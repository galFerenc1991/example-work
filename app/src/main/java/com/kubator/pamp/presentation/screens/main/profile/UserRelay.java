package com.kubator.pamp.presentation.screens.main.profile;

import com.kubator.pamp.data.model.common.User;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.Relay;

import org.androidannotations.annotations.EBean;

/**
 * Created by shonliu on 12/26/17.
 */
@EBean(scope = EBean.Scope.Singleton)
public class UserRelay {

    public Relay<User> userRelay = BehaviorRelay.create();

}
