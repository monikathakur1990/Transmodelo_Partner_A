package com.transmodelo.conductor.ui.bottomsheetdialog.invoice_flow;

import com.transmodelo.conductor.base.MvpPresenter;

import java.util.HashMap;

public interface InvoiceDialogIPresenter<V extends InvoiceDialogIView> extends MvpPresenter<V> {

    void statusUpdate(HashMap<String, Object> obj, Integer id);

}
