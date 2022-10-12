package com.transmodelo.conductor.ui.activity.profile;

import com.transmodelo.conductor.base.MvpPresenter;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ProfileIPresenter<V extends ProfileIView> extends MvpPresenter<V> {

    void profileUpdate(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part filename);

    void getProfile();
    void getSettings();
    void verifyEmail(String email);
    void verifyCredentials(String countryCode, String phoneNumber);


}
