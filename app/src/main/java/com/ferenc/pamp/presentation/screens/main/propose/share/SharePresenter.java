package com.ferenc.pamp.presentation.screens.main.propose.share;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.presentation.base.models.UserContact;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactAdapter;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactDH;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ContactManager;
import com.ferenc.pamp.presentation.utils.FirebaseDynamicLinkGenerator;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.GoodDealValidateManager;
import com.ferenc.pamp.presentation.utils.ToastManager;

import java.util.ArrayList;
import java.util.Collections;
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

    public SharePresenter(ShareContract.View _view, ShareContract.Model _model, GoodDealManager _goodDealManager, GoodDealResponseManager _goodDealResponseManager,  boolean _isReBroadcastFlow, boolean _isUpdateGoodDeal, ContactManager _contactManager) {
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
                .flatMap(usedContact -> Observable.just(mContactManager.getContactsDH()))
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

    private ArrayList<UserContact> getContactList() {
        ArrayList<UserContact> _phoneContactList = new ArrayList<>();
        ContentResolver cr = PampApp_.getInstance().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d("Contact", "Name: " + name);
                        Log.d("Contact", "Phone Number: " + phoneNo);

                        _phoneContactList.add(new UserContact(name, phoneNo));
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }

        Collections.sort(_phoneContactList, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        return _phoneContactList;
    }

    @Override
    public void share(List<ContactDH> contactDHList) {
        if (!mIsReBroadcastFlow){
            List<String> selectedContacts = getSelectedContacts(contactDHList);
            if (!selectedContacts.isEmpty()) {
                if (GoodDealValidateManager.validate(mGoodDealManager.getGoodDeal())) {

                    mView.showProgressMain();
                    mCompositeDisposable.add((mIsUpdateGoodDeal
                            ? mModel.updateGoodDeal(mGoodDealManager.getGoodDeal().getId(), createRequestParameter(contactDHList))
                            : mModel.createGoodDeal(createRequestParameter(contactDHList)))
                            .subscribe(goodDealResponse -> {
                                mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                                mView.hideProgress();
                                mView.sendSmsWith(FirebaseDynamicLinkGenerator.getDynamicLink(goodDealResponse.id), getSelectedContacts(contactDHList), goodDealResponse);


                            }, throwable -> {
                                mView.hideProgress();
                                mView.showErrorMessage(Constants.MessageType.ERROR_WHILE_SELECT_ADDRESS);
                            }));
                } else {
                    mView.openVerificationErrorPopUP();
                }
            } else {
                ToastManager.showToast("Please select contacts");
            }
        } else {
            mView.showProgressMain();
            mCompositeDisposable.add(mModel.resendGoodDeal(createRequestParameter(contactDHList))
                    .subscribe(goodDealResponse -> {
                        mView.hideProgress();
                        mView.sendSmsWith(FirebaseDynamicLinkGenerator.getDynamicLink(goodDealResponse.id), getSelectedContacts(contactDHList), goodDealResponse);
                    }, throwable -> {
                        mView.hideProgress();
                        mView.showErrorMessage(Constants.MessageType.ERROR_WHILE_SELECT_ADDRESS);
                    }));
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
