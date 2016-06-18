package com.logitrips.userapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ulziiburen on 1/22/2016.
 */
@DatabaseTable(tableName = "favouritecars")
public class FavouriteCars {


    @DatabaseField(generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    @DatabaseField
    private int car_id;
}
