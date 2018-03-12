package com.softalanta.wapi.registration.view.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.softalanta.wapi.registration.model.Verification;
import com.softalanta.wapi.registration.util.SmsReceiver;

/**
 * Created by chris on 2/7/18.
 */

public class VerificationViewModel extends ViewModel implements Verification.VerificationCallback {

    private MutableLiveData<Verification> verificationMutableLiveData;

    public VerificationViewModel(){

        SmsReceiver.bindListener(new SmsReceiver.SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                /* Process message to get PIN */
                try {
                    //Verification verification = verificationMutableLiveData.getValue();
                   // verification.setVerificationCode(1);
                 //   verification.setStatus(1);
                  //  verificationMutableLiveData.setValue(verification);

                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public LiveData<Verification> getVerification(String phoneNumber, String verificationUrl,String resendSmsUrl){
        if(this.verificationMutableLiveData == null) {
            verificationMutableLiveData = new MutableLiveData<>();
            verificationMutableLiveData.setValue(new Verification(phoneNumber,verificationUrl,resendSmsUrl));
        }
        else {
            verificationMutableLiveData.getValue().setStatus(0);
        }
        return verificationMutableLiveData;
    }

    public void verifyNumber(Verification verification,Context context){
        verification.setStatus(1);
        verificationMutableLiveData.setValue(verification);
        Verification.verify(verification,this,context );
    }

    public void resendSms(Verification verification,Context context){
        Verification.resendSms(verification,this,context);
    }

    @Override
    public void onVerificationResponse(Verification verification) {
        verificationMutableLiveData.setValue(verification);
    }
}
