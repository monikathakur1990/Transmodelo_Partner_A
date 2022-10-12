package com.transmodelo.conductor.ui.activity.setting;

import com.transmodelo.conductor.base.MvpPresenter;

public interface SettingsIPresenter<V extends SettingsIView> extends MvpPresenter<V> {
    void changeLanguage(String languageID);
}
