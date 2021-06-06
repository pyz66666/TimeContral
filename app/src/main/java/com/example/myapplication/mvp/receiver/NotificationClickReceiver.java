package com.example.myapplication.mvp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.mvp.ui.activity.MainActivity;
import com.sdsmdg.tastytoast.TastyToast;

import static com.example.myapplication.mvp.ui.activity.CalendarAddActivity.ALARM_SINGLE_ACTION;

public class NotificationClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(ALARM_SINGLE_ACTION)){
            Log.d("设置日期","成功成功成功");

        }if(action.equals("test")){
            Log.d("接受成功","success");
        }

    }
}
