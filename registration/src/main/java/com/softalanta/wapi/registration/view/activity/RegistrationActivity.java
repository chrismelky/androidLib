package com.softalanta.wapi.registration.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.softalanta.wapi.registration.R;
import com.softalanta.wapi.registration.model.Country;
import com.softalanta.wapi.registration.model.Registration;
import com.softalanta.wapi.registration.view.adapters.CountryListAdapter;
import com.softalanta.wapi.registration.view.viewmodel.RegistrationViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User registration activity class/controller
 */
public class RegistrationActivity extends AppCompatActivity {

    private TextView selectCountryTextView;
    private EditText countryCode;
    private ArrayAdapter<Country> countryListAdapter;
    private Country selectedCountry;
    private PhoneNumberUtil phoneUtil;
    private AsYouTypeFormatter formatter;
    private EditText phoneNumberText;
    private Button registerBtn;
    private TextView errorMessage;
    private Boolean firstAttempt =true;
    private List<Country> countries;
    private RegistrationViewModel registrationViewModel;
    private ProgressDialog progressDialog;
    private String registrationUrl;
    private String verificationUrl;
    private String resendSmsUrl;
    private Registration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        Intent intent = getIntent();
        registrationUrl = intent.getStringExtra("REGISTRATION_POST_URL");
        verificationUrl = intent.getStringExtra("VERIFICATION_POST_URL");
        resendSmsUrl = intent.getStringExtra("  RESEND_SMS_POST_URL");
        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initializeVariables();
        initializeHandlers();
    }


    private void initializeVariables(){
        // Phone number util to validate user phone number format based on country */
        phoneUtil = PhoneNumberUtil.getInstance();
        /*Initialize view elements */
        selectCountryTextView = findViewById(R.id.selected_country);
        countryCode = findViewById(R.id.selected_country_code);
        phoneNumberText = findViewById(R.id.phone_number);
        registerBtn = findViewById(R.id.register_btn);
        errorMessage = findViewById(R.id.error_message);
        countries = new ArrayList<>();
        // Initialize view model object for persist registration activity state data against configuration change e.g screen rotation
        registrationViewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);

        if(selectedCountry != null) {
            selectCountryTextView.setText(selectedCountry.getName());
        }

        progressDialog = new ProgressDialog(RegistrationActivity.this);
    }

    private void initializeHandlers(){
        /* Observer to receive List of countries from view model when they become available or list change */
        final Observer<List<Country>> countriesObserver = new Observer<List<Country>>() {
            @Override
            public void onChanged(@Nullable List<Country> _countries) {
                countries = _countries;
            }
        };
        /* Observer handler for registration status messages from registration view model  */
        final Observer<Registration> registrationObserver = new Observer<Registration>() {
            @Override
            public void onChanged(@Nullable Registration _registration) {
                registration = _registration;

                if(registration != null){
                    if(registration.getStatus() == 3) {
                        // Registration successful
                        registrationSuccessFul();
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, registration.getResponseMessage(), Toast.LENGTH_SHORT).show();

                    }
                    else if(registration.getStatus() ==2) {
                        // Registration error
                        Toast.makeText(RegistrationActivity.this, registration.getResponseMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

            }
        };

        /* Get current states and register observer for state changes */
        registrationViewModel.getCountries(getApplicationContext()).observe(this,countriesObserver);
        registrationViewModel.getRegistrationMutableLiveData().observe(this,registrationObserver);

        /* Show country selection popup on */
        selectCountryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCountrySelection();
            }
        });

        /* Register user On registration button clicked */
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstAttempt = false;
                if (null == selectedCountry || phoneNumberText.getText() == null){
                    errorMessage.setText(getText(R.string.invalid_number));
                }
                else{
                    try {

                        Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phoneNumberText.getText(), selectedCountry.getCode());
                        if (!phoneUtil.isValidNumber(phoneNumber)) {
                            errorMessage.setText(getText(R.string.invalid_number));

                        } else {
                            String fullNumber = Integer.toString(phoneNumber.getCountryCode()).concat(Long.toString(phoneNumber.getNationalNumber()));
                            confirmRegisterUser(fullNumber);
                        }
                    } catch (NumberParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void confirmRegisterUser(final String phoneNumber) {
        /* Create progress dialogue to show when registration in progress */
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);

        /* create dialogue for registration confirmation dialogue */
        LayoutInflater layoutInflater= this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.dialog_confirm_registration_number,null);
        builder.setView(dialogView);

        final TextView willVerify = (TextView) dialogView.findViewById(R.id.will_verify);
        final TextView phoneNumberToVerify = (TextView) dialogView.findViewById(R.id.phone_number_to_verify);
        final TextView confirmVerify = (TextView) dialogView.findViewById(R.id.confirm_verify);

        willVerify.setText(R.string.will_verify);
        phoneNumberToVerify.setText("+".concat(phoneNumber));
        confirmVerify.setText(R.string.do_you_what_continue);
        /* register handler to handel confirm registration */
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                registerUser(phoneNumber);
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

    private void registerUser(String phoneNumber) {
        /* if non empty url provided continue with registration otherwise show notification */
        if(registrationUrl != null && !registrationUrl.isEmpty()) {
            registration.setRegistrationUrl(registrationUrl);
            registration.setPhoneNumber(phoneNumber);
            registration.setStatus(1);
            showProgressDialog(getString(R.string.please_wait));
            registrationViewModel.register(registration,getApplicationContext());
        }
        else {
            Toast.makeText(this,getString(R.string.no_post_url),Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgressDialog(String message){
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * Bind country  select
     */
    private void showCountrySelection() {

        final AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle(R.string.select_country);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.select_country_dialog, null);
        builder.setView(dialogView);

        final ListView listView = (ListView) dialogView.findViewById(R.id.selected_country_list_view);
        countryListAdapter = new CountryListAdapter(this, countries);
        listView.setAdapter(countryListAdapter);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountry = (Country) listView.getAdapter().getItem(i);
                registration.setCountry(selectedCountry);
                onSelectCountry();
                dialog.dismiss();
            }
        });
    }


    private void onSelectCountry() {

        selectCountryTextView.setText(registration.getCountry().getName());
        countryCode.setText(Integer.toString(registration.getCountry().getCallingCode()));
        formatter = phoneUtil.getAsYouTypeFormatter(registration.getCountry().getCode());

        phoneNumberText.addTextChangedListener(new PhoneNumberTextWatcher(selectedCountry.getCode()) {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);

                try {
                    Phonenumber.PhoneNumber p = phoneUtil.parse(phoneNumberText.getText(),registration.getCountry().getCode());
                    if (!phoneUtil.isValidNumber(p) && !firstAttempt) {
                        errorMessage.setText(getText(R.string.invalid_number));
                    }else {
                        errorMessage.setText(null);
                    }
                } catch (NumberParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void registrationSuccessFul() {
        Intent result = new Intent();
        result.putExtra("phoneNumber",registration.getPhoneNumber());
        setResult(RESULT_OK, result);
        finish();
    }


    public class PhoneNumberTextWatcher implements TextWatcher {
        /**
         * Indicates the change was caused by ourselves.
         */
        private boolean mSelfChange = false;
        /**
         * Indicates the formatting has been stopped.
         */
        private boolean mStopFormatting;
        private AsYouTypeFormatter mFormatter;

        /**
         * The formatting is based on the current system locale and future locale changes
         * may not take effect on this instance.
         */
        public PhoneNumberTextWatcher() {
            this(Locale.getDefault().getCountry());
        }

        /**
         * The formatting is based on the given <code>countryCode</code>.
         *
         * @param countryCode the ISO 3166-1 two-letter country code that indicates the country/region
         *                    where the phone number is being entered.
         */
        public PhoneNumberTextWatcher(String countryCode) {
            if (countryCode == null) throw new IllegalArgumentException();
            mFormatter = phoneUtil.getAsYouTypeFormatter(countryCode);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            if (mSelfChange || mStopFormatting) {
                return;
            }
            // If the user manually deleted any non-dialable characters, stop formatting
            if (count > 0 && hasSeparator(s, start, count)) {
                stopFormatting();
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mSelfChange || mStopFormatting) {
                return;
            }
            // If the user inserted any non-dialable characters, stop formatting
            if (count > 0 && hasSeparator(s, start, count)) {
                stopFormatting();
            }
        }

        @Override
        public synchronized void afterTextChanged(Editable s) {
            if (mStopFormatting) {
                // Restart the formatting when all texts were clear.
                mStopFormatting = !(s.length() == 0);
                return;
            }
            if (mSelfChange) {
                // Ignore the change caused by s.replace().
                return;
            }
            String formatted = reformat(s, Selection.getSelectionEnd(s));
            if (formatted != null) {
                int rememberedPos = mFormatter.getRememberedPosition();
                mSelfChange = true;
                s.replace(0, s.length(), formatted, 0, formatted.length());
                // The text could be changed by other TextWatcher after we changed it. If we found the
                // text is not the one we were expecting, just give up calling setSelection().
                if (formatted.equals(s.toString())) {
                    Selection.setSelection(s, rememberedPos);
                }
                mSelfChange = false;
            }
        }

        /**
         * Generate the formatted number by ignoring all non-dialable chars and stick the cursor to the
         * nearest dialable char to the left. For instance, if the number is  (650) 123-45678 and '4' is
         * removed then the cursor should be behind '3' instead of '-'.
         */
        private String reformat(CharSequence s, int cursor) {
            // The index of char to the leftward of the cursor.
            int curIndex = cursor - 1;
            String formatted = null;
            mFormatter.clear();
            char lastNonSeparator = 0;
            boolean hasCursor = false;
            int len = s.length();
            for (int i = 0; i < len; i++) {
                char c = s.charAt(i);
                if (PhoneNumberUtils.isNonSeparator(c)) {
                    if (lastNonSeparator != 0) {
                        formatted = getFormattedNumber(lastNonSeparator, hasCursor);
                        hasCursor = false;
                    }
                    lastNonSeparator = c;
                }
                if (i == curIndex) {
                    hasCursor = true;
                }
            }
            if (lastNonSeparator != 0) {
                formatted = getFormattedNumber(lastNonSeparator, hasCursor);
            }
            return formatted;
        }

        private String getFormattedNumber(char lastNonSeparator, boolean hasCursor) {
            return hasCursor ? mFormatter.inputDigitAndRememberPosition(lastNonSeparator)
                    : mFormatter.inputDigit(lastNonSeparator);
        }

        private void stopFormatting() {
            mStopFormatting = true;
            mFormatter.clear();
        }

        private boolean hasSeparator(final CharSequence s, final int start, final int count) {
            for (int i = start; i < start + count; i++) {
                char c = s.charAt(i);
                if (!PhoneNumberUtils.isNonSeparator(c)) {
                    return true;
                }
            }
            return false;
        }

    }
}
