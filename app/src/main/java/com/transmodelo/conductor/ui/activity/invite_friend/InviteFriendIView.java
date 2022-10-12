package com.transmodelo.conductor.ui.activity.invite_friend;

import com.transmodelo.conductor.base.MvpView;
import com.transmodelo.conductor.data.network.model.UserResponse;

public interface InviteFriendIView extends MvpView {

    void onSuccess(UserResponse response);
    void onError(Throwable e);

}
