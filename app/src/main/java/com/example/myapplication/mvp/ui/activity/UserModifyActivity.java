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
    //?????????????????????????????????
    private static final int MY_ADD_CASE_CALL_PHONE = 6;
    //????????????????????????
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
                items.add(new GenderBean(1,"???"));
                items.add(new GenderBean(1,"???"));
                 new CircleDialog.Builder()
                        .configDialog(params -> {
                            params.backgroundColorPress = Color.CYAN;
                            //??????????????????
                            params.animStyle = R.style.dialogWindowAnim;
                        })
                        .setTitle("??????")
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
                        .setNegative("??????", null)
                        .show(getSupportFragmentManager())
                ;
                break;
            case R.id.birth_date:
                //???????????????
                mPvTime1 = new TimePickerBuilder(UserModifyActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        birthDate.setText(format.format(date));
                    }
                })
                        .setOutSideCancelable(true)
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setTitleText("????????????")//????????????
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

    //??????????????????,????????????
    private void initToolBar() {
        titleToolbar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                //??????????????????
                if(action == CommonTitleBar.ACTION_LEFT_BUTTON){
                    finish();
                }

                if(action == CommonTitleBar.ACTION_RIGHT_BUTTON){
                    List<PersonDataBean> personDataBean = new ArrayList<>();
                    //???????????????????????????????????????????????????????????????
                    personDataBean = LitePal.where("email = ?", SPUtils.getInstance().getString("email")).find(PersonDataBean.class);
                    if(personDataBean.size()==0){
                        //??????
                        PersonDataBean mPersonDataBean = new PersonDataBean();
                        mPersonDataBean.setEmail(SPUtils.getInstance().getString("email"));
                        mPersonDataBean.setBirthday(birthDate.getText().toString());
                        mPersonDataBean.setGender(gender.getText().toString());
                        mPersonDataBean.setImageUri(mImages.get(0));
                        mPersonDataBean.setUserName(modifyUserName.getText().toString());
                        mPersonDataBean.setSign(sign.getText().toString());
                        mPersonDataBean.save();
                        TastyToast.makeText(UserModifyActivity.this,"??????????????????",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        SPUtils.getInstance().put("birthday",birthDate.getText().toString());
                        SPUtils.getInstance().put("gender",gender.getText().toString());
                        SPUtils.getInstance().put("userName",modifyUserName.getText().toString());
                        SPUtils.getInstance().put("sign",sign.getText().toString());
                        SPUtils.getInstance().put("imageUri",mImages.get(0));
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        //??????
                        ContentValues values = new ContentValues();
                        values.put("birthday",birthDate.getText().toString());
                        values.put("gender",gender.getText().toString());
                        values.put("userName",modifyUserName.getText().toString());
                        values.put("sign",sign.getText().toString());
                        values.put("imageUri",mImages.get(0));
                        LitePal.updateAll(PersonDataBean.class,values,"email = ?",SPUtils.getInstance().getString("email"));
                        TastyToast.makeText(UserModifyActivity.this,"??????????????????",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        //????????????
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

        builder = new AlertDialog.Builder(this);//???????????????
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.dialog_select_photo, null);//?????????????????????

        builder.setView(layout);//????????????????????????
        dialog = builder.create();//????????????????????????
        dialog.show();//???????????????

        takePhotoTV = layout.findViewById(R.id.photograph);
        choosePhotoTV = layout.findViewById(R.id.photo);
        cancelTV = layout.findViewById(R.id.cancel);
        //????????????
        takePhotoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"???????????????";
                //  6.0???????????????????????? ?????????????????????,SD???????????????
                //???????????????????????????true???????????????
                if (ContextCompat.checkSelfPermission(UserModifyActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(UserModifyActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserModifyActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_ADD_CASE_CALL_PHONE);
                } else {

                        //?????????,??????????????????
                        //???????????????
                        ImageSelector.builder()
                                .setCrop(true) // ???????????????????????????????????????
                                .setCropRatio(1.0f) // ????????????????????????,??????1.0f????????????????????????????????????
                                .onlyTakePhoto(true)  // ???????????????????????????
                                .start(UserModifyActivity.this, TAKEPHOTO);

                }
                dialog.dismiss();
            }
        });
        choosePhotoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"???????????????";
                //  6.0???????????????????????? SD???????????????
                if (ContextCompat.checkSelfPermission(UserModifyActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserModifyActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_ADD_CASE_CALL_PHONE2);

                } else {
                    //????????????
                    ImageSelector.builder()
                            .useCamera(true) // ????????????????????????
                            .setCrop(true)  // ???????????????????????????????????????
                            .setCropRatio(1.0f) // ????????????????????????,??????1.0f????????????????????????????????????
                            .setSingle(true)  //??????????????????
                            .canPreview(true) //??????????????????????????????,????????????true
                            .start(UserModifyActivity.this, REQUEST_CODE); // ????????????
                }
                dialog.dismiss();
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//???????????????
            }
        });
    }

    private void takePhoto() throws IOException {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // ????????????
        File file = createFileIfNeed(  PHOTO_NAME_POSTFIX_SDF.format(new Date())+"UserIcon.png");
        //????????????????????????????????????
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            /**
             * 7.0 ??????????????????????????????????????????Uri????????????????????????FileProvider
             * ????????????????????????MIUI?????????????????????size???0?????????
             */
            uri = FileProvider.getUriForFile(this, "com.example.myapplication.mvp.fileprovider", file);
        }
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 1);
    }

    // ???sd?????????????????????????????????????????????????????????????????????
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
     * ????????????????????????
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
                            .setCrop(true) // ???????????????????????????????????????
                            .setCropRatio(1.0f) // ????????????????????????,??????1.0f????????????????????????????????????
                            .onlyTakePhoto(true)  // ???????????????????????????
                            .start(this, TAKEPHOTO);

            } else {
                Toast.makeText(this,"?????????????????????",Toast.LENGTH_SHORT).show();
                //"????????????");
                // TODO: 2018/12/4 ?????????????????????????????????,????????????????????????
            }
        }


        if (requestCode == MY_ADD_CASE_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImageSelector.builder()
                        .useCamera(true) // ????????????????????????
                        .setCrop(true)  // ???????????????????????????????????????
                        .setCropRatio(1.0f) // ????????????????????????,??????1.0f????????????????????????????????????
                        .setSingle(true)  //??????????????????
                        .canPreview(true) //??????????????????????????????,????????????true
                        .start(this, REQUEST_CODE); // ????????????
            } else {
                //"????????????");
                // TODO: 2018/12/4 ?????????????????????????????????,????????????????????????
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * startActivityForResult????????????????????????????????????????????????
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
                Log.d("??????",img);
               // Glide.with(UserModifyActivity.this).load(UriUtils.getImageContentUri(UserModifyActivity.this, img)).into(image);
                Glide.with(UserModifyActivity.this).load(img).into(image);
                TastyToast.makeText(UserModifyActivity.this,"????????????",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);

            }

        } else if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK
                && null != data) {
            mImages = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            String img = mImages.get(0);
            Log.d("??????",img);
            Glide.with(UserModifyActivity.this).load(img).into(image);
            TastyToast.makeText(UserModifyActivity.this,"????????????",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
        }
    }









}
