package com.transmodelo.conductor.ui.fragment.offline;

import android.content.Context;
import android.content.Intent;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.transmodelo.conductor.R;
import com.transmodelo.conductor.base.BaseFragment;
import com.transmodelo.conductor.common.Constants;
import com.transmodelo.conductor.common.swipe_button.SwipeButton;
import com.transmodelo.conductor.common.fcm.MyFireBaseMessagingService;

import org.json.JSONObject;

import java.util.HashMap;

import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class OfflineFragment extends BaseFragment implements OfflineIView {

    private OfflinePresenter presenter = new OfflinePresenter();

    @BindView(R.id.menu_img)
    ImageView menuImg;
    @BindView(R.id.tvApprovalDesc)
    TextView tvApprovalDesc;
    @BindView(R.id.swipeBtnEnabled)
    SwipeButton swipeBtnEnabled;

    private DrawerLayout drawer;
    private Context context;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_offline;
    }

    @Override
    public View initView(View view) {
        ButterKnife.bind(this, view);
        this.context = getContext();
        presenter.attachView(this);
        drawer = activity().findViewById(R.id.drawer_layout);
        String s = getArguments().getString("status");
        if (!TextUtils.isEmpty(s))
            if (s.equalsIgnoreCase(Constants.User.Account.ONBOARDING))
                tvApprovalDesc.setVisibility(View.VISIBLE);
            else if (s.equalsIgnoreCase(Constants.User.Account.BANNED)) {
                tvApprovalDesc.setVisibility(View.VISIBLE);
                tvApprovalDesc.setText(getString(R.string.banned_desc));
            } else if (s.equalsIgnoreCase(Constants.User.Account.BALANCE)) {
                tvApprovalDesc.setVisibility(View.VISIBLE);
                tvApprovalDesc.setText(getString(R.string.low_balance));
                swipeBtnEnabled.setVisibility(View.INVISIBLE);
            }

        swipeBtnEnabled.setOnStateChangeListener(active -> {
            if (active) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("service_status", Constants.User.Service.ACTIVE);
                presenter.providerAvailable(map);
            }
        });
        return view;
    }

    @OnClick({R.id.menu_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_img:
                drawer.openDrawer(Gravity.START);
                break;
        }
    }

    @Override
    public void onSuccess(Object object) {
        try {
            JSONObject jsonObj = new JSONObject(new Gson().toJson(object));
            Log.e("onSuccess: ", jsonObj.toString() );

            if (jsonObj.has("error"))
                Toasty.error(activity(), jsonObj.optString("error"), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("onSuccess: ", e.getMessage().toString() );

        }
        context.sendBroadcast(new Intent(MyFireBaseMessagingService.INTENT_FILTER));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        swipeBtnEnabled.toggleState();
        if (e != null) try {
            onErrorBase(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
