package com.ferenc.pamp.presentation.screens.main.propose.description;

import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.presentation.utils.GoodDealManager;

/**
 * Created by
 * Ferenc on 2017.11.22..
 */

public class DescriptionPresenter implements DescriptionContract.Presenter {

    private DescriptionContract.View mView;
    private GoodDealManager mGoodDealManager;
    private GoodDealRequest mGoodDeal;

    public DescriptionPresenter(DescriptionContract.View _view, GoodDealManager _goodDealManager) {
        this.mView = _view;
        this.mGoodDealManager = _goodDealManager;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        GoodDealRequest reUseGoodDeal = mGoodDealManager.getGoodDeal();
        if (reUseGoodDeal.getProduct() != null) {
            mView.setName(reUseGoodDeal.getProduct());
            mView.setDescription(reUseGoodDeal.getDescription());
            mView.setPrice(String.valueOf(reUseGoodDeal.getPrice()));
            mView.setUnit(reUseGoodDeal.getUnit());
            mView.setQuantity(String.valueOf(reUseGoodDeal.getQuantity()));
        }
    }

    @Override
    public void clickedInputField(int _requestCode) {
        mView.openInputActivity(_requestCode);
    }

    @Override
    public void saveName(String _name) {
        mGoodDeal = mGoodDealManager.getGoodDeal();
        mGoodDeal.setProduct(_name);
        mGoodDealManager.saveGoodDeal(mGoodDeal);
        mView.setName(_name);
    }


    @Override
    public void saveDescription(String _description) {
        mGoodDeal = mGoodDealManager.getGoodDeal();
        mGoodDeal.setDescription(_description);
        mGoodDealManager.saveGoodDeal(mGoodDeal);
        mView.setDescription(_description);
    }

    @Override
    public void savePrice(String _price) {
        mGoodDeal = mGoodDealManager.getGoodDeal();
        if (!_price.equals("")) {
            mGoodDeal.setPrice(Integer.valueOf(_price));
            mView.setPrice(String.valueOf(_price));
        } else {
            mGoodDeal.setPrice(0);
            mView.setPrice("");
        }
        mGoodDealManager.saveGoodDeal(mGoodDeal);
    }

    @Override
    public void savePriceDescription(String _priceDescription) {
        mGoodDeal = mGoodDealManager.getGoodDeal();
        mGoodDeal.setUnit(_priceDescription);
        mGoodDealManager.saveGoodDeal(mGoodDeal);
        mView.setUnit(_priceDescription);
    }

    @Override
    public void saveQuantity(String _quantity) {
        mGoodDeal = mGoodDealManager.getGoodDeal();
        if (!_quantity.equals("")) {
            mGoodDeal.setQuantity(Integer.valueOf(_quantity));
            mView.setQuantity(String.valueOf(_quantity));
        } else {
            mGoodDeal.setQuantity(0);
            mView.setQuantity("");
        }
        mGoodDealManager.saveGoodDeal(mGoodDeal);
    }

    @Override
    public void unsubscribe() {

    }
}
