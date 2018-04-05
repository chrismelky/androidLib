package com.softalanta.wapi.registration.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.softalanta.wapi.registration.R;
import com.softalanta.wapi.registration.R2;
import com.softalanta.wapi.registration.model.Country;
import com.softalanta.wapi.registration.model.Registration;
import com.softalanta.wapi.registration.util.PhoneNumberTextWatcher;
import com.softalanta.wapi.registration.view.viewmodel.RegistrationViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * User registration activity class/controller
 */
public class RegistrationActivity extends AppCompatActivity {


    @BindView(R2.id.selected_country_name) TextView selectCountryName;

    @BindView(R2.id.selected_country_calling_code) EditText selectedCountryCallingCode;

    @BindView(R2.id.phone_number) EditText phoneNumber;

    @BindView(R2.id.register_btn) Button registerBtn;

    @BindView(R2.id.error_message) TextView errorMessage;

    private Country selectedCountry;

    private RegistrationViewModel registrationViewModel;

    private String registrationUrl;

    private String verificationUrl;

    private Observer<Country> defaultCountryObserver;

    private PhoneNumberUtil phoneUtil;

    private Boolean firstAttempt = true;

    private List<PhoneNumberTextWatcher> addedTextWatchers = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        registrationUrl = intent.getStringExtra("REGISTRATION_URL");
        verificationUrl = intent.getStringExtra("VERIFICATION_URL");

        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setObservers();
        registrationViewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);
        registrationViewModel.getDefaultCountry(this).observe(this,defaultCountryObserver);

        // Phone number util to validate user phone number format based on country */
        phoneUtil = PhoneNumberUtil.getInstance();

    }

    private void setObservers(){
        defaultCountryObserver = new Observer<Country>() {
            @Override
            public void onChanged(@Nullable Country country) {
                if(country != null){
                    selectedCountry = country;
                    selectedCountryChanged();
                }
            }
        };
    }

    private void selectedCountryChanged(){
        if(selectedCountry != null) {
            selectCountryName.setText(selectedCountry.getName());
            selectedCountryCallingCode.setText(Integer.toString(selectedCountry.getCallingCode()));
            clearPhoneNumberTextWatcher();
            PhoneNumberTextWatcher textWatcher = new PhoneNumberTextWatcher(selectedCountry.getCode()) {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    try {
                        if(phoneNumber.getText().length() >1) {
                            Phonenumber.PhoneNumber p = phoneUtil.parse(phoneNumber.getText(), selectedCountry.getCode());
                            if (!phoneUtil.isValidNumber(p) && !firstAttempt) {
                                errorMessage.setText(getText(R.string.invalid_number));
                            } else {
                                errorMessage.setText(null);
                            }
                        }
                    } catch (NumberParseException e) {
                        e.printStackTrace();
                    }
                }
            };

            phoneNumber.addTextChangedListener(textWatcher);
            addedTextWatchers.add(textWatcher);
        }
    }
    private void clearPhoneNumberTextWatcher(){
        for (PhoneNumberTextWatcher textWatcher:addedTextWatchers){
            phoneNumber.removeTextChangedListener(textWatcher);
        }
    }

    @OnClick(R2.id.selected_country_name)
    public void onSelectedCountryClick(){
       Intent countryCodeActivity= new Intent(this,CountryCodeActivity.class);
       startActivityForResult(countryCodeActivity,1);
    }

    public void confirmRegistration(final String fullNumber){

        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setMessage("Are you sure you want to register ");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                registrationViewModel.register(registrationUrl,new Registration(fullNumber));
            }
        });
        /* register handler to cancel confirm dialogue */
        builder.setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R2.id.register_btn)
    public void register() {
        firstAttempt = false;
        if (null == selectedCountry || phoneNumber.getText() == null){
            errorMessage.setText(getText(R.string.invalid_number));
        }
        else{
            if (!phoneNumber.getText().toString().isEmpty() && phoneNumber.getText().length() > 1) {
                    try {
                    Phonenumber.PhoneNumber _phoneNumber = phoneUtil.parse(phoneNumber.getText(), selectedCountry.getCode());
                    if (!phoneUtil.isValidNumber(_phoneNumber)) {
                        errorMessage.setText(getText(R.string.invalid_number));

                    } else {
                        String fullNumber = Integer.toString(_phoneNumber.getCountryCode()).concat(Long.toString(_phoneNumber.getNationalNumber()));
                        confirmRegistration(fullNumber);
                    }
                } catch(NumberParseException e){
                    e.printStackTrace();
                }
            }
            else {
                errorMessage.setText(getText(R.string.invalid_number));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                selectedCountry = (Country) data.getSerializableExtra("SELECTED_COUNTRY");
                selectedCountryChanged();
            }
        }
    }
}
