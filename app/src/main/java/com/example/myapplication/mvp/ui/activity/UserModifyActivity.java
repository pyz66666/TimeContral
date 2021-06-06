package com.example.myapplication.mvp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.UriUtils;
import com.example.myapplication.R;
import com.example.myapplication.mvp.base.BaseActivity;
import com.example.myapplication.mvp.base.BasePresenter;
import com.example.myapplication.mvp.model.entity.GenderBean;
import com.example.myapplication.mvp.model.entity.PersonDataBean;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.view.listener.OnLvItemClickListener;
import com.sdsmdg.tastytoast.TastyToast;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class UserModifyActivity extends BaseActivity {

    private static final int PRC_PHOTO_PREVIEW = 2;
    private static final int PRC_PHOTO_PICKER = 1;
    //调取系统摄像头的请求码
    private static final int MY_ADD_CASE_CALL_PHONE = 6;
    //打开相册的请求码
    private static final int MY_ADD_CASE_CALL_PHONE2 = 7;

    private static final int TAKEPHOTO = 3;
    private static final int REQUEST_CODE = 1;

    @BindView(R.id.title_toolbar)
    CommonTitleBar titleToolbar;
    @BindView(R.id.gender)
    TextView gender;
    @BindView(R.id.birth_date)
    TextView birthDate;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.modify_username)
    EditText modifyUserName;
    @BindView(R.id.sign)
    EditText sign;

    private File takePhotoDir;

    private TimePickerView mPvTime1;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private View layout;
    private TextView takePhotoTV;
    private TextView choosePhotoTV;
    private TextView cancelTV;
    private ArrayList<String> mImages;


    @Override
    public void initView() {
        initToolBar();
    }
    @Override
    public void initListener() {
        
    }

    @Override
    public int getContentViewID() {
        return R.layout.user_modify_layout;
    }

    @Override
    public void initDate() {
        if(!SPUtils.getInstance().getString("birthday").isEmpty()){
            birthDate.setText(SPUtils.getInstance().getString("birthday"));
        }
        if(!SPUtils.getInstance().getString("gender").isEmpty()){
            gender.setText(SPUtils.getInstance().getString("gender"));
        }
        if(!SPUtils.getInstance().getString("userName").isEmpty()){
            modifyUserName.setText(SPUtils.getInstance().getString("userName"));
        }
        if(!SPUtils.getInstance().getString("sign").isEmpty()){
            sign.setText(SPUtils.getInstance().getString("sign"));
        }
        if(!SPUtils.getInstance().getString("imageUri").isEmpty()){
            Glide.with(UserModifyActivity.this).load(SPUtils.getInstance().getString("imageUri")).into(image);
        }
    }

    @OnClick({R.id.gender,R.id.birth_date,R.id.image})
    public void OnItemClick(View view){
        switch (view.getId()){
            case R.id.gender:
                final List<GenderBean> items = new ArrayList<>();
                items.add(new GenderBean(1,"男"));
                items.add(new GenderBean(1,"女"));
                 new CircleDialog.Builder()
                        .configDialog(params -> {
                            params.backgroundColorPress = Color.CYAN;
                            //增加弹出动画
                            params.animStyle = R.style.dialogWindowAnim;
                        })
                        .setTitle("标题")
                        .configTitle(params -> {
//                                params.backgroundColor = Color.RED;
                        })
                        .setItems(items, new OnLvItemClickListener() {
                            @Override
                            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                gender.setText(items.get(position).getItemLabel());
                                return true;
                            }
                        })
                        .setNegative("取消", null)
                        .show(getSupportFragmentManager())
                ;
                break;
            case R.id.birth_date:
                //时间选择器
                mPvTime1 = new TimePickerBuilder(UserModifyActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        birthDate.setText(format.format(date));
                    }
                })
                        .setOutSideCancelable(true)
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setTitleText("结束时间")//标题文字
                        .build();
                mPvTime1.show();
                break;
            case R.id.image:
                viewInit();
                break;
            default:break;
        }
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

    //初始化标题栏,增加监听
    private void initToolBar() {
        titleToolbar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                //点击左边图片
                if(action == CommonTitleBar.ACTION_LEFT_BUTTON){
                    finish();
                }

                if(action == CommonTitleBar.ACTION_RIGHT_BUTTON){
                    List<PersonDataBean> personDataBean = new ArrayList<>();
                    //查询数据库中的数据，没有就新插入，有就修改
                    personDataBean = LitePal.where("email = ?", SPUtils.getInstance().getString("email")).find(PersonDataBean.class);
                    if(personDataBean.size()==0){
                        //插入
                        PersonDataBean mPersonDataBean = new PersonDataBean();
                        mPersonDataBean.setEmail(SPUtils.getInstance().getString("email"));
                        mPersonDataBean.setBirthday(birthDate.getText().toString());
                        mPersonDataBean.setGender(gender.getText().toString());
                        mPersonDataBean.setImageUri(mImages.get(0));
                        mPersonDataBean.setUserName(modifyUserName.getText().toString());
                        mPersonDataBean.setSign(sign.getText().toString());
                        mPersonDataBean.save();
                        TastyToast.makeText(UserModifyActivity.this,"信息修改成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        SPUtils.getInstance().put("birthday",birthDate.getText().toString());
                        SPUtils.getInstance().put("gender",gender.getText().toString());
                        SPUtils.getInstance().put("userName",modifyUserName.getText().toString());
                        SPUtils.getInstance().put("sign",sign.getText().toString());
                        SPUtils.getInstance().put("imageUri",mImages.get(0));
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        //修改
                        ContentValues values = new ContentValues();
                        values.put("birthday",birthDate.getText().toString());
                        values.put("gender",gender.getText().toString());
                        values.put("userName",modifyUserName.getText().toString());
                        values.put("sign",sign.getText().toString());
                        values.put("imageUri",mImages.get(0));
                        LitePal.updateAll(PersonDataBean.class,values,"email = ?",SPUtils.getInstance().getString("email"));
                        TastyToast.makeText(UserModifyActivity.this,"信息修改成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        //放到缓存
                        SPUtils.getInstance().put("birthday",birthDate.getText().toString());
                        SPUtils.getInstance().put("gender",gender.getText().toString());
                        SPUtils.getInstance().put("userName",modifyUserName.getText().toString());
                        SPUtils.getInstance().put("sign",sign.getText().toString());
                        SPUtils.getInstance().put("imageUri",mImages.get(0));
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
        });
    }



    public void viewInit() {

        builder = new AlertDialog.Builder(this);//创建对话框
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.dialog_select_photo, null);//获取自定义布局

        builder.setView(layout);//设置对话框的布局
        dialog = builder.create();//生成最终的对话框
        dialog.show();//显示对话框

        takePhotoTV = layout.findViewById(R.id.photograph);
        choosePhotoTV = layout.findViewById(R.id.photo);
        cancelTV = layout.findViewById(R.id.cancel);
        //设置监听
        takePhotoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"点击了照相";
                //  6.0之后动态申请权限 摄像头调取权限,SD卡写入权限
                //判断是否拥有权限，true则动态申请
                if (ContextCompat.checkSelfPermission(UserModifyActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(UserModifyActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserModifyActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_ADD_CASE_CALL_PHONE);
                } else {

                        //有权限,去打开摄像头
                        //拍照并剪裁
                        ImageSelector.builder()
                                .setCrop(true) // 设置是否使用图片剪切功能。
                                .setCropRatio(1.0f) // 图片剪切的宽高比,默认1.0f。宽固定为手机屏幕的宽。
                                .onlyTakePhoto(true)  // 仅拍照，不打开相册
                                .start(UserModifyActivity.this, TAKEPHOTO);

                }
                dialog.dismiss();
            }
        });
        choosePhotoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"点击了相册";
                //  6.0之后动态申请权限 SD卡写入权限
                if (ContextCompat.checkSelfPermission(UserModifyActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserModifyActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_ADD_CASE_CALL_PHONE2);

                } else {
                    //打开相册
                    ImageSelector.builder()
                            .useCamera(true) // 设置是否使用拍照
                            .setCrop(true)  // 设置是否使用图片剪切功能。
                            .setCropRatio(1.0f) // 图片剪切的宽高比,默认1.0f。宽固定为手机屏幕的宽。
                            .setSingle(true)  //设置是否单选
                            .canPreview(true) //是否点击放大图片查看,，默认为true
                            .start(UserModifyActivity.this, REQUEST_CODE); // 打开相册
                }
                dialog.dismiss();
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//关闭对话框
            }
        });
    }

    private void takePhoto() throws IOException {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // 获取文件
        File file = createFileIfNeed(  PHOTO_NAME_POSTFIX_SDF.format(new Date())+"UserIcon.png");
        //拍照后原图回存入此路径下
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            /**
             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
             */
            uri = FileProvider.getUriForFile(this, "com.example.myapplication.mvp.fileprovider", file);
        }
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 1);
    }

    // 在sd卡中创建一保存图片（原图和缩略图共用的）文件夹
    private File createFileIfNeed(String fileName) throws IOException {
        String fileA = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/nbinpic";
        File fileJA = new File(fileA);
        if (!fileJA.exists()) {
            fileJA.mkdirs();
        }
        File file = new File(fileA, fileName);
        if (file.exists()) {
            file.delete();

        }
        file.createNewFile();
        return file;
    }

    private static final SimpleDateFormat PHOTO_NAME_POSTFIX_SDF = new SimpleDateFormat("yyyy-MM-dd_HH-mm_ss", Locale.getDefault());




    /**
     * 申请权限回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_ADD_CASE_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageSelector.builder()
                            .setCrop(true) // 设置是否使用图片剪切功能。
                            .setCropRatio(1.0f) // 图片剪切的宽高比,默认1.0f。宽固定为手机屏幕的宽。
                            .onlyTakePhoto(true)  // 仅拍照，不打开相册
                            .start(this, TAKEPHOTO);

            } else {
                Toast.makeText(this,"拒绝了你的请求",Toast.LENGTH_SHORT).show();
                //"权限拒绝");
                // TODO: 2018/12/4 这里可以给用户一个提示,请求权限被拒绝了
            }
        }


        if (requestCode == MY_ADD_CASE_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setCrop(true)  // 设置是否使用图片剪切功能。
                        .setCropRatio(1.0f) // 图片剪切的宽高比,默认1.0f。宽固定为手机屏幕的宽。
                        .setSingle(true)  //设置是否单选
                        .canPreview(true) //是否点击放大图片查看,，默认为true
                        .start(this, REQUEST_CODE); // 打开相册
            } else {
                //"权限拒绝");
                // TODO: 2018/12/4 这里可以给用户一个提示,请求权限被拒绝了
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * startActivityForResult执行后的回调方法，接收返回的图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKEPHOTO ) {
            if(resultCode == RESULT_OK){
                mImages = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                String img = mImages.get(0);
                Log.d("拍照",img);
               // Glide.with(UserModifyActivity.this).load(UriUtils.getImageContentUri(UserModifyActivity.this, img)).into(image);
                Glide.with(UserModifyActivity.this).load(img).into(image);
                TastyToast.makeText(UserModifyActivity.this,"拍照成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);

            }

        } else if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK
                && null != data) {
            mImages = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            String img = mImages.get(0);
            Log.d("拍照",img);
            Glide.with(UserModifyActivity.this).load(img).into(image);
            TastyToast.makeText(UserModifyActivity.this,"打开相册",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
        }
    }









}
