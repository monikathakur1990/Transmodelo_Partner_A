package com.transmodelo.conductor.ui.bottomsheetdialog.rating;

import com.transmodelo.conductor.base.MvpPresenter;

import java.util.HashMap;

public interface RatingDialogIPresenter<V extends RatingDialogIView> extends MvpPresenter<V> {

    void rate(HashMap<String, Object> obj, Integer id);
}
