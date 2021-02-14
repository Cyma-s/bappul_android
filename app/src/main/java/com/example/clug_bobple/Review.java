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

    public int getStar_rate() {
        return star_rate;
    }

    public void setStar_rate(int star_rate) {
        this.star_rate = star_rate;
    }

    public Review(String name, String date, String content, int star_rate) {
        this.name = name;
        this.date = date;
        this.content = content;
        this.star_rate = star_rate;
    }

    String name;
    String date;
    String content;
    int star_rate;
}