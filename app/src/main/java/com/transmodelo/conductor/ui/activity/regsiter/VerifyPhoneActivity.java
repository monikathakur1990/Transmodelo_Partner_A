package com.transmodelo.conductor.ui.activity.regsiter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.transmodelo.conductor.R;
import com.transmodelo.conductor.base.BaseActivity;
import com.transmodelo.conductor.common.Constants;
import com.transmodelo.conductor.common.SharedHelper;
import com.transmodelo.conductor.data.network.model.SettingsResponse;
import com.transmodelo.conductor.data.network.model.User;
import com.transmodelo.conductor.ui.activity.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VerifyPhoneActivity extends BaseActivity implements RegisterIView {

    private EditText editTextOtp;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private Map<String, RequestBody> map = new HashMap<>();
    private String mobile;
    private String isoCode;
    private RegisterPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_verify_phone;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        editTextOtp = findViewById(R.id.txtOtp);
        presenter = new RegisterPresenter();
        presenter.attachView(this);
        presenter.getSettings();
        Intent intent = getIntent();

        mobile = intent.getStringExtra("mobile");
        isoCode = intent.getStringExtra("ISOCode");

        map.put("first_name", toRequestBody(intent.getStringExtra("first_name")));
        map.put("last_name", toRequestBody(intent.getStringExtra("last_name")));
        map.put("email", toRequestBody(intent.getStringExtra("email")));
        map.put("mobile", toRequestBody(intent.getStringExtra("mobile")));
        map.put("country_code", toRequestBody(intent.getStringExtra("country_code")));
        map.put("password", toRequestBody(intent.getStringExtra("password")));
        map.put("password_confirmation", toRequestBody(intent.getStringExtra("password_confirmation")));
        map.put("device_token", toRequestBody(intent.getStringExtra("device_token")));
        map.put("device_id", toRequestBody(intent.getStringExtra("device_id")));
        map.put("service_type", toRequestBody(intent.getStringExtra("service_type")));
        map.put("service_model", toRequestBody(intent.getStringExtra("service_model")));
        map.put("service_number", toRequestBody(intent.getStringExtra("service_number")));
        map.put("device_type", toRequestBody(intent.getStringExtra("device_type")));
        map.put("referral_unique_id", toRequestBody(intent.getStringExtra("referral_unique_id")));

        sendVerificationCode(mobile, isoCode);

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextOtp.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextOtp.setError("Enter valid code");
                    editTextOtp.requestFocus();
                    return;
                }
//
//                //verifying the code entered manually
                verifyVerificationCode(code);


//
//                SharedHelper.putKey(VerifyPhoneActivity.this, Constants.SharedPref.DIAL_CODE,
//                        "+" + isoCode);
//                SharedHelper.putKey(VerifyPhoneActivity.this, Constants.SharedPref.MOBILE,
//                        mobile);
//                register(map);


            }
        });
    }

    private void sendVerificationCode(String mobile, String isoCode) {
        Log.e("sendVerificationCode:", isoCode + mobile);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                isoCode + "" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextOtp.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
            Log.e("onVerifiCompleted: ", code);

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e("onVerificationFailed: ", e.getMessage());
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //storing the verification id that is sent to the user
            mVerificationId = s;
            Log.e("onCodeSent: ", s);
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "onComplete: ", Toast.LENGTH_LONG).show();
                            //verification successful we will start the profile activity
                            Toasty.success(VerifyPhoneActivity.this, "Verification Successful", Toast.LENGTH_SHORT, true).show();
                            //Intent intent = new Intent(VerifyPhoneActivity.this, RegisterActivity.class);
                            // startActivity(intent);
                            SharedHelper.putKey(VerifyPhoneActivity.this, Constants.SharedPref.DIAL_CODE,
                                    "+" + isoCode);
                            SharedHelper.putKey(VerifyPhoneActivity.this, Constants.SharedPref.MOBILE,
                                    mobile);
                            register(map);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Toasty.error(VerifyPhoneActivity.this, message, Toast.LENGTH_SHORT, true).show();

                        }
                    }
                });
    }

    private void register(Map map) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        showLoading();
        map.toString();
        presenter.register(map, parts);
    }

    @Override
    public void onSuccess(User user) {
        Log.i("API", "onSuccess: ---> Register->Verify Phone Activity: User Data: " + user);
        hideLoading();
        SharedHelper.putKey(this, Constants.SharedPref.USER_ID, String.valueOf(user.getId()));
        SharedHelper.putKey(this, Constants.SharedPref.ACCESS_TOKEN, user.getAccessToken());
        SharedHelper.putKey(this, Constants.SharedPref.LOGGGED_IN, "true");
        Toasty.success(this, getString(R.string.register_success), Toast.LENGTH_SHORT, true).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @Override
    public void onSuccess(Object verifyEmail) {

    }


    @Override
    public void onSuccess(SettingsResponse response) {

    }

    @Override
    public void onSuccessPhoneNumber(Object object) {

    }

    @Override
    public void onVerifyPhoneNumberError(Throwable e) {

    }

    @Override
    public void onVerifyEmailError(Throwable e) {

    }
}

