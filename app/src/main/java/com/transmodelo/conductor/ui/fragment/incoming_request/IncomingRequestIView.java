package com.transmodelo.conductor.ui.fragment.incoming_request;

import com.transmodelo.conductor.base.MvpView;

public interface IncomingRequestIView extends MvpView {

    void onSuccessAccept(Object responseBody);
    void onSuccessCancel(Object object);
    void onError(Throwable e);
}
