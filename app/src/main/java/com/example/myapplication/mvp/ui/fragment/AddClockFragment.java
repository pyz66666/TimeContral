package com.example.myapplication.mvp.ui.fragment;

import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseFragment;
import com.example.myapplication.mvp.base.BasePresenter;

public class AddClockFragment extends BaseFragment {

    private static final int DATEVIEW = 0;
    private static final int CLOCKVIEW = 1;

    private int mType;

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public static AddClockFragment newInstance(int type){
        AddClockFragment addDateFragment = new AddClockFragment();
        addDateFragment.setType(type);
        return addDateFragment;
    }

    @Override
    public int getViewId() {
        return R.layout.add_clock_layout;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
