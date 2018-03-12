package com.softalanta.wapi.registration.model;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chris on 2/13/18.
 */

public class Country {
    private String name;
    private String code;
    private int callingCode;
    private int icon;
    private static CountriesCallbacks mClient;

    public Country(String name, String code, int callingCode){
        this.name = name;
        this.code = code;
        this.callingCode =callingCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(int callingCode) {
        this.callingCode = callingCode;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


    /**
     * Callbacks to be implemented by client of this service to get data back from services
     */
    public interface CountriesCallbacks{
        /**
         *
         * @param countries
         * Callback when countries successful fetched from list
         */
        void onGetCountries(List<Country> countries);

    }

    public static void getCountries(CountriesCallbacks _mClient, Context context){
        mClient = _mClient;
        new GetCountriesAsync().execute(context);
    }

    /**
     * Fetch country list on background thread
     */
    public static class GetCountriesAsync extends AsyncTask<Context ,Integer ,List<Country>> {

        @Override
        protected List<Country> doInBackground(Context... contexts) {

            Country cou[];
            List<Country> countries =new ArrayList<>();
            InputStream raw = null;
            try {

                raw = contexts[0].getAssets().open("country-codes.json");
                Reader is = new BufferedReader(new InputStreamReader(raw, "UTF8"));
                Gson gson = new Gson();
                cou =gson.fromJson(is,Country[].class);
                countries = Arrays.asList(cou);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return countries;
        }

        @Override
        protected void onPostExecute(List<Country> _countries) {
            super.onPostExecute(_countries);
            mClient.onGetCountries(_countries);

        }
    }
}
