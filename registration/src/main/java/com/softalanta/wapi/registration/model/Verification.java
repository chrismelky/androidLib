package com.softalanta.wapi.registration.model;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.softalanta.wapi.registration.util.VolleySingletonService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 3/12/18.
 */

public class Verification {


    private String phoneNumber;

    private int requestVerificationCode;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRequestVerificationCode() {
        return requestVerificationCode;
    }

    public void setRequestVerificationCode(int requestVerificationCode) {
        this.requestVerificationCode = requestVerificationCode;
    }
}
