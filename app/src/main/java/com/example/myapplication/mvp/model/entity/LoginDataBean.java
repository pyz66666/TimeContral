package com.example.myapplication.mvp.model.entity;

import org.litepal.crud.LitePalSupport;

public class LoginDataBean extends LitePalSupport {

    private String email;

    private String userName;

    private String passWord;

    private String tel;


    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

}
