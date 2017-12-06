package com.ferenc.pamp.presentation.screens.main.propose.share;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.presentation.base.models.GoodDeal;
import com.ferenc.pamp.presentation.base.models.UserContact;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactAdapter;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactDH;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.ferenc.pamp.presentation.utils.GoodDealValidateManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.27..
 */

public class SharePresenter implements ShareContract.Presenter {

    private ShareContract.View mView;
    private ShareContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;
    private List<ContactDH> mPhoneContactList;
    private GoodDealRequest mGoodDealRequest;
    private GoodDealManager mGoodDealManager;

    public SharePresenter(ShareContract.View _view, ShareContract.Model _model, GoodDealManager _goodDealManager) {
        this.mView = _view;
        this.mModel = _model;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mPhoneContactList = new ArrayList<>();
        this.mGoodDealRequest = new GoodDealRequest();
        this.mGoodDealManager = _goodDealManager;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.getUsedUserContact()
                .flatMap(usedContact -> {
                    List<ContactDH> contactDHList = new ArrayList<>();
                    String header = " ";
//                    boolean isCurrent = false;
                    contactDHList.add(new ContactDH("VOS CONTACTS", ContactAdapter.TYPE_CONTACT_HEADER));
                    for (UserContact contact : getContactList()) {
                        String contactName = contact.getName();
                        if (!contactName.startsWith(header)) {
                            header = String.valueOf(contactName.charAt(0));
                            contactDHList.add(new ContactDH(header, ContactAdapter.TYPE_HEADER));
                        }
//                        isCurrent = country.equalsIgnoreCase(mCountry);
//                        if (isCurrent) {
//                            selectedPos = countryDHList.size();
//                            mCountry = country;
//                        }
                        contactDHList.add(new ContactDH(contact, ContactAdapter.TYPE_ITEM));
                    }
                    return Observable.just(contactDHList);
                })
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
        return _phoneContactList;
    }

    @Override
    public void share(List<ContactDH> contactDHList) {
        List<String> selectedContacts = getSelectedContacts(contactDHList);
        if (!selectedContacts.isEmpty()) {
            if (GoodDealValidateManager.validate(mGoodDealManager.getGoodDeal())) {
                mView.showProgressMain();
                mCompositeDisposable.add(mModel.createGoodDeal(createRequestParameter(contactDHList))
                        .subscribe(goodDealResponse -> {
                            mView.hideProgress();
                            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                    .setLink(Uri.parse("https://example.com/"))
                                    .setDynamicLinkDomain("ra5v9.app.goo.gl")
                                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                                    .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                                    .buildDynamicLink();

                            Uri dynamicLinkUri = dynamicLink.getUri();

                            mView.sendSmsWith(dynamicLinkUri, getSelectedContacts(contactDHList));

                        }, throwable -> {
                            mView.hideProgress();

                        }));
            } else {
                mView.openVerificationErrorPopUP();
            }
        } else {
            ToastManager.showToast("Please select contacts");
        }

    }

    private GoodDealRequest createRequestParameter(List<ContactDH> contactDHList) {
        GoodDeal savedGoodDeal = mGoodDealManager.getGoodDeal();
        mGoodDealRequest.product = savedGoodDeal.getName();
        mGoodDealRequest.description = savedGoodDeal.getDescription();
        mGoodDealRequest.price = savedGoodDeal.getPrice();
        mGoodDealRequest.quantity = savedGoodDeal.getQuantity();
        mGoodDealRequest.unit = savedGoodDeal.getPriceDescription();
        mGoodDealRequest.closingDate = savedGoodDeal.getCloseDate();
        mGoodDealRequest.deliveryStartDate = savedGoodDeal.getStartDelivery();
        mGoodDealRequest.deliveryEndDate = savedGoodDeal.getEndDelivery();
        mGoodDealRequest.deliveryAddress = savedGoodDeal.getDeliveryPlace();
        mGoodDealRequest.contacts = getSelectedContacts(contactDHList);
        return mGoodDealRequest;
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
