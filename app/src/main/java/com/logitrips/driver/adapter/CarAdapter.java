//package com.logitrips.driver.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.NetworkImageView;
//import com.logitrips.userapp.R;
//import com.logitrips.userapp.model.Car;
//import com.logitrips.userapp.util.CircleImageView;
//import com.logitrips.userapp.util.MySingleton;
//
//import java.util.List;
//
//
///**
// * Created by Ulziiburen on 8/30/15.
// */
//public class CarAdapter extends ArrayAdapter<Car> {
//    Context mContext;
//
//    private ImageLoader mImageLoader;
//    public CarAdapter(Context context, List<Car> mListItems) {
//        super(context, 0, 0, mListItems);
//        this.mContext = context;
//        mImageLoader = MySingleton.getInstance(context).getImageLoader();
//    }
//
//    @Override
//    public View getView(int position, View v, ViewGroup parent) {
//        // TODO Auto-generated method stub
//        final Car item = getItem(position);
//        Holder hol = null;
//        if (v == null) {
//            v = ((Activity) mContext).getLayoutInflater().inflate(
//                    R.layout.car_item, parent, false);
//            hol = new Holder();
//            hol.image = (NetworkImageView) v.findViewById(R.id.item_car_image);
//            hol.driver_image = (CircleImageView) v.findViewById(R.id.item_driver_image);
//            hol.car_desc = (TextView) v.findViewById(R.id.item_car_desc);
//            hol.driver_name = (TextView) v.findViewById(R.id.item_driver_name);
//            hol.price = (TextView) v.findViewById(R.id.item_price);
//            hol.rating=(RatingBar)v.findViewById(R.id.item_rating);
//            v.setTag(hol);
//        } else
//            hol = (Holder) v.getTag();
//
//
//        hol.price.setText("$" + item.getHourly_price());
//        hol.driver_name.setText(item.getDriver_name());
//        hol.car_desc.setText(item.getCar_model() + ", " + item.getYear());
//        hol.image.setImageUrl(item.getCar_pic_urls()[0], mImageLoader);
//        hol.image.setDefaultImageResId(R.drawable.nocar);
////        Drawable drawable = hol.rating.getProgressDrawable();
////        drawable.setColorFilter(Color.parseColor("#fe7e17"), PorterDuff.Mode.SRC_ATOP);
//        hol.rating.setRating((float)item.getCar_rating());
//        hol.driver_image.setImageUrl(  mContext.getString(R.string.driver_image_url)+item.getDriver_pic_url(),mImageLoader);
//        hol.driver_image.setDefaultImageResId(R.drawable.nodriver);
//        return v;
//    }
//
//    class Holder {
//        NetworkImageView image;
//        CircleImageView driver_image;
//        TextView price;
//        TextView driver_name;
//        TextView car_desc;
//        RatingBar rating;
//    }
//}
