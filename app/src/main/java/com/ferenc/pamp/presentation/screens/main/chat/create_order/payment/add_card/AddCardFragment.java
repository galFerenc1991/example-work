package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.CardRepository;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.base.models.BankCard;
import com.ferenc.pamp.presentation.base.models.BankCardNumber;
import com.ferenc.pamp.presentation.base.models.ExpDate;
import com.ferenc.pamp.presentation.custom.bank_card_inputs.BankCardCVVInputActivity_;
import com.ferenc.pamp.presentation.custom.bank_card_inputs.BankCardExpirationInputActivity_;
import com.ferenc.pamp.presentation.custom.bank_card_inputs.BankCardNumberInputActivity_;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.save_card.SaveCardFragment_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.SignedUserManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.jakewharton.rxbinding2.view.RxView;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

/**
 * Created by
 * Ferenc on 2017.12.24..
 */

@EFragment
public class AddCardFragment extends ContentFragment implements AddCardContract.View {

    @Override
    public void setPresenter(AddCardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_add_card;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private AddCardContract.Presenter mPresenter;

    @ViewById(R.id.rlCardNumber_FAC)
    protected RelativeLayout rlCardNumber;
    @ViewById(R.id.rlExpirationDate_FAC)
    protected RelativeLayout rlExpirationDate_FAC;
    @ViewById(R.id.rlCardCVV_FAC)
    protected RelativeLayout rlCardCVV;
    @ViewById(R.id.tvCardNumber_FAC)
    protected TextView tvCardNumber;
    @ViewById(R.id.tvExpirationDate_FAC)
    protected TextView tvExpirationDate;
    @ViewById(R.id.tvCardCVV_FAC)
    protected TextView tvCardCVV;
    @ViewById(R.id.btnValidate_FAC)
    protected Button btnValidate;

    @Bean
    protected CardRepository mCardRepository;

    @Bean
    protected SignedUserManager mSignedUserManager;

    @FragmentArg
    protected boolean withEditProfile;

    @FragmentArg
    protected double mQuantity;

    @AfterInject
    @Override
    public void initPresenter() {
        new AddCardPresenter(this, mQuantity, mCardRepository, withEditProfile, mSignedUserManager);
    }

    @AfterViews
    protected void initUI() {
        initClickListeners();
        mPresenter.subscribe();
    }

    private void initClickListeners() {
        RxView.clicks(rlCardNumber)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedCardNumber(Constants.REQUEST_CODE_ACTIVITY_CARD_NUMBER));
        RxView.clicks(rlExpirationDate_FAC)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedExpirationDate(Constants.REQUEST_CODE_ACTIVITY_EXPIRATION));
        RxView.clicks(rlCardCVV)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedCardCVV(Constants.REQUEST_CODE_ACTIVITY_CARD_CVV));

        RxView.clicks(btnValidate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedValidate());
    }


    @Override
    public void openCardNumberInputScreen(int _requestCode, BankCardNumber _bankCardNumber) {
        BankCardNumberInputActivity_.intent(this)
                .mBankCardNumber(_bankCardNumber)
                .startForResult(_requestCode);
    }

    @Override
    public void openCardExpirationInputScreen(int _requestCode, ExpDate _expDate) {
        BankCardExpirationInputActivity_.intent(this)
                .mExpDate(_expDate)
                .startForResult(_requestCode);
    }

    @Override
    public void openCardCVVInputScreen(int _requestCode, String _cardCVV) {
        BankCardCVVInputActivity_.intent(this)
                .mCardCVV(_cardCVV)
                .startForResult(_requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra(Constants.KEY_INPUT_RESULT);
            switch (requestCode) {
                case Constants.REQUEST_CODE_ACTIVITY_CARD_NUMBER:
                    mPresenter.saveCardNumber(data.getParcelableExtra(Constants.KEY_INPUT_RESULT));
                    break;
                case Constants.REQUEST_CODE_ACTIVITY_EXPIRATION:
                    mPresenter.saveExpirationDate(data.getParcelableExtra(Constants.KEY_INPUT_RESULT));
                    break;
                case Constants.REQUEST_CODE_ACTIVITY_CARD_CVV:
                    mPresenter.saveCVV(result);
                    break;
            }
        }
    }

    @Override
    public void setCardNumber(String _cardNumber) {
        tvCardNumber.setText(_cardNumber);
    }

    @Override
    public void setExpirationDate(String _expirationDate) {
        tvExpirationDate.setText(_expirationDate);
    }

    @Override
    public void setCVV(String _cvv) {
        tvCardCVV.setText(_cvv);
    }

    @Override
    public void openSetNewCardScreen(String _cardType, String _last4, String _token, double _quantity) {
        mActivity.replaceFragment(SaveCardFragment_.builder()
                .mCardType(_cardType)
                .mLast4(_last4)
                .mQuantity(_quantity)
                .mStripeToken(_token)
                .build());
    }

    @Override
    public void getTokenWithStripe(BankCard _bankCard) {
        showProgressMain();
        Stripe stripe = new Stripe(mActivity, "pk_test_JjnCq5JreSkRqrcqgInmTDAn");
        Card card = new Card(_bankCard.getCardNumber(), _bankCard.getCardExpMonth(), _bankCard.getCardExpYear(), _bankCard.getCardCVC());
        stripe.createToken(card, new TokenCallback() {
            @Override
            public void onError(Exception error) {
                hideProgress();
                ToastManager.showToast(error.getMessage());
            }

            @Override
            public void onSuccess(Token token) {
                hideProgress();
                mPresenter.createCard(token.getId(), token.getCard().getBrand(), token.getCard().getLast4());
            }
        });
    }

    @Override
    public void closeAddCardScreen(com.ferenc.pamp.data.model.common.Card _card) {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_BANK_CARD, _card);
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
