package com.transmodelo.conductor.ui.bottomsheetdialog.rating;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.Rating;

public interface RatingDialogIView extends MvpView {

    void onSuccess(Rating rating);
    void onError(Throwable e);
}
