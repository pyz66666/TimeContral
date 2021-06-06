package com.example.myapplication.mvp.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseActivity;
import com.example.myapplication.mvp.base.BasePresenter;
import com.example.myapplication.mvp.model.entity.MemoRandumBean;
import com.example.myapplication.mvp.ui.adapter.CalendarAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.sdsmdg.tastytoast.TastyToast;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CalendarActivity extends BaseActivity {
    private static final int CALENDAR = 1;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.title_toolbar)
    CommonTitleBar titleToolbar;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.day)
    TextView day;
    @BindView(R.id.add_img)
    FloatingActionButton addImg;
    @BindView(R.id.calendar_recycle)
    RecyclerView calendarRecycle;
    CalendarAdapter calendarAdapter;

    Calendar selectCalender = new Calendar();

    android.icu.util.Calendar mCalendar = android.icu.util.Calendar.getInstance();
    MemoRandumBean mMemoRandumBean = new MemoRandumBean();


    @Override
    public void initView() {
        initRecycle();
        selectCalender.setYear(calendarView.getCurYear());
        selectCalender.setDay(calendarView.getCurDay());
        selectCalender.setMonth(calendarView.getCurMonth());
    // calendarView. ();
        initCalendarView();
        initTitleBar();
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击定位到当前
              calendarView.scrollToCurrent(true);
              calendarView.scrollToCalendar(mCalendar.get(android.icu.util.Calendar.YEAR),mCalendar.get(android.icu.util.Calendar.MONTH)+1,mCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
            }
        });
    }
    //初始化recyclerview
    private void initRecycle() {
        List<MemoRandumBean> list = (List<MemoRandumBean>) LitePal.where("email = ? and day = ?", SPUtils.getInstance().getString("email"), String.valueOf(calendarView.getCurYear())+String.valueOf(calendarView.getCurMonth())+String.valueOf(calendarView.getCurDay())).find(MemoRandumBean.class);
        if(list!=null || list.size()!=0 || !list.isEmpty()){
            calendarAdapter = new CalendarAdapter(list,CalendarActivity.this);
            calendarAdapter.setNewData(list);
        }else {
            calendarAdapter = new CalendarAdapter(null,CalendarActivity.this);
        }
        calendarAdapter.setOnItemClick(new CalendarAdapter.OnItemClick() {
            @Override
            public void delete(MemoRandumBean memoRandumBean) {
               LitePal.deleteAll(MemoRandumBean.class,"title = ? and email = ? and startTime = ?",memoRandumBean.getTitle(),memoRandumBean.getEmail(),memoRandumBean.getStartTime());
                List<MemoRandumBean> list = (List<MemoRandumBean>) LitePal.where("email = ? and day = ?", SPUtils.getInstance().getString("email"), String.valueOf(selectCalender.getYear())+String.valueOf(selectCalender.getMonth())+String.valueOf(selectCalender.getDay())).find(MemoRandumBean.class);
                if(list!=null || list.size()!=0 || !list.isEmpty()){
                    calendarAdapter.setNewData(list);
                }
                calendarAdapter.notifyDataSetChanged();
            }
        });
        calendarRecycle.setLayoutManager(new LinearLayoutManager(this));
        calendarRecycle.setAdapter(calendarAdapter);
    }

    //初始化标题栏的监听方法
    private void initTitleBar() {
        titleToolbar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                //点击左边图片
                if(action == CommonTitleBar.ACTION_LEFT_BUTTON){
                    finish();
                }

            }
        });
    }

    //初始化日记的一些监听方法
    private void initCalendarView() {

        //日历的点击事件
        calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }
            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                year.setVisibility(View.VISIBLE);
                day.setVisibility(View.VISIBLE);
                selectCalender = calendar;
                year.setText(String.valueOf(calendar.getYear()));
                day.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
                Log.d("日期", String.valueOf(calendar.getYear()));
                Log.d("日期", String.valueOf(calendar.getMonth()));
                Log.d("日期", String.valueOf(calendar.getDay()));
                List<MemoRandumBean> list = (List<MemoRandumBean>) LitePal.where("email = ? and day = ?", SPUtils.getInstance().getString("email"), String.valueOf(calendar.getYear())+String.valueOf(calendar.getMonth())+String.valueOf(calendar.getDay())).find(MemoRandumBean.class);
                if(list!=null || list.size()!=0 || !list.isEmpty()){
                    calendarAdapter.setNewData(list);
                }
            }
        });
        //日历的长按点击事件
        calendarView.setOnCalendarLongClickListener(new CalendarView.OnCalendarLongClickListener() {
            @Override
            public void onCalendarLongClickOutOfRange(Calendar calendar) {
            }
            @Override
            public void onCalendarLongClick(Calendar calendar) {
                Intent intent = new Intent(CalendarActivity.this,CalendarAddActivity.class);
                intent.putExtra("curTime",calendar);
                startActivityForResult(intent,CALENDAR);
                TastyToast.makeText(CalendarActivity.this,"长安点击事件",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getContentViewID() {
        return R.layout.calendar_layout;
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CALENDAR){
            if(resultCode == RESULT_OK){
                List<MemoRandumBean> list = (List<MemoRandumBean>) LitePal.where("email = ? and day = ?", SPUtils.getInstance().getString("email"), String.valueOf(selectCalender.getYear())+String.valueOf(selectCalender.getMonth())+String.valueOf(selectCalender.getDay())).find(MemoRandumBean.class);
                if(list!=null || list.size()!=0 || !list.isEmpty()){
                    calendarAdapter.setNewData(list);
                }
            }
        }
    }
}
