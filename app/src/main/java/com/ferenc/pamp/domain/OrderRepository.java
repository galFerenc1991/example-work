package com.ferenc.pamp.domain;

import com.ferenc.pamp.data.api.Rest;
import com.ferenc.pamp.data.service.OrderService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by
 * Ferenc on 2017.12.20..
 */

@EBean(scope = EBean.Scope.Singleton)
public class OrderRepository extends NetworkRepository {

    @Bean
    protected Rest rest;

    private OrderService orderService;

    @AfterInject
    protected void initService() {
        orderService = rest.getOrderService();
    }

}
