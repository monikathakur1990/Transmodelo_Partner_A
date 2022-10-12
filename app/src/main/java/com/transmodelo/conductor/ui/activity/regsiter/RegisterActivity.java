package com.transmodelo.conductor.ui.activity.regsiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.google.firebase.iid.FirebaseInstanceId;
import com.transmodelo.conductor.BuildConfig;
import com.transmodelo.conductor.R;
import com.transmodelo.conductor.base.BaseActivity;
import com.transmodelo.conductor.common.Constants;
import com.transmodelo.conductor.common.SharedHelper;
import com.transmodelo.conductor.common.Utilities;
import com.transmodelo.conductor.data.network.model.ServiceType;
import com.transmodelo.conductor.data.network.model.SettingsResponse;
import com.transmodelo.conductor.data.network.model.User;
import com.transmodelo.conductor.ui.countrypicker.Country;
import com.transmodelo.conductor.ui.countrypicker.CountryPicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class RegisterActivity extends BaseActivity implements RegisterIView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;
    @BindView(R.id.txtLastName)
    EditText txtLastName;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;
    @BindView(R.id.chkTerms)
    CheckBox chkTerms;
    @BindView(R.id.spinnerServiceType)
    MultiSpinnerSearch spinnerServiceType;
    @BindView(R.id.txtVehicleModel)
    EditText txtVehicleModel;
    @BindView(R.id.txtVehicleNumber)
    EditText txtVehicleNumber;
    @BindView(R.id.lnrReferralCode)
    LinearLayout lnrReferralCode;
    @BindView(R.id.txtReferalCode)
    EditText txtReferalCode;
    @BindView(R.id.countryImage)
    ImageView countryImage;
    @BindView(R.id.countryNumber)
    TextView countryNumber;
    @BindView(R.id.phoneNumber)
    EditText phoneNumber;

    private String countryDialCode = "+593";
    private String countryCode = "EC";
    private CountryPicker mCountryPicker;

    private RegisterPresenter presenter;
    private int selected_pos = -1;
    private ArrayList<Integer> selected_pos_array = new ArrayList<>();
    private List<ServiceType> lstServiceTypes = new ArrayList<>();

    private boolean isEmailAvailable = true;
    private boolean isPhoneNumberAvailable = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter = new RegisterPresenter();
        presenter.attachView(this);
        spinnerServiceType.setSearchEnabled(false);

        // If you will set the limit, this button will not display automatically.
        spinnerServiceType.setShowSelectAllButton(false);
        spinnerServiceType.setLimit(1, new MultiSpinnerSearch.LimitExceedListener() {
            @Override
            public void onLimitListener(KeyPairBoolData data) {
                Toasty.error(getApplicationContext(), R.string.limit_exceded, Toast.LENGTH_SHORT, true).show();
            }
        });

        //A text that will display in clear text button
        spinnerServiceType.setClearText("Cerrar");

        setupSpinner(null);
        presenter.getSettings();
        // Pass true If you want searchView above the list. Otherwise false. default = true.

        spinnerServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clickFunctions();

        setCountryList();

       /* if (BuildConfig.DEBUG) {
            txtEmail.setText("stevejobs@yopmail.com");
            txtFirstName.setText("steve");
            txtLastName.setText("jobs");
            txtVehicleModel.setText("DUKE");
            txtVehicleNumber.setText("PY 01 AA 1111");
            phoneNumber.setText("9003440134");
            txtPassword.setText("112233");
            txtConfirmPassword.setText("112233");
        }*/
        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        if (intent.hasExtra("Register Data")) {


            HashMap<String, Object> map1 = new HashMap<>();

            map1 = (HashMap<String, Object>) bundle.getSerializable("Register Data");
            txtFirstName.setText(map1.get("first_name").toString());
            txtLastName.setText(map1.get("last_name").toString());
            txtEmail.setText(map1.get("email").toString());
            presenter.verifyEmail(txtEmail.getText().toString().trim());
        }
        if (SharedHelper.getKey(this, Constants.SharedPref.DEVICE_TOKEN).isEmpty()) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.w("PasswordActivity", "getInstanceId failed", task.getException());
                    return;
                }
                Log.d("FCM_TOKEN", task.getResult().getToken());

                SharedHelper.putKey(RegisterActivity.this, Constants.SharedPref.DEVICE_TOKEN, task.getResult().getToken());
            });
        }

        @SuppressLint("HardwareIds")
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("DEVICE_ID: ", deviceId);
        SharedHelper.putKeyFCM(RegisterActivity.this, Constants.SharedPref.DEVICE_ID, deviceId);
    }

    private void setCountryList() {
        mCountryPicker = CountryPicker.newInstance("Select Country");
        List<Country> countryList = Country.getAllCountries();
        Collections.sort(countryList, (s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        mCountryPicker.setCountriesList(countryList);

        setListener();
    }

    private void setListener() {
        mCountryPicker.setListener((name, code, dialCode, flagDrawableResID) -> {
            countryNumber.setText(dialCode);
            countryDialCode = dialCode;
            countryImage.setImageResource(flagDrawableResID);
            mCountryPicker.dismiss();
        });

        countryImage.setOnClickListener(v -> mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER"));

        countryNumber.setOnClickListener(v -> mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER"));

        getUserCountryInfo();
    }

    private void getUserCountryInfo() {
        Country country = getDeviceCountry(RegisterActivity.this);
        countryImage.setImageResource(R.drawable.flag_bo);
        countryNumber.setText("+591");
        countryDialCode = "+591";//country.getDialCode();
        countryCode = "BO";//country.getCode();
    }

    private boolean validation() {
        if (txtEmail.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtFirstName.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_first_name), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtLastName.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_last_name), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (selected_pos_array.size() <= 0) {
            Toasty.error(this, getString(R.string.invalid_service_type), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtVehicleModel.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_car_model), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtVehicleNumber.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_car_number), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (phoneNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_phone_number), Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtPassword.getText().toString().length() < 6) {
            Toasty.error(this, getString(R.string.invalid_password_length), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtConfirmPassword.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_confirm_password), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
            Toasty.error(this, getString(R.string.password_should_be_same), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (!chkTerms.isChecked()) {
            Toasty.error(this, getString(R.string.please_accept_terms_conditions), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (isEmailAvailable) {
            showErrorMessage(txtEmail, getString(R.string.email_already_exist));
            return false;
        } else if (isPhoneNumberAvailable) {
            showErrorMessage(phoneNumber, getString(R.string.phone_number_already_exists));
            return false;
        } else return true;
    }

    private void showErrorMessage(EditText view, String message) {
        Toasty.error(this, message, Toast.LENGTH_SHORT).show();
        view.requestFocus();
        view.setText(null);
    }

    private void register(String countryCode, String phoneNumber) {
        Intent intent = new Intent(this, VerifyPhoneActivity.class);
        intent.putExtra("first_name", txtFirstName.getText().toString());
        intent.putExtra("last_name", txtLastName.getText().toString());
        intent.putExtra("email", txtEmail.getText().toString());
        intent.putExtra("mobile", phoneNumber);
        intent.putExtra("country_code", countryCode);
        intent.putExtra("password", txtPassword.getText().toString());
        intent.putExtra("password_confirmation", txtConfirmPassword.getText().toString());
        intent.putExtra("device_token", SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN));
        intent.putExtra("device_id", SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID));
        intent.putExtra("service_type", selected_pos_array.toString());//lstServiceTypes.get(selected_pos).getId() + "");
        intent.putExtra("service_model", txtVehicleModel.getText().toString());
        intent.putExtra("service_number", txtVehicleNumber.getText().toString());
        intent.putExtra("device_type", BuildConfig.DEVICE_TYPE);
        intent.putExtra("referral_unique_id", txtReferalCode.getText().toString());
//        intent.putExtra("mobile", phoneNumber);
        intent.putExtra("ISOCode", countryCode);
        startActivity(intent);
    }

    @OnClick({R.id.next, R.id.back, R.id.lblTerms})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.next:
                Log.d("TAG", "Next Button Clicked");
                if (validation()) if (Utilities.isConnected())
                    fbPhoneLogin(countryCode, countryDialCode, phoneNumber.getText().toString());
                else showAToast(getString(R.string.no_internet_connection));
                break;
            case R.id.lblTerms:
                showTermsConditionsDialog();
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void clickFunctions() {
        txtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            isEmailAvailable = true;
            if (!hasFocus && !TextUtils.isEmpty(txtEmail.getText().toString()))
                if (Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches())
                    presenter.verifyEmail(txtEmail.getText().toString().trim());
        });

        phoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            isPhoneNumberAvailable = true;
            if (!hasFocus && !TextUtils.isEmpty(phoneNumber.getText().toString()))
                presenter.verifyCredentials(countryDialCode, phoneNumber.getText().toString());
        });
    }

    private void showTermsConditionsDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.terms_and_conditions));
        WebView wv = new WebView(this);
        wv.loadUrl(BuildConfig.TERMS_CONDITIONS);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton("Close", (dialog, id) -> dialog.dismiss());
        alert.show();
    }


    @Override
    public void onSuccess(Object verifyEmail) {
        Log.e("onSuccess: ", verifyEmail.toString());
        hideLoading();
        isEmailAvailable = false;
    }

    @Override
    public void onVerifyEmailError(Throwable e) {
        isEmailAvailable = true;
        Log.e("onVerifyEmailError: ", e.getMessage());
        showErrorMessage(txtEmail, getString(R.string.email_already_exist));
    }

    @Override
    public void onSuccess(SettingsResponse response) {
        lstServiceTypes = response.getServiceTypes();
        lnrReferralCode.setVisibility(response.getReferral().getReferral().equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);
        setupSpinner(response);
    }

    private void setupSpinner(@Nullable SettingsResponse response) {
        ArrayList<String> lstNames = new ArrayList<>(response != null ? response.getServiceTypes().size() : 0);
        if (response != null) for (ServiceType serviceType : response.getServiceTypes())
            lstNames.add(serviceType.getName());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lstNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServiceType.setAdapter(dataAdapter);
        final List<KeyPairBoolData> listArray0 = new ArrayList<>();
        for (int i = 0; i < lstNames.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i);
            h.setName(lstNames.get(i));
            h.setSelected(false);
            listArray0.add(h);
        }
        spinnerServiceType.setItems(listArray0, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                selected_pos_array.clear();
                for (int i = 0; i < listArray0.size(); i++) {
                    if (listArray0.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + listArray0.get(i).getName() + " : " + listArray0.get(i).isSelected());
                        System.out.println(i);
                        System.out.println(lstServiceTypes.get(i).getId());
                        System.out.println(lstServiceTypes.get(i).getName());
                        selected_pos_array.add(lstServiceTypes.get(i).getId());
                    }
                }
            }
        });
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    public void onSuccess(User user) {

    }

    public void fbPhoneLogin(String strCountryCode, String strCountryISOCode, String strPhoneNumber) {
        /*final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder mBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN);
        mBuilder.setReadPhoneStateEnabled(true);
        mBuilder.setReceiveSMS(true);
        PhoneNumber phoneNumber = new PhoneNumber(strCountryISOCode, strPhoneNumber, strCountryCode);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                mBuilder.setInitialPhoneNumber(phoneNumber).
                        build());
        startActivityForResult(intent, APP_REQUEST_CODE);*/
        register(strCountryISOCode, strPhoneNumber);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.APP_REQUEST_CODE && data != null) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (!loginResult.wasCancelled()) {
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        Log.d("AccountKit", "onSuccess: Account Kit" + AccountKit.getCurrentAccessToken().getToken());
                        if (AccountKit.getCurrentAccessToken().getToken() != null) {
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            SharedHelper.putKey(RegisterActivity.this, Constants.SharedPref.DIAL_CODE,
                                    "+" + phoneNumber.getCountryCode());
                            SharedHelper.putKey(RegisterActivity.this, Constants.SharedPref.MOBILE,
                                    phoneNumber.getPhoneNumber());
                            register(SharedHelper.getKey(RegisterActivity.this, Constants.SharedPref.DIAL_CODE),
                                    SharedHelper.getKey(RegisterActivity.this, Constants.SharedPref.MOBILE));
                        }
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Log.e("AccountKit", "onError: Account Kit" + accountKitError);
                    }
                });
            }
        }
    }

    @Override
    public void onSuccessPhoneNumber(Object object) {

        isPhoneNumberAvailable = false;
        Log.e("onSuccessPhoneNumber: ", object.toString());
    }

    @Override
    public void onVerifyPhoneNumberError(Throwable e) {
        Log.e("onVerifyPhonrror: ", e.getMessage());
        isPhoneNumberAvailable = true;
        showErrorMessage(phoneNumber, getString(R.string.mobile_number_already_exist));
    }
}
