package com.transmodelo.conductor.ui.activity.setting;

import com.transmodelo.conductor.base.MvpView;

public interface SettingsIView extends MvpView {

    void onSuccess(Object o);

    void onError(Throwable e);

}
