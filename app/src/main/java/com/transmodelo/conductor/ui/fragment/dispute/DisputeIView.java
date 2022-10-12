package com.transmodelo.conductor.ui.fragment.dispute;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.DisputeResponse;

import java.util.List;

public interface DisputeIView extends MvpView {

    void onSuccessDispute(List<DisputeResponse> responseList);

    void onSuccess(Object object);

    void onError(Throwable e);
}
