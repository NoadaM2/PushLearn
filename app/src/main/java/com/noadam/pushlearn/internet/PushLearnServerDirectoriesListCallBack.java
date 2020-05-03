package com.noadam.pushlearn.internet;

import com.noadam.pushlearn.entities.Directory;

import java.util.ArrayList;

public interface PushLearnServerDirectoriesListCallBack {
    void onResponse(ArrayList<Directory> directories);
    void onError(Throwable t);
}
