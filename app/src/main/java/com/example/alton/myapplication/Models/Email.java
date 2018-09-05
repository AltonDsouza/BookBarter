package com.example.alton.myapplication.Models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by alton on 2/21/2018.
 */

public class Email implements Serializable {
    String email;
    String  message;
    String date;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Email{" +
                "email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
