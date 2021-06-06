package com.example.myapplication.mvp.ui.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseActivity;
import com.example.myapplication.mvp.base.BasePresenter;
import com.example.myapplication.mvp.ui.adapter.MainPageAdapter;
import com.example.myapplication.mvp.ui.fragment.StatisticFragment;
import com.example.myapplication.mvp.ui.fragment.TimeFragment;
import com.example.myapplication.mvp.ui.fragment.UserFragment;
import com.google.android.material.navigation.NavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final int MODIFYDATA = 1;
    @BindView(R.id.main_navigation_bar)
    BottomNavigationBar mainNavigationBar;
    @BindView(R.id.main_vp)
    ViewPager mainVp;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.title_toolbar)
    CommonTitleBar mCommonTitleBar;

    List<Fragment> list;
    FragmentManager fragmentManager;

    AlertDialog.Builder mBuilder;
    private ImageView mImageView;
    private TextView mUserName;
    private TextView mSign;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {
        initDrawerLayout();
        setMainNavigationBar();
        isAllowed();
        Log.d("后台权限", String.valueOf(isAllowed()));
        fragmentManager = getSupportFragmentManager();
        initFragment();
        mCommonTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                //点击左边图片
                if(action == CommonTitleBar.ACTION_LEFT_BUTTON){
                    drawer.openDrawer(GravityCompat.START);
                }
                //点击右边图片
                if(action == CommonTitleBar.ACTION_RIGHT_BUTTON){
                    Intent intent = new Intent(MainActivity.this,CalendarActivity.class);
                    startActivity(intent);
                }
                //点击中间文字
                if(action == CommonTitleBar.ACTION_CENTER_TEXT){

                }
            }
        });
    }
    //定义侧边栏
    private void initDrawerLayout() {
        navView.inflateHeaderView(R.layout.nav_head_layout);
        View headerView = navView.getHeaderView(0);
        TextView userModify = headerView.findViewById(R.id.user_modify);
        TextView appDetails = headerView.findViewById(R.id.app_details);
        TextView checkUpdate = headerView.findViewById(R.id.check_update);
        TextView clearSp = headerView.findViewById(R.id.clear_sp);
        mImageView = headerView.findViewById(R.id.headpic);
        mUserName = headerView.findViewById(R.id.userName);
        mSign = headerView.findViewById(R.id.sign);
        if(!SPUtils.getInstance().getString("imageUri").isEmpty()){
            Glide.with(MainActivity.this).load(SPUtils.getInstance().getString("imageUri")).into(mImageView);
        }
        if(!SPUtils.getInstance().getString("userName").isEmpty()){
            mUserName.setText(SPUtils.getInstance().getString("userName"));
        }
        if(!SPUtils.getInstance().getString("sign").isEmpty()){
            mSign.setText(SPUtils.getInstance().getString("sign"));
        }

        //个人信息设置点击时间
        userModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,UserModifyActivity.class);
                    startActivityForResult(intent,MODIFYDATA);
            }
        });
        //App详情点击事件
        appDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AppDetailsActivity.class);
                startActivity(intent);
            }
        });
        //检查版本点击事件
        checkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //清楚缓存点击事件
        clearSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("清除应用缓存");
                mBuilder.setMessage("清除缓存后将会自动跳转到登录界面，且下次进入app要重新输入邮箱和密码");
                mBuilder.setCancelable(false);//点击空白处弹窗不消失
                mBuilder.setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //清除所有缓存
                        SPUtils.getInstance().clear();
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.show();
            }
        });
    }

    @Override
    public void initListener() {
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_main;
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
        switch (v.getId()){

        }
    }
    /*
    * 设置底部导航栏
    * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setMainNavigationBar(){
        mainNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.statistics,"statistics"))
                .addItem(new BottomNavigationItem(R.drawable.timer,"time"))
                .setActiveColor(R.color.end)
                .setMode(BottomNavigationBar.MODE_SHIFTING)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .initialise();
        mainNavigationBar.setOutlineProvider(null);
        mainNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mainVp.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }
    /**
    * 获取其他程序使用时间
    * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<UsageStats> getStatusData(){
        if(checkUsagePremission()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); //现在的日期
            long endTime = calendar.getTimeInMillis();//开始时间
            calendar.add(Calendar.DAY_OF_WEEK,-1);
            long statt = calendar.getTimeInMillis();//结束时间
            UsageStatsManager m = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> list = m.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY,statt,endTime);
           /* //通过包名获取其他app的图片
            PackageManager pm = getPackageManager();
            try {
                ApplicationInfo appInfo = pm.getApplicationInfo(list.get(0).getPackageName(),PackageManager.GET_META_DATA);
                Drawable icon = pm.getApplicationIcon(appInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }*/
            return  list;
        }
      return null;

    }

    /*
    * 初始化fragment
    * */
    public void initFragment(){
        list = new ArrayList<>();
        list.add(new StatisticFragment());
        list.add(new TimeFragment());
        mainVp.setAdapter(new MainPageAdapter(fragmentManager,0,list));
        mainVp.addOnPageChangeListener(this);
        mainVp.setCurrentItem(0);
    }
    /*
    * ViewPage监听器的三P个方法
    * */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
            mainNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //判断有没有获取UsagePremission权限
    public boolean checkUsagePremission(){
        AppOpsManager appOps = (AppOpsManager) this
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), this.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return  granted;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MODIFYDATA){
            if(resultCode == RESULT_OK){
                if(!SPUtils.getInstance().getString("imageUri").isEmpty()){
                    Glide.with(MainActivity.this).load(SPUtils.getInstance().getString("imageUri")).into(mImageView);
                }
                if(!SPUtils.getInstance().getString("userName").isEmpty()){
                    mUserName.setText(SPUtils.getInstance().getString("userName"));
                }
                if(!SPUtils.getInstance().getString("sign").isEmpty()){
                    mSign.setText(SPUtils.getInstance().getString("sign"));
                }
            }
        }
    }

    private boolean isAllowed() {
        AppOpsManager ops = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        try {
            int op = 10021;
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]{int.class, int.class, String.class});
            Integer result = (Integer) method.invoke(ops, op, android.os.Process.myUid(), getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;

        } catch (Exception e) {
            Log.e("TAG", "not support");
        }
        return false;
    }

}
