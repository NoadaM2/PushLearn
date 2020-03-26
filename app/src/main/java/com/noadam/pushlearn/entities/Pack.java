package com.noadam.pushlearn.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Pack {

    private int idComPack;
    private String packName;
    private String type;
    private boolean isChecked;


    public Pack(String packName, String type, int idComPack) {
        this.type = type;
        this.packName = packName;
        this.idComPack = idComPack;
    }


    public Pack(String packName) {
        this.packName = packName;
        this.type = "created";
        this.idComPack = 0;
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

    public int getIdComPack() {
        return idComPack;
    }

    public void setIdComPack(int idComPack) {
        this.idComPack = idComPack;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}