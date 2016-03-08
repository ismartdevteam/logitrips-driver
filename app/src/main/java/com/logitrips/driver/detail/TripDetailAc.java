//package com.logitrips.driver.detail;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.android.volley.toolbox.ImageLoader;
//import com.logitrips.userapp.CarImageFrag;
//import com.logitrips.userapp.R;
//import com.logitrips.userapp.model.Car;
//import com.logitrips.userapp.util.CircleImageView;
//import com.logitrips.userapp.util.MySingleton;
//import com.logitrips.userapp.util.Utils;
//
//import org.joda.time.DateTime;
//import org.joda.time.Interval;
//
//public class TripDetailAc extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {
//    private Car car;
//    private Bundle b;
//    private TextView location;
//
//    private TextView start_date;
//    private TextView end_date;
//    private TextView duration;
//    private TextView car_info;
//    private TextView driver;
//    private TextView payment_status;
//    private TextView status;
//    private TextView total_price;
//    private TextView pick_time;
//    private TextView drop_time;
//    private CircleImageView driver_img;
//    private ImageLoader mImageLoader;
//    private Spinner pick_location;
//    private ViewPager mPager;
//
//    private PagerAdapter mPagerAdapter;
//    private Spinner drop_location;
//
//    private Button save;
//    private Button cancelTrip;
//    private TextView image_pos;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_trip_detail);
//        car = (Car) getIntent().getSerializableExtra("Car");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        mImageLoader = MySingleton.getInstance(this).getImageLoader();
//        initView();
//
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        image_pos.setText((position + 1) + " of " + car.getCar_pic_urls().length);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
//
//    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
//        public ScreenSlidePagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return CarImageFrag.newInstance(car.getCar_pic_urls()[position]);
//        }
//
//        @Override
//        public int getCount() {
//            return car.getCar_pic_urls().length;
//        }
//
//
//    }
//    private void initView() {
//        mPager = (ViewPager) findViewById(R.id.trip_det_image_slider);
//        image_pos = (TextView) findViewById(R.id.trip_det_image_pos);
//        image_pos.setText("1 of " + car.getCar_pic_urls().length);
//        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
//        mPager.setAdapter(mPagerAdapter);
//        mPager.setOnPageChangeListener(this);
//        end_date = (TextView) findViewById(R.id.trip_det_end_date);
//        start_date = (TextView) findViewById(R.id.trip_det_start);
//        duration = (TextView) findViewById(R.id.trip_det_duration);
//        location = (TextView) findViewById(R.id.trip_det_loc);
//        car_info = (TextView) findViewById(R.id.trip_det_car_info);
//        driver = (TextView) findViewById(R.id.trip_det_driver_name);
//        total_price = (TextView) findViewById(R.id.trip_det_total_price);
//        pick_time = (TextView) findViewById(R.id.trip_det_pick_time);
//
//        drop_time = (TextView) findViewById(R.id.trip_det_drop_time);
//
//        payment_status = (TextView) findViewById(R.id.trip_det_payment_status);
//        status = (TextView) findViewById(R.id.trip_det_status);
//        driver_img = (CircleImageView) findViewById(R.id.trip_det_driver_img);
//        driver_img.setImageUrl(getString(R.string.driver_image_url) + car.getDriver_pic_url(), mImageLoader);
//        driver_img.setDefaultImageResId(R.drawable.nodriver);
//        pick_location = (Spinner) findViewById(R.id.trip_det_pick_loc);
//
//        drop_location = (Spinner) findViewById(R.id.trip_det_drop_loc);
//
//        //set
//        location.setText(Utils.dest_str[car.getLocation() - 1]);
//        start_date.setText(car.getStart_date());
//        end_date.setText(car.getEnd_date());
//        driver.setText(car.getDriver_name());
//
//        makeDuration();
//        total_price.setText("$" + car.getTotal_fee());
//        payment_status.setText(Utils.payment_status[car.getPayment_status()]);
//        pick_time.setText(car.getTime_pick());
//        drop_time.setText(car.getTime_drop());
//        status.setText(Utils.trip_status[car.getTrip_status()]);
//        car_info.setText(car.getCar_model()+","+car.getYear());
//
//    }
//
//    private void makeDuration() {
//        DateTime startDate = DateTime.parse(car.getStart_date());
//        DateTime endDate = DateTime.parse(car.getEnd_date());
//        Interval interval = new Interval(startDate, endDate);
//       int  days = (int) interval.toDuration().getStandardDays();
//      int  hours = (int)interval.toDuration().getStandardHours();
//        String dayStr = getResources().getQuantityString(R.plurals.day, days, days);
////        String hourStr = getResources().getQuantityString(R.plurals.hour,  hours,hours);
//        duration.setText(dayStr + " ");
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
//    @Override
//    public void onClick(View v) {
//
//    }
//}
