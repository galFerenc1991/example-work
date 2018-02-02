package com.ferenc.pamp.domain;

import com.ferenc.pamp.data.api.Rest;
import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.data.model.common.BankAccount;
import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.bank_account.BankAccountRequest;
import com.ferenc.pamp.data.service.BankAccountService;
import com.ferenc.pamp.data.service.CardService;
import com.ferenc.pamp.presentation.custom.bank_account.BankAccountContract;
import com.ferenc.pamp.presentation.utils.SignedUserManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by
 * Ferenc on 2018.01.05..
 */

@EBean(scope = EBean.Scope.Singleton)
public class BankAccountRepository extends NetworkRepository implements BankAccountContract.BankAccountModel {

    @Bean
    protected Rest rest;

    @Bean
    protected SignedUserManager mSignedUserManager;

    private BankAccountService bankAccountService;

    @AfterInject
    protected void initService() {
        bankAccountService = rest.getBankAccountService();
    }

    @Override
    public Observable<BankAccount> attachBankAccount(BankAccountRequest request) {
        return getNetworkObservable(bankAccountService.attachBankAccount(request)
                .flatMap(bankAccount -> {
                    User user = mSignedUserManager.getCurrentUser();
                    user.setBankAccount(bankAccount);
                    return Observable.just(bankAccount);
                }));
    }

    @Override
    public Observable<BankAccount> updateBankAccount(BankAccountRequest request) {
        return getNetworkObservable(bankAccountService.attachBankAccount(request)
                .flatMap(bankAccount -> {
                    User user = mSignedUserManager.getCurrentUser();
                    user.setBankAccount(bankAccount);
                    return Observable.just(bankAccount);
                }));
    }
}
