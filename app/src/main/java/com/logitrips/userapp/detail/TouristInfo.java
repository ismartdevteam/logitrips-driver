package com.logitrips.userapp.detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.logitrips.userapp.R;
import com.logitrips.userapp.countrycode.Country;
import com.logitrips.userapp.countrycode.CountryAdapterWithoutCode;
import com.logitrips.userapp.countrycode.OnPhoneChangedListener;
import com.logitrips.userapp.countrycode.PhoneUtils;
import com.logitrips.userapp.model.Booking;
import com.logitrips.userapp.model.Car;
import com.logitrips.userapp.model.Chat;
import com.logitrips.userapp.model.Insurance;
import com.logitrips.userapp.util.CircleImageView;
import com.logitrips.userapp.util.CustomRequest;
import com.logitrips.userapp.util.MySingleton;
import com.logitrips.userapp.util.Utils;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class TouristInfo extends AppCompatActivity implements View.OnClickListener {
    private static final int ADD_PERSON_REQ = 5641;
    private Car car;
    private Bundle b;
    private TextView location;
    private TextView car_class;
    private TextView hourly_price;
    private TextView daily_price;
    private TextView priceTv;
    private TextView insuranceTv;
    private int created_trip_id = 0;

    private TextView duration;
    private TextView car_info;
    private TextView driver;
    private TextView country_tv;
    private TextView total_price;
    private CircleImageView driver_img;


    private Spinner country_spinner;

    private EditText fullname;
    private EditText email;
    public EditText phoneEdit;
    private EditText meetLoc;
    private SharedPreferences loginsSp;
    private ImageLoader mImageLoader;
    private Calendar c;
    protected SparseArray<ArrayList<Country>> mCountriesMap = new SparseArray<ArrayList<Country>>();
    protected String mLastEnteredPhone;
    protected PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();
    protected CountryAdapterWithoutCode countryAdapter;
    private String coutryName;


    private TextView addBooking;
    private TextView addPerson;
    private LinearLayout bookingLin;
    private LinearLayout personLin;
    private List<Booking> bookingList = new ArrayList<>();
    private List<Insurance> insuranceList = new ArrayList<>();
    public static final int ADD_DAY_REQ = 255;
    private double totalPrice = 0.0;
    //paypal
    private static final String TAG = "paymentExample";

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AWyvNMO9GGke36tHZcUUZe8_CJVX4Oz2s_SMRYM-9L8nAq7BGVbynzLWywjwHlFnu2SOCY2ChjcQ6F-M";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private SharedPreferences sp;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .rememberUser(true)
            .acceptCreditCards(true)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Logitrips");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_info);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        b = getIntent().getExtras();
        loginsSp = getSharedPreferences("user", 1);
        car = (Car) getIntent().getSerializableExtra("Car");
        Log.e("car", car.getCar_class());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullname.getText().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Enter fullname", Toast.LENGTH_LONG).show();
                    return;
                }


                if (email.getText().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Enter email number", Toast.LENGTH_LONG).show();
                    return;
                }

                if (phoneEdit.getText().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Enter phone number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (meetLoc.getText().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Enter Meet-up location", Toast.LENGTH_LONG).show();
                    return;
                }


                if (totalPrice > 0 && bookingList.size() > 0) {
                    try {
                        createTrip(createJson());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Please add day", Toast.LENGTH_LONG).show();
            }
        });
        c = Calendar.getInstance();
        initView();

    }

    private void initView() {
        country_tv = (TextView) findViewById(R.id.info_country_tv);
        duration = (TextView) findViewById(R.id.info_duration);
        location = (TextView) findViewById(R.id.info_locaiton);
        car_info = (TextView) findViewById(R.id.info_car);
        driver = (TextView) findViewById(R.id.info_driver_name);
        total_price = (TextView) findViewById(R.id.info_total_price);
        driver_img = (CircleImageView) findViewById(R.id.info_driver_img);
        car_class = (TextView) findViewById(R.id.info_car_class);
        daily_price = (TextView) findViewById(R.id.info_daily_price);
        hourly_price = (TextView) findViewById(R.id.info_hourly_price);
        priceTv = (TextView) findViewById(R.id.info_price);
        insuranceTv = (TextView) findViewById(R.id.info_insurance);


        country_spinner = (Spinner) findViewById(R.id.info_country_spin);

        fullname = (EditText) findViewById(R.id.info_fullname);
        email = (EditText) findViewById(R.id.info_email);
        phoneEdit = (EditText) findViewById(R.id.info_phone);
        meetLoc = (EditText) findViewById(R.id.info_meet_up_loc);

        location.setText(Utils.dest_str[car.getLocation() - 1]);
        car_info.setText(car.getCar_model() + ", " + car.getYear());


        mImageLoader = MySingleton.getInstance(this).getImageLoader();
        car_class.setText(car.getCar_class());
        hourly_price.setText("USD " + car.getHourly_price() + " per hour");
        daily_price.setText("USD " + car.getDaily_price() + " per day");
        driver.setText(car.getDriver_name() + "");
        driver_img.setImageUrl(getString(R.string.driver_image_url) + car.getDriver_pic_url(), mImageLoader);
        driver_img.setDefaultImageResId(R.drawable.nodriver);
        fullname.setText(loginsSp.getString("name", ""));
        email.setText(loginsSp.getString("email", ""));


        // phone and country
        countryAdapter = new CountryAdapterWithoutCode(this);

        country_spinner.setAdapter(countryAdapter);
        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_tv.setText(countryAdapter.getItem(position).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        country_tv.setOnClickListener(this);


        new AsyncPhoneInitTask(this).execute();

        //calculating prices with time
        addBooking = (TextView) findViewById(R.id.info_add_booking);
        addPerson = (TextView) findViewById(R.id.info_add_person);
        addBooking.setOnClickListener(this);
        addPerson.setOnClickListener(this);
        bookingLin = (LinearLayout) findViewById(R.id.info_booking_list);
        personLin = (LinearLayout) findViewById(R.id.info_insurance_list);


        jsonToList();
        makeDuration();
        updatePrice();
    }

    private void jsonToList() {
        try {
            JSONObject bookingDetail = new JSONObject(b.getString("json"));
            JSONArray bookingAr = bookingDetail.getJSONArray("booking_detail");
            for (int i = 0; i < bookingAr.length(); i++) {
                JSONObject obj = bookingAr.getJSONObject(i);
                Booking booking = new Booking();
                booking.setNumber(i + 1);
                booking.setStart_time(obj.getString("time"));
                booking.setPick_loc(obj.getInt("location_id"));
                booking.setHours(obj.getInt("hours"));
                booking.setDate(obj.getString("date"));
                bookingList.add(booking);
                addToBooking(booking);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addToInsurance(final Insurance insurance) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.insurance_item, null);
        TextView number = (TextView) view.findViewById(R.id.person_det_number);
        TextView dateStr = (TextView) view.findViewById(R.id.person_det_birthdate);
        TextView name = (TextView) view.findViewById(R.id.person_det_name);
        TextView country = (TextView) view.findViewById(R.id.person_det_country);
        TextView passport = (TextView) view.findViewById(R.id.person_det_passport);
        ImageView delete = (ImageView) view.findViewById(R.id.person_det_delete);
        name.setText(insurance.getFullname());
        number.setText("Insurance " + (insurance.getNumber()));
        dateStr.setText(insurance.getBirthday());
        country.setText(insurance.getContry());
        passport.setText(insurance.getPassport_num());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personLin.removeView(view);
                insuranceList.remove(insurance);
                updatePrice();

            }
        });
        personLin.addView(view);
    }

    private void verifyPayment(final String paypal_id) {
        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {
            CustomRequest loginReq = new CustomRequest(Request.Method.POST,
                    getString(R.string.main_url) + "/mobile/verifypayment", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("verify data", response.toString());
                    try {
                        switch (response.getInt("response")) {
                            case 200:
                                Toast.makeText(TouristInfo.this, "Successfully verified your trip", Toast.LENGTH_SHORT).show();

                                break;

                            case 100:
                                Toast.makeText(TouristInfo.this, R.string.error_request, Toast.LENGTH_SHORT).show();
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    Toast.makeText(TouristInfo.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", loginsSp.getString("auth_key", ""));
                    return params;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", loginsSp.getInt("user_id", 0) + "");
                    params.put("driver_id", car.getDriver_id() + "");
                    params.put("payment_status", 1 + "");
                    params.put("total_fee", totalPrice + "");
                    params.put("paypal_id", paypal_id + "");
                    params.put("trip_id", created_trip_id + "");

                    return params;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(loginReq);
        }

    }

    private void callPayPal(double money, int trip_id) {
        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(money), "USD", "trip_id:" + trip_id,
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(TouristInfo.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);

    }

    private void addToBooking(final Booking booking) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.book_item, null);
        TextView number = (TextView) view.findViewById(R.id.booking_det_number);
        TextView dateStr = (TextView) view.findViewById(R.id.booking_det_date);
        TextView hours = (TextView) view.findViewById(R.id.booking_det_hours);
        TextView startTime = (TextView) view.findViewById(R.id.booking_det_start_time);
        TextView pickLoc = (TextView) view.findViewById(R.id.booking_det_pick_loc);
        ImageView delete = (ImageView) view.findViewById(R.id.booking_det_delete);
        number.setText("Booking " + (booking.getNumber()));
        dateStr.setText(booking.getDate());
        hours.setText(booking.getHours() + "");
        startTime.setText(booking.getStart_time() + "");
        pickLoc.setText(Utils.pick_up_location[booking.getPick_loc()]);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingLin.removeView(view);
                bookingList.remove(booking);
                makeDuration();
                updatePrice();

            }
        });
        bookingLin.addView(view);

    }

    private double getInsurancePrice() {
        int perPerson = insuranceList.size();
        double firstPrice = 2.5;
        double multiple = 1;
        if (bookingList.size() > 3) {

            multiple = (int) bookingList.size() / 3;
            double sda = (double) bookingList.size() / 3;
            Log.e("haha te1 ulzii", multiple + " iim bna" + sda);
            if (multiple < sda) {
                multiple = multiple + 1;
            }

        }

        return (firstPrice * multiple) * perPerson;
    }

    private void updatePrice() {
        double price = 0.0;
        double insurance = 0.0;
        for (int i = 0; i < bookingList.size(); i++) {
            int hour = bookingList.get(i).getHours();
            if (hour < 6) {
                price += hour * car.getHourly_price();
            } else {
                if (i < 1)
                    price += car.getDaily_price();
                else
                    price += car.getDay2_price();
            }

        }

        insurance = getInsurancePrice();

        priceTv.setText("USD " + price);
        insuranceTv.setText("USD " + insurance);
        totalPrice = price + insurance;
        total_price.setText("USD" + totalPrice);

    }

    private int getHour() {
        int hour = 0;
        for (Booking book : bookingList) {

            hour = hour + book.getHours();
        }
        return hour;
    }

    private void makeDuration() {

        int hour = getHour();

        String hourStr = getResources().getQuantityString(R.plurals.hour, hour, hour);
        duration.setText(hourStr);
    }

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
        if (v == addBooking) {
            Intent intent = new Intent(TouristInfo.this, AddDayAc.class);
            startActivityForResult(intent, ADD_DAY_REQ);
        }
        if (v == addPerson) {
            Intent intent = new Intent(TouristInfo.this, AddPersonAc.class);
            startActivityForResult(intent, ADD_PERSON_REQ);
        }
        if (v == country_tv) {
            country_spinner.performClick();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_DAY_REQ) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Booking booking = (Booking) data.getSerializableExtra("Booking");
                booking.setNumber(bookingList.size() + 1);
                bookingList.add(booking);
                addToBooking(booking);
                makeDuration();
                updatePrice();
            }
        }
        if (requestCode == ADD_PERSON_REQ) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Insurance insurance = (Insurance) data.getSerializableExtra("Person");
                insurance.setNumber(insuranceList.size() + 1);
                insuranceList.add(insurance);
                addToInsurance(insurance);
                updatePrice();
            }
        }
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        JSONObject res = confirm.toJSONObject().getJSONObject("response");
                        if (res.getString("state").equals("approved")) {
                            Toast.makeText(getApplicationContext(), "Successfully charged amount", Toast.LENGTH_LONG).show();
                            verifyPayment(res.getString("id"));
                        } else
                            Toast.makeText(getApplicationContext(), "Error charging amount:" + res.getString("state"), Toast.LENGTH_LONG).show();


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    class AsyncPhoneInitTask extends AsyncTask<Void, Void, ArrayList<Country>> {

        private int mSpinnerPosition = -1;
        private Context mContext;

        public AsyncPhoneInitTask(Context context) {
            mContext = context;
        }

        @Override
        protected ArrayList<Country> doInBackground(Void... params) {
            ArrayList<Country> data = new ArrayList<Country>(233);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(mContext.getApplicationContext().getAssets().open("countries.dat"), "UTF-8"));

                // do reading, usually loop until end of file reading
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    //process line
                    Country c = new Country(mContext, line, i);
                    data.add(c);
                    ArrayList<Country> list = mCountriesMap.get(c.getCountryCode());
                    if (list == null) {
                        list = new ArrayList<Country>();
                        mCountriesMap.put(c.getCountryCode(), list);
                    }
                    list.add(c);
                    i++;
                }
            } catch (IOException e) {
                //log the exception
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //log the exception
                    }
                }
            }
            String phone = "";
            if (!TextUtils.isEmpty(phone + "")) {
                return data;
            }
            String countryRegion = PhoneUtils.getCountryRegionFromPhone(mContext);
            int code = mPhoneNumberUtil.getCountryCodeForRegion(countryRegion);
            ArrayList<Country> list = mCountriesMap.get(code);
            if (list != null) {
                for (Country c : list) {
                    if (c.getPriority() == 0) {
                        mSpinnerPosition = c.getNum();
                        break;
                    }
                }
            }
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Country> data) {
            countryAdapter.addAll(data);
            if (mSpinnerPosition > 0) {
                country_spinner.setSelection(mSpinnerPosition);
                country_tv.setText(countryAdapter.getItem(mSpinnerPosition).getName());
            }
        }
    }

    protected OnPhoneChangedListener mOnPhoneChangedListener = new OnPhoneChangedListener() {
        @Override
        public void onPhoneChanged(String phone) {
            try {
                mLastEnteredPhone = phone;
                Phonenumber.PhoneNumber p = mPhoneNumberUtil.parse(phone, null);
                ArrayList<Country> list = mCountriesMap.get(p.getCountryCode());
                Country country = null;
                if (list != null) {
                    if (p.getCountryCode() == 1) {
                        String num = String.valueOf(p.getNationalNumber());
                        if (num.length() >= 3) {
                            String code = num.substring(0, 3);
                            if (CANADA_CODES.contains(code)) {
                                for (Country c : list) {
                                    // Canada has priority 1, US has priority 0
                                    if (c.getPriority() == 1) {
                                        country = c;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (country == null) {
                        for (Country c : list) {
                            if (c.getPriority() == 0) {
                                country = c;
                                break;
                            }
                        }
                    }
                }
                if (country != null) {
                    final int position = country.getNum();
                }
            } catch (NumberParseException ignore) {
            }

        }
    };

    private String createJson() {
        int user_id = loginsSp.getInt("user_id", 0);
        JSONObject tripJson = new JSONObject();
        try {

            tripJson.put("user_id", user_id);
            tripJson.put("driver_id", car.getDriver_id());
            tripJson.put("car_id", car.getCar_id());
            tripJson.put("total_fee", totalPrice);
            tripJson.put("location_pick", meetLoc.getText() + "");
            tripJson.put("payment_status", 1);
            tripJson.put("fullname", fullname.getText() + "");
            tripJson.put("phone", phoneEdit.getText() + "");
            tripJson.put("country", country_tv.getText() + "");
            //booking
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
            tripJson.put("booking_detail", array);
            //insurance
            JSONArray ins = new JSONArray();
            for (int i = 0; i < insuranceList.size(); i++) {
                Insurance insurance = insuranceList.get(i);
                JSONObject obj = new JSONObject();
                obj.put("name", insurance.getFullname());
                obj.put("birthday", insurance.getBirthday().replace("/", "-"));
                obj.put("country", insurance.getContry());
                obj.put("passport", insurance.getPassport_num());
                ins.put(obj);
            }
            tripJson.put("insurance", ins);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tripJson.toString();
    }

    private void createTrip(final String json) throws JSONException {
        if (!Utils.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {

            Log.e("json", json);
//
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getApplicationContext().getString(R.string.main_url) + "/mobile/createtrip", new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {
                    Log.e("register res", res.toString());
                    JSONObject response = null;
                    try {
                        response = new JSONObject(res);
                        if (response.getInt("response") == 200) {
                            JSONObject data = response.getJSONObject("data");
                            created_trip_id = data.getInt("trip_id");
                            callPayPal(totalPrice, created_trip_id);
                        } else
                            Toast.makeText(getApplicationContext(), "Error creating trip", Toast.LENGTH_LONG).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })


            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return json == null ? null : json.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                json, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", loginsSp.getString("auth_key", ""));
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = new String(response.data);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }

    }

    protected static final TreeSet<String> CANADA_CODES = new TreeSet<String>();

    static {
        CANADA_CODES.add("204");
        CANADA_CODES.add("236");
        CANADA_CODES.add("249");
        CANADA_CODES.add("250");
        CANADA_CODES.add("289");
        CANADA_CODES.add("306");
        CANADA_CODES.add("343");
        CANADA_CODES.add("365");
        CANADA_CODES.add("387");
        CANADA_CODES.add("403");
        CANADA_CODES.add("416");
        CANADA_CODES.add("418");
        CANADA_CODES.add("431");
        CANADA_CODES.add("437");
        CANADA_CODES.add("438");
        CANADA_CODES.add("450");
        CANADA_CODES.add("506");
        CANADA_CODES.add("514");
        CANADA_CODES.add("519");
        CANADA_CODES.add("548");
        CANADA_CODES.add("579");
        CANADA_CODES.add("581");
        CANADA_CODES.add("587");
        CANADA_CODES.add("604");
        CANADA_CODES.add("613");
        CANADA_CODES.add("639");
        CANADA_CODES.add("647");
        CANADA_CODES.add("672");
        CANADA_CODES.add("705");
        CANADA_CODES.add("709");
        CANADA_CODES.add("742");
        CANADA_CODES.add("778");
        CANADA_CODES.add("780");
        CANADA_CODES.add("782");
        CANADA_CODES.add("807");
        CANADA_CODES.add("819");
        CANADA_CODES.add("825");
        CANADA_CODES.add("867");
        CANADA_CODES.add("873");
        CANADA_CODES.add("902");
        CANADA_CODES.add("905");
    }

}
