package com.example.clug_bobple;

public class Recent {
    String home_title;
    String home_content;
    String home_username;

    public String getHome_username() {
        return home_username;
    }

    public void setHome_username(String home_username) {
        this.home_username = home_username;
    }

    public Recent(String home_title, String home_content, String home_username) {
        this.home_title = home_title;
        this.home_content = home_content;
        this.home_username = home_username;
    }

    public String getHome_title() {
        return home_title;
    }

    public void setHome_title(String home_title) {
        this.home_title = home_title;
    }

    public String getHome_content() {
        return home_content;
    }

    public void setHome_content(String home_content) {
        this.home_content = home_content;
    }


}
