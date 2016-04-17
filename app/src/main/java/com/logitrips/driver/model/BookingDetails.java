package com.logitrips.driver.model;

import java.util.List;

/**
 * Created by Ulziiburen on 2/17/2016.
 */
public class BookingDetails {
    private String date ;
    private String location ;
    private int hours ;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    private String time ;

}
