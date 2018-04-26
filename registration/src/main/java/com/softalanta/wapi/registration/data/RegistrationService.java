package com.softalanta.wapi.registration.data;

import com.softalanta.wapi.registration.data.api.RegistrationApi;
import com.softalanta.wapi.registration.data.api.RetrofitBuilder;
import com.softalanta.wapi.registration.model.Registration;
import com.softalanta.wapi.registration.model.Response;
import com.softalanta.wapi.registration.model.Verification;
import com.softalanta.wapi.registration.model.VerificationResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by chris on 3/15/18.
 */

public class RegistrationService {

    private RegistrationApi registrationApi;

    private static RegistrationService mInstance;


    RegistrationService(Retrofit retrofit){
        registrationApi = retrofit.create(RegistrationApi.class);
    }

    public static synchronized RegistrationService getInstance(){
        if(mInstance == null){
            mInstance = new RegistrationService(RetrofitBuilder.getInstance());
        }
        return mInstance;
    }

    public Observable<Response> register(String url, Registration registration){
        return registrationApi.register(url, registration);
    }

    public Observable<Response> resendVerificationCode(String url, Registration registration){
        return registrationApi.resendVerificationCode(url,registration);
    }

    public Observable<VerificationResponse> verify(String url, Verification verification){
        return registrationApi.verify(url,verification);
    }

}
