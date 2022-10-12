package com.transmodelo.conductor.ui.activity.forgot_password;

import com.transmodelo.conductor.base.MvpPresenter;

import java.util.HashMap;

public interface ForgotIPresenter<V extends ForgotIView> extends MvpPresenter<V> {

    void forgot(HashMap<String, Object> obj);

}
