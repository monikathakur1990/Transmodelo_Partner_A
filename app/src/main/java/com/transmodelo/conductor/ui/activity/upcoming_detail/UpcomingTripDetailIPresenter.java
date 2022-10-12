package com.transmodelo.conductor.ui.activity.upcoming_detail;


import com.transmodelo.conductor.base.MvpPresenter;

public interface UpcomingTripDetailIPresenter<V extends UpcomingTripDetailIView> extends MvpPresenter<V> {

    void getUpcomingDetail(String request_id);

}
