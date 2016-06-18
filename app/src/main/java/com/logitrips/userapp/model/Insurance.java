package com.logitrips.userapp.model;

import java.io.Serializable;

/**
 * Created by Ulzii on 2/1/2016.
 */
public class Insurance implements Serializable {
    private int number;
    private String fullname;
    private String birthday;
    private String contry;
    private String passport_num;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getContry() {
        return contry;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPassport_num() {
        return passport_num;
    }

    public void setPassport_num(String passport_num) {
        this.passport_num = passport_num;
    }
}
