package com.logitrips.driver.model;

/**
 * Created by Ulzii on 2/1/2016.
 */
public class Chat {
    private int user_id;
    private int driver_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }

    public int is_recv() {
        return is_recv;
    }

    public void setIs_recv(int is_recv) {
        this.is_recv = is_recv;
    }

    private String message;
    private String date_sent;
    private int is_recv;

}
