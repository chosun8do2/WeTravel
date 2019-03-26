package org.mrlee.wetravel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lee on 2019-02-20.
 */

public class Board {
    private String user;
    private String title;

    public String getStartday() {
        return startday;
    }

    public void setStartday(String startday) {
        this.startday = startday;
    }

    public String getEndday() {
        return endday;
    }

    public void setEndday(String endday) {
        this.endday = endday;
    }

    private String startday;
    private String endday;
    public Board(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public Board(String user, String image, String title, String content, String startday, String endday) {
        this.user = user;
        this.image = image;
        this.title = title;
        this.content = content;
        this.startday = startday;
        this.endday = endday;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("image", image);
        result.put("title", title);
        result.put("content", content);
        result.put("startday", startday);
        result.put("endday", endday);
        return result;
    }
}
