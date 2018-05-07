package com.example.merts.scheduleme;

import com.google.firebase.firestore.Exclude;

/**
 * Created by furkan on 5.05.2018.
 */

public class LocationClass {
    private String email;
    private String documentId;
    private String title;
    private String description;
    private String address;

    public LocationClass() {
    }

    public LocationClass(String email, String title, String description, String address) {
        this.email=email;
        this.title=title;
        this.description=description;
        this.address=address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
