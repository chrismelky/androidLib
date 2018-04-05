package com.softalanta.wapi.registration.data.api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chris on 4/4/18.
 */

public class RetrofitBuilder {

    private static Retrofit mInstance;

    public static synchronized Retrofit getInstance(){
        if(mInstance == null){
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Log.d("Request with", chain.request().url().toString());
                    return chain.proceed(chain.request());
                }
            }).build();

            mInstance =  new Retrofit.Builder()
                    .baseUrl("http://192.168.43.201:4000/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return mInstance;
    }
}
