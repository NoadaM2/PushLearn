package com.noadam.pushlearn.internet;

public interface SignInCallBack {
    void getHashFromSignIn(String value);
    void onError();
}
