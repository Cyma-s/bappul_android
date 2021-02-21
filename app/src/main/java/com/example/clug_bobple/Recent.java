package com.example.clug_bobple;

public class Recent {
    String home_title;
    String home_content;

    public Recent(String home_title, String home_content) {
        this.home_title = home_title;
        this.home_content = home_content;
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
