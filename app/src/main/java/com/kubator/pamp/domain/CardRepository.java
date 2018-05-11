package com.kubator.pamp.domain;

import com.kubator.pamp.data.api.Rest;
import com.kubator.pamp.data.model.auth.TokenRequest;
import com.kubator.pamp.data.model.common.Card;
import com.kubator.pamp.data.model.common.User;
import com.kubator.pamp.data.service.CardService;
import com.kubator.pamp.presentation.screens.main.chat.create_order.payment.add_card.AddCardContract;
import com.kubator.pamp.presentation.utils.GoodDealResponseManager;
import com.kubator.pamp.presentation.utils.SignedUserManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.28..
 */

@EBean(scope = EBean.Scope.Singleton)
public class CardRepository extends NetworkRepository implements AddCardContract.Model {

    @Bean
    protected Rest rest;

    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;

    @Bean
    protected SignedUserManager signedUserManager;

    private CardService cardService;

    @AfterInject
    protected void initService() {
        cardService = rest.getCardService();
    }

    @Override
    public Observable<Card> createCard(TokenRequest _tokenRequest) {
        return getNetworkObservable(cardService.createCard(_tokenRequest)
                .flatMap(card -> {
                    User user = signedUserManager.getCurrentUser();
                    user.setCard(card);
                    signedUserManager.saveUser(user);
                    return Observable.just(card);
                }));
    }
}
