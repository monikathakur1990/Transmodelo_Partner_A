package com.transmodelo.conductor.ui.activity.password;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.ForgotResponse;
import com.transmodelo.conductor.data.network.model.User;

public interface PasswordIView extends MvpView {

    void onSuccess(ForgotResponse forgotResponse);

    void onSuccess(User object);

    void onError(Throwable e);
}
