package com.transmodelo.conductor.base;

import android.app.Activity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.transmodelo.conductor.data.network.model.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment implements MvpView {

    View view;

    public abstract int getLayoutId();

    public abstract void initView(View view);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
            initView(view);
        }

        return view;
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void showLoading() {
        ((BaseActivity) getActivity()).showLoading();
    }

    @Override
    public void hideLoading() {
        ((BaseActivity) getActivity()).hideLoading();
    }

    @Override
    public void onSuccessRefreshToken(User user) {
        ((BaseActivity) getActivity()).onSuccessRefreshToken(user);
    }

    @Override
    public void onErrorRefreshToken(Throwable throwable) {
        ((BaseActivity) getActivity()).onErrorRefreshToken(throwable);
    }

    public void onErrorBase(Throwable e) {
        ((BaseActivity) getActivity()).onErrorBase(e);
    }

    @Override
    public void onSuccessLogout(Object object) {
        ((BaseActivity) getActivity()).onSuccessLogout(object);
    }

    @Override
    public void onError(Throwable throwable) {
        ((BaseActivity) getActivity()).onError(throwable);
    }


}
