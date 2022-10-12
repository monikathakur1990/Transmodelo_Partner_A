package com.transmodelo.conductor.ui.activity.instant_ride;

import com.google.gson.JsonObject;
import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.EstimateFare;
import com.transmodelo.conductor.data.network.model.TripResponse;

import org.json.JSONObject;

public interface InstantRideIView extends MvpView {

    void onSuccess(EstimateFare estimateFare);

    void onSuccess(Object response);

    void onError(Throwable e);

}
