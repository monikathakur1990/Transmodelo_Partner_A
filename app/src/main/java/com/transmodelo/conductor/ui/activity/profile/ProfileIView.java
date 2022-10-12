package com.transmodelo.conductor.ui.activity.profile;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.SettingsResponse;
import com.transmodelo.conductor.data.network.model.UserResponse;

public interface ProfileIView extends MvpView {

    void onSuccess(UserResponse user);

    void onSuccessUpdate(UserResponse object);
    void onSuccess(SettingsResponse response);
    void onError(Throwable e);

    void onSuccessPhoneNumber(Object object);
    void onSuccess(Object verifyEmail);
    void onVerifyPhoneNumberError(Throwable e);

    void onVerifyEmailError(Throwable e);

}
