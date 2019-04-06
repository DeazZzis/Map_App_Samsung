package com.example.deathis.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post {

    private String title, lat, lng, text, time, author;
    private ArrayList<Rep> rep_up, rep_down;

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


    public ArrayList<Rep> getRep_up() {
        return rep_up;
    }

    public void setRep_up(ArrayList<Rep> rep_up) {
        this.rep_up = rep_up;
    }

    public ArrayList<Rep> getRep_down() {
        return rep_down;
    }

    public void setRep_down(ArrayList<Rep> rep_down) {
        this.rep_down = rep_down;
    }
}
