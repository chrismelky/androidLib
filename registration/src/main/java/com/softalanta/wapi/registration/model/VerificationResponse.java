package com.softalanta.wapi.registration.model;

/**
 * Created by chris on 4/4/18.
 */

public class VerificationResponse extends Response {

    private String token;

    private String userId;

    public VerificationResponse(Throwable e) {
        super(e);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
