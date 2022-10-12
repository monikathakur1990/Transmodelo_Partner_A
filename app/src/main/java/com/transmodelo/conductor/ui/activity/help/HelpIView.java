package com.transmodelo.conductor.ui.activity.help;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.Help;

public interface HelpIView extends MvpView {

    void onSuccess(Help object);

    void onError(Throwable e);
}
