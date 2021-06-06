package com.example.myapplication.mvp.model.entity;

import org.litepal.crud.LitePalSupport;

public class PersonDataBean extends LitePalSupport {
    
    //邮箱
    private String email;
    //性别
    private String gender;
    //生日
    private String birthday;
    //用户名
    private String userName;
    //签名
    private String sign;
    //头像图片路径
    private String imageUri;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
