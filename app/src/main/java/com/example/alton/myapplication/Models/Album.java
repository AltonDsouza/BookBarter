package com.example.alton.myapplication.Models;

import java.io.Serializable;

/**
 * Created by alton on 1/9/2018.
 */

public class Album implements Serializable
{
    private String uid;
    private String bookname;
    private String author;

    private int thumbnail;


    public Album(String bookname, String author, int thumbnail) {
        this.bookname = bookname;
        this.author = author;
        this.thumbnail = thumbnail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Album{" +
                "bookname='" + bookname + '\'' +
                ", author='" + author + '\'' +
                ", thumbnail=" + thumbnail +
                '}';
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
