package com.transmodelo.conductor.ui.activity.payment;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.Card;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface PaymentIView extends MvpView {
    void onSuccess(Object card);

    void onSuccess(List<Card> cards);

    void onError(Throwable e);
}
