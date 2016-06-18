package com.logitrips.userapp.model;

import java.io.Serializable;

/**
 * Created by Ulzii on 2/1/2016.
 */
public class Booking implements Serializable {
    private int number;
    private String date;
    private String start_time;
    private String message;
    private int hours;
    private int pick_loc;

    public int getPick_loc() {
        return pick_loc;
    }

    public void setPick_loc(int pick_loc) {
        this.pick_loc = pick_loc;
    }


    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


}
