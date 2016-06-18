package com.logitrips.userapp.model;

/**
 * Created by Ulziiburen on 1/22/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static String databaseName = "logitrips.ormlite";
    private static int databaseVersion = 1;
    Dao<FavouriteCars, Integer> favCarDao = null;
    Dao<Car, Integer> carDao = null;

    private static DatabaseHelper helper = null;

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
        // TODO Auto-generated constructor stub

    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DatabaseHelper(context);
        }

        return helper;
    }

    // creating tables
    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
        // TODO Auto-generated method stub
        try {

            TableUtils.createTable(arg1, FavouriteCars.class);
            TableUtils.createTable(arg1, Car.class);


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void insertListCar(final List<Car> list) {
        DeleteCars();
        try {

            helper.getCarDao().callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {

                    for (Car obj : list) {
                        helper.getCarDao().create(obj);
                    }
                    return null;
                }

            });
        } catch (Exception e) {
            Log.d("ormlite", "updateListOfObjects. Exception " + e.toString());
        }
    }

    // updgrade tables
    @Override
    public void onUpgrade(SQLiteDatabase arg0, ConnectionSource connectionSource, int arg2,
                          int arg3) {
        // TODO Auto-generated method stub

        try {
            TableUtils.dropTable(connectionSource, FavouriteCars.class, true);
            TableUtils.dropTable(connectionSource, Car.class, true);

            onCreate(arg0, connectionSource);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Dao<FavouriteCars, Integer> getFavDao() throws SQLException {
        if (favCarDao == null) {
            favCarDao = getDao(FavouriteCars.class);
        }
        return favCarDao;
    }

    public Dao<Car, Integer> getCarDao() throws SQLException {
        if (carDao == null) {
            carDao = getDao(Car.class);
        }
        return carDao;
    }

    public void DeleteCars() {
        try {
            TableUtils.clearTable(connectionSource, Car.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void DeleteFavCars() {
        try {
            TableUtils.clearTable(connectionSource, FavouriteCars.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        favCarDao = null;
        carDao = null;

        super.close();
    }

}
