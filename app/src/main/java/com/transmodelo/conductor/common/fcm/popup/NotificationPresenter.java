package com.transmodelo.conductor.common.fcm.popup;

import com.transmodelo.conductor.base.BasePresenter;
import com.transmodelo.conductor.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NotificationPresenter <V extends NotificationIView> extends BasePresenter<V>
        implements NotificationIPresenter<V> {

    @Override
    public void getTrip(HashMap<String, Object> params) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getTrip(params)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }
}
