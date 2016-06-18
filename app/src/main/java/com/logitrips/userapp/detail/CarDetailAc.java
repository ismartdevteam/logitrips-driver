package com.logitrips.userapp.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.logitrips.userapp.CarImageFrag;
import com.logitrips.userapp.LoginAc;
import com.logitrips.userapp.R;
import com.logitrips.userapp.model.Car;
import com.logitrips.userapp.model.DatabaseHelper;
import com.logitrips.userapp.model.FavouriteCars;
import com.logitrips.userapp.util.CircleImageView;
import com.logitrips.userapp.util.CustomRequest;
import com.logitrips.userapp.util.MySingleton;
import com.logitrips.userapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CarDetailAc extends ActionBarActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private Car car;
    private ActionBar bar;
    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;
    private TextView image_pos;
    private TextView driver_name;
    private TextView driver_loc;

    private TextView driving_exp;
    private TextView knowledge;
    private TextView lang_skills;
    private TextView model_year;
    private TextView car_type;
    private TextView hourly_fee;
    private TextView daily_fee;
    private TextView location;
    private TextView average_rating;
    private TextView see_all;
    private SharedPreferences loginsSp;
    private CircleImageView driver_img;
    private ImageLoader mImageLoader;
    private RatingBar rating_star;
    private Button book_car;
    private LinearLayout main_lin;
    private DatabaseHelper helper;
    private boolean isFav;
    private Bundle b;
    private Calendar c;
    private String[] carImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        b = getIntent().getExtras();
        helper = new DatabaseHelper(this);


        mImageLoader = MySingleton.getInstance(this).getImageLoader();
        loginsSp = getSharedPreferences("user", 1);
        car = (Car) getIntent().getSerializableExtra("Car");
        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(car.getCar_model() + ", " + car.getYear());
        mPager = (ViewPager) findViewById(R.id.det_image_slider);
        image_pos = (TextView) findViewById(R.id.det_image_pos);
        try {
            JSONArray jsonArray = new JSONArray(car.getCar_pic_urls());
            carImages = new String[jsonArray.length()];
            for (int i = 0; i < carImages.length; i++) {
                carImages[i] = jsonArray.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        image_pos.setText("1 of " + carImages.length);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(this);
        initDet();

    }


    private int checkLogin() {
        if (!loginsSp.getBoolean("isLogin", false)) {
            return 0;
        } else {
            return loginsSp.getInt("user_id", 0);
        }
    }

    private void initDet() {
        driver_name = (TextView) findViewById(R.id.det_driver_name);
        driver_loc = (TextView) findViewById(R.id.det_country);

        main_lin = (LinearLayout) findViewById(R.id.det_main_lin);
        rating_star = (RatingBar) findViewById(R.id.det_rating);
        rating_star.setRating((float) car.getCar_rating());

        book_car = (Button) findViewById(R.id.det_book_btn);
        book_car.setOnClickListener(this);
        driver_img = (CircleImageView) findViewById(R.id.det_driver_img);
        driver_img.setImageUrl(getString(R.string.driver_image_url) + car.getDriver_pic_url(), mImageLoader);
        driver_img.setDefaultImageResId(R.drawable.nodriver);
        driving_exp = (TextView) findViewById(R.id.det_driving_exp);
        knowledge = (TextView) findViewById(R.id.det_know_loc);
        lang_skills = (TextView) findViewById(R.id.det_lang_skills);
        model_year = (TextView) findViewById(R.id.det_car_model);
        car_type = (TextView) findViewById(R.id.det_car_type);
        hourly_fee = (TextView) findViewById(R.id.det_hourly_fee);
        daily_fee = (TextView) findViewById(R.id.det_daily_fee);
        location = (TextView) findViewById(R.id.det_location);
        average_rating = (TextView) findViewById(R.id.det_average_rating);
        see_all = (TextView) findViewById(R.id.det_see_all);

        driver_name.setText(car.getDriver_name() + "");
        driver_loc.setText(Utils.dest_str[car.getLocation() - 1]);

        c = Calendar.getInstance();
        String dateStr = c.get(Calendar.YEAR) + "-"
                + (c.get(Calendar.MONTH) + 1) + "-" +
                c.get(Calendar.DAY_OF_MONTH);
        String drivingExpStr = getResources().getQuantityString(R.plurals.year, c.get(Calendar.YEAR) - car.getDriver_year(), c.get(Calendar.YEAR) - car.getDriver_year());
        driving_exp.setText(drivingExpStr);

        knowledge.setText(car.getDriver_knowledge());
        try {
            JSONArray langAr = new JSONArray(car.getDriver_langStr());
            for (int i = 0; i < langAr.length(); i++) {
                lang_skills.append(langAr.getString(i) + ", ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        car_type.setText(car.getCar_class());

        model_year.setText(car.getCar_model() + ", " + car.getYear());
        hourly_fee.setText("$" + car.getHourly_price());
        daily_fee.setText("$" + car.getDaily_price());
        location.setText(Utils.dest_str[car.getLocation() - 1]);
        average_rating.setText(car.getCar_rating() + "");

//        getReview(car.getCar_id() + "");
    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        image_pos.setText((position + 1) + " of " + carImages.length);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void getReview(String car_id) {
        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {

            CustomRequest loginReq = new CustomRequest(Request.Method.GET,
                    getString(R.string.main_url) + "/mobile/getcarreviews?" + "car_id=" + location, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {
                        switch (response.getInt("response")) {
                            case 200:
                                getReviewComments(response.getJSONArray("data"));
                                break;
                            case 300:
                                addEmptyComment();
                                break;
                            case 100:
                                Toast.makeText(CarDetailAc.this, R.string.error_request, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CarDetailAc.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

            MySingleton.getInstance(this).addToRequestQueue(loginReq);
        }

    }

    private void getReviewComments(JSONArray ar) throws JSONException {

        for (int i = 0; i < ar.length(); i++) {
            JSONObject item = ar.getJSONObject(i);
            if (i > 3)
                break;
            View v = getLayoutInflater().inflate(R.layout.review_item, null);
            TextView name = (TextView) v.findViewById(R.id.review_name);
            TextView comment = (TextView) v.findViewById(R.id.review_comment);
            CircleImageView image = (CircleImageView) v.findViewById(R.id.review_img);
            TextView date = (TextView) v.findViewById(R.id.review_date);

            name.setText(item.getString("user_id") + "," + item.getString("country"));
            comment.setText(item.getString("comment") + "");
            image.setImageUrl(item.getString("profile_pic"), mImageLoader);

            main_lin.addView(v);
        }
        if (ar.length() > 3) {
            see_all.setText(getString(R.string.see_all) + ar.length() + ")");
            see_all.setOnClickListener(this);
        }


    }

    private void addEmptyComment() {
        View empty = getLayoutInflater().inflate(R.layout.empty_list, null);
        main_lin.addView(empty);

    }

    @Override
    public void onClick(View v) {
        if (v == book_car) {
            int user_id = checkLogin();
            if (user_id != 0) {
//                sendFav(car.getCar_id(),user_id);
                Intent intent = new Intent(CarDetailAc.this, TouristInfo.class);
                intent.putExtra("Car", car);

                intent.putExtras(b);
                startActivity(intent);

            } else {
                Intent intent = new Intent(CarDetailAc.this, LoginAc.class);

                startActivity(intent);
            }
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CarImageFrag.newInstance(carImages[position]);
        }

        @Override
        public int getCount() {
            return carImages.length;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_car_detail, menu);
        try {
            List<FavouriteCars> favs = helper.getFavDao().queryForEq("car_id", car.getCar_id());
            if (favs.size() == 1) {
                isFav = true;
                menu.getItem(0).setIcon(R.drawable.menu_fav_orange);
            } else
                menu.getItem(0).setIcon(R.drawable.menu_fav_gray);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fav) {
            int user_id = checkLogin();
            if (user_id != 0) {
                sendFav(car.getCar_id(), user_id + "", item, loginsSp.getString("auth_key", ""));

            } else {
                Intent intent = new Intent(CarDetailAc.this, LoginAc.class);

                startActivity(intent);
            }
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendFav(final int car_id, final String user_id, final MenuItem item, final String auth) {
        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {

            CustomRequest loginReq = new CustomRequest(Request.Method.POST,
                    getString(R.string.main_url) + "/mobile/postfavouritecar", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("send fav :", response.toString());
                    try {
                        switch (response.getInt("response")) {
                            case 200:
                                try {
                                    if (!isFav) {
                                        FavouriteCars favouriteCars = new FavouriteCars();
                                        favouriteCars.setCar_id(car_id);

                                        helper.getFavDao().create(favouriteCars);

                                        item.setIcon(R.drawable.menu_fav_orange);
                                        isFav = true;
                                    } else {
                                        DeleteBuilder del = helper.getFavDao().deleteBuilder();
                                        del.where().eq("car_id", car_id);
                                        del.delete();
                                        item.setIcon(R.drawable.menu_fav_gray);
                                        isFav = false;
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 300:
                                addEmptyComment();
                                break;
                            case 100:
                                Toast.makeText(CarDetailAc.this, R.string.error_request, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CarDetailAc.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", user_id + "");
                    params.put("car_id", car_id + "");
                    if (isFav)
                        params.put("favourite", 0 + "");
                    else
                        params.put("favourite", 1 + "");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", auth);
                    return params;
                }
            };

            MySingleton.getInstance(this).addToRequestQueue(loginReq);
        }

    }
}
