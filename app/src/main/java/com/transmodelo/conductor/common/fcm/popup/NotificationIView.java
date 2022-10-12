package com.transmodelo.conductor.common.fcm.popup;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.TripResponse;

public interface NotificationIView  extends MvpView {
    void onSuccess(TripResponse tripResponse);

    void onError(Throwable e);
}