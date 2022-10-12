package com.transmodelo.conductor.ui.fragment.status_flow;

import com.transmodelo.conductor.base.MvpPresenter;

import java.util.HashMap;

public interface StatusFlowIPresenter<V extends StatusFlowIView> extends MvpPresenter<V> {

    void statusUpdate(HashMap<String, Object> obj, Integer id);

    void waitingTime(String time, String requestId);

    void checkWaitingTime(String requestId);
}
