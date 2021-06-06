package com.example.myapplication.mvp.model.entity;

import android.print.PrinterCapabilitiesInfo;

import com.haibin.calendarview.Calendar;

import org.litepal.crud.LitePalSupport;

public class MemoRandumBean extends LitePalSupport {

    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //地点
    private String place;
    //备注
    private String remark;
    //事件标题
    private String title;
    //email作为判断
    private String email;
    //年月日
    private Calendar mCalendar;
    //当天时间
    private String day;
    //是否为重要事件
    private int condition;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

    public void setCalendar(Calendar calendar) {
        mCalendar = calendar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
