package com.transmodelo.conductor.ui.activity.add_card;

import com.transmodelo.conductor.base.MvpView;

public interface AddCardIView extends MvpView {

    void onSuccess(Object card);

    void onError(Throwable e);
}
