package com.example.myapplication.mvp.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseActivity;
import com.example.myapplication.mvp.base.BasePresenter;
import com.example.myapplication.mvp.model.entity.AppDataBean;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sdsmdg.tastytoast.TastyToast;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static org.litepal.LitePalApplication.getContext;

public class StatisticDetailsActivity extends BaseActivity {

    @BindView(R.id.title_toolbar)
    CommonTitleBar mCommonTitleBar;
    @BindView(R.id.details_icon)
    ImageView detailIcon;
    @BindView(R.id.last_use_time)
    TextView lastUseTime;
    @BindView(R.id.package_name)
    TextView packageName;
    @BindView(R.id.version_number)
    TextView versionNumber;
    @BindView(R.id.version_code)
    TextView versionCode;
    @BindView(R.id.system_app)
    TextView systemApp;
    @BindView(R.id.root)
    TextView root;
    @BindView(R.id.apk_path)
    TextView apkPath;
    @BindView(R.id.sha1_sign)
    TextView sha1Sign;
    @BindView(R.id.sha256_sign)
    TextView sha256Sign;
    @BindView(R.id.md5_sign)
    TextView md5Sign;
    ClipboardManager cm =(ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    ClipboardManager cm1 =(ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    ClipboardManager cm2 =(ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    ClipboardManager cm3 =(ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    ClipboardManager cm4 =(ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);

    String packagename;
    long lasttime;

    List<UsageStats> mList = new ArrayList<>();
    UsageStats getApp ;

    AppDataBean mAppDataBean ;

    @Override
    public void initView() {
        initToolBar();

    }

    @Override
    public void initListener() {

    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_statistic_details;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initDate() {
        Intent intent = getIntent();
        packagename =  intent.getStringExtra("packagename");
        lasttime = intent.getLongExtra("lasttime",0);
        Glide.with(this).load(AppUtils.getAppIcon(packagename)).into(detailIcon);
        lastUseTime.setText(getDate(lasttime));
        packageName.setText(packagename);
        versionNumber.setText(AppUtils.getAppVersionName(packagename).toString());
        versionCode.setText(String.valueOf(AppUtils.getAppVersionCode(packagename)));
        if (AppUtils.isAppSystem(packagename)) {
            systemApp.setText("true");
        }else {
            systemApp.setText("false");
        }
        if (AppUtils.isAppRoot()) {
            root.setText("true");
        }else {
            root.setText("false");
        }
        apkPath.setText(AppUtils.getAppPath(packagename));
        sha1Sign.setText(AppUtils.getAppSignatureSHA1(packagename));
        sha256Sign.setText(AppUtils.getAppSignatureSHA256(packagename));
        md5Sign.setText(AppUtils.getAppSignatureMD5(packagename));
        detailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.launchApp(packagename);
            }
        });
        mList = getStatusData();
        for (UsageStats usageStats : mList){
            if (usageStats.getPackageName().equals(packagename)) {
                getApp = usageStats;
                return;
            }
        }
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void destory() {

    }


    @OnClick({R.id.md5_sign,R.id.apk_path,R.id.sha256_sign,R.id.sha1_sign,R.id.package_name})
    public void onItemClick(View v){
        switch (v.getId()){

            case R.id.md5_sign:
                cm.setText(md5Sign.getText().toString());
                TastyToast.makeText(this,"已复制到粘贴板",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();
                break;
            case R.id.apk_path:
                cm1.setText(md5Sign.getText().toString());
                TastyToast.makeText(this,"已复制到粘贴板",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();
                break;
            case R.id.sha1_sign:
                cm2.setText(sha1Sign.getText().toString());
                TastyToast.makeText(this,"已复制到粘贴板",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();
                break;
            case R.id.sha256_sign:
                cm3.setText(sha256Sign.getText().toString());
                TastyToast.makeText(this,"已复制到粘贴板",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();
                break;
            case R.id.package_name:
                cm4.setText(packageName.getText().toString());
                TastyToast.makeText(this,"已复制到粘贴板",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    //初始化标题栏,增加监听
    private void initToolBar() {
        mCommonTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                //点击左边图片
                if(action == CommonTitleBar.ACTION_LEFT_BUTTON){
                    finish();
                }
            }
        });
    }




    public String getDate(long time){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new Date(time);
        String str = sdf.format(date);
        return str;
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
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            long statt = calendar.getTimeInMillis();//结束时间
            //long statt = DateTransUtils.getZeroClockTimestamp(endTime);//结束时间
            UsageStatsManager m = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> list = m.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY,statt,endTime);
            //通过包名获取其他app的图片
            PackageManager pm = getContext().getPackageManager();
            try {
                ApplicationInfo appInfo = pm.getApplicationInfo(list.get(0).getPackageName(),PackageManager.GET_META_DATA);
                Drawable icon = pm.getApplicationIcon(appInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return  list;
        }
        return null;

    }

    //判断有没有获取UsagePremission权限
    public boolean checkUsagePremission(){
        AppOpsManager appOps = (AppOpsManager) getContext()
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), getContext().getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return  granted;
    }

}