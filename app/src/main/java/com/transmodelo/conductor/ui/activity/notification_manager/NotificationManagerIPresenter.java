package com.transmodelo.conductor.ui.activity.notification_manager;

import com.transmodelo.conductor.base.MvpPresenter;

public interface NotificationManagerIPresenter<V extends NotificationManagerIView> extends MvpPresenter<V> {
    void getNotificationManager();
}
