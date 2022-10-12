package com.transmodelo.conductor.ui.activity.add_card;

import com.transmodelo.conductor.base.MvpPresenter;

public interface AddCardIPresenter<V extends AddCardIView> extends MvpPresenter<V> {

    void addCard(String stripeToken);
}
