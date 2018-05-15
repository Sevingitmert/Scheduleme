package com.example.merts.scheduleme;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

/**
 * Created by furkan on 1.05.2018.
 */

public class AlarmClass {
    private String email;
    private String documentId;
    private String title;
    private String description;
    private String time;
    private String day;

    public AlarmClass() {
        //public no-arg constructor needed
    }

    public AlarmClass(String email, String title, String description, String time, String day) {
        this.email = email;
        this.title = title;
        this.description = description;
        this.time = time;
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
