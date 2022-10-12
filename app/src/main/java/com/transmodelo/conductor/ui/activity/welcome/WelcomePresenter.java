package com.transmodelo.conductor.ui.activity.welcome;

import android.os.Handler;

import com.transmodelo.conductor.base.BasePresenter;
import com.transmodelo.conductor.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WelcomePresenter<V extends WelcomeIView> extends BasePresenter<V> implements WelcomeIPresenter<V> {

    @Override
    public void handlerCall() {
        new Handler().postDelayed(() -> getMvpView().verifyAppInstalled(), 0);
    }

    @Override
    public void checkVersion(HashMap<String, Object> map) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .checkversion(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onCheckVersionError));
    }

}
