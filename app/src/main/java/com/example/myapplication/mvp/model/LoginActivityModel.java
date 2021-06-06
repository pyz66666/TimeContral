package com.example.myapplication.mvp.model;

import android.text.TextUtils;

import com.example.myapplication.mvp.base.BaseModel;
import com.example.myapplication.mvp.base.BasePresenter;
import com.example.myapplication.mvp.contract.LoginActivityContract;
import com.example.myapplication.mvp.model.entity.LoginDataBean;
import com.example.myapplication.mvp.presenter.LoginActivityPresenter;
import com.sdsmdg.tastytoast.TastyToast;

import org.litepal.LitePal;

import java.util.List;

public class LoginActivityModel extends BaseModel<LoginActivityPresenter> implements LoginActivityContract.Model {

    public LoginActivityModel(BasePresenter mPresenter) {
        super((LoginActivityPresenter) mPresenter);
    }

    public void judgeLogin(String password ,String email) {
    }
}
