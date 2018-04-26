package com.softalanta.wapi.registration.view.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.softalanta.wapi.registration.R;
import com.softalanta.wapi.registration.R2;
import com.softalanta.wapi.registration.model.Registration;
import com.softalanta.wapi.registration.model.Response;
import com.softalanta.wapi.registration.model.Verification;
import com.softalanta.wapi.registration.model.VerificationResponse;
import com.softalanta.wapi.registration.view.dialog.Progress;
import com.softalanta.wapi.registration.view.viewmodel.VerificationViewModel;

import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationActivity extends AppCompatActivity {

    @BindView(R2.id.verification_code) EditText verificationCode;

    @BindView(R2.id.verify_btn) Button verifyBtn;

    @BindView(R2.id.error_message) TextView errorMessage;

    @BindView(R2.id.resend_sms) Button resendSmsBtn;

    private ProgressDialog nDialog;

    private String resendSmsUrl;

    private String verificationUrl;

    private String phoneNumber;

    private Verification verification;

    private Progress progress;

    private Observer<Response> resendSmsResponseObserver;

    private Observer<VerificationResponse> verificationResponseObserver;

    private VerificationViewModel verificationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_activity);
        ButterKnife.bind(this);

        /**
         * Hide action bar to display as full screen view
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("PHONE_NUMBER");
        resendSmsUrl = intent.getStringExtra("RESEND_SMS_URL");
        verificationUrl = intent.getStringExtra("VERIFICATION_URL");

        initializeVariables();
        setObservers();

    }

    private void initializeVariables(){

       /* initialize progress dialogue */
        nDialog = new ProgressDialog(VerificationActivity.this);
        /* Initialize ViewModel to persist activity state across configuration change */
        verificationViewModel = ViewModelProviders.of(this).get(VerificationViewModel.class);
    }

    private void setObservers(){
        resendSmsResponseObserver = new Observer<Response>() {
            @Override
            public void onChanged(@Nullable Response response) {
                onResendSmsResponse(response);
            }
        };

        verificationResponseObserver = new Observer<VerificationResponse>() {
            @Override
            public void onChanged(@Nullable VerificationResponse response) {
                onVerificationResponse(response);
            }
        };
    }

    private void showProgress(){
        progress = new Progress(this);
        progress.defaultProgress(getString(R.string.loading));
    }

    @OnClick(R2.id.verify_btn)
    public  void verify(){
        showProgress();
        Integer requestVerificationCode = Integer.valueOf(verificationCode.getText().toString());
        verificationViewModel.verify(verificationUrl,new Verification(phoneNumber,requestVerificationCode))
                              .observe(this,verificationResponseObserver);
    }

    private void onVerificationResponse(VerificationResponse response) {
        progress.dismiss();
        if(response.getError()  != null){
            Toast.makeText(this,response.getError(),Toast.LENGTH_SHORT).show();
        }else {
            startMainActivity(response.getToken(), response.getUserId());
            Log.i("Token",response.getToken());
        }
    }

    private void startMainActivity(String token, String userId){

        SharedPreferences preferences = this.getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TOKEN",token);
        editor.putString("USER_ID",userId);
        editor.putString("PHONE_NUMBER",phoneNumber);
        editor.apply();

        PackageManager pm = getPackageManager();
        try {
            Intent intent=pm.getLaunchIntentForPackage(getApplicationContext().getPackageName());
            assert intent != null;
            startActivity(intent);
            finish();
        }
        catch (NullPointerException e){
            Toast.makeText(this,"No launch activity found",Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R2.id.resend_sms)
    public void resendVerificationSms(){
        showProgress();
        verificationViewModel.resendVerificationSms(resendSmsUrl,new Registration(phoneNumber)).observe(this,resendSmsResponseObserver);
    }

    private void onResendSmsResponse(Response response) {
        progress.dismiss();
        String message;
        if(response.getError() != null){
            message =response.getError();
        }
        else {
            message = response.getSuccessMessage();
        }
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onDestroy() {
        nDialog = null;
        progress = null;
        resendSmsResponseObserver = null;
        verificationResponseObserver = null;
        verificationViewModel = null;
        super.onDestroy();
    }
}
