package com.logitrips.userapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ulzii on 6/3/2016.
 */
@DatabaseTable
public class DriverLanguage {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String lang;
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

    public String getLang() {
        return lang;
    }

    public void setLang(String url) {
        this.lang = url;
    }
}
