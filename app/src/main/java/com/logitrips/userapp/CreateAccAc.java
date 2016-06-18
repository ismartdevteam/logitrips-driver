package com.logitrips.userapp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.logitrips.userapp.countrycode.Country;
import com.logitrips.userapp.countrycode.CountryAdapter;
import com.logitrips.userapp.countrycode.CustomPhoneNumberFormattingTextWatcher;
import com.logitrips.userapp.countrycode.OnPhoneChangedListener;
import com.logitrips.userapp.countrycode.PhoneUtils;
import com.logitrips.userapp.util.Services;
import com.logitrips.userapp.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;


public class CreateAccAc extends ActionBarActivity implements View.OnClickListener,View.OnTouchListener{
        ActionBar bar;
    protected SparseArray<ArrayList<Country>> mCountriesMap = new SparseArray<ArrayList<Country>>();

    protected PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();
    protected Spinner mSpinner;

    protected String mLastEnteredPhone;
    protected EditText mPhoneEdit;
    protected CountryAdapter mAdapter;

    protected TextView mBtnLink;
    private ImageView show_pass_btn;
    private EditText password_edit;
    private EditText email_edit;
    private EditText phone_edit;
    private EditText fname_edit;
    private EditText lname_edit;
    private  String coutryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(R.string.sign_up);
        mSpinner = (Spinner) findViewById(R.id.phone_country_spinner);
        mSpinner.setOnItemSelectedListener(mOnItemSelectedListener);

        mAdapter = new CountryAdapter(this);

        mSpinner.setAdapter(mAdapter);

        mPhoneEdit = (EditText) findViewById(R.id.reg_phone);
        mPhoneEdit.addTextChangedListener(new CustomPhoneNumberFormattingTextWatcher(mOnPhoneChangedListener));
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (dstart > 0 && !Character.isDigit(c)) {
                        return "";
                    }
                }
                return null;
            }
        };

        mPhoneEdit.setFilters(new InputFilter[]{filter});
        show_pass_btn = (ImageView) findViewById(R.id.reg_show_pass_btn);
        show_pass_btn.setOnTouchListener(this);
        mBtnLink = (TextView) findViewById(R.id.sign_up_btn);
        mBtnLink.setOnClickListener(this);
        new AsyncPhoneInitTask(this).execute();


        //views
        password_edit = (EditText) findViewById(R.id.reg_password);
        email_edit = (EditText) findViewById(R.id.reg_email);
        fname_edit = (EditText) findViewById(R.id.reg_fname);
        lname_edit = (EditText) findViewById(R.id.reg_lname);
        phone_edit = (EditText) findViewById(R.id.reg_phone);
    }

    @Override
    public void onClick(View v) {
        if(v==mBtnLink){
            if(fname_edit.getText().toString().length()>1 &&  lname_edit.getText().toString().length()>1 &&
                    phone_edit.getText().toString().length()>5 && password_edit.getText().toString().length()>3){
                if(Utils.isValidEmail(email_edit.getText())){
                    Services.signUp(CreateAccAc.this,email_edit.getText().toString(),password_edit.getText().toString()
                    ,fname_edit.getText().toString(),lname_edit.getText().toString(),phone_edit.getText().toString(),coutryName);
                }else
                    Toast.makeText(CreateAccAc.this, R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(CreateAccAc.this, R.string.fill_the_fields, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            password_edit.setInputType(InputType.TYPE_CLASS_TEXT);

            password_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            password_edit.setSelection(password_edit.getText().length());
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            password_edit.setInputType(
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            password_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            password_edit.setSelection(password_edit.getText().length());

            return true;
        }
        return false;
    }

    protected class AsyncPhoneInitTask extends AsyncTask<Void, Void, ArrayList<Country>> {

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
            if (!TextUtils.isEmpty(mPhoneEdit.getText().toString())) {
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
            mAdapter.addAll(data);
            if (mSpinnerPosition > 0) {
                mSpinner.setSelection(mSpinnerPosition);
            }
        }
    }

    protected String validate() {
        String region = null;
        String phone = null;
        if (mLastEnteredPhone != null) {
            try {
                Phonenumber.PhoneNumber p = mPhoneNumberUtil.parse(mLastEnteredPhone, null);
                StringBuilder sb = new StringBuilder(16);
                sb.append('+').append(p.getCountryCode()).append(p.getNationalNumber());
                phone = sb.toString();
                region = mPhoneNumberUtil.getRegionCodeForNumber(p);
            } catch (NumberParseException ignore) {
            }
        }
        if (region != null) {
            return phone;
        } else {
            return null;
        }
    }

    protected void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    protected void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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


    protected AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Country c = (Country) mSpinner.getItemAtPosition(position);
            if (mLastEnteredPhone != null && mLastEnteredPhone.startsWith(c.getCountryCodeStr())) {
                return;
            }
            coutryName=c.getName();
            mPhoneEdit.getText().clear();
            mPhoneEdit.getText().insert(mPhoneEdit.getText().length() > 0 ? 1 : 0, String.valueOf(c.getCountryCode()));
            mPhoneEdit.setSelection(mPhoneEdit.length());
            mLastEnteredPhone = null;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

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
                    mSpinner.post(new Runnable() {
                        @Override
                        public void run() {
                            mSpinner.setSelection(position);
                        }
                    });
                }
            } catch (NumberParseException ignore) {
            }

        }
    };
}

