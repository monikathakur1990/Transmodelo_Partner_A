package com.transmodelo.conductor.ui.activity.earnings;


import com.transmodelo.conductor.base.MvpPresenter;

public interface EarningsIPresenter<V extends EarningsIView> extends MvpPresenter<V> {

    void getEarnings();
}
