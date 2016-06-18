package com.logitrips.userapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.logitrips.userapp.R;
import com.logitrips.userapp.model.Car;
import com.logitrips.userapp.util.CircleImageView;
import com.logitrips.userapp.util.MySingleton;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


public class FavRecyclerViewAdapter extends RecyclerView.Adapter<FavRecyclerViewAdapter.ViewHolder> {

    private final List<Car> mValues;

    private ImageLoader mImageLoader;

    public FavRecyclerViewAdapter(List<Car> items, Context con) {
        mValues = items;

        mImageLoader = MySingleton.getInstance(con).getImageLoader();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder hol, int position) {
        hol.mItem = mValues.get(position);


        hol.price.setText("$" +  hol.mItem .getHourly_price());
        hol.driver_name.setText( hol.mItem .getDriver_name());
        hol.car_desc.setText( hol.mItem .getCar_model() + ", " +  hol.mItem .getYear());
        try {
            JSONArray image = new JSONArray(hol.mItem.getCar_pic_urls());
            hol.image.setImageUrl(image.getString(0) ,mImageLoader);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hol.image.setDefaultImageResId(R.drawable.nocar);

        hol.rating.setRating((float) hol.mItem .getCar_rating());
        hol.driver_image.setImageUrl(hol.mItem .getDriver_pic_url(),mImageLoader);
        hol.driver_image.setDefaultImageResId(R.drawable.nodriver);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final NetworkImageView image;
        public final  CircleImageView driver_image;
        public final  TextView price;
        public final   TextView driver_name;
        public final   TextView car_desc;
        public final  RatingBar rating;

        public Car mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
          image = (NetworkImageView) view.findViewById(R.id.item_car_image);
            driver_image = (CircleImageView) view.findViewById(R.id.item_driver_image);
            car_desc = (TextView) view.findViewById(R.id.item_car_desc);
            driver_name = (TextView) view.findViewById(R.id.item_driver_name);
            price = (TextView) view.findViewById(R.id.item_price);
            rating=(RatingBar)view.findViewById(R.id.item_rating);


        }

        @Override
        public String toString() {
            return super.toString() + " '"+ "'";
        }
    }
}
