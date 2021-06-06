package com.example.myapplication.mvp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseActivity;
import com.example.myapplication.mvp.base.BasePresenter;
import com.google.android.material.tabs.TabLayout;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import butterknife.BindView;

public class AppDetailsActivity extends BaseActivity {

    @BindView(R.id.app_details_icon)
    ImageView appDetailsIcon;
    @BindView(R.id.app_name)
    TextView appName;
    @BindView(R.id.app_package_name)
    TextView appPackageName;
    @BindView(R.id.app_route)
    TextView appRoute;
    @BindView(R.id.title_toolbar)
    CommonTitleBar titleToolbar;

    @Override
    public void initView() {
        initToolBar();
        appDetailsIcon.setImageDrawable(AppUtils.getAppIcon());
        appName.setText(AppUtils.getAppName());
        appPackageName.setText(AppUtils.getAppPackageName());
        appRoute.setText(AppUtils.getAppPath());


    }

    @Override
    public void initListener() {

    }

    @Override
    public int getContentViewID() {
        return R.layout.app_details;
    }

    @Override
    public void initDate() {

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void destory() {

    }

    @Override
    public void onClick(View v) {

    }

    //初始化标题栏,增加监听
    private void initToolBar() {
        titleToolbar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                //点击左边图片
                if(action == CommonTitleBar.ACTION_LEFT_BUTTON){
                    finish();
                }
            }
        });
    }
}