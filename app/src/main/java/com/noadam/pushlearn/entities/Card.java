package com.noadam.pushlearn.entities;

public class Card {

    private int _id;
    private String packName;
    private String question;
    private String answer;
    private int iterating_times;
    private boolean shown;

    public Card(String packName, String question, String answer) {
        this.packName = packName;
        this.question = question;
        this.answer = answer;
        this.iterating_times = 0;
        this.shown = false;
    }

    public Card(String packName, String question, String answer, int iterating_times) {
        this.packName = packName;
        this.question = question;
        this.answer = answer;
        this.iterating_times = iterating_times;
        this.shown = false;
    }

    public Card(String packName, String question, String answer, int iterating_times, boolean shown) {
        this.packName = packName;
        this.question = question;
        this.answer = answer;
        this.iterating_times = iterating_times;
        this.shown = shown;
    }

    public Card(int _id, String packName, String question, String answer, int iterating_times) {
        this._id = _id;
        this.packName = packName;
        this.question = question;
        this.answer = answer;
        this.iterating_times = iterating_times;
        this.shown = false;
    }

    public Card(int _id, String packName, String question, String answer, int iterating_times, boolean shown) {
        this._id = _id;
        this.packName = packName;
        this.question = question;
        this.answer = answer;
        this.iterating_times = iterating_times;
        this.shown = shown;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getPackName() {
        return packName;
    }

    public String getAnswer() {
        return answer;
    }

    public int get_id() {
        return _id;
    }

    public int getIteratingTimes() {
        return iterating_times;
    }

    public void setIteratingTimes(Integer i) {
        this.iterating_times = i;
    }

    public boolean getShown() {
        return shown;
    }

    public void setShown(boolean i) {
        this.shown = i;
    }
}