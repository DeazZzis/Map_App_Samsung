package com.example.deathis.myapplication;

import java.util.ArrayList;

public class Post {

    private String title,lat, lng, text, time, author;


    public String getLat() {
        return lat;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getLng() {
        return lng;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
