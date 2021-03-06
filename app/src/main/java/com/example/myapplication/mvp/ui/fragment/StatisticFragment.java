package com.example.myapplication.mvp.ui.fragment;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseFragment;
import com.example.myapplication.mvp.base.BasePresenter;
import com.example.myapplication.mvp.model.entity.AppDataBean;
import com.example.myapplication.mvp.model.utils.DateTransUtils;
import com.example.myapplication.mvp.ui.activity.CalendarActivity;
import com.example.myapplication.mvp.ui.activity.StatisticDetailsActivity;
import com.example.myapplication.mvp.ui.adapter.StRecyclerViewAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;

public class StatisticFragment extends BaseFragment implements OnChartValueSelectedListener {

    private static final Object TAG = "?????????";
    @BindView(R.id.st_piechart)
    PieChart stPieChart;
    @BindView(R.id.st_recycler_view)
    RecyclerView stRecyclerView;

  /*  @BindView(R.id.st_refresh)
    SwipeRefreshLayout stRefresh;*/

    private List<UsageStats> mList;

    private Typeface tf;

    //????????????????????????????????????
    String name1;
    String name2;
    String name3;
    String name4;
    Drawable icon;
    Drawable icon2;
    Drawable icon3;
    Drawable icon4;
    String time1 = "null";
    String time2 = "null";
    String time3 = "null";
    String time4 = "null";
    float t1 = 20;
    float t2 = 30;
    float t3 = 40;
    float t4 = 10;
    float sum;
    private ApplicationInfo mMAppInfo;
    private ApplicationInfo mMAppInfo1;
    private ApplicationInfo mMAppInfo2;
    private ApplicationInfo mMAppInfo3;
    String sumTime;

    StRecyclerViewAdapter stRecyclerViewAdapter;

    //?????????bean???????????????recyclerView????????????
    List<AppDataBean> dataList = new ArrayList<>();
    AppDataBean mAppDataBean = new AppDataBean();


    @Override
    public int getViewId() {
        return R.layout.statistic_layout;
    }

    @Override
    public void initData() {

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {
        UsageStats usageStats;
        PackageManager mPm = getContext().getPackageManager();
        List<UsageStats> list = getStatusData();
        for(int i = 0; i< list.size()-1; i++){
            for(int j = 0; j< list.size()-i-1; j++){
                if(list.get(j+1).getTotalTimeInForeground()> list.get(j).getTotalTimeInForeground()){
                    usageStats = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1 ,usageStats);
                }
            }
        }
        for(int i = 0;i<10;i++){
            try {
                AppDataBean appDataBean = new AppDataBean();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    appDataBean.setLasttime( list.get(i).getLastTimeUsed());
                }
                appDataBean.setAppName(AppUtils.getAppName(list.get(i).getPackageName()));
                appDataBean.setIcon(AppUtils.getAppIcon(list.get(i).getPackageName()));
                appDataBean.setUseTime(getTime((int) list.get(i).getTotalTimeInForeground()));
                appDataBean.setUseCount(String.valueOf(getLaunchCount(list.get(i))));
                appDataBean.setPackagename(list.get(i).getPackageName());
                Log.d("recyclerview??????",AppUtils.getAppName(list.get(i).getPackageName()));
                Log.d("recyclerview??????",getTime((int) list.get(i).getTotalTimeInForeground()));
                Log.d("recyclerview??????",String.valueOf(getLaunchCount(list.get(i))));
                dataList.add(appDataBean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        initChart();
        initAdapter();

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
    /*
    * ??????????????????
    * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void  initChart(){

        UsageStats usageStats;
        //???list????????????
        mList = getStatusData();
        if(mList!=null || mList.size()!=0 ){
            for(int i = 0; i< mList.size()-1; i++){
                for(int j = 0; j< mList.size()-i-1; j++){
                    if(mList.get(j+1).getTotalTimeInForeground()> mList.get(j).getTotalTimeInForeground()){
                        usageStats = mList.get(j);
                        mList.set(j, mList.get(j+1));
                        mList.set(j+1 ,usageStats);
                    }
                }
            }
            for(UsageStats usageStats1: mList){
                Log.d("??????",usageStats1.getPackageName()+"_____"+usageStats1.getTotalTimeInForeground());
            }
            String name =  AppUtils.getAppName(mList.get(0).getPackageName());
            time1 = getTime((int) mList.get(0).getTotalTimeInForeground()) ;
            time2 = getTime((int) mList.get(1).getTotalTimeInForeground()) ;
            time3 = getTime((int) mList.get(2).getTotalTimeInForeground()) ;
            time4 = getTime((int) mList.get(3).getTotalTimeInForeground()) ;
            sum = mList.get(0).getTotalTimeInForeground()+mList.get(1).getTotalTimeInForeground()+mList.get(2).getTotalTimeInForeground()+mList.get(3).getTotalTimeInForeground();
            t1 =  mList.get(0).getTotalTimeInForeground()/sum*100;
            t2 =  mList.get(1).getTotalTimeInForeground()/sum*100;
            t3 =  mList.get(2).getTotalTimeInForeground()/sum*100;
            t4 =  mList.get(3).getTotalTimeInForeground()/sum*100;
            try {
                PackageManager pm =getContext().getPackageManager();
                ApplicationInfo app = pm.getApplicationInfo( mList.get(0).getPackageName(),PackageManager.GET_META_DATA);
                Log.d("11111", String.valueOf(pm.getApplicationLabel(app)));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Log.d("11111", mList.get(0).getPackageName());
            Log.d("11111",AppUtils.getAppName());

        }
        sumTime = getTime((int) sum);

        stPieChart.setUsePercentValues(true);
        stPieChart.getDescription().setEnabled(false);
        stPieChart.setExtraOffsets(5, 10, 5, 5);

       stPieChart.setDragDecelerationFrictionCoef(0.95f);
        //??????????????????
       stPieChart.setCenterText(sumTime);

       stPieChart.setDrawHoleEnabled(true);
       stPieChart.setHoleColor(Color.WHITE);

       stPieChart.setTransparentCircleColor(Color.WHITE);
       stPieChart.setTransparentCircleAlpha(110);

       stPieChart.setHoleRadius(58f);
       stPieChart.setTransparentCircleRadius(61f);

       stPieChart.setDrawCenterText(true);

       stPieChart.setRotationAngle(0);
        // ????????????
       stPieChart.setRotationEnabled(true);
       stPieChart.setHighlightPerTapEnabled(true);

        //?????????????????????
       stPieChart.setUsePercentValues(true);

         //????????????
       stPieChart.setOnChartValueSelectedListener(this);
        name1 = AppUtils.getAppName(mList.get(0).getPackageName());
        name2 = AppUtils.getAppName(mList.get(1).getPackageName());
        name3 = AppUtils.getAppName(mList.get(2).getPackageName());
        name4 = AppUtils.getAppName(mList.get(3).getPackageName());
        icon = AppUtils.getAppIcon(mList.get(0).getPackageName());
        icon = AppUtils.getAppIcon(mList.get(1).getPackageName());
        icon = AppUtils.getAppIcon(mList.get(2).getPackageName());
        icon = AppUtils.getAppIcon(mList.get(3).getPackageName());
        //????????????
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(mList.get(0).getTotalTimeInForeground(), name1));
        entries.add(new PieEntry(mList.get(1).getTotalTimeInForeground(), name2));
        entries.add(new PieEntry(mList.get(2).getTotalTimeInForeground(), name3));
        entries.add(new PieEntry(mList.get(3).getTotalTimeInForeground(), name4));

        setData(entries);

        stPieChart.animateXY(1400, 1400);

        Legend l = stPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

       stPieChart.setEntryLabelColor(Color.WHITE);
       stPieChart.setEntryLabelTextSize(12f);



    }
    /*
    * ????????????????????????
    * */
    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
    /*
    * ????????????????????????
    * */
    private void setData(ArrayList<PieEntry> entries){
        PieDataSet dataSet = new PieDataSet(entries, "??????????????????");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        //???????????????
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        stPieChart.setData(data);

        stPieChart.highlightValues(null);
        //??????
        stPieChart.invalidate();
    }
    /*
    * ??????recyclerview???adapter
    * */
    private void initAdapter(){
        List list = new ArrayList();
        list.add(new Object());
        stRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         stRecyclerViewAdapter = new StRecyclerViewAdapter(dataList);
        stRecyclerViewAdapter.setOnItemClick(new StRecyclerViewAdapter.OnItemClick() {
            @Override
            public void click(AppDataBean appDataBean) {
                Intent intent = new Intent(getActivity(), StatisticDetailsActivity.class);
                intent.putExtra("lasttime",appDataBean.getLasttime());
                intent.putExtra("packagename",appDataBean.getPackagename());
                startActivity(intent);
            }
        });
        stRecyclerView.setAdapter(stRecyclerViewAdapter);
        //??????????????????
        /*stRefresh.setRefreshing(false);*/
    }


    /**
     * ??????????????????????????????
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<UsageStats> getStatusData(){
        if(checkUsagePremission()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); //???????????????
            long endTime = calendar.getTimeInMillis();//????????????
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            long statt = calendar.getTimeInMillis();//????????????
            //long statt = DateTransUtils.getZeroClockTimestamp(endTime);//????????????
            UsageStatsManager m = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> list = m.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY,statt,endTime);
            //????????????????????????app?????????
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
    /**
     * ??????????????????????????????
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<UsageStats> getStatusData2(){
        if(checkUsagePremission()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); //???????????????
            long endTime = calendar.getTimeInMillis();//????????????
            calendar.add(Calendar.DAY_OF_WEEK,-1);
            long statt = calendar.getTimeInMillis();//????????????
            //long statt = DateTransUtils.getZeroClockTimestamp(endTime);//????????????
            UsageStatsManager m = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> list = m.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY,statt,endTime);
            //????????????????????????app?????????
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

    //?????????????????????UsagePremission??????
    public boolean checkUsagePremission(){
        AppOpsManager appOps = (AppOpsManager) getContext()
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), getContext().getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return  granted;
    }

    public static String getTime(int second) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(second);
        return hms;
    }

    //???????????????????????????app?????????????????????
    private int getLaunchCount(UsageStats usageStats) throws IllegalAccessException {
        Field field = null;
        try {
            field = usageStats.getClass().getDeclaredField("mLaunchCount");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return (int) field.get(usageStats);
    }







}
