package com.example.myapplication.mvp.presenter;

import com.example.myapplication.mvp.base.BasePresenter;
import com.example.myapplication.mvp.model.LoginActivityModel;
import com.example.myapplication.mvp.ui.activity.LoginActivity;

public class LoginActivityPresenter extends BasePresenter<LoginActivityModel, LoginActivity> {

    public LoginActivityPresenter(LoginActivityModel model, LoginActivity rootView) {
        super(model, rootView);
    }

   public void getData(){
        //mModel.judgeLogin();
   }
}
