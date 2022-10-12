package com.transmodelo.conductor.ui.activity.card;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.Card;

import java.util.List;

public interface CardIView extends MvpView {

    void onSuccess(Object card);

    void onSuccess(List<Card> cards);

    void onError(Throwable e);

    void onSuccessChangeCard(Object card);
}
