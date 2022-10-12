package com.transmodelo.conductor.ui.activity.sociallogin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.transmodelo.conductor.BuildConfig;
import com.transmodelo.conductor.R;
import com.transmodelo.conductor.base.BaseActivity;
import com.transmodelo.conductor.common.Constants;
import com.transmodelo.conductor.common.SharedHelper;
import com.transmodelo.conductor.data.network.model.Token;
import com.transmodelo.conductor.ui.activity.main.MainActivity;
import com.transmodelo.conductor.ui.activity.regsiter.RegisterActivity;
import com.transmodelo.conductor.ui.countrypicker.Country;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SocialLoginActivity extends BaseActivity implements SocialLoginIView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lblFacebook)
    TextView lblFacebook;
    /*@BindView(R.id.lblGoogle)
    TextView lblGoogle;*/

    private String country_dial_code = Constants.DEFAULT_COUNTRY_DIAL_CODE;
    private String country_code = Constants.DEFAULT_COUNTRY_CODE;

    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private SocialLoginPresenter<SocialLoginActivity> presenter = new SocialLoginPresenter<>();
    private CallbackManager callbackManager;
    private HashMap<String, Object> map = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_social_login;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);

        callbackManager = CallbackManager.Factory.create();
        presenter.attachView(this);
        map.put("device_token", SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN));
        map.put("device_id", SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID));
        map.put("device_type", BuildConfig.DEVICE_TYPE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.google_signin_server_client_id)).
                requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.tranxitpro.partner",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("artaxi:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        getUserCountryInfo();

    }

    @OnClick({R.id.back, R.id.lblFacebook/*, R.id.lblGoogle*/})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.lblFacebook:
                fbLogin();
                break;
           /* case R.id.lblGoogle:
                showLoading();
                startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
                break;*/
        }
    }

    private void getUserCountryInfo() {
        Country country = getDeviceCountry(SocialLoginActivity.this);
        country_dial_code = country.getDialCode();
        country_code = country.getCode();
    }

    void fbLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            public void onSuccess(LoginResult loginResult) {
                if (AccessToken.getCurrentAccessToken() != null) {

                    AccessToken.refreshCurrentAccessTokenAsync();
                    Profile profile = Profile.getCurrentProfile();
                    map.put("login_by", Constants.Login.FACEBOOK);
                    map.put("accessToken", loginResult.getAccessToken().getToken());
                    GraphRequest request = GraphRequest.newMeRequest(
                            //loginResult.getAccessToken(),
                            AccessToken.getCurrentAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    Log.v("LoginActivity Response ", response.toString());

                                    try {
                                        map.put("first_name", object.getString("first_name"));
                                        map.put("last_name", object.getString("last_name"));
                                        map.put("email", object.getString("email"));
                                        //      Log.v("Email = ", " bh");
                                        //Toast.makeText(getApplicationContext(), "Name " + Name, Toast.LENGTH_LONG).show();
                                        register();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,email,first_name, last_name");
                    request.setParameters(parameters);
                    request.executeAsync();


                }
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                if (exception instanceof FacebookAuthorizationException)
                    if (AccessToken.getCurrentAccessToken() != null)
                        LoginManager.getInstance().logOut();
                Log.e("Facebook", exception.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("FbLogin",requestCode+" "+resultCode+" "+data);

        if (requestCode == RC_SIGN_IN) {
            hideLoading();
            String TAG = "Google";
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                map.put("login_by", Constants.Login.GOOGLE);
                Runnable runnable = () -> {
                    try {
                        String scope = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE;
                        String accessToken = GoogleAuthUtil.getToken(getApplicationContext(), account.getAccount(), scope, new Bundle());
                        Log.d(TAG, "accessToken:" + accessToken);
                        Log.d(TAG, "Scope:" + scope);
                        map.put("accessToken", accessToken);
                       // fbOtpVerify(country_code, country_dial_code, "");

                    } catch (IOException | GoogleAuthException e) {
                        e.printStackTrace();
                    }
                };
                AsyncTask.execute(runnable);
                register();
            } catch (ApiException e) {
                Log.w(TAG, "signInResult : failed code = " + e.getStatusCode());
            }
        } /*else *//*if (requestCode == APP_REQUEST_CODE) *//*{
           // AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
           *//* if (!loginResult.wasCancelled()) {
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        Log.d("AccountKit", "onSuccess: Account Kit" + AccountKit.getCurrentAccessToken().getToken());
                        if (AccountKit.getCurrentAccessToken().getToken() != null) {
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            SharedHelper.putKey(SocialLoginActivity.this, Constants.SharedPref.DIAL_CODE,
                                    "+" + phoneNumber.getCountryCode());
                            SharedHelper.putKey(SocialLoginActivity.this, Constants.SharedPref.MOBILE,
                                    phoneNumber.getPhoneNumber());

                        }
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Log.e("AccountKit", "onError: Account Kit" + accountKitError);
                    }
                });
            }*//*
            register();
        }*/
    }

    private void register() {
       // map.put("mobile", SharedHelper.getKey(this, Constants.SharedPref.MOBILE));
       // map.put("country_code", SharedHelper.getKey(this, Constants.SharedPref.DIAL_CODE));
        /*if(map.containsKey("login_by")) {
            if (map.get("login_by").equals("google")) presenter.loginGoogle(map);
            else if (map.get("login_by").equals("facebook")) presenter.loginFacebook(map);
            System.out.println("RRR map = " + map);*/
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("Register Data",map);
        startActivity(intent);
            showLoading();
        //}
    }

    @Override
    public void onSuccess(Token token) {
        hideLoading();
        String accessToken = token.getTokenType() + " " + token.getAccessToken();
        SharedHelper.putKey(this, Constants.SharedPref.ACCESS_TOKEN, accessToken);
        SharedHelper.putKey(this, Constants.SharedPref.LOGGGED_IN, "true");
        finishAffinity();
        startActivity(new Intent(this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

}
