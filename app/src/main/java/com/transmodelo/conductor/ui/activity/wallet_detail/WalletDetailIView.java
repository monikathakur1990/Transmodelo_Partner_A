package com.transmodelo.conductor.ui.activity.wallet_detail;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.Transaction;

import java.util.ArrayList;

public interface WalletDetailIView extends MvpView {
    void setAdapter(ArrayList<Transaction> myList);
}
