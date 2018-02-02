package com.ferenc.pamp.data.service;

import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.data.model.common.BankAccount;
import com.ferenc.pamp.data.model.home.bank_account.BankAccountRequest;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by
 * Ferenc on 2018.01.05..
 */

public interface BankAccountService {

    @POST("/dealer")
    Observable<BankAccount> attachBankAccount(@Body BankAccountRequest request);

    @PUT("/dealer")
    Observable<BankAccount> updateBankAccount(@Body BankAccountRequest request);
}
