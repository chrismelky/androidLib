package com.softalanta.wapi.registration.view;

import com.softalanta.wapi.registration.model.Country;
import com.softalanta.wapi.registration.model.Registration;

import java.util.List;

/**
 * Created by chris on 3/16/18.
 */

public interface RegistrationView {

    void  showCountryList(List<Country> countryList);

    void setSelectedCountry(Country country);

    void showErrorMessage();

    void registerUser(Registration registration);

    void showProgressDialogue();

    void hideProgressDialogue();

}
