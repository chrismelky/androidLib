package com.softalanta.wapi.registration.model;

import java.io.Serializable;

/**
 * Created by chris on 2/13/18.
 */

public class Country implements Serializable {

    private String name;
    private String code;
    private int callingCode;
    private int icon;

    public Country(String name, String code, int callingCode){
        this.name = name;
        this.code = code;
        this.callingCode =callingCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(int callingCode) {
        this.callingCode = callingCode;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}
