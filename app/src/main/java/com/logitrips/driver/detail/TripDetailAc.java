package com.logitrips.driver.detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.logitrips.driver.MainActivity;
import com.logitrips.driver.R;
import com.logitrips.driver.model.Trip;
import com.logitrips.driver.model.BookingDetails;
import com.logitrips.driver.util.CircleImageView;
import com.logitrips.driver.util.CustomRequest;
import com.logitrips.driver.util.MySingleton;
import com.logitrips.driver.util.Utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripDetailAc extends AppCompatActivity implements View.OnClickListener {
    private Trip trip;
    private Bundle b;
    private TextView country;
    private TextView destination;

    private TextView pick_location;

    private TextView trip_dates;
    private TextView driver;
    private TextView trip_det_booking_label;
    private TextView status;
    private TextView total_fee;
    private CircleImageView driver_img;
    private ImageLoader mImageLoader;
    private LinearLayout booking_details;
    private LinearLayout trip_det_button_lin;
    private NetworkImageView image;
    private Button decline;
    private Button approve;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd");
    SimpleDateFormat monthFormat = new SimpleDateFormat("dd MMM  yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        trip = (Trip) getIntent().getSerializableExtra("Trip");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageLoader = MySingleton.getInstance(this).getImageLoader();
        try {
            initView();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initView() throws ParseException, JSONException {

        trip_dates = (TextView) findViewById(R.id.trip_det_dates);
        country = (TextView) findViewById(R.id.trip_det_country);
        destination = (TextView) findViewById(R.id.trip_det_destination);
        driver = (TextView) findViewById(R.id.trip_det_driver_name);
        total_fee = (TextView) findViewById(R.id.trip_det_fee);
        trip_det_booking_label = (TextView) findViewById(R.id.trip_det_booking_label);
        image = (NetworkImageView) findViewById(R.id.trip_det_image);


        status = (TextView) findViewById(R.id.trip_det_status);
        driver_img = (CircleImageView) findViewById(R.id.trip_det_driver_img);
        driver_img.setImageUrl(getString(R.string.driver_image_url) + trip.getUser_pic_url(), mImageLoader);
        driver_img.setDefaultImageResId(R.drawable.nodriver);
        pick_location = (TextView) findViewById(R.id.trip_det_pick);
        pick_location.setText(trip.getLocation_pick());
        booking_details = (LinearLayout) findViewById(R.id.trip_det_records);
        trip_det_button_lin = (LinearLayout) findViewById(R.id.trip_det_button_lin);
        decline = (Button) findViewById(R.id.trip_det_decline);
        approve = (Button) findViewById(R.id.trip_det_approve);
        decline.setOnClickListener(this);
        approve.setOnClickListener(this);
        //set


        image.setImageUrl(trip.getCar_pic_url(), mImageLoader);
        country.setText(trip.getUser_country());
        driver.setText(trip.getUsername());
        destination.setText(trip.getDestination() + "");
        Date startDate = format.parse(trip.getStart_date());
        Date endDate = format.parse(trip.getEnd_date());
        trip_dates.setText(monthFormat.format(startDate) + " - " + monthFormat.format(endDate));
        total_fee.setText( trip.getTotal_fee());


        JSONArray booking_detail = new JSONArray(trip.getDetails());
        List<BookingDetails> bookDetails = new ArrayList<BookingDetails>();
        if (booking_detail.length() == 0)
            trip_det_booking_label.setVisibility(View.GONE);
        for (int k = 0; k < booking_detail.length(); k++) {
            JSONObject tripDetObj = booking_detail.getJSONObject(k);
            BookingDetails bookingDetails = new BookingDetails();
            bookingDetails.setDate(tripDetObj.getString("date"));
            bookingDetails.setLocation(tripDetObj.getString("location"));
            bookingDetails.setHours(tripDetObj.getInt("hours"));
            bookingDetails.setTime(tripDetObj.getString("time"));
            bookDetails.add(bookingDetails);
        }

        for (int i = 0; i < bookDetails.size(); i++) {
            BookingDetails bookingDetail = bookDetails.get(i);
            View v = getLayoutInflater().inflate(R.layout.trip_det_item, null);
            TextView book_loc = (TextView) v.findViewById(R.id.book_det_loc);
            TextView book_date = (TextView) v.findViewById(R.id.book_det_date);
            String endTime = (Integer.parseInt(bookingDetail.getTime().split(":")[0]) + bookingDetail.getHours()) + ":00";
            String startTime = bookingDetail.getTime().split(":")[0] + ":" + bookingDetail.getTime().split(":")[1];
            book_date.setText(monthFormat.format(format.parse(bookingDetail.getDate())) + ", " + startTime + "-" + endTime);
            booking_details.addView(v);
        }
        changeStatus(trip.getTrip_status());
    }

    private void changeStatus(int code) {
        if (code != 0)
            trip_det_button_lin.setVisibility(View.GONE);
        status.setText(Utils.trip_status[code]);
        status.setBackgroundDrawable(getResources().getDrawable(Utils.trip_status_col[code]));
    }

    private void approve(final int is_approve, final String trip_id) {
        if (!Utils.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), R.string.no_net, Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(TripDetailAc.this,
                "", getString(R.string.loading));
        CustomRequest request = new CustomRequest(Request.Method.POST,
                getString(R.string.main_url) + "/mobiledriver/updatetrip", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("create off day res", response.toString());
                try {
                    switch (response.getInt("response")) {
                        case 200:
                            switch (is_approve) {
                                case 0:
                                    changeStatus(2);
                                    Toast.makeText(getApplicationContext(), R.string.successfully_desclined, Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    changeStatus(1);
                                    Toast.makeText(getApplicationContext(), R.string.successfully_desclined, Toast.LENGTH_SHORT).show();
                                    break;
                            }

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
                params.put("trip_id", trip_id + "");
                params.put("is_approve", is_approve + "");

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(TripDetailAc.this, MainActivity.class));
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int code = 0;
        if (v == approve) {
            code = 1;
        }
        approve(code, trip.getTrip_id() + "");

    }
}
