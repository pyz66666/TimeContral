package com.example.myapplication.mvp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseActivity;
import com.example.myapplication.mvp.base.BasePresenter;
import com.example.myapplication.mvp.model.entity.LoginDataBean;
import com.example.myapplication.mvp.model.utils.EamilUtil;
import com.example.myapplication.mvp.model.utils.SendEmail;
import com.sdsmdg.tastytoast.TastyToast;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetActivity extends BaseActivity {

    @BindView(R.id.forget_email)
    TextView forgetEmail;
    @BindView(R.id.forget_message)
    TextView forgetMessage;
    @BindView(R.id.new_password)
    TextView newPassword;
    @BindView(R.id.send_message)
    Button sendMessage;

    private String mCode;

    private static final int MY_PERMISSION_REQUEST_CODE = 10000;

    @Override
    public void initView() {
        setStatusBarFullTransparent();
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_forget;
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

    @OnClick({R.id.send_message,R.id.forget_btn})
    public void BtnOnclick(View view){
        switch (view.getId()){
            case R.id.send_message:
                if(TextUtils.isEmpty(forgetEmail.getText())){
                    TastyToast.makeText(ForgetActivity.this,"请输入邮箱",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                    break;
                }else {
                    List<LoginDataBean> dataBean = LitePal.where("email = ?",forgetEmail.getText().toString()).find(LoginDataBean.class);
                    if(dataBean==null || dataBean.size()==0 || dataBean.isEmpty()){
                        TastyToast.makeText(ForgetActivity.this,"该邮箱未注册",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }else {
                        check();
                        sendMessage.setEnabled(false);
                        CountDownTimer timer = new CountDownTimer(60*1000, 1000){

                            @Override
                            public void onTick(long millisUntilFinished) {
                                    sendMessage.setText("还剩"+millisUntilFinished/1000+"秒");
                            }

                            @Override
                            public void onFinish() {
                                    sendMessage.setText("发送验证码");
                                    sendMessage.setEnabled(true);
                            }
                        }.start();
                    }
                    break;
                }

            case R.id.forget_btn:

                if(forgetMessage.getText().toString().trim().equals(mCode.trim())){
                    if(TextUtils.isEmpty(newPassword.getText())){
                        TastyToast.makeText(ForgetActivity.this,"请输入新密码",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                        break;
                    }else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("passWord",newPassword.getText().toString().trim());
                        LitePal.updateAll(LoginDataBean.class,contentValues,"email = ?",forgetEmail.getText().toString().trim());
                        TastyToast.makeText(ForgetActivity.this,"密码修改成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        finish();
                        break;
                    }

                }else {
                    TastyToast.makeText(ForgetActivity.this,"验证码错误",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    break;
                }

        }
    }
    //随机生成一个六位数的验证码
    public int randomCount(){
        int numcode = (int) ((Math.random() * 9 + 1) * 100000);
        return numcode;
    }

    public void check(){
        //检查是否有网络获取的权限
        boolean isAllGranted =checkPremissionAllGranted(
                new String[] {
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET
                }
        );
        //如果权限都有就发邮件
        if(isAllGranted){
           sendMessage();
            return;
        }//否则就请求权限
        else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.INTERNET
                    },MY_PERMISSION_REQUEST_CODE
            );
        }
    }

    /*
    * 检查权限
    * */
    private boolean checkPremissionAllGranted(String [] premissions){
        for(String premission : premissions){
            if(ContextCompat.checkSelfPermission(this,premission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    /*
    * 权限获取结果
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_PERMISSION_REQUEST_CODE){
            boolean isAllGranted = true;

            for(int grant : grantResults){
                if(grant != PackageManager.PERMISSION_DENIED){
                    isAllGranted =false;
                    break;
                }
            }
            if(isAllGranted){
               sendMessage();
            }
            else {

            }
        }
    }

    public void sendMessage(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                mCode = String.valueOf(randomCount());
                SendEmail se = new SendEmail(forgetEmail.getText().toString());
                try {
                    se.sendHtmlEmail(Long.parseLong(mCode));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}