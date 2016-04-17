package com.logitrips.driver.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Ulzii on 2/1/2016.
 */
public class OffDay {
    private int offday_id;
    private Date date;
    private String time_end;
    private String time_start;

    private String reason;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }


    public int getOffday_id() {
        return offday_id;
    }

    public void setOffday_id(int offday_id) {
        this.offday_id = offday_id;
    }



    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
