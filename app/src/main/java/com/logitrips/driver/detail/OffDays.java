package com.logitrips.driver.detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.logitrips.driver.R;
import com.logitrips.driver.model.OffDay;
import com.logitrips.driver.util.CustomRequest;
import com.logitrips.driver.util.MySingleton;
import com.logitrips.driver.util.Services;
import com.logitrips.driver.util.Utils;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class OffDays extends AppCompatActivity {
    private CalendarPickerView calendar;
    private Bundle b;
    private String driver_id = "";
    private List<OffDay> offDayList;
    private List<Date> dates;
    private List<String> reasons;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd");
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_days);
         progressDialog = ProgressDialog.show(this,
                "", getString(R.string.loading));
        offDayList = new ArrayList<OffDay>();
        dates = new ArrayList<Date>();
        reasons = new ArrayList<String>();
        b = getIntent().getExtras();
        driver_id = b.getString("driver_id", "0");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Date today = new Date();
        calendar.init(new Date(today.getMonth() - 1), nextYear.getTime())
                .withSelectedDate(today)
                .inMode(CalendarPickerView.SelectionMode.RANGE);

        getData(driver_id);
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
        calendar.setFilterText("select not highlighted dates");

        calendar.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter() {
            @Override
            public boolean isDateSelectable(Date date) {
                if (dates.contains(date)) {
                    int pos = dates.indexOf(date);

                    Snackbar.make(calendar, reasons.get(pos), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return false;
                } else {
                    return true;
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.action_off_days) {
            if (calendar.getSelectedDates().size() > 0) {
                final List<Date> selDates = calendar.getSelectedDates();

                AlertDialog.Builder alert = new AlertDialog.Builder(OffDays.this);
                final EditText edittext = new EditText(getApplicationContext());

                alert.setTitle("Write a reason");

                edittext.setTextColor(Color.BLACK);
                alert.setView(edittext);
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dates.addAll(selDates);
                        for (int i = 0; i < selDates.size(); i++) {
                            reasons.add(edittext.getText().toString());
                        }
                        calendar.highlightDates(selDates);
                        Date startDate = selDates.get(0);
                        Date endDate = selDates.get(selDates.size() - 1);
                        Log.e("dates", startDate + "-" + endDate);

                        Services.createOffday(OffDays.this, format.format(startDate), format.format(endDate), edittext.getText().toString(), driver_id);


                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.select_date, Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.off_day, menu);

        return true;
    }


    private void getData(String driver_id) {

        Log.e("url", getString(R.string.main_url) + "/mobiledriver/getoffdays?driver_id=" + driver_id);
        if (!Utils.isNetworkAvailable(OffDays.this)) {
            progressDialog.dismiss();
            Toast.makeText(OffDays.this, R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {
            CustomRequest daysReq = new CustomRequest(Request.Method.GET,
                    getString(R.string.main_url) + "/mobiledriver/getoffdays?driver_id=" + driver_id, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    Log.e("daysReq data", response.toString());
                    try {
                        switch (response.getInt("response")) {
                            case 200:

                                getDays(response.getJSONArray("data"));
                                break;

                            case 100:
                                Toast.makeText(OffDays.this, R.string.error_request, Toast.LENGTH_SHORT).show();
                                break;
                            case 300:
                                Toast.makeText(OffDays.this, R.string.no_available_cars, Toast.LENGTH_SHORT).show();
                                break;
                        }

                        calendar.highlightDates(dates);
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    // TODO Auto-generated method stub
                    Toast.makeText(OffDays.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }

            });
            MySingleton.getInstance(OffDays.this).addToRequestQueue(daysReq);
        }

    }

    private void getDays(JSONArray array) {


        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                OffDay offDay = new OffDay();
                offDay.setOffday_id(obj.getInt("offday_id"));

                offDay.setReason(obj.getString("reason"));
                Date sdate = format.parse(obj.getString("date_start"));
                Date edate = format.parse(obj.getString("date_end"));
                getDaysBetweenDates(sdate, edate, offDay.getReason());

//                offDayList.add(offDay);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public void getDaysBetweenDates(Date startdate, Date enddate, String reason) {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate)) {
            Date result = calendar.getTime();

            dates.add(result);
            reasons.add(reason);
            calendar.add(Calendar.DATE, 1);
        }

    }
}
