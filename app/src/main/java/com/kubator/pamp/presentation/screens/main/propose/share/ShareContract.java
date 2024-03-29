package com.kubator.pamp.presentation.screens.main.propose.share;

import android.net.Uri;

import com.kubator.pamp.data.model.home.good_deal.GoodDealRequest;
import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;
import com.kubator.pamp.presentation.screens.main.propose.share.adapter.ContactDH;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.11.27..
 */

public interface ShareContract {
    interface View extends ContentView, BaseView<Presenter> {

        boolean isReedContactsPermissionNotGranted();

        void checkReedContactsPermission();

        boolean isSendSMSPermissionNotGranted();

        void checkSendSMSPermission();

        void setContactAdapterList(List<ContactDH> _list);

        void updateItem(ContactDH item, int position);

        void sendSmsWith(Uri _dynamicLink, List<String> _selectedContacts, GoodDealResponse _goodDealResponse);

        void openVerificationErrorPopUP();

        void openResendVerificationErrorPopUP();

        void getShortLink(List<String> _selectedContacts, GoodDealResponse _goodDealResponse);
    }

    interface Presenter extends BasePresenter {
        void selectItem(ContactDH item, int position);

        void share(List<ContactDH> contactDHList);

        void search(String searchText);
    }

    interface Model extends BaseModel {
        Observable<List<String>> getUsedUserContact();

        Observable<GoodDealResponse> createGoodDeal(GoodDealRequest request);

        Observable<GoodDealResponse> resendGoodDeal(GoodDealRequest request);

        Observable<GoodDealResponse> updateGoodDeal(String id, GoodDealRequest request);
    }
}
