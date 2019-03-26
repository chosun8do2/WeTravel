package org.mrlee.wetravel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lee on 2019-02-07.
 */

public class User {
    public User(){

    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(String makeDate) {
        this.makeDate = makeDate;
    }
    public User(String email, String makeDate, String name, String language, String location, int gender) {
        this.email = email;
        this.makeDate = makeDate;
        this.name = name;
        this.language = language;
        this.location = location;
        this.gender = gender;
        //this.uid = uid;
        //this.pushToken = pushToken;
        //this.comment = comment;
        //this.profileImageUrl = profileImageUrl;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("makeDate", makeDate);
        result.put("name", name);
        result.put("language", language);
        result.put("location", location);
        result.put("gender", gender); // 0: 남자 1 : 여자
        //result.put("uid", uid);
        //result.put("pushToken", pushToken);
        //result.put("comment", comment);
        //result.put("profileImageUrl", profileImageUrl);
        return result;
    }

    private String email;

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String makeDate;
    private String name;
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    private int gender;

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public String getLocation() {
        return location;
    }

    private String language;
    private String location;
}
