package com.noadam.pushlearn.entities;

public class User {
    private int id;
    private String nickname;
    private int rating;
    private int language_id;
    private int premium;

    public User(int id, String nickname, int rating, int language_id, int premium) {
        this.id = id;
        this.nickname = nickname;
        this.rating = rating;
        this.language_id = language_id;
        this.premium = premium;
    }

    public User(String nickname, int rating, int language_id) {
        this.nickname = nickname;
        this.rating = rating;
        this.language_id = language_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }

    public int getPremium() {
        return premium;
    }

    public void setPremium(int premium) {
        this.premium = premium;
    }
}
