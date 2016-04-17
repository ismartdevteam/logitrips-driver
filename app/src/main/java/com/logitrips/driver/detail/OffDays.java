package com.logitrips.driver.detail;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OffDays extends AppCompatActivity {
    private CalendarPickerView calendar;
    private Bundle b;
    private String driver_id = "";
    private List<OffDay> offDayList;
    private List<Date> dates;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd");
    SimpleDateFormat Fullformat = new SimpleDateFormat("dd MMMM  yyyy", Locale.US);
    private ProgressDialog progressDialog;
    private Button removeDay;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_days);
        progressDialog = ProgressDialog.show(this,
                "", getString(R.string.loading));
        offDayList = new ArrayList<OffDay>();
        dates = new ArrayList<Date>();
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
                .inMode(CalendarPickerView.SelectionMode.SINGLE);

        getData(driver_id);

        calendar.setFilterText("select not highlighted dates");

        calendar.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter() {
            @Override
            public boolean isDateSelectable(Date date) {
                if (dates.contains(date)) {
                    pos = dates.indexOf(date);
                    OffDay offDay = offDayList.get(pos);
                    Snackbar.make(calendar, offDay.getReason()+" "+offDay.getTime_start()+" - " +offDay.getTime_end(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    removeDay.setEnabled(true);
                    return true;
                } else {
                    removeDay.setEnabled(false);
                    return true;
                }

            }
        });
        removeDay = (Button) findViewById(R.id.off_remove_day);
        removeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOffday(offDayList.get(pos));
            }
        });
    }

    private void deleteOffday(final OffDay off) {
        if (!Utils.isNetworkAvailable(OffDays.this)) {
            Toast.makeText(getApplicationContext(), R.string.no_net, Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(OffDays.this,
                "", getString(R.string.loading));
        CustomRequest request = new CustomRequest(Request.Method.POST,
                getString(R.string.main_url) + "/mobiledriver/deleteoffday", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("create off day res", response.toString());
                try {
                    switch (response.getInt("response")) {
                        case 200:
                            Toast.makeText(getApplicationContext(), R.string.delete_off_successfully, Toast.LENGTH_SHORT).show();
                            offDayList.remove(off);
                            dates.remove(pos);
                            calendar.clearHighlightedDates();
                            calendar.highlightDates(dates);
                            break;

                        default:
                            Toast.makeText(getApplicationContext(), R.string.error_request, Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.i("error", error.getMessage() + "");
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("offday_id", off.getOffday_id() + "");

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.action_off_days) {

            if (calendar.getSelectedDates().size() > 0) {
                final Date selDate = calendar.getSelectedDate();
                if (!dates.contains(selDate)) {
                    finish();
                    Bundle b = new Bundle();
                    b.putString("date", Fullformat.format(selDate));
                    b.putString("driver_id", driver_id);
                    Intent createOffDay = new Intent(OffDays.this, CreateOffDay.class);
                    createOffDay.putExtras(b);
                    startActivity(createOffDay);
                } else {
                    int pos = dates.indexOf(selDate);
                    OffDay offDay=offDayList.get(pos);
                    Snackbar.make(calendar, offDay.getReason()+"  "+offDay.getTime_start()+"-"+offDay.getTime_end(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

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

                        }


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
                offDay.setTime_end(obj.getString("time_end"));
                offDay.setTime_start(obj.getString("time_start"));
                Date date = format.parse(obj.getString("date"));
                offDay.setDate(date);
                dates.add(date);
                offDayList.add(offDay);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        calendar.highlightDates(dates);

    }

//    public void getDaysBetweenDates(Date startdate, Date enddate, String reason) {
//
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(startdate);
//
//        while (calendar.getTime().before(enddate)) {
//            Date result = calendar.getTime();
//
//            dates.add(result);
//            reasons.add(reason);
//            calendar.add(Calendar.DATE, 1);
//        }
//
//    }
}
