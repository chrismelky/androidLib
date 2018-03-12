package com.softalanta.wapi.registration.view.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.softalanta.wapi.registration.model.Country;
import com.softalanta.wapi.registration.model.Registration;

import java.util.List;


/**
 * Created by chris on 2/8/18.
 * View Model Class to hold registrationMutableLiveData states data for registrationMutableLiveData Activity to persist across configuration change;
 */

public class RegistrationViewModel extends ViewModel implements Registration.RegistrationCallback,Country.CountriesCallbacks {

    /**
     * List of countries with caller codes
     */
    private MutableLiveData<List<Country>> countries;

    private MutableLiveData<Registration> registrationMutableLiveData;

    /**
     *
     * @return List of countries with codes
     */
    public  LiveData<List<Country>> getCountries(){
        if(countries ==null){
            countries = new MutableLiveData<>();
            Country.getCountries(this);
        }
        return  countries;
    }

    public void register(Registration registration){
        registrationMutableLiveData.setValue(registration);
        Registration.register(registration,this);
    }

    public LiveData<Registration> getRegistrationMutableLiveData(){
        if(registrationMutableLiveData == null){
            registrationMutableLiveData = new MutableLiveData<>();
            registrationMutableLiveData.setValue(new Registration());
        }

        return registrationMutableLiveData;
    }

    @Override
    public void onRegistrationResponse(Registration registration) {
          registrationMutableLiveData.setValue(registration);
    }

    @Override
    public void onGetCountries(List<Country> _countries) {
       countries.setValue(_countries);
    }


}