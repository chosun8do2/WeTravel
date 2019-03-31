package org.mrlee.wetravel;

import java.util.HashMap;
import java.util.Map;

public class survey {
    public int age;
    public int smoking;
    public int drinking;
    public int drink_type;
    public int noize;
    public int type;
    public int picture;
    public int hotel;
    public int car;

    public survey(){

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("age", age);
        result.put("smoking", smoking);
        result.put("drinking", drinking);
        result.put("drink_type", drink_type);
        result.put("noize", noize);
        result.put("type", type); // 0: 남자 1 : 여자
        result.put("picture", picture);
        result.put("hotel", hotel);
        result.put("car", car);
        return result;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSmoking() {
        return smoking;
    }

    public void setSmoking(int smoking) {
        this.smoking = smoking;
    }

    public int getDrinking() {
        return drinking;
    }

    public void setDrinking(int drinking) {
        this.drinking = drinking;
    }

    public int getDrink_type() {
        return drink_type;
    }

    public void setDrink_type(int drink_type) {
        this.drink_type = drink_type;
    }

    public int getNoize() {
        return noize;
    }

    public void setNoize(int noize) {
        this.noize = noize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getHotel() {
        return hotel;
    }

    public void setHotel(int hotel) {
        this.hotel = hotel;
    }

    public int getCar() {
        return car;
    }

    public void setCar(int car) {
        this.car = car;
    }



    public survey(int age, int smoking, int drinking, int drink_type, int noize, int type, int picture, int hotel, int car) {
        this.age = age;
        this.smoking = smoking;
        this.drinking = drinking;
        this.drink_type = drink_type;
        this.noize = noize;
        this.type = type;
        this.picture = picture;
        this.hotel = hotel;
        this.car = car;
    }
}
