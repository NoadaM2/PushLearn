package com.noadam.pushlearn.entities;

public class ComPack {

    private int myComPackId;
    private String myComPackName;
    private int myComPackRating;
    private String myComPackDescription;
    private String myComPackAccess;
    private int myComPackDirectoryId;
    private int myComPackSubdirectoryId;

    public ComPack(String packName) { // From pack
        this.myComPackId = 0;
        this.myComPackName = packName;
        this.myComPackRating = 0;
        this.myComPackDescription = "";
        this.myComPackAccess = "public";
        this.myComPackDirectoryId = 0;
        this.myComPackSubdirectoryId = 0;
    }

    public ComPack(int id, String packName, int rating, String description, String access, int directory_id, int subdirectory_id) { // From server
        this.myComPackId = id;
        this.myComPackName = packName;
        this.myComPackRating = rating;
        this.myComPackDescription = description;
        this.myComPackAccess = access;
        this.myComPackDirectoryId = directory_id;
        this.myComPackSubdirectoryId = subdirectory_id;
    }

    public int getMyComPackId() {
        return myComPackId;
    }

    public void setMyComPackId(int myComPackId) {
        this.myComPackId = myComPackId;
    }

    public String getMyComPackName() {
        return myComPackName;
    }

    public void setMyComPackName(String myComPackName) {
        this.myComPackName = myComPackName;
    }

    public int getMyComPackRating() {
        return myComPackRating;
    }

    public void setMyComPackRating(int myComPackRating) {
        this.myComPackRating = myComPackRating;
    }

    public String getMyComPackDescription() {
        return myComPackDescription;
    }

    public void setMyComPackDescription(String myComPackDescription) {
        this.myComPackDescription = myComPackDescription;
    }

    public String getMyComPackAccess() {
        return myComPackAccess;
    }

    public void setMyComPackAccess(String myComPackAccess) {
        this.myComPackAccess = myComPackAccess;
    }

    public int getMyComPackDirectoryId() {
        return myComPackDirectoryId;
    }

    public void setMyComPackDirectoryId(int myComPackDirectoryId) {
        this.myComPackDirectoryId = myComPackDirectoryId;
    }

    public int getMyComPackSubdirectoryId() {
        return myComPackSubdirectoryId;
    }

    public void setMyComPackSubdirectoryId(int myComPackSubdirectoryId) {
        this.myComPackSubdirectoryId = myComPackSubdirectoryId;
    }
}
