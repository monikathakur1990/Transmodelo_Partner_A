package com.transmodelo.conductor.ui.fragment.offline;

import com.transmodelo.conductor.base.MvpView;

public interface OfflineIView extends MvpView {

    void onSuccess(Object object);
    void onError(Throwable e);
}
