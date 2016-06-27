package com.example.vlad.test;

import android.net.Uri;

/**
 * Simple class that holds all needed information from the news post
 */
public class Post {
    /**
     * news post unique identifier
     */
    private int id;
    /**
     * news post message
     */
    private String message;
    /**
     * Url to an image inside of a news post
     */
    private Uri imageUrl;

    public Post(int id, String message, Uri imageUri) {
        this.id = id;
        this.message = message;
        this.imageUrl = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }
}
