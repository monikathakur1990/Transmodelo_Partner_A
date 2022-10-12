package com.transmodelo.conductor.ui.fragment.incoming_request;

import com.transmodelo.conductor.base.MvpPresenter;

public interface IncomingRequestIPresenter<V extends IncomingRequestIView> extends MvpPresenter<V> {

    void accept(Integer id);
    void cancel(Integer id);
}
