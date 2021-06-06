package com.example.myapplication.mvp.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseActivity;
import com.example.myapplication.mvp.base.BasePresenter;

import com.example.myapplication.mvp.model.entity.MemoRandumBean;
import com.example.myapplication.mvp.receiver.NotificationClickReceiver;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;
import com.sdsmdg.tastytoast.TastyToast;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarAddActivity extends BaseActivity {
    
    private static final int DATEVIEW = 0;
    private static final int CLOCKVIEW = 1;
    public static final String ALARM_SINGLE_ACTION = "com.example.myapplication.ALARM_SINGLE_ACTION";
    @BindView(R.id.title_toolbar)
    CommonTitleBar titleToolbar;
    @BindView(R.id.clock_btn)
    JellyToggleButton clockBtn;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.end_time)
    TextView endTime;
    @BindView(R.id.calendar_title)
    EditText calendarTitle;
    @BindView(R.id.calendar_place)
    EditText calendarPlace;
    @BindView(R.id.calendar_remark)
    EditText calendarRemark;


    private TimePickerView mPvTime;
    private TimePickerView mPvTime1;
    public int condition = 0; //定义一个状态来判断是否调用闹钟
    //保存到数据库
    MemoRandumBean mMemoRandumBean = new MemoRandumBean();

    String timeStart = "00:00";
    String timeEnd = "23:59";
    Date mDateStart =new Date();
    Date mDateEnd = new Date();
    Calendar setTime = Calendar.getInstance();
    com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();

    String day;

    @Override
    public void initView() {
            //  获取上个页面传来的时间数据
            Intent intent = getIntent();
            calendar = (com.haibin.calendarview.Calendar) intent.getSerializableExtra("curTime");
            Log.d("日期", String.valueOf(calendar.getYear()));
            Log.d("日期", String.valueOf(calendar.getMonth()));
            Log.d("日期", String.valueOf(calendar.getDay()));
            day = String.valueOf(calendar.getYear())+String.valueOf(calendar.getMonth())+String.valueOf(calendar.getDay());
            ButterKnife.bind(this);
            initToolBar();
            mDateStart.setYear(calendar.getYear());
            mDateStart.setMonth(calendar.getMonth());
            mDateStart.setDate(calendar.getDay());
            mDateStart.setHours(0);
            mDateStart.setMinutes(0);
            mDateStart.setSeconds(0);
            mDateEnd.setYear(calendar.getYear());
            mDateEnd.setMonth(calendar.getMonth());
            mDateEnd.setDate(calendar.getDay());
            mDateEnd.setHours(23);
            mDateEnd.setMinutes(59);
            mDateEnd.setSeconds(59);
            Log.d("现在时间",new SimpleDateFormat("HH:mm").format(mDateStart));
            Log.d("现在时间",new SimpleDateFormat("HH:mm").format(mDateEnd));

            //设置按钮的颜色，以及监听事件
            clockBtn.setLeftBackgroundColor(Color.parseColor("#FFFFFF"));
            clockBtn.setRightBackgroundColor(Color.parseColor("#00BFFF"));
            clockBtn.setLeftThumbColor(Color.parseColor("#02D9F8"));
            clockBtn.setRightThumbColor(Color.parseColor("#02D9F8"));
            clockBtn.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener() {
                @Override
                public void onStateChange(float process, State state, JellyToggleButton jtb) {
                        if(state == State.LEFT){
                            condition = 0;//不重要
                        }
                        if(state == State.RIGHT){
                            condition = 1;//重要
                        }
                }
            });

    }
    //初始化标题栏,增加监听
    private void initToolBar() {
        titleToolbar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                //点击左边图片
                if(action == CommonTitleBar.ACTION_LEFT_BUTTON){
                    finish();
                }
                //点击右边图片
                if(action == CommonTitleBar.ACTION_RIGHT_BUTTON){
                    if(TextUtils.isEmpty(calendarTitle.getText())||TextUtils.isEmpty(calendarPlace.getText())||
                            TextUtils.isEmpty(calendarRemark.getText())||TextUtils.isEmpty(startTime.getText())||TextUtils.isEmpty(endTime.getText())){
                        TastyToast.makeText(CalendarAddActivity.this,"请填写完全部内容",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                    }else {
                        mMemoRandumBean.setEmail(SPUtils.getInstance().getString("email"));
                        mMemoRandumBean.setCalendar(calendar);
                        mMemoRandumBean.setPlace(calendarPlace.getText().toString());
                        mMemoRandumBean.setRemark(calendarRemark.getText().toString());
                        mMemoRandumBean.setStartTime(startTime.getText().toString());
                        mMemoRandumBean.setEndTime(endTime.getText().toString());
                        mMemoRandumBean.setTitle(calendarTitle.getText().toString());
                        mMemoRandumBean.setCondition(condition);
                        mMemoRandumBean.setDay(day);
                        mMemoRandumBean.save();
                        if(condition == 1){
                            setTime.setTime(mDateStart);
                            Log.d("设置日期",mDateStart.toString());
                            Intent intent = new Intent(CalendarAddActivity.this, NotificationClickReceiver.class);
                            intent.setAction(ALARM_SINGLE_ACTION);
                            PendingIntent piIntent = PendingIntent.getBroadcast(CalendarAddActivity.this,0,intent,0);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                            //对Android版本做一个判断,不同的版本调用不同的方法
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),piIntent);
                            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP,mDateStart.getTime(),piIntent);
                            }else {
                                alarmManager.set(AlarmManager.RTC_WAKEUP,mDateStart.getTime(),piIntent);
                            }

                        }
                        TastyToast.makeText(CalendarAddActivity.this,"添加成功", TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
        });
    }

    @OnClick({R.id.start_time,R.id.end_time})
    public void onClickItem(View view){
        switch (view.getId()){
            case R.id.start_time:
                //时间选择器
                mPvTime = new TimePickerBuilder(CalendarAddActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        date.setYear(calendar.getYear()-1900);
                        date.setMonth(calendar.getMonth()-1);
                        date.setDate(calendar.getDay());
                        if(date.getTime()>=mDateEnd.getTime()){
                            TastyToast.makeText(CalendarAddActivity.this,"开始时间需要小于结束时间",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                        }else {
                            mDateStart = date;
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            startTime.setText(format.format(date));

                            Log.d("日期", new SimpleDateFormat("y-M-d h:m:s a E").format(date));
                            Log.d("开始时间", new SimpleDateFormat("y-M-d h:m:s a E").format(mDateStart));
                        }

                    }
                })
                        .setOutSideCancelable(true)
                        .setType(new boolean[]{false, false, false, true, true, false})
                        .setTitleText("开始时间")//标题文字
                        .build();
                mPvTime.show();
                break;
            case R.id.end_time:
                //时间选择器
                mPvTime1 = new TimePickerBuilder(CalendarAddActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        date.setYear(calendar.getYear()-1900);
                        date.setMonth(calendar.getMonth()-1);
                        date.setDate(calendar.getDay());
                        if(date.getTime()<=mDateStart.getTime()){
                            TastyToast.makeText(CalendarAddActivity.this,"结束时间需要大于等于开始时间",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                        }else {
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            mDateEnd = date;
                            endTime.setText(format.format(date));

                            Log.d("开始时间", new SimpleDateFormat("y-M-d h:m:s a E").format(mDateEnd));
                        }

                    }
                })
                        .setOutSideCancelable(true)
                        .setType(new boolean[]{false, false, false, true, true, false})
                        .setTitleText("结束时间")//标题文字
                        .build();
                mPvTime1.show();
                break;
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_calendar_add;
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
            case R.id.rl_date:
                break;
        }
    }


}