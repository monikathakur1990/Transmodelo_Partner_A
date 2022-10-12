package com.transmodelo.conductor.ui.activity.splash;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.CheckVersion;

public interface SplashIView extends MvpView {

    void verifyAppInstalled();

    void onSuccess(Object user);

    void onSuccess(CheckVersion user);

    void onError(Throwable e);

    void onCheckVersionError(Throwable e);
}
