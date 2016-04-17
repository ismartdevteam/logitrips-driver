package com.logitrips.driver.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.logitrips.driver.R;
import com.logitrips.driver.util.Services;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateOffDay extends AppCompatActivity implements View.OnClickListener {
    Bundle b;
    private TimePicker timePicker;
    private TextView date;
    private EditText reason;
    private TextView start_time;
    private TextView end_time;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd");
    SimpleDateFormat Fullformat = new SimpleDateFormat("dd MMMM  yyyy", Locale.US);
    Date selDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_off);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.create_off_day);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        b = getIntent().getExtras();
        timePicker = (TimePicker) findViewById(R.id.off_timepicker);
        reason = (EditText) findViewById(R.id.off_reason_edit);
        date = (TextView) findViewById(R.id.off_pick_day);
        start_time = (TextView) findViewById(R.id.off_start_time);
        end_time = (TextView) findViewById(R.id.off_end_time);

        start_time.setOnClickListener(this);
        end_time.setOnClickListener(this);
        timePicker = (TimePicker) findViewById(R.id.off_timepicker);
        timePicker.setIs24HourView(true);
        date.setText(b.getString("date"));
        try {
            selDate = Fullformat.parse(b.getString("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_off_day, menu);

        return true;
    }

    private void back() {
        finish();
        Intent intent = new Intent(CreateOffDay.this, OffDays.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            back();
        }
        if (item.getItemId() == R.id.action_create_off) {
            if (reason.getText().toString().length() > 1)

                Services.createOffday(CreateOffDay.this, format.format(selDate), start_time.getText().toString(),
                        end_time.getText().toString(), reason.getText().toString(), b.getString("driver_id"));

            else
                Toast.makeText(CreateOffDay.this, R.string.write_reason, Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        timePicker.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.off_start_time:
                timePicker.setOnTimeChangedListener(null);
                String[] sTimes = start_time.getText().toString().split(":");
                if (android.os.Build.VERSION.SDK_INT < 23) {
                    timePicker.setCurrentHour(Integer.parseInt(sTimes[0]));
                    timePicker.setCurrentMinute(Integer.parseInt(sTimes[1]));
                } else {
                    timePicker.setHour(Integer.parseInt(sTimes[0]));
                    timePicker.setMinute(Integer.parseInt(sTimes[1]));
                }
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        String hour = hourOfDay + "";
                        String min = "" + minute;
                        if (minute < 10)
                            min = "0" + minute;
                        if (hourOfDay < 10)
                            hour = "0" + hourOfDay;


                        start_time.setText(hour + ":" + min);
                    }
                });
                break;
            case R.id.off_end_time:
                timePicker.setOnTimeChangedListener(null);
                String[] eTimes = end_time.getText().toString().split(":");
                if (android.os.Build.VERSION.SDK_INT < 23) {
                    timePicker.setCurrentHour(Integer.parseInt(eTimes[0]));
                    timePicker.setCurrentMinute(Integer.parseInt(eTimes[1]));
                } else {
                    timePicker.setHour(Integer.parseInt(eTimes[0]));
                    timePicker.setMinute(Integer.parseInt(eTimes[1]));
                }

                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        String hour = hourOfDay + "";
                        String min = "" + minute;
                        if (minute < 10)
                            min = "0" + minute;
                        if (hourOfDay < 10)
                            hour = "0" + hourOfDay;


                        end_time.setText(hour + ":" + min);
                    }
                });
                break;


        }

    }
}
