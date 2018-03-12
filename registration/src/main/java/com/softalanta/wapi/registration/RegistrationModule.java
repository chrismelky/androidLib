package com.softalanta.wapi.registration;

import android.app.Application;
import android.content.Context;

/**
 * Created by chris on 3/9/18.
 */

public class RegistrationModule extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        RegistrationModule.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return RegistrationModule.context;
    }


}
