package com.example.alton.myapplication.Models;

import java.io.Serializable;

/**
 * Created by alton on 2/14/2018.
 */

public class Review implements Serializable{
    String review;
    String emailId;
    String bookName;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "Review{" +
                "review='" + review + '\'' +
                ", emailId='" + emailId + '\'' +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
