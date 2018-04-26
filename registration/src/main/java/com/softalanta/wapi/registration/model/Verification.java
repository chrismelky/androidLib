package com.softalanta.wapi.registration.model;


/**
 * Created by chris on 3/12/18.
 */

public class Verification {

    private String mobileNumber;

    private Integer requestVerificationCode;

    public Verification(String phoneNumber,Integer requestVerificationCode){
        this.mobileNumber = phoneNumber;
        this.requestVerificationCode = requestVerificationCode;
    }

    public String getPhoneNumber() {
        return mobileNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mobileNumber = phoneNumber;
    }

    public int getRequestVerificationCode() {
        return requestVerificationCode;
    }

    public void setRequestVerificationCode(int requestVerificationCode) {
        this.requestVerificationCode = requestVerificationCode;
    }
}
