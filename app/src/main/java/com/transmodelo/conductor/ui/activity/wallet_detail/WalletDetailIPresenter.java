package com.transmodelo.conductor.ui.activity.wallet_detail;

import com.transmodelo.conductor.base.MvpPresenter;
import com.transmodelo.conductor.data.network.model.Transaction;

import java.util.ArrayList;

public interface WalletDetailIPresenter<V extends WalletDetailIView> extends MvpPresenter<V> {
    void setAdapter(ArrayList<Transaction> myList);
}
