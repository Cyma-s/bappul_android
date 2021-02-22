package com.example.clug_bobple;

public class Bapyak {

    public Bapyak(String title, String user_name, String entrance_year, String bapyak_date) {
        this.title = title;
        this.user_name = user_name;
        this.entrance_year = entrance_year;
        this.bapyak_date = bapyak_date;
    }

    public String getTitle() {
        return title;
    }

    public String getBapyak_date() {
        return bapyak_date;
    }

    public void setBapyak_date(String bapyak_date) {
        this.bapyak_date = bapyak_date;
    }

    public void setTitle(String title) {
        this.title = title;
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

    String title;
    String user_name;
    String entrance_year;
    String bapyak_date;
}