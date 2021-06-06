package com.example.myapplication.mvp.model.entity;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppDataBean implements Serializable {

    //app名称
    private String appName;
    //app七天使用时间
    private String useTime;
    //app图标
    private Drawable icon;
    //app使用次数
    private String useCount;
    //包名
    private String packagename;
    //最近使用时间
    private long lasttime;

    public long getLasttime() {
        return lasttime;
    }

    public void setLasttime(long lasttime) {
        this.lasttime = lasttime;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getUseCount() {
        return useCount;
    }

    public void setUseCount(String useCount) {
        this.useCount = useCount;
    }
}
