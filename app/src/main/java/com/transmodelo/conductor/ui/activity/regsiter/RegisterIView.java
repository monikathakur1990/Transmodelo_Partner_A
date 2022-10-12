package com.transmodelo.conductor.ui.activity.regsiter;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.SettingsResponse;
import com.transmodelo.conductor.data.network.model.User;

public interface RegisterIView extends MvpView {

    void onSuccess(User user);

    void onSuccess(Object verifyEmail);

    void onSuccess(SettingsResponse response);

    void onError(Throwable e);

    void onSuccessPhoneNumber(Object object);

    void onVerifyPhoneNumberError(Throwable e);

    void onVerifyEmailError(Throwable e);

}
