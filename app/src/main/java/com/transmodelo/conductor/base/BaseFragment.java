package com.transmodelo.conductor.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transmodelo.conductor.R;
import com.transmodelo.conductor.data.network.model.User;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseFragment extends Fragment implements MvpView {

    protected View view;
    protected ProgressDialog progressDialog;

    private BaseActivity mActivity;


    public abstract int getLayoutId();

    public abstract View initView(View view);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
            initView(view);
        }

        progressDialog = new ProgressDialog(activity());
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        return view;
    }

    @Override
    public FragmentActivity activity() {
        return getActivity();
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    public void onErrorBase(Throwable e) {
        if (mActivity != null) {
            mActivity.onErrorBase(e);
        }
    }

    @Override
    public void onSuccessRefreshToken(User user) {
        if (mActivity != null) {
            mActivity.onSuccessRefreshToken(user);
        }
    }

    @Override
    public void onErrorRefreshToken(Throwable throwable) {
        if (mActivity != null) {
            mActivity.onErrorRefreshToken(throwable);
        }
    }

    @Override
    public void onSuccessLogout(Object object) {
        if (mActivity != null) {
            mActivity.onSuccessLogout(object);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (mActivity != null) {
            mActivity.onError(throwable);
        }
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }
}
