package com.softalanta.wapi.registration.view.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.softalanta.wapi.registration.data.CountryCodeService;
import com.softalanta.wapi.registration.data.RegistrationService;
import com.softalanta.wapi.registration.model.Country;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by chris on 2/8/18.
 * View Model Class to hold registrationMutableLiveData states data for registrationMutableLiveData Activity to persist across configuration change;
 */

public class CountryCodeViewModel extends ViewModel {

    private Disposable disposable;
    private MutableLiveData<List<Country>> countries = new MutableLiveData<>();

    public LiveData<List<Country>> loadCountries(final Context context){

        final List<Country> countryList = new ArrayList<>();

        CountryCodeService countryCodeService = CountryCodeService.getInstance(context);

        disposable =  countryCodeService.getCountryCodes().
                subscribeOn(Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeWith(new DisposableObserver<Country>() {
                    @Override
                    public void onNext(Country country) {
                        countryList.add(country);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        countries.setValue(countryList);
                    }
                });

          return countries;
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();
    }
}