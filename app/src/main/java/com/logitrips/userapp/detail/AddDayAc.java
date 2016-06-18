
package com.logitrips.userapp.detail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.logitrips.userapp.R;
import com.logitrips.userapp.model.Booking;
import com.logitrips.userapp.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDayAc extends AppCompatActivity {
    private ActionBar actionBar;
    private TextView date;
    private TextView time;
    private Spinner hours;
    private Spinner location;
    private Booking booking = new Booking();
    TimePicker timePicker;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_day);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        initView();
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            year = arg1;
            month = arg2;
            day = arg3;
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void initView() {
        timePicker = (TimePicker) findViewById(R.id.add_day_time_picker);
        timePicker.setIs24HourView(Boolean.TRUE);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        date = (TextView) findViewById(R.id.add_day_date);
        showDate(year, month + 1, day);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.setVisibility(View.GONE);
                new DatePickerDialog(AddDayAc.this, myDateListener,
                        year, month, day).show();
            }
        });

        //timepicker
        time = (TextView) findViewById(R.id.add_day_start_time);
        String[] sTimes = time.getText().toString().split(":");
        if (android.os.Build.VERSION.SDK_INT < 23) {
            timePicker.setCurrentHour(Integer.parseInt(sTimes[0]));
            timePicker.setCurrentMinute(Integer.parseInt(sTimes[1]));
        } else {
            timePicker.setHour(Integer.parseInt(sTimes[0]));
            timePicker.setMinute(Integer.parseInt(sTimes[1]));
        }
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.setVisibility(View.VISIBLE);


            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String hour = hourOfDay + "";
                String min = "" + minute;
                if (minute < 10)
                    min = "0" + minute;
                if (hourOfDay < 10)
                    hour = "0" + hourOfDay;

//                booking.setStart_time(hour + ":" + min);
                time.setText(hour + ":" + min);

            }
        });

        hours = (Spinner) findViewById(R.id.add_day_hours);
        ArrayAdapter<String> hoursAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, Utils.add_hours);
        hoursAdapter.setDropDownViewResource(R.layout.spinner_item);
        hours.setAdapter(hoursAdapter);
        hours.setSelection(0);

        location = (Spinner) findViewById(R.id.add_day_location);
        ArrayAdapter<String> locAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, Utils.pick_up_location);
        locAdapter.setDropDownViewResource(R.layout.spinner_item);
        location.setAdapter(locAdapter);
        location.setSelection(0);
    }

    private void showDate(int year, int month, int day) {
        String monthStr = "";
        if (month < 10)
            monthStr = "0" + month;
        else
            monthStr = month + "";
        date.setText(new StringBuilder().append(year).append("/")
                .append(monthStr).append("/").append(day));
        booking.setDate(date.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_day_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent returnIntent = getIntent();
            booking.setHours(Integer.parseInt(Utils.add_hours[hours.getSelectedItemPosition()]));
            booking.setStart_time(time.getText().toString() + "");
            booking.setPick_loc(location.getSelectedItemPosition());
            returnIntent.putExtra("Booking", booking);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        if (id == android.R.id.home) {
            Intent returnIntent = getIntent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
