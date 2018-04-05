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


}
