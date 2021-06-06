package com.example.myapplication.mvp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseActivity;
import com.example.myapplication.mvp.base.BasePresenter;

import org.litepal.tablemanager.Connector;

import java.sql.Connection;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.skip)
    TextView skip;

    private Timer timer;
    private TimerTask task;
    private SQLiteDatabase db;

    @Override
    public void initView() {

        setStatusBarFullTransparent();
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_splash;
    }

    @Override
    public void initDate() {
        timerTask();
        skip.setOnClickListener(this);
        //创建数据库
        db = Connector.getDatabase();
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
        switch(v.getId()){
            case R.id.skip:
                task.cancel();
                jumpToActivity();
                finish();
                break;
            default:
                break;
    }
    }


    /*
     * 跳转到主界面或者登录界面
     * */
    public void jumpToActivity(){
        if(TextUtils.isEmpty(SPUtils.getInstance().getString("email"))){
            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            //若缓存中有email，则直接进入登录界面，并将缓存中的email传入
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            intent.putExtra("user_email",SPUtils.getInstance().getString("email"));
            startActivity(intent);
            finish();
        }

    }

    private void timerTask() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                jumpToActivity();
                /*Message message = mHandler.obtainMessage(1);
                mHandler.sendMessage(message);*/
            }
        };
        timer.schedule(task, 5000);
    }


}
