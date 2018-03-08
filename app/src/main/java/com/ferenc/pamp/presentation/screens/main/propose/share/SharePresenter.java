package com.ferenc.pamp.presentation.screens.main.propose.share;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactAdapter;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactDH;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ContactManager;
import com.ferenc.pamp.presentation.utils.FirebaseDynamicLinkGenerator;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.GoodDealValidateManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by
 * Ferenc on 2017.11.27..
 */

public class SharePresenter implements ShareContract.Presenter {

    private ShareContract.View mView;
    private ShareContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;
    private List<ContactDH> mPhoneContactList;
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
//                            mView.sendSmsWith(FirebaseDynamicLinkGenerator.getDynamicLink(goodDealResponse.id), getSelectedContacts(contactDHList), goodDealResponse);
                            mView.getShortLink(getSelectedContacts(contactDHList), goodDealResponse);
                        }, throwable -> {
                            mView.hideProgress();
//                            mView.showErrorMessage(Constants.MessageType.ERROR_WHILE_SELECT_ADDRESS);
                            ToastManager.showToast(throwable.getLocalizedMessage());
                        }));
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
//                            mView.sendSmsWith(FirebaseDynamicLinkGenerator.getDynamicLink(goodDealResponse.id), getSelectedContacts(contactDHList), goodDealResponse);
                            mView.getShortLink(getSelectedContacts(contactDHList), goodDealResponse);
                        }, throwable -> {
                            mView.hideProgress();
                            mView.showErrorMessage(Constants.MessageType.UNKNOWN);
                        }));
            } else {
                ToastManager.showToast("Please set all required fields");
            }
        }


    }

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
