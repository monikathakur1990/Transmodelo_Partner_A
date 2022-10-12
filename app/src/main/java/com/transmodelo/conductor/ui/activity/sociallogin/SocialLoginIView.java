package com.transmodelo.conductor.ui.activity.sociallogin;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.Token;

public interface SocialLoginIView extends MvpView {

    void onSuccess(Token token);
    void onError(Throwable e);
}
