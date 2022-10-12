package com.transmodelo.conductor.ui.bottomsheetdialog.invoice_flow;

import com.transmodelo.conductor.base.MvpView;

public interface InvoiceDialogIView extends MvpView {

    void onSuccess(Object object);
    void onError(Throwable e);
}
