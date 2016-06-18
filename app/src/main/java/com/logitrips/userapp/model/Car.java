package com.logitrips.userapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ulziiburen on 12/21/15.
 */
@DatabaseTable
public class Car implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int car_id;
    @DatabaseField
    private String car_model;
    @DatabaseField
    private int year;
    @DatabaseField
    private String car_class;
    @DatabaseField
    private double car_rating;
    @DatabaseField
    private String car_pic_urls;
    @DatabaseField
    private String start_date;
    @DatabaseField
    private String end_date;
    @DatabaseField
    private int trip_status;
    @DatabaseField
    private int payment_status;
    @DatabaseField
    private double total_fee;


    @DatabaseField
    private String driver_langStr;

    @DatabaseField
    private int location;
    @DatabaseField
    private double hourly_price;
    @DatabaseField
    private double daily_price;
    @DatabaseField
    private double day2_price;
    @DatabaseField
    private int driver_id;
    @DatabaseField
    private String driver_name;
    @DatabaseField
    private String driver_pic_url;
    @DatabaseField
    private String driver_knowledge;
    @DatabaseField
    private String lang_driver;
    @DatabaseField
    private int driver_year;
    @DatabaseField
    private int driver_smoking;
    @DatabaseField
    private String location_drop;
    @DatabaseField
    private String location_pick;
    @DatabaseField
    private String time_pick;
    @DatabaseField
    private String time_drop;
    public String getDriver_langStr() {
        return driver_langStr;
    }

    public void setDriver_langStr(String driver_langStr) {
        this.driver_langStr = driver_langStr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getLocation_pick() {
        return location_pick;
    }

    public void setLocation_pick(String location_pick) {
        this.location_pick = location_pick;
    }


    public String getLocation_drop() {
        return location_drop;
    }

    public void setLocation_drop(String location_drop) {
        this.location_drop = location_drop;
    }

    public String getTime_pick() {
        return time_pick;
    }

    public void setTime_pick(String time_pick) {
        this.time_pick = time_pick;
    }

    public String getTime_drop() {
        return time_drop;
    }

    public void setTime_drop(String time_drop) {
        this.time_drop = time_drop;
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

    public int getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(int trip_status) {
        this.trip_status = trip_status;
    }

    public int getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(int payment_status) {
        this.payment_status = payment_status;
    }

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }


    public String getLang_driver() {
        return lang_driver;
    }

    public void setLang_driver(String lang_driver) {
        this.lang_driver = lang_driver;
    }


    public int getDriver_smoking() {
        return driver_smoking;
    }

    public void setDriver_smoking(int driver_smoking) {
        this.driver_smoking = driver_smoking;
    }



    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCar_class() {
        return car_class;
    }

    public void setCar_class(String car_class) {
        this.car_class = car_class;
    }

    public double getCar_rating() {
        return car_rating;
    }

    public void setCar_rating(double car_rating) {
        this.car_rating = car_rating;
    }

    public String getCar_pic_urls() {
        return car_pic_urls;
    }

    public void setCar_pic_urls(String car_pic_urls) {
        this.car_pic_urls = car_pic_urls;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public double getHourly_price() {
        return hourly_price;
    }

    public void setHourly_price(double hourly_price) {
        this.hourly_price = hourly_price;
    }

    public double getDaily_price() {
        return daily_price;
    }

    public void setDaily_price(double daily_price) {
        this.daily_price = daily_price;
    }

    public double getDay2_price() {
        return day2_price;
    }

    public void setDay2_price(double day2_price) {
        this.day2_price = day2_price;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_pic_url() {
        return driver_pic_url;
    }

    public void setDriver_pic_url(String driver_pic_url) {
        this.driver_pic_url = driver_pic_url;
    }

    public String getDriver_knowledge() {
        return driver_knowledge;
    }

    public void setDriver_knowledge(String driver_knowledge) {
        this.driver_knowledge = driver_knowledge;
    }

    public int getDriver_year() {
        return driver_year;
    }

    public void setDriver_year(int driver_year) {
        this.driver_year = driver_year;
    }


}
