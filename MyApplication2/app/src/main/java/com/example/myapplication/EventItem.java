package com.example.myapplication;

public class EventItem {
    private String title;
    private long date;

    public EventItem(String title, long date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public long getDate() {
        return date;
    }
}


