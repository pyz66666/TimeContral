package com.example.myapplication.mvp.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseFragment;
import com.example.myapplication.mvp.base.BasePresenter;
import com.example.myapplication.mvp.ui.activity.UserModifyActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class UserFragment extends BaseFragment {

    @BindView(R.id.user_modify)
    TextView userModify;

    @Override
    public int getViewId() {
        return R.layout.user_fragment;
    }

    @OnClick({R.id.user_modify})
    public void onItemClick(View view){
        switch (view.getId()){
            case R.id.user_modify:
                Intent intent = new Intent(getActivity(), UserModifyActivity.class);
                startActivity(intent);
        }
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
