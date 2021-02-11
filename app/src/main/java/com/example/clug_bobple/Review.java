package com.example.clug_bobple;

public class Review {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Review(String name, String date, String content) {
        this.name = name;
        this.date = date;
        this.content = content;
    }

    String name;
    String date;
    String content;

}