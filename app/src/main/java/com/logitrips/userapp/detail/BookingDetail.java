package com.logitrips.userapp.detail;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.logitrips.userapp.MenuAc;
import com.logitrips.userapp.R;
import com.logitrips.userapp.adapter.BookingDetAdapter;
import com.logitrips.userapp.model.Booking;
import com.logitrips.userapp.model.Car;
import com.logitrips.userapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookingDetail extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private BookingDetAdapter adapter;
    private List<Booking> bookingList = new ArrayList<>();
    public static final int ADD_REQ = 255;
    private ActionBar actionBar;
    private TextView submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.booking_list);
        submit = (TextView) findViewById(R.id.booking_submit);
        submit.setOnClickListener(this);

        adapter = new BookingDetAdapter(bookingList, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQ) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Booking booking = (Booking) data.getSerializableExtra("Booking");
                Log.d("booking", booking.getStart_time());
                bookingList.add(booking);
                adapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.add_day_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (bookingList.size() < 1) {
            Toast.makeText(getApplicationContext(), "Please add day", Toast.LENGTH_LONG).show();
        } else {

            try {
                JSONObject object = new JSONObject();
                object.put("location", 1);
                JSONArray array = new JSONArray();
                for (int i = 0; i < bookingList.size(); i++) {
                    Booking booking = bookingList.get(i);
                    JSONObject obj = new JSONObject();
                    obj.put("date", booking.getDate().replace("/", "-"));
                    obj.put("hours", booking.getHours());
                    obj.put("time", booking.getStart_time());
                    obj.put("location_id", booking.getPick_loc());
                    array.put(obj);
                }
                object.put("booking_detail", array);
                Log.e("json", object.toString());

                Intent intent = new Intent(BookingDetail.this, MenuAc.class);
                Bundle b = new Bundle();
                b.putString("json", object.toString());
                intent.putExtras(b);
                startActivity(intent);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
