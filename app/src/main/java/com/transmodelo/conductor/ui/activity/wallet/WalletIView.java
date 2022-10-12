package com.transmodelo.conductor.ui.activity.wallet;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.WalletMoneyAddedResponse;
import com.transmodelo.conductor.data.network.model.WalletResponse;

public interface WalletIView extends MvpView {

    void onSuccess(WalletResponse response);

    void onSuccess(WalletMoneyAddedResponse response);

    void onError(Throwable e);
}
