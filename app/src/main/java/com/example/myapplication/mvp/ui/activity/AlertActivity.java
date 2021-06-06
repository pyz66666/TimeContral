package com.example.myapplication.mvp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.myapplication.R;

public class AlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED//锁屏状态下显示
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD//解锁
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON//保持屏幕常量
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);//打开屏幕

        //下面就是根据自己的跟你需求来写，跟写一个Activity一样的
        //拿到传过来的数据


    }
}