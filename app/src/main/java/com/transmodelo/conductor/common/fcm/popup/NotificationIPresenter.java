package com.transmodelo.conductor.common.fcm.popup;

import com.transmodelo.conductor.base.MvpPresenter;

import java.util.HashMap;

public interface NotificationIPresenter <V extends NotificationIView> extends MvpPresenter<V> {

    void getTrip(HashMap<String, Object> params);

}