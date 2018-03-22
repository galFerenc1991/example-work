package com.ferenc.pamp.presentation.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.ferenc.pamp.presentation.base.models.UserContact;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactAdapter;
import com.ferenc.pamp.presentation.screens.main.propose.share.adapter.ContactDH;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * Created by shonliu on 12/21/17.
 */
@EBean
public class ContactManager {

    private ArrayList<UserContact> mUserContacts;

    @RootContext
    Context context;

    public List<ContactDH> getContactsDH(List<String> _usedContacts) {

        mUserContacts = getSortedContactList();
        ArrayList<UserContact> mUsedContacts = getUsedContacts(_usedContacts);

        List<ContactDH> contactDHList = new ArrayList<>();
        String header = " ";
        String usedContactsHeader = "LES DESTINATAIRES DE VOS BONS PLANS";
        String myContactsHeader = "VOS CONTACTS";

        contactDHList.add(new ContactDH(usedContactsHeader, ContactAdapter.TYPE_CONTACT_HEADER));

        for (UserContact contact : mUsedContacts) {
            String contactName = contact.getName();
            if (!contactName.startsWith(header)) {
                header = String.valueOf(contactName.charAt(0));
                contactDHList.add(new ContactDH(header, ContactAdapter.TYPE_HEADER));
            }
            contactDHList.add(new ContactDH(contact, ContactAdapter.TYPE_ITEM));
        }

        contactDHList.add(new ContactDH(myContactsHeader, ContactAdapter.TYPE_CONTACT_HEADER));

        for (UserContact contact : mUserContacts) {
            String contactName = contact.getName();
            if (!contactName.startsWith(header)) {
                header = String.valueOf(contactName.charAt(0));
                contactDHList.add(new ContactDH(header, ContactAdapter.TYPE_HEADER));
            }
            contactDHList.add(new ContactDH(contact, ContactAdapter.TYPE_ITEM));
        }
        return contactDHList;
    }

    private ArrayList<UserContact> getSortedContactList() {
        ArrayList<UserContact> _phoneContactList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                            null);

                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        _phoneContactList.add(new UserContact(name, phoneNo));
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return deleteAllDuplicateContacts(_phoneContactList);
    }

    private ArrayList<UserContact> getUsedContacts(List<String> _usedContacts) {
        ArrayList<UserContact> usedContacts = new ArrayList<>();

        for (String number : _usedContacts) {
            for (UserContact userContact : mUserContacts)
                if (userContact.getPhoneNumber().equals(number)) {
                    usedContacts.add(userContact);
                }
        }

        mUserContacts.removeAll(usedContacts);

        Collections.sort(usedContacts, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        return usedContacts;
    }

    private ArrayList<UserContact> deleteAllDuplicateContacts(ArrayList<UserContact> _phoneContactList) {
        Set<UserContact> set = new TreeSet<>((o1, o2) -> {
            if (o1.getPhoneNumber().equalsIgnoreCase(o2.getPhoneNumber())) {
                return 0;
            }
            return 1;
        });
        set.addAll(_phoneContactList);
        return new ArrayList<>(set);
    }

    public List<ContactDH> deleteAllDuplicateContactsDH(List<ContactDH> _phoneContactList) {
        Set<ContactDH> set = new TreeSet<>((o1, o2) -> {
            if (o1.getUserContact().getPhoneNumber().equalsIgnoreCase(o2.getUserContact().getPhoneNumber())) {
                return 0;
            }
            return 1;
        });
        set.addAll(_phoneContactList);
        return new ArrayList<>(set);
    }

}
