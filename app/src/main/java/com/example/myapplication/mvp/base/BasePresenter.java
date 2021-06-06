package com.example.myapplication.mvp.base;

import android.content.Context;

public abstract class BasePresenter<M extends BaseModel, V extends BaseActivity> {

    protected M mModel;
    protected V mRootView;
   //protected F mFragment;

    public BasePresenter(M model, V rootView) {
        this.mModel = model;
        this.mRootView = rootView;


    }


    /*public void bindFragment(F mFragment){
        this.mFragment = mFragment;
    }*/

   /* public void unBindFragmnet(){
        mFragment = null;
    }*/


}
