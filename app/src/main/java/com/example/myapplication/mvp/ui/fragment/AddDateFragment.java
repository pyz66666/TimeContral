package com.example.myapplication.mvp.ui.fragment;

import android.graphics.Color;

import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseFragment;
import com.example.myapplication.mvp.base.BasePresenter;
import com.nightonke.jellytogglebutton.JellyToggleButton;

import butterknife.BindView;

public class AddDateFragment extends BaseFragment {

    @BindView(R.id.clock_btn)
    JellyToggleButton clockBtn;


    private int mType;
    private static final int DATEVIEW = 0;
    private static final int CLOCKVIEW = 1;

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public static AddDateFragment newInstance(int type){
        AddDateFragment addDateFragment = new AddDateFragment();
        addDateFragment.setType(type);
        return addDateFragment;
    }

    @Override
    public int getViewId() {

        return R.layout.add_date_layout;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        if(mType == DATEVIEW){
            clockBtn.setLeftBackgroundColor(Color.parseColor("#00BFFF"));
            clockBtn.setRightBackgroundColor(Color.parseColor("#87FADF"));
            clockBtn.setLeftThumbColor(Color.parseColor("#02D9F8"));
            clockBtn.setRightThumbColor(Color.parseColor("#02D9F8"));
        }

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
