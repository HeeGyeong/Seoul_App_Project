package com.example.heegyeong.project_second;

/**
 * Created by Heegyeong on 2016-11-14.
 */
public class ChatData {
    static private String message;
    static private float rating;

    public ChatData() { }

    public ChatData(String message, float rating) {
        this.message = message;
        this.rating = rating;
    }

    static public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    static public float getRating(){ return rating;}

    public void setRating(float rating) { this.rating = rating;}
}