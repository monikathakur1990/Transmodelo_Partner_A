package com.transmodelo.conductor.ui.activity.profile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.transmodelo.conductor.BuildConfig;
import com.transmodelo.conductor.MvpApplication;
import com.transmodelo.conductor.R;
import com.transmodelo.conductor.base.BaseActivity;
import com.transmodelo.conductor.common.Constants;
import com.transmodelo.conductor.common.SharedHelper;
import com.transmodelo.conductor.common.Utilities;
import com.transmodelo.conductor.data.network.model.Service;
import com.transmodelo.conductor.data.network.model.ServiceType;
import com.transmodelo.conductor.data.network.model.SettingsResponse;
import com.transmodelo.conductor.data.network.model.UserResponse;
import com.transmodelo.conductor.ui.activity.change_password.ChangePasswordActivtiy;
import com.transmodelo.conductor.ui.activity.main.MainActivity;
import com.transmodelo.conductor.ui.countrypicker.Country;
import com.transmodelo.conductor.ui.countrypicker.CountryPicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ProfileActivity extends BaseActivity implements ProfileIView {


    String TAG = "ProfileActivity";

    private ProfilePresenter presenter = new ProfilePresenter();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;
    @BindView(R.id.txtLastName)
    EditText txtLastName;
    @BindView(R.id.phoneNumber)
    EditText txtPhoneNumber;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    /* @BindView(R.id.txtService)
     EditText txtService;*/
    @BindView(R.id.serviceTxt)
    TextView serviceTxt;
    @BindView(R.id.txtModel)
    EditText txtModel;
    @BindView(R.id.txtNumber)
    EditText txtNumber;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.lblChangePassword)
    TextView lblChangePassword;
    /*@BindView(R.id.qr_scan)
    ImageView ivQrScan;*/
    @BindView(R.id.txtService)
    AppCompatSpinner spinnerServiceType;
    @BindView(R.id.countryImage)
    ImageView countryImage;
    @BindView(R.id.countryNumber)
    TextView countryNumber;

    private File imgFile = null;
    /* private String countryDialCode = Constants.DEFAULT_COUNTRY_DIAL_CODE;
     private String countryCode = Constants.DEFAULT_COUNTRY_CODE;*/
    //  private String qrCodeUrl;
    private int selected_pos = -1;
    private AlertDialog mDialog;
    private String countryDialCode = "+593";
    private String countryCode = "EC";
    private boolean isEmailAvailable = true;
    private boolean isPhoneNumberAvailable = true;
    private CountryPicker mCountryPicker;
    private int serviceId = 0;
    private List<Service> servicelist = new ArrayList<>();
    private List<ServiceType> lstServiceTypes = new ArrayList<>();

    List<Country> countryList;
    List<Country> countryListNew;

    @Override
    public int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    public void initView() {
        Log.i(TAG, "initView: ");
        ButterKnife.bind(this);
        presenter.attachView(this);
        showLoading();
        setupSpinner(null);

        presenter.getProfile();

        spinnerServiceType.setEnabled(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.profile));

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

    }

    private void setCountryList() {
        mCountryPicker = CountryPicker.newInstance("Select Country");
        countryList = Country.getAllCountries();
        countryListNew = Country.getAllCountries();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.btnSave, R.id.lblChangePassword, R.id.imgProfile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                profileUpdate();
                break;
            case R.id.lblChangePassword:
                startActivity(new Intent(ProfileActivity.this, ChangePasswordActivtiy.class));
                break;
            case R.id.imgProfile:
                MultiplePermissionTask();
                break;
            /*case R.id.qr_scan:
                if (!TextUtils.isEmpty(qrCodeUrl)) showQRCodePopup();
                break;*/
            /*case R.id.txtPhoneNumber:
                fbOtpVerify(countryCode, countryDialCode, "");
                break;*/
        }
    }

    @Override
    public void onSuccess(SettingsResponse response) {
        lstServiceTypes = response.getServiceTypes();
        //  lnrReferralCode.setVisibility(response.getReferral().getReferral().equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);
        setupSpinner(response);
    }

    private void getUserCountryInfo() {
        Country country = getDeviceCountry(ProfileActivity.this);

//        countryImage.setImageResource(R.drawable.flag_ec);

//        countryNumber.setText("+593");
//        countryDialCode = "+593";
//        countryCode = "EC";

    }

    private void clickFunctions() {
        txtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            isEmailAvailable = true;
            if (!hasFocus && !TextUtils.isEmpty(txtEmail.getText().toString()))
                if (Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches())
                    presenter.verifyEmail(txtEmail.getText().toString().trim());
        });

        txtPhoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            isPhoneNumberAvailable = true;
            if (!hasFocus && !TextUtils.isEmpty(txtPhoneNumber.getText().toString()))
                presenter.verifyCredentials(countryDialCode, txtPhoneNumber.getText().toString());
        });
    }

    private void showQRCodePopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_qrcode_image, null);

        ImageView qr_image = view.findViewById(R.id.qr_image);
        ImageView close = view.findViewById(R.id.ivClose);

        /*Glide.with(ProfileActivity.this)
                .load(qrCodeUrl)
                .apply(RequestOptions
                        .placeholderOf(R.drawable.ic_qr_code)
                        .dontAnimate().error(R.drawable.ic_qr_code))
                .into(qr_image);*/

        builder.setView(view);
        mDialog = builder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        close.setOnClickListener(view1 -> mDialog.dismiss());

        mDialog.show();
    }

    void profileUpdate() {
        if (txtFirstName.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_first_name), Toast.LENGTH_SHORT).show();
        } else if (txtLastName.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_last_name), Toast.LENGTH_SHORT).show();
        } else if (txtPhoneNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_phone_number), Toast.LENGTH_SHORT).show();
        } else if (txtEmail.getText().toString().isEmpty()
                || !Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
            Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
//        }else if (selected_pos == -1) {
//            Toasty.error(this, getString(R.string.invalid_service_type), Toast.LENGTH_SHORT, true).show();

        } else if (txtModel.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_car_model), Toast.LENGTH_SHORT, true).show();

        } else if (txtNumber.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_car_number), Toast.LENGTH_SHORT, true).show();

        } else updateDetails();
    }

    private void updateDetails() {
        Map<String, RequestBody> map = new HashMap<>();

        map.put("first_name", toRequestBody(txtFirstName.getText().toString()));
        map.put("last_name", toRequestBody(txtLastName.getText().toString()));
        map.put("email", toRequestBody(txtEmail.getText().toString()));
        map.put("mobile", toRequestBody(txtPhoneNumber.getText().toString()));
        map.put("country_code", toRequestBody(countryNumber.getText().toString()));
        //    map.put("service_type",toRequestBody( lstServiceTypes.get(selected_pos).getId() + ""));
        map.put("service_model", toRequestBody(txtModel.getText().toString()));
        map.put("service_number", toRequestBody(txtNumber.getText().toString()));

        MultipartBody.Part filePart = null;
        if (imgFile != null)
            try {
                File compressedImageFile = new Compressor(this).compressToFile(imgFile);
                filePart = MultipartBody.Part.createFormData("avatar", compressedImageFile.getName(),
                        RequestBody.create(MediaType.parse("image*//*"), compressedImageFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

        Utilities.printV("Params ===> 2", map.toString());
        showLoading();
        presenter.profileUpdate(map, filePart);
    }

    private void setupSpinner(@Nullable SettingsResponse response) {
        int selectedId = 0;
        ArrayList<String> lstNames = new ArrayList<>(response != null ? response.getServiceTypes().size() : 0);
        if (response != null) for (ServiceType serviceType : response.getServiceTypes()) {
            lstNames.add(serviceType.getName());
            //  Log.d("TAG","Service Id:"+serviceId);
            if (serviceType.getId() == serviceId)
                selectedId = lstNames.indexOf(serviceType.getName());

            //  lstNames.add(serviceType.getId());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lstNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServiceType.setAdapter(dataAdapter);
        spinnerServiceType.setSelection(selectedId);
        hideLoading();
    }

    @Override
    public void onSuccess(UserResponse user) {

        Utilities.printV("User===>", user.getFirstName() + user.getLastName());
        Utilities.printV("TOKEN===>", SharedHelper.getKey(MvpApplication.getInstance(),
                Constants.SharedPref.ACCESS_TOKEN, ""));

        String loginBy = user.getLoginBy();

        if (loginBy.equalsIgnoreCase("facebook") || loginBy.equalsIgnoreCase("google"))
            lblChangePassword.setVisibility(View.INVISIBLE);
        else lblChangePassword.setVisibility(View.VISIBLE);
        servicelist = user.getServicelist();
        String serviceNameList = "";
        for (int i = 0; i < servicelist.size(); i++) {
            serviceNameList += servicelist.get(i).getServiceType().getName() + ",";
        }
        serviceTxt.setText(serviceNameList);
        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtPhoneNumber.setText(String.valueOf(user.getMobile()));
        txtNumber.setText(user.getService().getServiceNumber());
        txtModel.setText(user.getService().getServiceModel());
        serviceId = user.getService().getServiceType().getId();

        if (user.getCountry_code() != null) countryNumber.setText(user.getCountry_code());

        if (user.getCountry_code() != null) {
            for (int i = 0; i < countryListNew.size(); i++) {
                if (countryListNew.get(i).getDialCode().equals(user.getCountry_code())) {
                    countryImage.setImageResource(countryListNew.get(i).getFlag());
                }
            }
        }

        //        countryImage.setImageResource(R.drawable.flag_ec);

//        countryNumber.setText("+593");
        countryDialCode = "" + user.getCountry_code();
//        countryCode = "EC";


        Log.i(TAG, "onSuccess: country_code: " + user.getCountry_code());

        //   Log.d("TAG","Service Id000:"+serviceId);

        txtEmail.setText(user.getEmail());
        SharedHelper.putKey(this, Constants.SharedPref.STRIPE_PUBLISHABLE_KEY, user.getStripePublishableKey());
        /*if (user.getService() != null)
           spinnerServiceType.setSelection(user.getService().getServiceType());*/
        //   txtService.setText((user.getService().getServiceType() != null)
        //           ? user.getService().getServiceType().getName() : "");
        Glide.with(activity())
                .load(BuildConfig.BASE_IMAGE_URL + user.getAvatar())
                .apply(RequestOptions
                        .placeholderOf(R.drawable.ic_user_placeholder)
                        .dontAnimate()
                        .error(R.drawable.ic_user_placeholder))
                .into(imgProfile);
        Constants.showOTP = user.getRide_otp().equals("1");
        // qrCodeUrl = !TextUtils.isEmpty(user.getQrcode_url()) ? BuildConfig.BASE_URL + user.getQrcode_url() : null;
        //ivQrScan.setVisibility(TextUtils.isEmpty(qrCodeUrl) ? View.INVISIBLE : View.VISIBLE);
        presenter.getSettings();
    }

    @Override
    public void onSuccessUpdate(UserResponse object) {
        hideLoading();
        Intent profileIntent = new Intent(this, MainActivity.class);
        profileIntent.putExtra("avatar", BuildConfig.BASE_IMAGE_URL + object.getAvatar());
        startActivity(profileIntent);
        Toasty.success(this, getString(R.string.profile_updated), Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, ProfileActivity.this,
                new DefaultCallback() {
                    @Override
                    public void onImagesPicked(@NonNull List<File> imageFiles,
                                               EasyImage.ImageSource source, int type) {

                        imgFile = imageFiles.get(0);

                        Glide.with(activity())
                                .load(Uri.fromFile(imgFile))
                                .apply(RequestOptions
                                        .placeholderOf(R.drawable.ic_user_placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.ic_user_placeholder))
                                .into(imgProfile);
                    }
                });


        if (requestCode == Constants.APP_REQUEST_CODE && data != null) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (!loginResult.wasCancelled())
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        Log.d("AccountKit", "onSuccess: Account Kit" + Objects.requireNonNull(AccountKit.getCurrentAccessToken()).getToken());
                        if (Objects.requireNonNull(AccountKit.getCurrentAccessToken()).getToken() != null) {
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            SharedHelper.putKey(ProfileActivity.this, Constants.SharedPref.DIAL_CODE, "+" + phoneNumber.getCountryCode());
                            SharedHelper.putKey(ProfileActivity.this, Constants.SharedPref.MOBILE, phoneNumber.getPhoneNumber());
                            txtPhoneNumber.setText(phoneNumber.getPhoneNumber());
                            presenter.verifyCredentials("+" + phoneNumber.getCountryCode(), phoneNumber.getPhoneNumber());
                        }
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Log.e("AccountKit", "onError: Account Kit" + accountKitError);
                    }
                });
        }
    }

    private boolean hasMultiplePermission() {
        return EasyPermissions.hasPermissions(this, Constants.MULTIPLE_PERMISSION);
    }

    @AfterPermissionGranted(Constants.RC_MULTIPLE_PERMISSION_CODE)
    void MultiplePermissionTask() {

        if (hasMultiplePermission()) pickImage();

        else EasyPermissions.requestPermissions(
                this, getString(R.string.please_accept_permission),
                Constants.RC_MULTIPLE_PERMISSION_CODE,
                Constants.MULTIPLE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onSuccessPhoneNumber(Object object) {
        updateDetails();
    }

    @Override
    public void onVerifyPhoneNumberError(Throwable e) {
        Toasty.error(this, getString(R.string.phone_number_already_exists), Toast.LENGTH_SHORT).show();
        txtPhoneNumber.requestFocus();
    }

    @Override
    public void onSuccess(Object verifyEmail) {

        isEmailAvailable = false;
    }

    @Override
    public void onVerifyEmailError(Throwable e) {
        isEmailAvailable = true;
        Toasty.error(this, getString(R.string.email_already_exist), Toast.LENGTH_SHORT).show();
        txtEmail.requestFocus();

    }
}
