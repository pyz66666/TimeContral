package com.example.myapplication.mvp.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.AppUtils;
import com.example.myapplication.R;
import com.example.myapplication.mvp.receiver.NotificationClickReceiver;
import com.example.myapplication.mvp.ui.activity.AlertActivity;
import com.example.myapplication.mvp.ui.activity.MainActivity;
import com.mylhyl.circledialog.CircleDialog;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

//前台服务

public class DeskService extends Service {
    private static final int NOTICE_ID = 100;
    private static final String TAG = "DemoService";
    private int hour;
    private int minute;
    private int second;
    private int type;
    String CHANNEL_ONE_ID;
    String CHANNEL_ONE_NAME;
    AlertDialog dialog = null;
    AlertDialog dialog2 = null;

    private AlertDialog.Builder builder;
    View layout;
    private LayoutInflater inflater;

    String topActivityName;

    Dialog mPopupWindow;

    public DeskService() {
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Bundle bundle =intent.getBundleExtra("time");
        hour = bundle.getInt("hour");
        minute = bundle.getInt("minute");
        second = bundle.getInt("second");
        type = bundle.getInt("type");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        CHANNEL_ONE_ID = "com.primedu.cn";
        CHANNEL_ONE_NAME = "Channel One";
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }

        //判断api是否大于18,弹出一个可见通知
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            Notification.Builder builder = new Notification.Builder(this);
            builder.setChannelId(CHANNEL_ONE_ID);
            builder.setSmallIcon(R.mipmap.timesquare);
            builder.setContentTitle("TimeControl");
            builder.setContentText("倒计时剩余："+hour);
            Log.d("MYService","service启动了");
            startForeground(NOTICE_ID,builder.build());
        }else {
            startForeground(NOTICE_ID,new Notification());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //若service被终止，可以重启service
        Intent clickIntent = new Intent(this, MainActivity.class);
        clickIntent.putExtra("id",1);
        PendingIntent clickInt = PendingIntent.getActivity(this,0,clickIntent,FLAG_UPDATE_CURRENT);
        Bundle bundle =intent.getBundleExtra("time");
        hour = bundle.getInt("hour");
        minute = bundle.getInt("minute");
        second = bundle.getInt("second");
        type = bundle.getInt("type");

                if(type == 1){
                    topActivityName = getLauncherTopApp(DeskService.this);
                    if(!topActivityName.trim().equals("com.example.myapplication") && !topActivityName.trim().equals("com.miui.home")  ){
                        Log.d("这是其他应用",topActivityName);
                        String name = AppUtils.getAppName(topActivityName);
                        if(dialog ==null || !dialog.isShowing()){
                            dialog = new AlertDialog.Builder(getApplicationContext(),R.style.AlertDialogCustom).setTitle("TimeContral")
                                    .setMessage(name + "让你分心了？坚持下去，点击按钮返回TimeContral")
                                    .setCancelable(false)
                                    .setPositiveButton("返回TimeContral", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent clickIntent2 = new Intent(getApplicationContext(), MainActivity.class);
                                            clickIntent2.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(clickIntent2);
                                        }
                                    })
                                    .create();

                            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                            dialog.show();
                        }

                       /* Intent it =new Intent(Intent.ACTION_MAIN);
                        ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.mvp.ui.activity.MainActivity");
                        it.setComponent(componentName);
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);*/
                    }
                }


        Log.d("MYService", String.valueOf(hour)+minute+second);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setChannelId(CHANNEL_ONE_ID);
        builder.setSmallIcon(R.mipmap.timesquare);
        builder.setContentTitle("TimeControl");
        builder.setContentText("倒计时剩余："+hour+"时"+minute+"分"+second+"秒");
        builder.setContentIntent(clickInt);
        Log.d("MYService","service启动了");
        startForeground(NOTICE_ID,builder.build());
        Log.d("MYService","service运行");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(NOTICE_ID);
        }
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (!isScreenOn) {

        }else {
            if(!getLauncherTopApp(DeskService.this).trim().equals("com.example.myapplication")){
                if(dialog2 ==null || !dialog2.isShowing()){
                    dialog2 = new AlertDialog.Builder(getApplicationContext(),R.style.AlertDialogCustom).setTitle("TimeContral")
                            .setMessage("恭喜你，成功坚持下来了，休息一下奖励自己吧！")
                            .setCancelable(false)
                            .setPositiveButton("返回TimeContral", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent clickIntent2 = new Intent(getApplicationContext(), MainActivity.class);
                                    clickIntent2.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(clickIntent2);
                                }
                            })
                            .create();

                    dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                    dialog2.show();
                }
            }
        }

        Log.d(TAG,"前台service被杀死了");
        Log.d("MYService","service被杀死了");
      /*  Intent intent = new Intent(getApplicationContext(),DeskService.class);
        startService(intent);*/
    }




    //获取最上层的activity
    public String getLauncherTopApp(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager =  (ActivityManager)(context.getSystemService(android.content.Context.ACTIVITY_SERVICE ));
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } else {
            //5.0
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

    public void showDialog(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = new AlertDialog.Builder(getApplicationContext(),R.style.AlertDialogCustom).setTitle("title")
                        .setMessage("这是一个由service弹出的对话框")
                        .setCancelable(false)
                        .setPositiveButton("button confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .create();

                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                dialog.show();
            }
        }, 3 * 1000);

    }

}