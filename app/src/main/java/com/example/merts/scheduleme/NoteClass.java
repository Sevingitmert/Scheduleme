package com.example.merts.scheduleme;

import com.google.firebase.firestore.Exclude;

/**
 * Created by furkan on 26.04.2018.
 */

public class NoteClass {
    private String email;
    private String documentId;
    private String title;
    private String description;


    public NoteClass(){
        //public no-arg constructor needed
    }

    public NoteClass(String email, String title, String description){
        this.email = email;
        this.title=title;
        this.description=description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }


}
