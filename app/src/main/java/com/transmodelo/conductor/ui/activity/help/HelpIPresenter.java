package com.transmodelo.conductor.ui.activity.help;


import com.transmodelo.conductor.base.MvpPresenter;

public interface HelpIPresenter<V extends HelpIView> extends MvpPresenter<V> {

    void getHelp();
}
