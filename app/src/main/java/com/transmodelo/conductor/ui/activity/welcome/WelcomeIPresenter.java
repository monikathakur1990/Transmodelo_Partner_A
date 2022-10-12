package com.transmodelo.conductor.ui.activity.welcome;

import android.os.Handler;

import com.transmodelo.conductor.base.MvpPresenter;

import java.util.HashMap;

public interface WelcomeIPresenter<V extends WelcomeIView> extends MvpPresenter<V> {
    void handlerCall();

    void checkVersion(HashMap<String, Object> map);
}
