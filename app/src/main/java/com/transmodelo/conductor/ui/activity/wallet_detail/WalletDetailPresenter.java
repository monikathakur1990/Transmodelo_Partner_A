package com.transmodelo.conductor.ui.activity.wallet_detail;

import com.transmodelo.conductor.base.BasePresenter;
import com.transmodelo.conductor.data.network.model.Transaction;

import java.util.ArrayList;

public class WalletDetailPresenter<V extends WalletDetailIView> extends BasePresenter<V> implements WalletDetailIPresenter<V> {
    @Override
    public void setAdapter(ArrayList<Transaction> myList) {
        getMvpView().setAdapter(myList);
    }
}
