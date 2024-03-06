package com.example.devdrops.model;

public class AnswerModel {
   String  Answer;
   String postedby;
   Long postedAt;
     int like;

    public AnswerModel(String answer, String postedby, Long postedAt, int like) {
        Answer = answer;
        this.postedby = postedby;
        this.postedAt = postedAt;
        this.like = like;
    }

    public AnswerModel() {
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getPostedby() {
        return postedby;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

    public Long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Long postedAt) {
        this.postedAt = postedAt;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
