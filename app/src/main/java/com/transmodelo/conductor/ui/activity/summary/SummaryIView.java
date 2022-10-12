package com.transmodelo.conductor.ui.activity.summary;


import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.Summary;

public interface SummaryIView extends MvpView {

    void onSuccess(Summary object);

    void onError(Throwable e);
}
