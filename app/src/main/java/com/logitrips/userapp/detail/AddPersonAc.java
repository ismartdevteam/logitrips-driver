
package com.logitrips.userapp.detail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.logitrips.userapp.R;
import com.logitrips.userapp.countrycode.Country;
import com.logitrips.userapp.countrycode.CountryAdapterWithoutCode;
import com.logitrips.userapp.countrycode.OnPhoneChangedListener;
import com.logitrips.userapp.countrycode.PhoneUtils;
import com.logitrips.userapp.model.Booking;
import com.logitrips.userapp.model.Insurance;
import com.logitrips.userapp.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

public class AddPersonAc extends AppCompatActivity {
    private ActionBar actionBar;
    private TextView birthday;
    private EditText name;
    private EditText passport;
    private TextView country_tv;

    private Insurance insurance = new Insurance();
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    protected SparseArray<ArrayList<Country>> mCountriesMap = new SparseArray<ArrayList<Country>>();
    protected String mLastEnteredPhone;
    protected PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();
    protected CountryAdapterWithoutCode countryAdapter;

    private Spinner country_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
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

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        birthday = (TextView) findViewById(R.id.add_person_birth);

        name = (EditText) findViewById(R.id.add_person_name);
        passport = (EditText) findViewById(R.id.add_person_passport);
        country_tv = (TextView) findViewById(R.id.add_person_country);
        country_spinner = (Spinner) findViewById(R.id.add_person_country_spin);


        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddPersonAc.this, myDateListener,
                        year, month, day).show();
            }
        });

        //country
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
        country_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                country_spinner.performClick();
            }
        });


        new AsyncPhoneInitTask(this).execute();


    }

    private void showDate(int year, int month, int day) {
        String monthStr = "";
        if (month < 10)
            monthStr = "0" + month;
        else
            monthStr = month + "";
        birthday.setText(new StringBuilder().append(year).append("/")
                .append(monthStr).append("/").append(day));
        insurance.setBirthday(birthday.getText().toString());
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
            if (name.getText().length() < 2) {
                Toast.makeText(getApplicationContext(), "Enter full name", Toast.LENGTH_LONG).show();
                return false;
            }
            if (birthday.getText().length() < 2) {
                Toast.makeText(getApplicationContext(), "Enter birthday", Toast.LENGTH_LONG).show();
                return false;
            }
            if (country_tv.getText().length() < 2) {
                Toast.makeText(getApplicationContext(), "Select country ", Toast.LENGTH_LONG).show();
                return false;
            }
            if (passport.getText().length() < 2) {
                Toast.makeText(getApplicationContext(), "Enter passport number ", Toast.LENGTH_LONG).show();
                return false;
            }
            Intent returnIntent = getIntent();
            insurance.setFullname(name.getText() + "");
            insurance.setContry(country_tv.getText() + "");
            insurance.setPassport_num(passport.getText() + "");
            returnIntent.putExtra("Person", insurance);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            return true;
        }
        if (id == android.R.id.home) {
            Intent returnIntent = getIntent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
