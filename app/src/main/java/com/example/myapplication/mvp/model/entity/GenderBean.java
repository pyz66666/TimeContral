package com.example.myapplication.mvp.model.entity;

import com.mylhyl.circledialog.callback.CircleItemLabel;

import java.util.IdentityHashMap;

public class GenderBean implements CircleItemLabel {

    private int id;
    private String type;

    public GenderBean(int i, String type) {
            this.id = i;
            this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getItemLabel() {
        return type;
    }
}
