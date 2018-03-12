package com.softalanta.wapi.registration.view.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.softalanta.wapi.registration.R;
import com.softalanta.wapi.registration.model.Verification;
import com.softalanta.wapi.registration.view.viewmodel.VerificationViewModel;

public class VerificationActivity extends AppCompatActivity {

    private EditText verificationCode;
    private Button verifyBtn;
    private TextView errorMessage;
    private ProgressDialog nDialog;
    private Button resendSmsBtn;
    private String resendSmsUrl;
    private String verificationUrl;
    private String phoneNumber;
    private Verification verification;

    private VerificationViewModel verificationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_activity);

        /**
         * Hide action bar to display as full screen view
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("PHONE_NUMBER_TO_VERIFY");
        resendSmsUrl = intent.getStringExtra("RESEND_SMS_GET_URL");
        verificationUrl = intent.getStringExtra("VERIFICATION_POST_URL");

        initializeVariables();
        initializeHandlers();

    }

    private void initializeVariables(){

        /* initialize view components */
        verificationCode = findViewById(R.id.verification_code);
        verifyBtn = findViewById(R.id.verify_btn);
        resendSmsBtn = findViewById(R.id.resend_sms);
        errorMessage = findViewById(R.id.error_message);

       /* initialize progress dialogue */
        nDialog = new ProgressDialog(VerificationActivity.this);

        /* Initialize ViewModel to persist activity state across configuration change */
        verificationViewModel = ViewModelProviders.of(this).get(VerificationViewModel.class);
    }


    private void initializeHandlers(){
        Observer<Verification> verificationObserver = new Observer<Verification>() {
            @Override
            public void onChanged(@Nullable Verification _verification) {
                /* set verification state */
                verification = _verification;
                if(verification.getStatus() == 3){
                    nDialog.dismiss();
                    verificationSuccessFul();
                }
                else if(verification.getStatus() == 2){
                    nDialog.dismiss();
                    Toast.makeText(VerificationActivity.this,verification.getMessage(),Toast.LENGTH_SHORT).show();
                }
                else if(verification.getStatus() == 5){
                    nDialog.dismiss();
                    Toast.makeText(VerificationActivity.this,verification.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        };

        verificationViewModel.getVerification(phoneNumber,verificationUrl,resendSmsUrl).observe(this,verificationObserver);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != verificationCode.getText()){
                    System.out.println(Integer.parseInt(verificationCode.getText().toString()));
                    verification.setVerificationCode(Integer.parseInt(verificationCode.getText().toString()));
                    verification.setStatus(1);
                     verify(verification);
                }
                else {
                    errorMessage.setText("Invalid Verification");
                }
            }
        });

        resendSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress("Resend Verification..");
                verification.setStatus(4);
                verificationViewModel.resendSms(verification,getApplicationContext());
            }
        });
    }


    private void showProgress(String message){

        nDialog.setMessage(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(false);
        nDialog.show();
    }

    private void verify(Verification verification)  {
        showProgress("Verifying..");
        verificationViewModel.verifyNumber(verification,getApplicationContext());
    }

    private void verificationSuccessFul(){
        SharedPreferences preferences = this.getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("JWT_TOKEN",verification.getJwtToken());
        editor.putString("USER_ID",verification.getUserId());
        editor.apply();

        String appPackageName =getApplicationContext().getPackageName();
        Intent mainIntent= getApplicationContext().getPackageManager().getLaunchIntentForPackage(appPackageName);
        if(mainIntent != null) {
            startActivity(mainIntent);
            finish();
        }else {
            Toast.makeText(this,"Whoops no application launcher activity found",Toast.LENGTH_SHORT).show();
        }

    }

}
