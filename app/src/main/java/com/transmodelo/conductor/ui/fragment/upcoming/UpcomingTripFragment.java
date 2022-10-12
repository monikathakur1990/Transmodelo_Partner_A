package com.transmodelo.conductor.ui.fragment.upcoming;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.transmodelo.conductor.R;
import com.transmodelo.conductor.base.BaseFragment;
import com.transmodelo.conductor.common.Constants;
import com.transmodelo.conductor.common.SharedHelper;
import com.transmodelo.conductor.common.Utilities;
import com.transmodelo.conductor.common.fcm.MyFireBaseMessagingService;
import com.transmodelo.conductor.data.network.model.HistoryList;
import com.transmodelo.conductor.ui.activity.upcoming_detail.UpcomingTripDetailActivity;
import com.transmodelo.conductor.ui.adapter.UpcomingTripAdapter;
import com.transmodelo.conductor.ui.bottomsheetdialog.cancel.CancelDialogFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.transmodelo.conductor.MvpApplication.DATUM_history;

public class UpcomingTripFragment extends BaseFragment implements UpcomingTripIView,
        UpcomingTripAdapter.ClickListener {

    @BindView(R.id.upcoming_trip_rv)
    RecyclerView upcomingTripRv;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.tv_error)
    TextView error;
    Unbinder unbinder;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    List<HistoryList> list = new ArrayList<>();
    UpcomingTripPresenter presenter = new UpcomingTripPresenter();
    Context context;

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.getUpcoming();
        }
    };

    public UpcomingTripFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_upcoming_trip;
    }

    @Override
    public View initView(View view) {

        unbinder = ButterKnife.bind(this, view);
        this.context = getContext();
        presenter.attachView(this);
        error.setText(getString(R.string.no_upcoming_found));
        getActivity().registerReceiver(myReceiver, new IntentFilter(MyFireBaseMessagingService.INTENT_FILTER));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        upcomingTripRv.setLayoutManager(mLayoutManager);
        upcomingTripRv.setItemAnimator(new DefaultItemAnimator());

        progressBar.setVisibility(View.VISIBLE);
        presenter.getUpcoming();

        return view;
    }

    @Override
    public void onSuccess(List<HistoryList> historyList) {

        Utilities.printV("SIZE", historyList.size() + "");

        progressBar.setVisibility(View.GONE);

        list.clear();
        list.addAll(historyList);
        loadAdapter();


    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        progressBar.setVisibility(View.GONE);
        if (e != null) try {
            onErrorBase(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void loadAdapter() {
        if (list.size() > 0) {
            UpcomingTripAdapter adapter = new UpcomingTripAdapter(list, context);
            adapter.setClickListener(this);
            upcomingTripRv.setAdapter(adapter);
            upcomingTripRv.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        } else {
            upcomingTripRv.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void redirectClick(HistoryList historyList, ImageView staticMap) {

        DATUM_history = historyList;
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity(), staticMap,
                        ViewCompat.getTransitionName(staticMap));
        startActivity(new Intent(context, UpcomingTripDetailActivity.class),
                options.toBundle());

    }

    @Override
    public void cancelRide(HistoryList historyList, int pos) {

        DATUM_history = historyList;
        SharedHelper.putKey(getContext(), Constants.SharedPref.CANCEL_ID, String.valueOf(DATUM_history.getId()));

        cancelRequestPopup();


    }

    void cancelRequestPopup() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity());
        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.cancel_request_title))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                    CancelDialogFragment cancelDialogFragment = new CancelDialogFragment();
                    cancelDialogFragment.show(getChildFragmentManager(), cancelDialogFragment.getTag());
                })
                .setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.cancel());
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        context.unregisterReceiver(myReceiver);

    }
}

