package com.noadam.pushlearn.entities;

public class ComPack {

    private int comPackID;
    private int comPackOwnerID;
    private String comPackName;
    private int comPackRating;
    private String comPackDescription;
    private String comPackAccess;
    private int comPackDirectoryId;
    private int comPackSubdirectoryId;

    public ComPack(String packName) { // From pack
        this.comPackID = 0;
        this.comPackOwnerID = 0;
        this.comPackName = packName;
        this.comPackRating = 0;
        this.comPackDescription = "";
        this.comPackAccess = "public";
        this.comPackDirectoryId = 0;
        this.comPackSubdirectoryId = 0;
    }

    public ComPack(int id, int owner_id,String packName, int rating, String description, String access, int directory_id, int subdirectory_id) { // From server
        this.comPackID = id;
        this.comPackOwnerID = owner_id;
        this.comPackName = packName;
        this.comPackRating = rating;
        this.comPackDescription = description;
        this.comPackAccess = access;
        this.comPackDirectoryId = directory_id;
        this.comPackSubdirectoryId = subdirectory_id;
    }

    public int getComPackID() {
        return comPackID;
    }

    public void setComPackID(int comPackID) {
        this.comPackID = comPackID;
    }

    public String getComPackName() {
        return comPackName;
    }

    public void setComPackName(String comPackName) {
        this.comPackName = comPackName;
    }

    public int getComPackRating() {
        return comPackRating;
    }

    public void setComPackRating(int comPackRating) {
        this.comPackRating = comPackRating;
    }

    public String getComPackDescription() {
        return comPackDescription;
    }

    public void setComPackDescription(String comPackDescription) {
        this.comPackDescription = comPackDescription;
    }

    public String getComPackAccess() {
        return comPackAccess;
    }

    public void setComPackAccess(String comPackAccess) {
        this.comPackAccess = comPackAccess;
    }

    public int getComPackDirectoryId() {
        return comPackDirectoryId;
    }

    public void setComPackDirectoryId(int comPackDirectoryId) {
        this.comPackDirectoryId = comPackDirectoryId;
    }

    public int getComPackSubdirectoryId() {
        return comPackSubdirectoryId;
    }

    public void setComPackSubdirectoryId(int comPackSubdirectoryId) {
        this.comPackSubdirectoryId = comPackSubdirectoryId;
    }

    public int getComPackOwnerID() {
        return comPackOwnerID;
    }

    public void setComPackOwnerID(int comPackOwnerID) {
        this.comPackOwnerID = comPackOwnerID;
    }
}
