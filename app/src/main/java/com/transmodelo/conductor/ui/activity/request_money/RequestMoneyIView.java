package com.transmodelo.conductor.ui.activity.request_money;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.RequestDataResponse;

public interface RequestMoneyIView extends MvpView {

    void onSuccess(RequestDataResponse response);
    void onSuccess(Object response);
    void onError(Throwable e);

}
