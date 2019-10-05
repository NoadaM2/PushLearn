package com.noadam.pushlearn.entities;

public class Card {

    private int _id;
    private String packName;
    private String question;
    private String answer;
    private int iterating_times;

    public Card(String packName, String question, String answer) {
        this.packName = packName;
        this.question = question;
        this.answer = answer;
    }

    public Card(int _id, String packName, String question, String answer) {
        this._id = _id;
        this.packName = packName;
        this.question = question;
        this.answer = answer;
    }

    public Card(int _id, String packName, String question, String answer, int iterating_times) {
        this._id = _id;
        this.packName = packName;
        this.question = question;
        this.answer = answer;
        this.iterating_times = iterating_times;
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

    public int get_iterating_times() {
        return iterating_times;
    }

    public void set_iterating_times(Integer i) {
        this.iterating_times = i;
    }
}