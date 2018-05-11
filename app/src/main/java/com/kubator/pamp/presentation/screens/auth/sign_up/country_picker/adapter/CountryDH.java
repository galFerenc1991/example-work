package com.kubator.pamp.presentation.screens.auth.sign_up.country_picker.adapter;

import com.kubator.pamp.data.model.auth.Country;
import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by
 * Ferenc on 2017.11.20..
 */

public class CountryDH implements RecyclerDH {

    public boolean isSelected;
    public Country country;
    public String header;

    public CountryDH(Country _country) {
        this.country = _country;
        this.isSelected = false;
    }

    public CountryDH(Country _country, boolean isSelected) {
        this.country = _country;
        this.isSelected = isSelected;
    }

    public CountryDH(String header) {
        this.header = header;
    }
}
