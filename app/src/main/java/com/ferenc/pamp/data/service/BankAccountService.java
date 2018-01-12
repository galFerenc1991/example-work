package com.ferenc.pamp.data.service;

import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.data.model.common.BankAccount;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by
 * Ferenc on 2018.01.05..
 */

public interface BankAccountService {

    @POST("/dealer/bankAccount")
    Observable<BankAccount> attachBankAccount(@Body TokenRequest request);
}
