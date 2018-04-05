package com.softalanta.wapi.registration.view.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.softalanta.wapi.registration.data.CountryCodeService;
import com.softalanta.wapi.registration.data.RegistrationService;
import com.softalanta.wapi.registration.model.Country;
import com.softalanta.wapi.registration.model.Registration;
import com.softalanta.wapi.registration.model.Response;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chris on 2/8/18.
 * View Model Class to hold registrationMutableLiveData states data for registrationMutableLiveData Activity to persist across configuration change;
 */

public class RegistrationViewModel extends ViewModel {

    private RegistrationService registrationService;

    private Disposable disposable;

    private MutableLiveData<Country> defaultCountry = new MutableLiveData<>();

    public RegistrationViewModel(){
        this.registrationService = RegistrationService.getInstance();
    }

    public LiveData<Country> getDefaultCountry(Context context){

        CountryCodeService countryCodeService = CountryCodeService.getInstance(context);
        disposable = countryCodeService.getDefaultCountry(context).
                    subscribeOn(Schedulers.computation()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribeWith(new DisposableObserver<Country>() {
                        @Override
                        public void onNext(Country country) {
                           defaultCountry.setValue(country);
                        }

                        @Override
                        public void onError(Throwable e) {
                          e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        return defaultCountry;
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();
    }

    public void register(String url,Registration registration) {
         Disposable reg = registrationService.register(url,registration).
                         subscribeOn(Schedulers.io()).
                         observeOn(AndroidSchedulers.mainThread()).
                         subscribeWith(new DisposableObserver<Response>(){

                             @Override
                             public void onNext(Response response) {
                                 System.out.println(response.getSuccessMessage());
                             }

                             @Override
                             public void onError(Throwable e) {
                                e.printStackTrace();
                             }

                             @Override
                             public void onComplete() {

                             }
                         });
    }
}