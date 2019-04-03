package org.mrlee.wetravel;

import java.util.HashMap;
import java.util.Map;

public class Notice {
    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Notice(){

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
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        return result;
    }

    public String title;
    public String content;
}
