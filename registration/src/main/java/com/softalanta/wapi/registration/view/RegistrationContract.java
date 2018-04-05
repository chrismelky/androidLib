package com.softalanta.wapi.registration.view;

import com.softalanta.wapi.registration.model.Country;
import com.softalanta.wapi.registration.model.Registration;

import java.util.List;

/**
 * Created by chris on 3/17/18.
 */

public class RegistrationContract {

    public interface View {

        void  showCountryList(List<Country> countryList);

        void onCountrySelected(Country country);

        void showErrorMessage();

        void onRegisterBtnClick(Registration registration);

        void showProgressDialogue();

        void hideProgressDialogue();

    }

    public interface Presenter{
        void registerUser();

        void  showCountryList(List<Country> countryList);

        void setSelectedCountry(Country country);

        void showProgressDialogue();

        void hideProgressDialogue();


        void loadCountries();
    }

}
