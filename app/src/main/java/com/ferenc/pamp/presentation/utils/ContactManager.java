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

    @RootContext
    Context context;

    public ArrayList<UserContact> getContactList() {
        ArrayList<UserContact> _phoneContactList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
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

        return deleteAllDuplicateContacts(_phoneContactList);
    }

    public ArrayList<UserContact> getSortedContactList() {
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

    private ArrayList<UserContact> deleteAllDuplicateContacts(ArrayList<UserContact> _phoneContactList){
        Set<UserContact> set = new TreeSet<>((o1, o2) -> {
            if (o1.getPhoneNumber().equalsIgnoreCase(o2.getPhoneNumber())) {
                return 0;
            }
            return 1;
        });
        set.addAll(_phoneContactList);
        return new ArrayList<>(set);
    }

    public ArrayList<UserContact> sortContactArray(ArrayList<UserContact> _userContacts) {

        Collections.sort(_userContacts, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        return _userContacts;
    }

    public List<ContactDH> getContactsDH() {
        List<ContactDH> contactDHList = new ArrayList<>();
        String header = " ";
        contactDHList.add(new ContactDH("VOS CONTACTS", ContactAdapter.TYPE_CONTACT_HEADER));

        for (UserContact contact : getSortedContactList()) {
            String contactName = contact.getName();
            if (!contactName.startsWith(header)) {
                header = String.valueOf(contactName.charAt(0));
                contactDHList.add(new ContactDH(header, ContactAdapter.TYPE_HEADER));
            }
            contactDHList.add(new ContactDH(contact, ContactAdapter.TYPE_ITEM));
        }
        return contactDHList;
    }

}
