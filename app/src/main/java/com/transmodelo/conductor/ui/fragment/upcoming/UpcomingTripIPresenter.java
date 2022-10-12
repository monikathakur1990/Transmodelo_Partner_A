package com.transmodelo.conductor.ui.fragment.upcoming;


import com.transmodelo.conductor.base.MvpPresenter;

public interface UpcomingTripIPresenter<V extends UpcomingTripIView> extends MvpPresenter<V> {

    void getUpcoming();

}
