package com.example.myapplication.mvp.ui.fragment;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.SPUtils;
import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseFragment;
import com.example.myapplication.mvp.base.BasePresenter;
import com.example.myapplication.mvp.service.DeskService;
import com.example.myapplication.mvp.ui.activity.LoginActivity;
import com.example.myapplication.mvp.ui.activity.MainActivity;
import com.gcssloop.widget.ArcSeekBar;
import com.mylhyl.circledialog.BaseCircleDialog;
import com.mylhyl.circledialog.CircleDialog;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import kotlin.BuilderInference;

import static android.content.ContentValues.TAG;
import static android.content.Context.VIBRATOR_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class TimeFragment extends BaseFragment {

    private static final int CHECK = 1;
    @BindView(R.id.arc_seek_bar)
    ArcSeekBar arcSeekBar;
    @BindView(R.id.time_countdownView)
    CountdownView timeCountdownView;
    @BindView(R.id.time_start)
    Button timeStart;
    @BindView(R.id.time_cancel)
    Button timeCancel;
    @BindView(R.id.timer)
    TextView timer;
    @BindView(R.id.select_btn)
    JellyToggleButton selectBtn;

    AlertDialog.Builder mBuilder;
    //????????????
   // AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Service.ALARM_SERVICE);

    long startTime = 3600000;
    private int currentBar = 3600000;
    private long time = 3600000;
    private CountDownTimer countDownTimer;
    private BaseCircleDialog dialogFragment;
    MediaPlayer mp;
    Vibrator vibrator;
    List<UsageStats> mStatsList = new ArrayList<>();
    int type = 0;

    @Override
    public int getViewId() {
        return R.layout.time_layout;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        setArcSeekBarListener();
        timeCountdownView.updateShow(time);
        selectBtn.setLeftBackgroundColor("#FFFFFF");
        selectBtn.setRightBackgroundColor("#A2EE47");
        selectBtn.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onStateChange(float process, State state, JellyToggleButton jtb) {
                if(state == State.LEFT){
                        type=0;
                }
                if(state == State.RIGHT){
                    mStatsList = getStatusData();
                    type = 1;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(getContext())) {

                            mBuilder = new AlertDialog.Builder(getContext());
                            mBuilder.setTitle("??????????????????????????????");
                            mBuilder.setMessage("??????????????????????????????????????????????????????????????????????????????????????????");
                            mBuilder.setCancelable(false);//??????????????????????????????
                            mBuilder.setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivityForResult(intent, 1);
                                }
                            });
                            mBuilder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    selectBtn.toggle();
                                    dialog.dismiss();
                                }
                            });
                            mBuilder.show();

                        }
                    }
                }
            }
        });

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    /**
    * bar???????????????
    * */
    public void setArcSeekBarListener(){
        arcSeekBar.setOnProgressChangeListener(new ArcSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(ArcSeekBar seekBar, int progress, boolean isUser) {
                Log.d("??????", String.valueOf(progress));
                time = (long) (7200000*progress*(0.01));
                timeCountdownView.updateShow(time);
            }
            @Override
            public void onStartTrackingTouch(ArcSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(ArcSeekBar seekBar) {

            }
        });
    }
    /**
    * ????????????????????????
    * */
    public void initCountdownView(){
            /**
            * ????????????
            * */

            timeCountdownView.setOnCountdownIntervalListener(1000, new CountdownView.OnCountdownIntervalListener() {
                @Override
                public void onInterval(CountdownView cv, long remainTime) {
                /*    currentBar = (int) (remainTime/7200000*100);
                    arcSeekBar.setProgress(currentBar);
                    Log.d("????????????", String.valueOf(remainTime));*/
                    Log.d("????????????", String.valueOf(remainTime));
                    Log.d("????????????", timeCountdownView.getHour()+"???"+timeCountdownView.getMinute()+"???"+timeCountdownView.getSecond()+"???");

                }
            });
            /**
            * ????????????????????????
            * */
            timeCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    arcSeekBar.setVisibility(View.VISIBLE);
                    timer.setText("Timer");
                }
            });
    }

    @OnClick({R.id.time_start,R.id.time_cancel})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.time_start:
                    countDownTimer = new CountDownTimer(time,1000) {
                        @Override
                        public  void  onTick(long millisUntilFinished) {
                                timeCountdownView.updateShow(millisUntilFinished);
                                Intent intent =new Intent(getActivity(),DeskService.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("hour",timeCountdownView.getHour());
                                bundle.putInt("minute",timeCountdownView.getMinute());
                                bundle.putInt("second",timeCountdownView.getSecond());
                                bundle.putInt("type",type);
                                intent.putExtra("time",bundle);
                                getActivity().startService(intent);

                        }
                        @Override
                        public void onFinish() {
                                cancel();
                                arcSeekBar.setVisibility(View.VISIBLE);
                                timer.setText("Timer");
                                timeCancel.setVisibility(View.GONE);
                                timeStart.setVisibility(View.VISIBLE);
                                timeCountdownView.updateShow(time);
                                selectBtn.setEnabled(true);
                                selectBtn.setClickable(true);
                                Intent intent1 = new Intent(getActivity(),DeskService.class);
                                getActivity().stopService(intent1);
                                Intent clickIntent2 = new Intent(getActivity(), MainActivity.class);
                                clickIntent2.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                startActivity(clickIntent2);
                            try {
                                startClock();
                                startShock();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            dialogFragment = new CircleDialog.Builder()
                                    //.setTypeface(typeface)
                                    .setTitle("??????????????????????????????")
                                    //.setManualClose(true)
                                    .configTitle(params -> params.isShowBottomDivider = true)
                                    .setWidth(0.8f)
                                    .setRadius(20)
                                    .setText("?????????????????????!")
                                    .setPositive("??????", null)
                                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            mp.stop();
                                            mp = null;
                                            vibrator.cancel();
                                            vibrator = null;
                                        }
                                    })
                                    .show(getFragmentManager());


                        }
                    };
                    initCountdownView();
                    countDownTimer.start();
                    arcSeekBar.setVisibility(View.GONE);
                    timer.setText("???????????????");
                    Log.d("????????????", String.valueOf(time));
                    timeStart.setVisibility(View.GONE);
                    timeCancel.setVisibility(View.VISIBLE);
                    Intent intent =new Intent(getActivity(),DeskService.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("hour",timeCountdownView.getHour());
                    bundle.putInt("minute",timeCountdownView.getMinute());
                    bundle.putInt("second",timeCountdownView.getSecond());
                    bundle.putInt("type",type);
                    intent.putExtra("time",bundle);
                    getActivity().startService(intent);
                    selectBtn.setEnabled(false);
                    selectBtn.setClickable(false);
                    break;

            case R.id.time_cancel:
                    countDownTimer.cancel();
                    countDownTimer = null;
                    arcSeekBar.setVisibility(View.VISIBLE);
                    timer.setText("Timer");
                    timeCancel.setVisibility(View.GONE);
                    timeStart.setVisibility(View.VISIBLE);
                    timeCountdownView.updateShow(time);
                    Intent intent1 = new Intent(getActivity(),DeskService.class);
                    getActivity().stopService(intent1);
                    selectBtn.setEnabled(true);
                    selectBtn.setClickable(true);
                break;


        }
    }

    private void startShock() {
        //???????????????????????????
        vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        //?????????????????????????????????
//				vibrator.vibrate(1000);
        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        //?????????????????????????????????-1???????????????0???????????????
        vibrator.vibrate( new long[]{1000,1000},0);
    }

    private void startClock() throws IOException {
        mp = new MediaPlayer();
        mp.setDataSource(getActivity(), RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));//????????????????????????????????????????????????????????????????????????
        mp.prepare();
        mp.start();
        mp.setLooping(true);
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
            calendar.add(Calendar.DAY_OF_WEEK,-1);
            long statt = calendar.getTimeInMillis();//????????????
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


    public String getLauncherTopApp(Context context, ActivityManager activityManager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } else {
            //5.0????????????????????????
            UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                }
            }
            if (!android.text.TextUtils.isEmpty(result)) {
                return result;
            }
        }
        return "";
    }


    private boolean isAllowed() {
        AppOpsManager ops = (AppOpsManager) getActivity().getSystemService(Context.APP_OPS_SERVICE);
        try {
            int op = 10021;
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]{int.class, int.class, String.class});
            Integer result = (Integer) method.invoke(ops, op, android.os.Process.myUid(), getActivity().getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;

        } catch (Exception e) {
            Log.e("TAG", "not support");
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
     /*   if (isAllowed()==false) {
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
            startActivityForResult(localIntent,CHECK);
        }*/

       /* if (isAllowed()==false) {

            mBuilder = new AlertDialog.Builder(getContext());
            mBuilder.setTitle("??????????????????????????????");
            mBuilder.setMessage("??????????????????????????????????????????????????????????????????????????????????????????");
            mBuilder.setCancelable(false);//??????????????????????????????
            mBuilder.setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //??????????????????
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                    startActivityForResult(localIntent,CHECK);
                }
            });
            mBuilder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectBtn.toggle();
                    dialog.dismiss();
                }
            });
            mBuilder.show();

        }*/
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            } else {
                //TODO do something you need
            }
        }
    }

    /**
     * ?????????????????????????????????????????????
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }



}
