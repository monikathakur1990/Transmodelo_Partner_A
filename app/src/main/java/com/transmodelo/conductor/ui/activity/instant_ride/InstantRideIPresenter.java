package com.transmodelo.conductor.ui.activity.instant_ride;

import com.transmodelo.conductor.base.MvpPresenter;

import java.util.Map;

public interface InstantRideIPresenter<V extends InstantRideIView> extends MvpPresenter<V> {

    void estimateFare(Map<String, Object> params);

    void requestInstantRide(Map<String, Object> params);

}
