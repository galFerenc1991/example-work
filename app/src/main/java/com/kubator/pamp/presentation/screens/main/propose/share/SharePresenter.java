package com.kubator.pamp.presentation.screens.main.propose.share;

import com.kubator.pamp.data.api.exceptions.ConnectionLostException;
import com.kubator.pamp.data.model.base.GeneralMessageResponse;
import com.kubator.pamp.data.model.home.good_deal.GoodDealRequest;
import com.kubator.pamp.presentation.screens.main.propose.share.adapter.ContactAdapter;
import com.kubator.pamp.presentation.screens.main.propose.share.adapter.ContactDH;
import com.kubator.pamp.presentation.utils.ContactManager;
import com.kubator.pamp.presentation.utils.FirebaseDynamicLinkGenerator;
import com.kubator.pamp.presentation.utils.GoodDealManager;
import com.kubator.pamp.presentation.utils.GoodDealResponseManager;
import com.kubator.pamp.presentation.utils.GoodDealValidateManager;
import com.kubator.pamp.presentation.utils.ToastManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by
 * Ferenc on 2017.11.27..
 */

public class SharePresenter implements ShareContract.Presenter {

    private ShareContract.View mView;
    private ShareContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;
    private List<ContactDH> mPhoneContactList;
    private List<ContactDH> mContactsList;
    private GoodDealManager mGoodDealManager;
    private GoodDealResponseManager mGoodDealResponseManager;
    private boolean mIsReBroadcastFlow;
    private boolean mIsUpdateGoodDeal;
    private ContactManager mContactManager;

    public SharePresenter(ShareContract.View _view, ShareContract.Model _model, GoodDealManager _goodDealManager, GoodDealResponseManager _goodDealResponseManager, boolean _isReBroadcastFlow, boolean _isUpdateGoodDeal, ContactManager _contactManager) {
        this.mView = _view;
        this.mModel = _model;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mPhoneContactList = new ArrayList<>();
        this.mGoodDealManager = _goodDealManager;
        this.mGoodDealResponseManager = _goodDealResponseManager;
        this.mIsReBroadcastFlow = _isReBroadcastFlow;
        this.mIsUpdateGoodDeal = _isUpdateGoodDeal;
        this.mContactManager = _contactManager;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.getUsedUserContact()
                .flatMap(usedContact -> Observable.just(mContactManager.getContactsDH(usedContact)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactDHList -> {
                    mPhoneContactList = contactDHList;
                    mView.hideProgress();
                    mView.setContactAdapterList(mPhoneContactList);
                }, throwable -> {
                    mView.hideProgress();
                }));
    }

    @Override
    public void selectItem(ContactDH item, int position) {
        if (item.isSelected()) {
            mView.updateItem(new ContactDH(item.getUserContact(), false, ContactAdapter.TYPE_ITEM), position);
        } else {
            mView.updateItem(new ContactDH(item.getUserContact(), true, ContactAdapter.TYPE_ITEM), position);
        }
    }

    @Override
    public void share(List<ContactDH> contactDHList) {
        List<String> selectedContacts = getSelectedContacts(contactDHList);

        if (!mIsReBroadcastFlow) {
            if (GoodDealValidateManager.validate(mGoodDealManager.getGoodDeal(), selectedContacts)) {
                mView.showProgressMain();
                mCompositeDisposable.add((mIsUpdateGoodDeal
                        ? mModel.updateGoodDeal(mGoodDealManager.getGoodDeal().getId(), createRequestParameter(contactDHList))
                        : mModel.createGoodDeal(createRequestParameter(contactDHList)))
                        .subscribe(goodDealResponse -> {
                            mView.hideProgress();
                            mView.sendSmsWith(FirebaseDynamicLinkGenerator.getDynamicLink(goodDealResponse.id), getSelectedContacts(contactDHList), goodDealResponse);
//                            mView.getShortLink(getSelectedContacts(contactDHList), goodDealResponse);
                        }, throwableConsumer));
            } else {
                mView.openVerificationErrorPopUP();
            }
        } else {
            if (GoodDealValidateManager.validate(mGoodDealManager.getGoodDeal(), selectedContacts)) {
                mView.showProgressMain();
                mCompositeDisposable.add(mModel.resendGoodDeal(createRequestParameter(contactDHList))
                        .subscribe(goodDealResponse -> {

                            if (mIsUpdateGoodDeal)
                                goodDealResponse.recipients = mGoodDealResponseManager.getGoodDealResponse().recipients;

                            mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                            mView.hideProgress();
                            mView.sendSmsWith(FirebaseDynamicLinkGenerator.getDynamicLink(goodDealResponse.id), getSelectedContacts(contactDHList), goodDealResponse);
//                            mView.getShortLink(getSelectedContacts(contactDHList), goodDealResponse);
                        }, throwableConsumer));
            } else {
                mView.openResendVerificationErrorPopUP();
//                ToastManager.showToast("Please set all required fields");
            }
        }


    }

    @Override
    public void search(String searchText) {
        List<ContactDH> searchedList = new ArrayList<>();

        for (ContactDH contactDH : mPhoneContactList)
            if (contactDH.getUserContact() != null)
                if (contactDH.getUserContact().getName().toLowerCase().contains(searchText.toLowerCase())
                        || contactDH.getUserContact().getPhoneNumber().contains(searchText.toLowerCase()))
                    searchedList.add(contactDH);

        Observable.just(searchText)
                .flatMap(search -> {
                    if (!search.equals("")) {
                        for (ContactDH contactDH : mPhoneContactList)
                            if (contactDH.getUserContact() != null)
                                if (contactDH.getUserContact().getName().toLowerCase().contains(search.toLowerCase())
                                    || contactDH.getUserContact().getPhoneNumber().contains(search.toLowerCase()))
                                    searchedList.add(contactDH);

                        return Observable.just(mContactManager.deleteAllDuplicateContactsDH(searchedList));
                    } else return Observable.just(mPhoneContactList);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(list-> {
                    mView.setContactAdapterList(list);
                }, throwable ->{});
    }

    private Consumer<Throwable> throwableConsumer = throwable -> {
        throwable.printStackTrace();
        mView.hideProgress();
        if (throwable instanceof ConnectionLostException) {
            ToastManager.showToast("Connection Lost");
        } else if (throwable instanceof HttpException) {
            int errorCode = ((HttpException) throwable).response().code();
            switch (errorCode) {
                case 400:
                    Gson gson = new Gson();
                    GeneralMessageResponse _data = gson.fromJson(((HttpException) throwable).response().errorBody().string(), GeneralMessageResponse.class);
                    ToastManager.showToast(_data.getMessage());
            }
        } else {
            ToastManager.showToast("Something went wrong");
        }
    };

    private GoodDealRequest createRequestParameter(List<ContactDH> contactDHList) {
        GoodDealRequest savedGoodDeal = mGoodDealManager.getGoodDeal();
        savedGoodDeal.contacts = getSelectedContacts(contactDHList);
        return savedGoodDeal;
    }

    private List<String> getSelectedContacts(List<ContactDH> contactDHList) {
        List<String> selectedContact = new ArrayList<>();
        for (ContactDH contact : contactDHList) {
            if (contact.isSelected()) {
                selectedContact.add(contact.getUserContact().getPhoneNumber());
            }
        }
        return selectedContact;
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
