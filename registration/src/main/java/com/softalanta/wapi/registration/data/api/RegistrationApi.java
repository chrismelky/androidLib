package com.softalanta.wapi.registration.data.api;


import com.softalanta.wapi.registration.model.Registration;
import com.softalanta.wapi.registration.model.Response;
import com.softalanta.wapi.registration.model.Verification;
import com.softalanta.wapi.registration.model.VerificationResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by chris on 3/15/18.
 */

public interface RegistrationApi {

    @POST
    Observable<Response> register(@Url String url,@Body Registration registration);

    @POST
    Observable<Response> resendVerificationCode(@Url String url, @Body Registration registration);

    @POST
    Observable<VerificationResponse> verify(@Url String url, @Body Verification verification);

}
