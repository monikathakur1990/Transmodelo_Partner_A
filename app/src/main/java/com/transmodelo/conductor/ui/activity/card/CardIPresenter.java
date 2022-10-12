package com.transmodelo.conductor.ui.activity.card;

import com.transmodelo.conductor.base.MvpPresenter;

public interface CardIPresenter<V extends CardIView> extends MvpPresenter<V> {

    void deleteCard(String cardId);

    void card();

    void changeCard(String cardId);
}
