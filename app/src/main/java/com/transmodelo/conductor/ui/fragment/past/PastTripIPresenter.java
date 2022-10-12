package com.transmodelo.conductor.ui.fragment.past;


import com.transmodelo.conductor.base.MvpPresenter;

public interface PastTripIPresenter<V extends PastTripIView> extends MvpPresenter<V> {

    void getHistory();

}
