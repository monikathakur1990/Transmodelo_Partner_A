package com.transmodelo.conductor.common.fcm.popup;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.transmodelo.conductor.BuildConfig;
import com.transmodelo.conductor.MvpApplication;
import com.transmodelo.conductor.R;
import com.transmodelo.conductor.data.network.model.TripResponse;
import com.transmodelo.conductor.data.network.model.User;
import com.transmodelo.conductor.ui.activity.main.MainActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.transmodelo.conductor.MvpApplication.mLastKnownLocation;

public class NotificationDialog extends Activity implements NotificationIView {
    @BindView(R.id.close_btn)
    Button close_btn;
    @BindView(R.id.details)
    Button details;
    //    Unbinder unbinder;
    @BindView(R.id.parent_container)
    LinearLayout parent_container;
    @BindView(R.id.user_img_popup)
    CircleImageView imgUser;
    @BindView(R.id.user_name_popup)
    TextView lblUserName;
    //    @BindView(R.id.ratingUser)
//    AppCompatRatingBar ratingUser;
    @BindView(R.id.user_location)
    TextView lblLocationName;
    @BindView(R.id.ratingUser)
    AppCompatRatingBar ratingUser;

//    @BindView(R.id.lblDrop)
//    TextView lblDrop;
//    @BindView(R.id.lblScheduleDate)
//    TextView lblScheduleDate;

    //    DisplayMetrics metrics = getResources().getDisplayMetrics();
//    int screenWidth = (int) (metrics.widthPixels * 0.80);
    public static MediaPlayer mPlayer;

    NotificationPresenter presenter = new NotificationPresenter();

    /* private BroadcastReceiver myReceiver = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {

             HashMap<String, Object> params = new HashMap<>();
             if (mLastKnownLocation != null) {
                 params.put("latitude", mLastKnownLocation.getLatitude());
                 params.put("longitude", mLastKnownLocation.getLongitude());
             }
             presenter.getTrip(params);
         }
     };*/
    public NotificationDialog() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.notification_popup);
//        getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this);
        presenter.attachView(this);
        mPlayer = MediaPlayer.create(this, R.raw.alert_tone);
        mPlayer.start();

        HashMap<String, Object> params = new HashMap<>();
        if (mLastKnownLocation != null) {
            params.put("latitude", mLastKnownLocation.getLatitude());
            params.put("longitude", mLastKnownLocation.getLongitude());
        }
        presenter.getTrip(params);
        parent_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationDialog.this, MainActivity.class);
                startActivity(intent);
                mPlayer.stop();
                finish();
            }
        });
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationDialog.this, MainActivity.class);
                startActivity(intent);
                mPlayer.stop();
                finish();
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                finishAffinity();
            }
        });
    }

//    @SuppressLint("SetTextI18n")
//    void init() {
//        Request_ data = DATUM;
//        if (data != null) {
//            lblUserName.setText(String.format("%s %s", data.getUser().getFirstName(),
//                    data.getUser().getLastName()));
////            ratingUser.setRating(Float.parseFloat(data.getUser().getRating()));
//            if (data.getSAddress() != null && !data.getSAddress().isEmpty()
//                    || data.getDAddress() != null && !data.getDAddress().isEmpty()) {
//                lblLocationName.setText(data.getSAddress());
////                lblDrop.setText(data.getDAddress());
//            }
//            if (data.getUser().getPicture() != null)
//                Glide.with(this).load(BuildConfig.BASE_IMAGE_URL + data.getUser()
//                        .getPicture()).apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder)
//                        .dontAnimate().error(R.drawable.ic_user_placeholder)).into(imgUser);
//        }
//
//
//    }

    @Override
    public void onSuccess(TripResponse tripResponse) {
        String accountStatus = tripResponse.getAccountStatus();
        String serviceStatus = tripResponse.getServiceStatus();

        MvpApplication.tripResponse = tripResponse;
        if (tripResponse.getRequests() != null) {
            lblUserName.setText(String.format("%s %s", tripResponse.getRequests().get(0).getRequest().getUser().getFirstName(),
                    tripResponse.getRequests().get(0).getRequest().getUser().getLastName()));
            ratingUser.setRating(Float.parseFloat(tripResponse.getRequests().get(0).getRequest().getUser().getRating()));
            lblLocationName.setText(tripResponse.getRequests().get(0).getRequest().getSAddress());
        } else {
            // Toast.makeText(this,"data is null"+tripResponse.getAccountStatus(),Toast.LENGTH_SHORT).show();
        }


        if (tripResponse.getRequests().get(0).getRequest().getUser().getPicture() != null)
            Glide.with(this).load(BuildConfig.BASE_IMAGE_URL + tripResponse.getRequests().get(0).getRequest().getUser()
                    .getPicture()).apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder)
                    .dontAnimate().error(R.drawable.ic_user_placeholder)).into(imgUser);

    }

    @Override
    public Activity activity() {
        return null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onErrorRefreshToken(Throwable throwable) {

    }

    @Override
    public void onSuccessRefreshToken(User user) {

    }

    @Override
    public void onSuccessLogout(Object object) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mPlayer.isPlaying())
            mPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer.isPlaying())
            mPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying())
            mPlayer.stop();
    }
}
