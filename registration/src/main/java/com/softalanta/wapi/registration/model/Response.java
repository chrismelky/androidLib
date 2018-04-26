package com.softalanta.wapi.registration.model;

import java.net.SocketTimeoutException;

import retrofit2.HttpException;

/**
 * Created by chris on 4/4/18.
 */

public class Response {

    private int statusCode;

    private String error;

    private String message;

    private String successMessage;

    public Response(Throwable e){
        String errorMessage= "Unknown Error";
        if(e instanceof SocketTimeoutException){
            errorMessage = "Could not connect to server";
        }
        else if(e instanceof HttpException){
           errorMessage = e.getMessage();
        }
        this.statusCode = 500;
        this.error = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
