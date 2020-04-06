package com.noadam.pushlearn.entities;

import androidx.annotation.NonNull;

public class SubDirectory extends Directory {
    private int directory_id;

    public SubDirectory(int id, int directory_id, String name) {
        super(id, name);
        this.directory_id = directory_id;
    }

    public int getDirectory_id() {
        return directory_id;
    }

    public void setDirectory_id(int directory_id) {
        this.directory_id = directory_id;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
