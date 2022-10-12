package com.transmodelo.conductor.ui.bottomsheetdialog.cancel;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.CancelResponse;

import java.util.List;

public interface CancelDialogIView extends MvpView {

    void onSuccessCancel(Object object);
    void onError(Throwable e);
    void onSuccess(List<CancelResponse> response);
    void onReasonError(Throwable e);
}
