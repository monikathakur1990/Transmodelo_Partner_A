package com.transmodelo.conductor.ui.bottomsheetdialog.cancel;

import com.transmodelo.conductor.base.MvpPresenter;

import java.util.HashMap;

public interface CancelDialogIPresenter<V extends CancelDialogIView> extends MvpPresenter<V> {

    void cancelRequest(HashMap<String, Object> obj);
    void getReasons();
}
