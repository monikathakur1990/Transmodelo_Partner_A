package com.transmodelo.conductor.ui.activity.document;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.DriverDocumentResponse;

public interface DocumentIView extends MvpView {

    void onSuccess(DriverDocumentResponse response);

    void onDocumentSuccess(DriverDocumentResponse response);

    void onError(Throwable e);

    void onSuccessLogout(Object object);

}
