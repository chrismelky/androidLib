package com.softalanta.wapi.registration.model;

import android.content.Context;

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

import java.io.Serializable;


/**
 * Registration model to hold registration state data
 */

public class Registration implements Serializable {

    private int status;

    private String responseMessage;

    private Country country;

    private String phoneNumber;

    private String registrationUrl;

    private int verificationCode;

    private Boolean isVerified;

    public Registration(){

    }

    public Registration(Country country , String phoneNumber, String registrationUrl, int status, String responseMessage){
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.registrationUrl = registrationUrl;
        this.status = status;
        this.responseMessage = responseMessage;
        this.isVerified = false;
    }


    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public String getRegistrationUrl() {
        return registrationUrl;
    }

    public void setRegistrationUrl(String registrationUrl) {
        this.registrationUrl = registrationUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }


    /**
     * Callbacks to be implemented by client of this service to get data back from services
     */
    public interface RegistrationCallback{
        /**
         * @param response
         * Callback when registration complete successful or error
         */
        void onRegistrationResponse(Registration response);

    }

    public static void register(final Registration registration , final RegistrationCallback client, final Context context) {

        JSONObject object = new JSONObject();
        try {

            object.put("mobileNumber",registration.getPhoneNumber());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, registration.getRegistrationUrl(), object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /* Return response code 3= successful and phone number as message */
                registration.setStatus(3);
                try {
                    registration.setResponseMessage(response.getString("successMessage"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                client.onRegistrationResponse(registration);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        errorMessage = response.getString("message");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                registration.setStatus(2);
                registration.setResponseMessage(errorMessage);
                client.onRegistrationResponse(registration);
            }
        });

        VolleySingletonService.getInstance(context).addToRequestToQueue(request);

    }

}
