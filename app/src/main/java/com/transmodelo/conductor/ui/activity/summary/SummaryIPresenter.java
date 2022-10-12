package com.transmodelo.conductor.ui.activity.summary;


import com.transmodelo.conductor.base.MvpPresenter;

public interface SummaryIPresenter<V extends SummaryIView> extends MvpPresenter<V> {

    void getSummary();
}
