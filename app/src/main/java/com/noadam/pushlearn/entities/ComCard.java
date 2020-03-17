package com.noadam.pushlearn.entities;

public class ComCard {

    private int _id;
    private int packID;
    private String question;
    private String answer;

    public ComCard(int _id, int packID, String question, String answer) {
        this._id = _id;
        this.packID = packID;
        this.question = question;
        this.answer = answer;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getPackID() {
        return packID;
    }

    public void setPackID(int packID) {
        this.packID = packID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
