package com.transmodelo.conductor.ui.activity.forgot_password;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.ForgotResponse;

public interface ForgotIView extends MvpView {

    void onSuccess(ForgotResponse forgotResponse);
    void onError(Throwable e);
}
