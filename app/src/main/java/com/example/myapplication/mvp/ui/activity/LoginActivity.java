package com.example.myapplication.mvp.ui.activity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseActivity;
import com.example.myapplication.mvp.contract.LoginActivityContract;
import com.example.myapplication.mvp.model.entity.LoginDataBean;
import com.example.myapplication.mvp.model.entity.PersonDataBean;
import com.example.myapplication.mvp.presenter.LoginActivityPresenter;
import com.sdsmdg.tastytoast.TastyToast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.util.BGAAsyncTask;

public class LoginActivity extends BaseActivity<LoginActivityPresenter> implements LoginActivityContract.View {


    private static final int REGISTER = 1;
    @BindView(R.id.forget)
    TextView froget;
    @BindView(R.id.login_email)
    EditText loginEmail;
    @BindView(R.id.login_password)
    EditText loginPassword;

    AlertDialog.Builder mBuilder;

    MyTask mMyTask = new MyTask(null);

    @Override
    public void initView() {
        setStatusBarFullTransparent();
    }

    @Override
    public void initListener() {



    }
    //判断有没有获取UsagePremission权限
    public boolean checkUsagePremission(){
        AppOpsManager appOps = (AppOpsManager) this
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), this.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return  granted;
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.register,R.id.login,R.id.forget})
    public void itemClick(View view){
        switch(view.getId()){
            case R.id.register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,REGISTER);
                break;
            case R.id.login:
               // mPresenter.getData();
                judgeLogin();
                break;
            case R.id.forget:
                Intent intent1 = new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void initDate() {

    }

    @Override
    public LoginActivityPresenter getPresenter() {
        return null;
    }

    @Override
    public void destory() {

    }

    @Override
    public void onClick(View v) {

    }
    /*
    * 获取返回的数据
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取从register传来的数据，并显示出来
        switch (requestCode){
            case REGISTER:
                if(data!=null){
                    if(data.getStringExtra("email")!= null){
                        String email = data.getStringExtra("email");
                        loginEmail.setText(email);

                    }
                    if(data.getStringExtra("password")!=null){
                        String password = data.getStringExtra("password");
                        loginPassword.setText(password);
                    }
                }


        }

    }

    public void judgeLogin(){
        List<LoginDataBean> loginDataBeans = LitePal.findAll(LoginDataBean.class);
        if(loginDataBeans.size()!=0){
            for(LoginDataBean data: loginDataBeans){
                if ((loginEmail.getText().toString().trim()).equals(data.getEmail().trim())) {
                    if (data.getPassWord().trim().equals(loginPassword.getText().toString().trim())) {
                        TastyToast.makeText(this,"登录成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        SPUtils.getInstance().put("email",loginEmail.getText().toString());
                        SPUtils.getInstance().put("password",loginPassword.getText().toString());
                        List<PersonDataBean> personDataBean = new ArrayList<>();
                        //查询数据库中的数据
                        personDataBean = LitePal.where("email = ?", SPUtils.getInstance().getString("email")).find(PersonDataBean.class);
                        if(personDataBean.size()!=0){
                            if(!personDataBean.get(0).getBirthday().isEmpty()){
                                SPUtils.getInstance().put("birthday",personDataBean.get(0).getBirthday());
                            }
                            if(!personDataBean.get(0).getGender().isEmpty()){
                                SPUtils.getInstance().put("gender",personDataBean.get(0).getGender());
                            }
                            if(!personDataBean.get(0).getUserName().isEmpty()){
                                SPUtils.getInstance().put("userName",personDataBean.get(0).getUserName());
                            }
                            if(!personDataBean.get(0).getSign().isEmpty()){
                                SPUtils.getInstance().put("sign",personDataBean.get(0).getSign());
                            }
                            if(!personDataBean.get(0).getImageUri().isEmpty()){
                                SPUtils.getInstance().put("imageUri",personDataBean.get(0).getImageUri());
                            }
                        }
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        TastyToast.makeText(this,"密码错误",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                }
            }
        }else {
            TastyToast.makeText(this,"未找到邮箱信息，请先注册",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
        }

    }


   private class MyTask extends BGAAsyncTask {
       public MyTask(Callback callback) {
           super(callback);
       }

       @Override
       protected Object doInBackground(Object[] objects) {
           judgeLogin();
           return null;
       }

   }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkUsagePremission()){
            mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("获取权限");
            mBuilder.setMessage("屏幕使用时间权限是本App运行的必要权限，请用户点击下方的跳转手动开启权限。");
            mBuilder.setCancelable(false);//点击空白处弹窗不消失
            mBuilder.setPositiveButton("跳转", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    dialog.dismiss();
                }
            });
            mBuilder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityUtils.finishAllActivities();
                }
            });
            mBuilder.show();

        }
        else {
            TastyToast.makeText(this,"已拥有获取屏幕时间权限",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
        }
    }


    //BGAAsyncTask.Callback mCallback;
}
