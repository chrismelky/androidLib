package com.softalanta.wapi.registration.model;

import java.io.Serializable;


/**
 * Registration model to hold registration state data
 */

public class Registration implements Serializable {

    private String mobileNumber;

    public Registration(String mobileNumber){
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
