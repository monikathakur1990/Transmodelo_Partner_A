package com.transmodelo.conductor.ui.activity.welcome;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.transmodelo.conductor.BuildConfig;
import com.transmodelo.conductor.R;
import com.transmodelo.conductor.base.BaseActivity;
import com.transmodelo.conductor.common.Constants;
import com.transmodelo.conductor.common.SharedHelper;
import com.transmodelo.conductor.common.Utilities;
import com.transmodelo.conductor.data.network.model.CheckVersion;
import com.transmodelo.conductor.data.network.model.Walkthrough;
import com.transmodelo.conductor.ui.activity.email.EmailActivity;
import com.transmodelo.conductor.ui.activity.main.MainActivity;
import com.transmodelo.conductor.ui.activity.regsiter.RegisterActivity;
import com.transmodelo.conductor.ui.activity.sociallogin.SocialLoginActivity;
import com.bumptech.glide.Glide;
import com.transmodelo.conductor.ui.activity.splash.SplashActivity;
import com.transmodelo.conductor.ui.activity.splash.SplashPresenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity implements
        WelcomeIView, ViewPager.OnPageChangeListener {


    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;
    @BindView(R.id.llLoginContainer)
    LinearLayout layoutLogin;
    @BindView(R.id.sign_in)
    Button signIn;
    @BindView(R.id.sign_up)
    Button signUp;
    @BindView(R.id.social_login)
    TextView socialLogin;

    private int dotsCount;
    private ImageView[] dots;
    private MyViewPagerAdapter adapter;

    private WelcomePresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        presenter = new WelcomePresenter();
        presenter.attachView(this);
        checkVersion();

        ButterKnife.bind(this);

        WelcomePresenter presenter = new WelcomePresenter();
        presenter.attachView(this);
        List<Walkthrough> list = new ArrayList<>();
        list.add(new Walkthrough(R.drawable.bg_walk_one,
                getString(R.string.walk_1), getString(R.string.walk_1_description)));
        list.add(new Walkthrough(R.drawable.bg_walk_two,
                getString(R.string.walk_2), getString(R.string.walk_2_description)));
        list.add(new Walkthrough(R.drawable.bg_walk_three,
                getString(R.string.walk_3), getString(R.string.walk_3_description)));

       /* screens = new int[]{
                R.layout.bg_walk_one,
                R.layout.bg_walk_two,
                R.layout.bg_walk_three
        };*/

//        adapter = new MyViewPagerAdapter(this,list);
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0);
//        viewPager.addOnPageChangeListener(this);
//        addBottomDots();

        Utilities.printV("TOKEN===>",    SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN));
        Utilities.printV("TOKEN ID===>", SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID));

        layoutLogin.setVisibility(View.GONE);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    private void checkVersion() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("version", BuildConfig.VERSION_CODE);
        map.put("device_type", BuildConfig.DEVICE_TYPE);
        map.put("sender", "provider");
        presenter.checkVersion(map);

        Utilities.printV("FCM TOKEN ID===>", SharedHelper.getKeyFCM(this, "device_id"));
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_unselected));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_selected));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void addBottomDots() {
        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];
        if (dotsCount == 0)
            return;

        layoutDots.removeAllViews();

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(7, 7, 7, 7);

            layoutDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.ic_dot_selected));
    }

    @OnClick({R.id.sign_in, R.id.sign_up, R.id.social_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_in:
                startActivity(new Intent(this, EmailActivity.class));
                break;
            case R.id.sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.social_login:
                startActivity(new Intent(this, SocialLoginActivity.class));
                break;
        }
    }

    @Override
    public void verifyAppInstalled() {
        checkUserAppInstalled();
    }

    @Override
    public void onSuccess(Object user) {

    }

    private void checkUserAppInstalled() {
        if (Utilities.isPackageExisted(WelcomeActivity.this, Constants.userAppPackageName))
            showWarningAlert(getString(R.string.user_provider_app_warning));
        else redirectToScreen();
    }
    private void showWarningAlert(String message) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WelcomeActivity.this);
            alertDialogBuilder
                    .setTitle(getResources().getString(R.string.warning))
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.continue_app),
                            (dialog, id) -> redirectToScreen());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void redirectToScreen() {
        if (SharedHelper.getKey(this, Constants.SharedPref.LOGGGED_IN).equalsIgnoreCase("true"))
            startActivity(new Intent(activity(), MainActivity.class));
        else
            layoutLogin.setVisibility(View.VISIBLE);
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        List<Walkthrough> list;
        Context context;

        MyViewPagerAdapter(Context context, List<Walkthrough> list) {
            this.list = list;
            this.context = context;
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item, container, false);
            Walkthrough walk = list.get(position);

            TextView title = itemView.findViewById(R.id.title);
            TextView description = itemView.findViewById(R.id.description);
            ImageView imageView = itemView.findViewById(R.id.img_pager_item);

            title.setText(walk.title);
            description.setText(walk.description);
            Glide.with(getApplicationContext()).load(walk.drawable).into(imageView);
            container.addView(itemView);

            return itemView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position,
                                @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
    @Override
    public void onSuccess(CheckVersion version) {
        try {
            Utilities.printV("jsonObj===>", version.getForceUpdate() + "");
            if (!version.getForceUpdate()) presenter.handlerCall();
            else showAlertDialog(version.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCheckVersionError(Throwable e) {
        hideLoading();
        presenter.handlerCall();
    }
    private void showAlertDialog(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.update_version_message));
        builder.setPositiveButton("Update", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
        builder.show();
    }
}
