package com.logitrips.userapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ulzii on 6/3/2016.
 */
@DatabaseTable
public class CarPic {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String url;
    @DatabaseField(foreign = true)
    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
