package com.noadam.pushlearn.internet;

import com.google.gson.Gson;

public interface GetComPackByIDCallBack {
    void getComPack(String value);
    void onError();
}
