package com.transmodelo.conductor.ui.fragment.status_flow;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.TimerResponse;

public interface StatusFlowIView extends MvpView {

    void onSuccess(Object object);

    void onWaitingTimeSuccess(TimerResponse object);

    void onError(Throwable e);
}
