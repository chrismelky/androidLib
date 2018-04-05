package com.softalanta.wapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.softalanta.wapi.registration.view.activity.RegistrationActivity;
import com.softalanta.wapi.wapilibexample.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = this.getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        String phoneNumber = preferences.getString("PHONE_NUMBER",null);
        if(phoneNumber == null) {
            Intent registrationIntent = new Intent(this, RegistrationActivity.class);
            registrationIntent.putExtra("REGISTRATION_URL", "http://192.168.43.201:4000/users");
            registrationIntent.putExtra("SMS_VERIFICATION", true);
            registrationIntent.putExtra("VERIFICATION_POST_URL", "http://192.168.43.201:4000/users/verify");
            registrationIntent.putExtra("RESEND_SMS_GET_URL", "http://192.168.43.201:4000/users/resend-verification-pin");
            startActivity(registrationIntent);
            finish();
        }
    }


}
