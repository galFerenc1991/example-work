package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import android.util.Log;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter.MessagesDH;
import com.ferenc.pamp.presentation.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by shonliu on 12/12/17.
 */

public class MessengerPresenter implements MessengerContract.Presenter{

    private MessengerContract.View mView;
    private MessengerContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;

    private int page;
    private int totalPages = Integer.MAX_VALUE;
    private boolean needRefresh;
    private GoodDealResponse mGoodDealResponse;
    private List<MessagesDH> mMessagesDH;
    private User mUser;

    public MessengerPresenter(MessengerContract.View mView, MessengerContract.Model _messageRepository, GoodDealResponse _goodDealResponse, User _myUser) {
        this.mView = mView;
        this.mModel = _messageRepository;
        this.mGoodDealResponse = _goodDealResponse;
        this.mCompositeDisposable = new CompositeDisposable();
        this.page = 1;
        this.mUser = _myUser;
        needRefresh = true;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.getMessages(mGoodDealResponse.id, page)
        .subscribe(model -> {
            mView.hideProgress();
            Log.d("MessengerPresenter", "getMessages Successfully");
            mMessagesDH = new ArrayList<>();

            MessageResponse one = new MessageResponse();
            one.code = Constants.M1_GOOD_DEAL_DIFFUSION;
            one.user = new User();
            one.user.setId("1");
            mMessagesDH.add(new MessagesDH(one, mGoodDealResponse,new User()));

            MessageResponse two = new MessageResponse();
            two.code = Constants.M1_GOOD_DEAL_DIFFUSION;
            two.user = new User();
            two.user.setId("2");
            mMessagesDH.add(new MessagesDH(two, mGoodDealResponse,new User()));

            MessageResponse three = new MessageResponse();
            three.code = Constants.M2_PRODUCT_ORDERING;
            three.user = new User();
            three.user.setId("2");
            mMessagesDH.add(new MessagesDH(three));

            MessageResponse four = new MessageResponse();
            four.code = Constants.M3_ORDER_CHANGING;
            four.user = new User();
            four.user.setId("2");
            mMessagesDH.add(new MessagesDH(four));

            MessageResponse five = new MessageResponse();
            five.code = Constants.M4_ORDER_CANCELLATION;
            five.user = new User();
            five.user.setId("2");
            mMessagesDH.add(new MessagesDH(five));

            MessageResponse six = new MessageResponse();
            six.code = Constants.M5_GOOD_DEAL_DELIVERY_DATE_CHANGED;
            six.user = new User();
            six.user.setId("2");
            mMessagesDH.add(new MessagesDH(six));

            MessageResponse seven = new MessageResponse();
            seven.code = Constants.M6_GOOD_DEAL_CLOSING_DATE_CHANGED;
            seven.user = new User();
            seven.user.setId("2");
            mMessagesDH.add(new MessagesDH(seven));

            MessageResponse eight = new MessageResponse();
            eight.code = Constants.M8_GOOD_DEAL_CANCELLATION;
            eight.user = new User();
            eight.user.setId("2");
            mMessagesDH.add(new MessagesDH(eight));

            MessageResponse nine = new MessageResponse();
            nine.code = Constants.M9_CLOSING_DATE;
            nine.user = new User();
            nine.user.setId("2");
            mMessagesDH.add(new MessagesDH(nine));

            MessageResponse ten = new MessageResponse();
            ten.code = Constants.M10_GOOD_DEAL_CLOSING;
            ten.user = new User();
            ten.user.setId("2");
            mMessagesDH.add(new MessagesDH(ten));

            MessageResponse eleven = new MessageResponse();
            eleven.code = Constants.M11_1_GOOD_DEAL_CONFIRMATION;
            eleven.user = new User();
            eleven.user.setId("2");
            mMessagesDH.add(new MessagesDH(eleven));

            MessageResponse twelve = new MessageResponse();
            twelve.code = Constants.M11_2_GOOD_DEAL_CONFIRMATION_REJECTED;
            twelve.user = new User();
            twelve.user.setId("2");
            mMessagesDH.add(new MessagesDH(twelve));

            MessageResponse thirteen = new MessageResponse();
            thirteen.code = Constants.M11_3_GOOD_DEAL_CONFIRMATION_APPLYED;
            thirteen.user = new User();
            thirteen.user.setId("2");
            mMessagesDH.add(new MessagesDH(thirteen));

            MessageResponse fourteen = new MessageResponse();
            fourteen.code = Constants.M12_DELIVERY_DATE;
            fourteen.user = new User();
            fourteen.user.setId("2");
            mMessagesDH.add(new MessagesDH(fourteen));
            mView.setMessagesList(mMessagesDH);

//            for (MessageResponse messageResponse : model.data) {
//
//                if (messageResponse.code.equals(Constants.M1_GOOD_DEAL_DIFFUSION)){
//                    mMessagesDH.add(new MessagesDH(messageResponse, mGoodDealResponse, mUser));
//                } else {
//                    mMessagesDH.add(new MessagesDH(messageResponse));
//                }
//            }

            mView.setMessagesList(mMessagesDH);

        }, throwable -> {
            mView.hideProgress();
            Log.d("MessengerPresenter", "Error " + throwable.getMessage());
        }));
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void openMessengerFragment() {
        mView.openMessengerFragment();
    }

    @Override
    public void loadNextPage() {

    }

}
