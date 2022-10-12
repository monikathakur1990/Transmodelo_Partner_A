package com.transmodelo.conductor.ui.activity.reset_password;

import com.transmodelo.conductor.base.MvpView;

public interface ResetIView extends MvpView {

    void onSuccess(Object object);
    void onError(Throwable e);
}
