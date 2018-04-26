package com.softalanta.wapi.registration.view.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.softalanta.wapi.registration.data.RegistrationService;
import com.softalanta.wapi.registration.model.Registration;
import com.softalanta.wapi.registration.model.Response;
import com.softalanta.wapi.registration.model.Verification;
import com.softalanta.wapi.registration.model.VerificationResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chris on 2/7/18.
 */

public class VerificationViewModel extends ViewModel{

    private RegistrationService registrationService;

    private CompositeDisposable compositeDisposable;

    private MutableLiveData<Response> resendSmsResponse= new MutableLiveData<>();

    private MutableLiveData<VerificationResponse> verificationResponse = new MutableLiveData<>();

    public VerificationViewModel(){
        this.registrationService = RegistrationService.getInstance();
        this.compositeDisposable = new CompositeDisposable();
    }

    public LiveData<Response> resendVerificationSms(String url, Registration registration){

        Disposable disposable= registrationService.resendVerificationCode(url,registration).
                                subscribeOn(Schedulers.io()).
                                observeOn(AndroidSchedulers.mainThread()).
                                subscribeWith(new DisposableObserver<Response>(){
                                    @Override
                                    public void onNext(Response response) {
                                        resendSmsResponse.setValue(response);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        resendSmsResponse.setValue(new Response(e));
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
           compositeDisposable.add(disposable);
       return resendSmsResponse;
    }

    public LiveData<VerificationResponse> verify(String verificationUrl, final Verification verification) {

        Disposable verifyDisposable = registrationService.verify(verificationUrl,verification)
                                                   .subscribeOn(Schedulers.io())
                                                   .observeOn(AndroidSchedulers.mainThread())
                                                   .subscribeWith(new DisposableObserver<VerificationResponse>() {
                                                       @Override
                                                       public void onNext(VerificationResponse response) {
                                                           verificationResponse.setValue(response);
                                                       }

                                                       @Override
                                                       public void onError(Throwable e) {
                                                         verificationResponse.setValue(new VerificationResponse(e));
                                                         e.printStackTrace();
                                                       }

                                                       @Override
                                                       public void onComplete() {

                                                       }
                                                   });
        compositeDisposable.add(verifyDisposable);
        return verificationResponse;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
