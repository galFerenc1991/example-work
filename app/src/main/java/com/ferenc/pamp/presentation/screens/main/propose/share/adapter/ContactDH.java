package com.ferenc.pamp.presentation.screens.main.propose.share.adapter;

import com.ferenc.pamp.presentation.base.models.UserContact;
import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public class ContactDH implements RecyclerDH {

    private boolean isSelected;
    private UserContact userContact;
    private String header;
    private int mItemType;

    public ContactDH(UserContact _userContact, int _itemType) {
        this.isSelected = false;
        this.userContact = _userContact;
        this.mItemType = _itemType;
    }

    public ContactDH(UserContact _userContact, boolean _isSelected, int _itemType) {
        this.userContact = _userContact;
        this.isSelected = _isSelected;
        this.mItemType = _itemType;
    }

    public ContactDH(String _header, int _itemType) {
        this.header = _header;
        this.mItemType = _itemType;
    }

    public int getItemType() {
        return mItemType;
    }

    public UserContact getUserContact() {
        return userContact;
    }

    public String getHeader() {
        return header;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}