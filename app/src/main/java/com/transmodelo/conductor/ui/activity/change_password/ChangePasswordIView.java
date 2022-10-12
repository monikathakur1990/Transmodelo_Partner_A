package com.transmodelo.conductor.ui.activity.change_password;

import com.transmodelo.conductor.base.MvpView;

public interface ChangePasswordIView extends MvpView {


    void onSuccess(Object object);
    void onError(Throwable e);
}
