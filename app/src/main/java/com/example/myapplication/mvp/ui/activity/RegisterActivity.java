package com.example.myapplication.mvp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseActivity;
import com.example.myapplication.mvp.base.BasePresenter;
import com.example.myapplication.mvp.model.entity.LoginDataBean;
import com.sdsmdg.tastytoast.TastyToast;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.exceptions.DataSupportException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {


    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.register_email)
    TextView registerEmail;
    @BindView(R.id.register_password)
    TextView registerPassword;
    @BindView(R.id.register_tel)
    TextView registerTel;
    @BindView(R.id.register_username)
    TextView registerUsername;
    LoginDataBean mLoginDataBean;
    private Intent mIntent;

    @Override
    public void initView() {
        setStatusBarFullTransparent();
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.register_btn})
    public void onItemClick(View view){
        switch (view.getId()){
            case R.id.register_btn:
                if (TextUtils.isEmpty(registerEmail.getText()) || TextUtils.isEmpty(registerPassword.getText()) || TextUtils.isEmpty(registerTel.getText()) || TextUtils.isEmpty(registerUsername.getText())) {
                    TastyToast.makeText(this,"请填写完全部内容", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    break;
                }
                if(RegexUtils.isTel(registerTel.getText())){
                    TastyToast.makeText(this,"请输入正确的手机号", TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                    break;
                }
                if(!RegexUtils.isEmail(registerEmail.getText())){
                    TastyToast.makeText(this,"请输入正确的邮箱", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    break;
                }
                mLoginDataBean.setTel(registerTel.getText().toString());
                mLoginDataBean.setEmail(registerEmail.getText().toString());
                mLoginDataBean.setPassWord(registerPassword.getText().toString());
                mLoginDataBean.setUserName(registerUsername.getText().toString());
                List<LoginDataBean> loginDataBeans = LitePal.findAll(LoginDataBean.class);
                for(LoginDataBean str: loginDataBeans){
                    if(str.getEmail().equals(registerEmail.getText())){
                        TastyToast.makeText(this,"邮箱已注册", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        break;
                    }
                    if(str.getTel().equals(registerTel.getText())){
                        TastyToast.makeText(this,"手机号已注册", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        break;
                    }
                }
                //信息存到数据库
                mLoginDataBean.save();
                TastyToast.makeText(this,"注册成功", TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);

                //界面跳转,顺便传数据
                mIntent = new Intent();
                mIntent.putExtra("email",mLoginDataBean.getEmail());
                mIntent.putExtra("password",mLoginDataBean.getPassWord());
                setResult(RESULT_OK,mIntent);
                finish();

        }
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_register;
    }

    @Override
    public void initDate() {
        mLoginDataBean = new LoginDataBean();


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




}
