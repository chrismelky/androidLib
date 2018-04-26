package com.softalanta.wapi.registration.data.api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chris on 4/4/18.
 * This class provide Retrofit instance using singleton pattern.
 * The Retrofit instance is build with okHttpClient to provide http interceptor
 * The Retrofit instance is build to use Gson converter to map JSON response to java object
 * The Retrofit instance is build to use Rx Java call adaptor convert all response to Observable.
 *
 */

public class RetrofitBuilder {

    private static Retrofit mInstance;

    public static synchronized Retrofit getInstance(){
        if(mInstance == null){
            //Create http client with interceptor
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new MInterceptor()).build();
            mInstance =  new Retrofit.Builder()
                    .baseUrl("http://domain.com")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return mInstance;
    }

    /**
     * Http request Interceptor to provide network call logging
     */
    private static class MInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originRequest =chain.request();
            Log.d("Http Request:", originRequest.method().concat(":").concat(originRequest.url().toString()));
            return chain.proceed(chain.request());
        }
    }
}
