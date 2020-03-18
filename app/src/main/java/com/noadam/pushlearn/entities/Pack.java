package com.noadam.pushlearn.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Pack {

    private String packName;
    private String type;

    public Pack(String packName,String type) {
        this.type = type;
        this.packName = packName;
    }


    public Pack(String packName) {
        this.packName = packName;
        this.type = "created";
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getType() {
        return type;
    }

}