package com.example.clug_bobple;

public class Comment {
    String user_name;
    String entrance_year;
    String comment_content;
    String date;

    public Comment(String user_name, String entrance_year, String comment_content, String date) {
        this.user_name = user_name;
        this.entrance_year = entrance_year;
        this.comment_content = comment_content;
        this.date = date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEntrance_year() {
        return entrance_year;
    }

    public void setEntrance_year(String entrance_year) {
        this.entrance_year = entrance_year;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
