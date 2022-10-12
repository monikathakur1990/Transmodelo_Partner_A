package com.transmodelo.conductor.ui.fragment.past;


import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.HistoryList;

import java.util.List;

public interface PastTripIView extends MvpView {

    void onSuccess(List<HistoryList> historyList);
    void onError(Throwable e);
}
