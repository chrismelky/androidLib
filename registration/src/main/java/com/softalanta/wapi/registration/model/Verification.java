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

    private int status;

    private String phoneNumber;

    private int verificationCode;

    private boolean isVerified;

    private String jwtToken;

    private String verificationUrl;

    private String resendSmsUrl;

    private String userId;

    private String message;

    public Verification() {
    }

    public Verification(String phoneNumber,String verificationUrl, String resendSmsUrl){

         this.phoneNumber = phoneNumber;
         this.verificationUrl = verificationUrl;
         this.resendSmsUrl = resendSmsUrl;
         this.isVerified = false;
     }

    public String getResendSmsUrl() {
        return resendSmsUrl;
    }

    public void setResendSmsUrl(String resendSmsUrl) {
        this.resendSmsUrl = resendSmsUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerificationUrl() {
        return verificationUrl;
    }

    public void setVerificationUrl(String verificationUrl) {
        this.verificationUrl = verificationUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    /**
     * Callbacks to be implemented by client of this service to get data back from services
     */
    public interface VerificationCallback {
        /**
         * @param verification Callback when verification complete successful or error
         */
        void onVerificationResponse(Verification verification);
    }

    public static void verify(final Verification verification, final VerificationCallback client, final Context context) {

        JSONObject object = new JSONObject();
        try {
            object.put("mobileNumber",verification.getPhoneNumber());
            object.put("verificationCode",verification.getVerificationCode());
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, verification.verificationUrl,object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    /* JWT Token for bearer login */
                    String token = response.getString("jwtToken");
                    /* User Id to set chat topic */
                    String userId = response.getString("userId");

                    verification.setStatus(3);
                    verification.setJwtToken(token);
                    verification.setUserId(userId);
                    client.onVerificationResponse(verification);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                error.printStackTrace();
                verification.setStatus(2);
                verification.setMessage(errorMessage);
                client.onVerificationResponse(verification);

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return  headers;
            }


        };
        VolleySingletonService.getInstance(context).addToRequestToQueue(request);
    }

    public static void resendSms(final Verification verification, final VerificationCallback client,Context context) {

        JSONObject object = new JSONObject();
        try {
            object.put("mobileNumber",verification.getPhoneNumber());
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, verification.getResendSmsUrl(),object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    verification.setMessage(response.getString("successMessage"));
                    verification.setStatus(5);
                    client.onVerificationResponse(verification);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                error.printStackTrace();
                verification.setStatus(2);
                verification.setMessage(errorMessage);
                client.onVerificationResponse(verification);

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return  headers;
            }
        };
        VolleySingletonService.getInstance(context).addToRequestToQueue(request);

    }


}
