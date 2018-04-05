package com.softalanta.wapi.registration.data;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.softalanta.wapi.registration.model.Country;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;

/**
 * Created by chris on 4/4/18.
 */

public class CountryCodeService {

    private static CountryCodeService mInstance;
    private static List<Country> countries;

    public CountryCodeService(Context context){
        countries = loadCountryCodes(context);
    }

    public static synchronized CountryCodeService getInstance(Context context){
        if(mInstance == null){
            mInstance = new CountryCodeService(context);
        }
        return mInstance;
    }

    private List<Country> loadCountryCodes(Context context){
        List<Country> _countries = new ArrayList<>();
        if(countries == null) {
            Country cou[];
            InputStream raw = null;
            try {

                raw = context.getAssets().open("country-codes.json");
                Reader reader = new BufferedReader(new InputStreamReader(raw, "UTF8"));
                Gson gson = new Gson();
                cou = gson.fromJson(reader, Country[].class);
                _countries = Arrays.asList(cou);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  _countries;
    }

    public Observable<Country> getCountryCodes(){

        return Observable.create(new ObservableOnSubscribe<Country>() {
            @Override
            public void subscribe(ObservableEmitter<Country> emitter) throws Exception {
                for (Country country:countries){
                    emitter.onNext(country);
                }
                emitter.onComplete();
            }
        });

    }

    public Observable<Country> getDefaultCountry(Context context){

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        final String countryID = manager.getSimCountryIso().toUpperCase();
        Log.d("Country Code", countryID);

        return Observable.create(new ObservableOnSubscribe<Country>() {
            @Override
            public void subscribe(ObservableEmitter<Country> emitter) throws Exception {
                for (Country country:countries){
                    if(country.getCode().equals(countryID)){
                        emitter.onNext(country);
                        break;
                    }
                }
                emitter.onComplete();
            }
        });
    }

}
