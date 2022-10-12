package com.transmodelo.conductor.ui.activity.earnings;


import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.EarningsList;

public interface EarningsIView extends MvpView {

    void onSuccess(EarningsList earningsLists);

    void onError(Throwable e);
}
