package com.transmodelo.conductor.ui.fragment.upcoming;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.HistoryList;

import java.util.List;

public interface UpcomingTripIView extends MvpView {

    void onSuccess(List<HistoryList> historyList);
    void onError(Throwable e);
}
