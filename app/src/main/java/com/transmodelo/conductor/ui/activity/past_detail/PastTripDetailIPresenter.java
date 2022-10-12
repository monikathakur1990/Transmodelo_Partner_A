package com.transmodelo.conductor.ui.activity.past_detail;


import com.transmodelo.conductor.base.MvpPresenter;

public interface PastTripDetailIPresenter<V extends PastTripDetailIView> extends MvpPresenter<V> {

    void getPastTripDetail(String request_id);
}
