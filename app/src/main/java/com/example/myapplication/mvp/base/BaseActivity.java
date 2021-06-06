package com.example.myapplication.mvp.base;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.multidex.MultiDex;

import com.jaeger.library.StatusBarUtil;

import org.litepal.LitePal;

import java.util.Random;

import butterknife.ButterKnife;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements View.OnClickListener {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(getContentViewID());
        ButterKnife.bind(this);
        mPresenter = getPresenter();
        initView();
        initDate();
        initListener();
        //litePal数据库使用
        LitePal.initialize(this);
        MultiDex.install(this);

    }
    /*
    * 提供控件初始化
    * */
    public abstract void initView();
    /*
    * 提供监听方法
    * */
    public abstract void initListener();
    /*
    * 提供layoutId
    * */
    public abstract int getContentViewID();
    /*
    *初始化数据
    * */
    public abstract void initDate();
    /*
    * 获取P层
    * */
    public abstract P getPresenter();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destory();
    }
    /*
    * 提供销毁
    * */
    public abstract void destory();




    /*
     * 方法全透明状态栏
     * */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId
     */
    protected void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明

        }
    }

    //  获取随机颜色
    public static String getRandColorCode() {
        String r,g,b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r ;
        g = g.length() == 1 ? "0" + g : g ;
        b = b.length() == 1 ? "0" + b : b ;

        return r + g + b;
    }



}
