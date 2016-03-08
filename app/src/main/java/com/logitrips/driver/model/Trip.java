package com.logitrips.driver.model;

/**
 * Created by Ulziiburen on 2/17/2016.
 */
public class Trip {
    private int trip_id;
    private int trip_status;

    private double total_fee;

    private String username;
    private String user_country;
    private String user_pic_url;
    private String destination;
    private String car_pic_url;
    private String start_date;
    private String end_date;
    private String booking_date;

    private String duration;
    private String location_pick;
    private String time_pick;
    private String location_drop;
    private String time_drop;


    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public int getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(int trip_status) {
        this.trip_status = trip_status;
    }

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_country() {
        return user_country;
    }

    public void setUser_country(String user_country) {
        this.user_country = user_country;
    }

    public String getUser_pic_url() {
        return user_pic_url;
    }

    public void setUser_pic_url(String user_pic_url) {
        this.user_pic_url = user_pic_url;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCar_pic_url() {
        return car_pic_url;
    }

    public void setCar_pic_url(String car_pic_url) {
        this.car_pic_url = car_pic_url;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocation_pick() {
        return location_pick;
    }

    public void setLocation_pick(String location_pick) {
        this.location_pick = location_pick;
    }

    public String getTime_pick() {
        return time_pick;
    }

    public void setTime_pick(String time_pick) {
        this.time_pick = time_pick;
    }

    public String getLocation_drop() {
        return location_drop;
    }

    public void setLocation_drop(String location_drop) {
        this.location_drop = location_drop;
    }

    public String getTime_drop() {
        return time_drop;
    }

    public void setTime_drop(String time_drop) {
        this.time_drop = time_drop;
    }
}
