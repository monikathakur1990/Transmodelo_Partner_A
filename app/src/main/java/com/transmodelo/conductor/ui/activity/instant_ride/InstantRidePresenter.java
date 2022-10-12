package com.transmodelo.conductor.ui.activity.instant_ride;

import android.util.Log;

import com.transmodelo.conductor.base.BasePresenter;
import com.transmodelo.conductor.data.network.APIClient;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InstantRidePresenter<V extends InstantRideIView> extends BasePresenter<V> implements InstantRideIPresenter<V> {

    @Override
    public void estimateFare(Map<String, Object> params) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .estimateFare(params)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void requestInstantRide(Map<String, Object> params) {
        Log.e("requestInstantRide: ", params.toString());

        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .requestInstantRide(params)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

}
