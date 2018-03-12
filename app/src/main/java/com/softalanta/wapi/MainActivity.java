package com.softalanta.wapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.softalanta.wapi.registration.view.activity.RegistrationActivity;
import com.softalanta.wapi.registration.view.activity.VerificationActivity;
import com.softalanta.wapi.wapilibexample.R;

public class MainActivity extends AppCompatActivity {

    private static int REGISTRATION_ACTIVITY = 100;
    private static int VERIFICATION_ACTIVITY = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent registrationIntent = new Intent(this,RegistrationActivity.class);
        registrationIntent.putExtra("REGISTRATION_POST_URL","http://192.168.43.201:4000/users");
        startActivityForResult(registrationIntent,REGISTRATION_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* handel result from registration activity */
        if(requestCode == REGISTRATION_ACTIVITY){
            if(resultCode == RESULT_OK){
               String phoneNumber = data.getStringExtra("phoneNumber");
                Intent verificationIntent = new Intent(this, VerificationActivity.class);
                verificationIntent.putExtra("VERIFICATION_POST_URL","http://192.168.43.201:4000/users/verify");
                verificationIntent.putExtra("RESEND_SMS_GET_URL","http://192.168.43.201:4000/users/resend-verification-pin");
                verificationIntent.putExtra("PHONE_NUMBER_TO_VERIFY",phoneNumber);
                startActivityForResult(verificationIntent,VERIFICATION_ACTIVITY);
            }
        }
        if(requestCode == VERIFICATION_ACTIVITY){
            if (resultCode == RESULT_OK){
                Toast.makeText(this,data.getStringExtra("userId"),Toast.LENGTH_SHORT).show();
                System.out.println(data.getStringExtra("jwtToken"));
            }
        }


    }
}
