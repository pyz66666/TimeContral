package com.example.myapplication.mvp.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.multidex.MultiDex;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment  {

    protected P mPresenter;
    protected Context mContext;
    protected Unbinder unbinder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getViewId() ,null);
        unbinder = ButterKnife.bind(this,view);
        initView();
        mPresenter = getPresenter();
        initData();
        MultiDex.install(getContext());
        return view;
    }

    public abstract int getViewId();

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mPresenter = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    public abstract void initData();

    public abstract void initView();

    public abstract P getPresenter();

}
