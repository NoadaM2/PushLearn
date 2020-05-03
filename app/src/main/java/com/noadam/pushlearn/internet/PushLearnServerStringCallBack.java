package com.noadam.pushlearn.internet;

public interface PushLearnServerStringCallBack {
    void onResponse(String value);
    void onError(Throwable t);
}
