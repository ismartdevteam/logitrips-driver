//package com.logitrips.driver.detail;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.InputFilter;
//import android.text.Spanned;
//import android.text.TextUtils;
//import android.util.Log;
//import android.util.SparseArray;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.android.volley.toolbox.ImageLoader;
//import com.google.i18n.phonenumbers.NumberParseException;
//import com.google.i18n.phonenumbers.PhoneNumberUtil;
//import com.google.i18n.phonenumbers.Phonenumber;
//import com.logitrips.userapp.R;
//import com.logitrips.userapp.countrycode.Country;
//import com.logitrips.userapp.countrycode.CountryAdapter;
//import com.logitrips.userapp.countrycode.CountryAdapterWithoutCode;
//import com.logitrips.userapp.countrycode.CustomPhoneNumberFormattingTextWatcher;
//import com.logitrips.userapp.countrycode.OnPhoneChangedListener;
//import com.logitrips.userapp.countrycode.PhoneUtils;
//import com.logitrips.userapp.model.Car;
//import com.logitrips.userapp.util.CircleImageView;
//import com.logitrips.userapp.util.MySingleton;
//import com.logitrips.userapp.util.Utils;
//
//import org.joda.time.DateTime;
//import org.joda.time.Interval;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.TreeSet;
//
//public class TouristInfo extends AppCompatActivity {
//    private Car car;
//    private Bundle b;
//    private TextView location;
//
//    private TextView date;
//    private TextView duration;
//    private TextView car_info;
//    private TextView driver;
//    private TextView country_tv;
//    private TextView total_price;
//    private TextView pick_time;
//    private TextView drop_time;
//    private CircleImageView driver_img;
//
//    private Spinner pick_location;
//
//    private Spinner drop_location;
//
//    private Spinner phone_number_spinner;
//
//    private Spinner country_spinner;
//
//    private EditText fullname;
//    private EditText email;
//    private EditText phone;
//    private SharedPreferences loginsSp;
//    private ImageLoader mImageLoader;
//    private Calendar c;
//    private int days, hours = 0;
//    private int now = 0;
//    protected SparseArray<ArrayList<Country>> mCountriesMap = new SparseArray<ArrayList<Country>>();
//    protected String mLastEnteredPhone;
//    protected PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();
//    protected CountryAdapter phoneCountryAdapter;
//    protected CountryAdapterWithoutCode countryAdapter;
//    private String coutryName;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tourist_info);
//        b = getIntent().getExtras();
//        loginsSp = getSharedPreferences("user", 1);
//        car = (Car) getIntent().getSerializableExtra("Car");
//        Log.e("car", car.getCar_class());
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        c = Calendar.getInstance();
//        initView();
//
//    }
//
//    private void initView() {
//
//
//        country_tv = (TextView) findViewById(R.id.info_country_tv);
//        date = (TextView) findViewById(R.id.info_date);
//        duration = (TextView) findViewById(R.id.info_duration);
//        location = (TextView) findViewById(R.id.info_locaiton);
//        car_info = (TextView) findViewById(R.id.info_car);
//        driver = (TextView) findViewById(R.id.info_driver_name);
//        total_price = (TextView) findViewById(R.id.info_total_price);
//        pick_time = (TextView) findViewById(R.id.info_pick_time);
//        drop_time = (TextView) findViewById(R.id.info_drop_time);
//        driver_img = (CircleImageView) findViewById(R.id.info_driver_img);
//
//
//        pick_location = (Spinner) findViewById(R.id.info_pick_loc);
//
//        drop_location = (Spinner) findViewById(R.id.info_drop_loc);
//
//        phone_number_spinner = (Spinner) findViewById(R.id.info_phone_country_spinner);
//
//        country_spinner = (Spinner) findViewById(R.id.info_country_spin);
//
//        fullname = (EditText) findViewById(R.id.info_fullname);
//        email = (EditText) findViewById(R.id.info_email);
//        phone = (EditText) findViewById(R.id.info_phone);
//
//        location.setText(Utils.dest_str[car.getLocation() - 1]);
//        date.setText(b.getString("end_date") + "");
//        car_info.setText(car.getCar_model() + ", " + car.getYear());
//
//
//        mImageLoader = MySingleton.getInstance(this).getImageLoader();
//        driver.setText(car.getDriver_name() + "");
//        driver_img.setImageUrl(getString(R.string.driver_image_url) + car.getDriver_pic_url(), mImageLoader);
//        driver_img.setDefaultImageResId(R.drawable.nodriver);
//        fullname.setText(loginsSp.getString("name", ""));
//        email.setText(loginsSp.getString("email", ""));
//
//
//        //spinners
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utils.dest_str); //selected item will look like a spinner set from XML
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        pick_location.setAdapter(spinnerArrayAdapter);
//        pick_location.setSelection(car.getLocation() - 1);
//
//        drop_location.setAdapter(spinnerArrayAdapter);
//
//        // phone and country
//        phoneCountryAdapter = new CountryAdapter(this);
//        countryAdapter = new CountryAdapterWithoutCode(this);
//
//        phone_number_spinner.setAdapter(phoneCountryAdapter);
//        phone_number_spinner.setOnItemSelectedListener(mOnItemSelectedListener);
//        country_spinner.setAdapter(countryAdapter);
//        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                country_tv.setText(countryAdapter.getItem(position).getName());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        phone.addTextChangedListener(new CustomPhoneNumberFormattingTextWatcher(mOnPhoneChangedListener));
//        InputFilter filter = new InputFilter() {
//            public CharSequence filter(CharSequence source, int start, int end,
//                                       Spanned dest, int dstart, int dend) {
//                for (int i = start; i < end; i++) {
//                    char c = source.charAt(i);
//                    if (dstart > 0 && !Character.isDigit(c)) {
//                        return "";
//                    }
//                }
//                return null;
//            }
//        };
//
//        phone.setFilters(new InputFilter[]{filter});
//
//
//        new AsyncPhoneInitTask(this).execute();
//
//        //calculating prices with time
//        now = c.get(Calendar.HOUR_OF_DAY);
//
//
//        DateTime startDate = DateTime.parse(b.getString("pick_date"));
//        DateTime endDate = DateTime.parse(b.getString("end_date"));
//        Interval interval = new Interval(startDate, endDate);
//        days = (int) interval.toDuration().getStandardDays();
//        hours = 24 - now;
//        makeDuration();
//        makeTotalPrice();
//    }
//
//    private void makeTotalPrice() {
//        double total = 0;
//        if (days < 1 && hours <= 5) {
//            total = car.getHourly_price() * hours;
//        } else if (days < 1 && hours > 5) {
//            total = car.getDaily_price();
//        } else {
//            total = days * car.getDay2_price();
//        }
//        total_price.setText("$" + total);
//
//    }
//
//    private void makeDuration() {
//        String dayStr = getResources().getQuantityString(R.plurals.day, days, days);
//        String hourStr = getResources().getQuantityString(R.plurals.hour, 24 - hours, 24 - hours);
//        duration.setText(dayStr + " " + hourStr);
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    protected class AsyncPhoneInitTask extends AsyncTask<Void, Void, ArrayList<Country>> {
//
//        private int mSpinnerPosition = -1;
//        private Context mContext;
//
//        public AsyncPhoneInitTask(Context context) {
//            mContext = context;
//        }
//
//        @Override
//        protected ArrayList<Country> doInBackground(Void... params) {
//            ArrayList<Country> data = new ArrayList<Country>(233);
//            BufferedReader reader = null;
//            try {
//                reader = new BufferedReader(new InputStreamReader(mContext.getApplicationContext().getAssets().open("countries.dat"), "UTF-8"));
//
//                // do reading, usually loop until end of file reading
//                String line;
//                int i = 0;
//                while ((line = reader.readLine()) != null) {
//                    //process line
//                    Country c = new Country(mContext, line, i);
//                    data.add(c);
//                    ArrayList<Country> list = mCountriesMap.get(c.getCountryCode());
//                    if (list == null) {
//                        list = new ArrayList<Country>();
//                        mCountriesMap.put(c.getCountryCode(), list);
//                    }
//                    list.add(c);
//                    i++;
//                }
//            } catch (IOException e) {
//                //log the exception
//            } finally {
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        //log the exception
//                    }
//                }
//            }
//            if (!TextUtils.isEmpty(phone.getText().toString())) {
//                return data;
//            }
//            String countryRegion = PhoneUtils.getCountryRegionFromPhone(mContext);
//            int code = mPhoneNumberUtil.getCountryCodeForRegion(countryRegion);
//            ArrayList<Country> list = mCountriesMap.get(code);
//            if (list != null) {
//                for (Country c : list) {
//                    if (c.getPriority() == 0) {
//                        mSpinnerPosition = c.getNum();
//                        break;
//                    }
//                }
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Country> data) {
//            phoneCountryAdapter.addAll(data);
//            countryAdapter.addAll(data);
//            if (mSpinnerPosition > 0) {
//                phone_number_spinner.setSelection(mSpinnerPosition);
//                country_spinner.setSelection(mSpinnerPosition);
//                country_tv.setText(countryAdapter.getItem(mSpinnerPosition).getName());
//            }
//        }
//    }
//
//    protected OnPhoneChangedListener mOnPhoneChangedListener = new OnPhoneChangedListener() {
//        @Override
//        public void onPhoneChanged(String phone) {
//            try {
//                mLastEnteredPhone = phone;
//                Phonenumber.PhoneNumber p = mPhoneNumberUtil.parse(phone, null);
//                ArrayList<Country> list = mCountriesMap.get(p.getCountryCode());
//                Country country = null;
//                if (list != null) {
//                    if (p.getCountryCode() == 1) {
//                        String num = String.valueOf(p.getNationalNumber());
//                        if (num.length() >= 3) {
//                            String code = num.substring(0, 3);
//                            if (CANADA_CODES.contains(code)) {
//                                for (Country c : list) {
//                                    // Canada has priority 1, US has priority 0
//                                    if (c.getPriority() == 1) {
//                                        country = c;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (country == null) {
//                        for (Country c : list) {
//                            if (c.getPriority() == 0) {
//                                country = c;
//                                break;
//                            }
//                        }
//                    }
//                }
//                if (country != null) {
//                    final int position = country.getNum();
//                    phone_number_spinner.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            phone_number_spinner.setSelection(position);
//                        }
//                    });
//                }
//            } catch (NumberParseException ignore) {
//            }
//
//        }
//    };
//
//    protected static final TreeSet<String> CANADA_CODES = new TreeSet<String>();
//
//    static {
//        CANADA_CODES.add("204");
//        CANADA_CODES.add("236");
//        CANADA_CODES.add("249");
//        CANADA_CODES.add("250");
//        CANADA_CODES.add("289");
//        CANADA_CODES.add("306");
//        CANADA_CODES.add("343");
//        CANADA_CODES.add("365");
//        CANADA_CODES.add("387");
//        CANADA_CODES.add("403");
//        CANADA_CODES.add("416");
//        CANADA_CODES.add("418");
//        CANADA_CODES.add("431");
//        CANADA_CODES.add("437");
//        CANADA_CODES.add("438");
//        CANADA_CODES.add("450");
//        CANADA_CODES.add("506");
//        CANADA_CODES.add("514");
//        CANADA_CODES.add("519");
//        CANADA_CODES.add("548");
//        CANADA_CODES.add("579");
//        CANADA_CODES.add("581");
//        CANADA_CODES.add("587");
//        CANADA_CODES.add("604");
//        CANADA_CODES.add("613");
//        CANADA_CODES.add("639");
//        CANADA_CODES.add("647");
//        CANADA_CODES.add("672");
//        CANADA_CODES.add("705");
//        CANADA_CODES.add("709");
//        CANADA_CODES.add("742");
//        CANADA_CODES.add("778");
//        CANADA_CODES.add("780");
//        CANADA_CODES.add("782");
//        CANADA_CODES.add("807");
//        CANADA_CODES.add("819");
//        CANADA_CODES.add("825");
//        CANADA_CODES.add("867");
//        CANADA_CODES.add("873");
//        CANADA_CODES.add("902");
//        CANADA_CODES.add("905");
//    }
//
//
//    protected AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            Country c = (Country) phone_number_spinner.getItemAtPosition(position);
//            if (mLastEnteredPhone != null && mLastEnteredPhone.startsWith(c.getCountryCodeStr())) {
//                return;
//            }
//            coutryName = c.getName();
//            phone.getText().clear();
//            phone.getText().insert(phone.getText().length() > 0 ? 1 : 0, String.valueOf(c.getCountryCode()));
//            phone.setSelection(phone.length());
//            mLastEnteredPhone = null;
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//        }
//    };
//}
