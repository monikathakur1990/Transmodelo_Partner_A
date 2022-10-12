package com.transmodelo.conductor.ui.activity.upcoming_detail;


import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.HistoryDetail;

public interface UpcomingTripDetailIView extends MvpView {

    void onSuccess(HistoryDetail historyDetail);
    void onError(Throwable e);
}
