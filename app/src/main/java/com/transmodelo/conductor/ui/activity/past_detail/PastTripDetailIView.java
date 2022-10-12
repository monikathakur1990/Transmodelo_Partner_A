package com.transmodelo.conductor.ui.activity.past_detail;


import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.HistoryDetail;

public interface PastTripDetailIView extends MvpView {

    void onSuccess(HistoryDetail historyDetail);
    void onError(Throwable e);
}
