package com.transmodelo.conductor.ui.activity.notification_manager;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.NotificationManager;

import java.util.List;

public interface NotificationManagerIView extends MvpView {

    void onSuccess(List<NotificationManager> managers);

    void onError(Throwable e);

}