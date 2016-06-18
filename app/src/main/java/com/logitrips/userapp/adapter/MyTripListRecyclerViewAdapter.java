package com.logitrips.userapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.logitrips.userapp.R;
import com.logitrips.userapp.model.Car;
import com.logitrips.userapp.util.CircleImageView;
import com.logitrips.userapp.util.MySingleton;
import com.logitrips.userapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


public class MyTripListRecyclerViewAdapter extends RecyclerView.Adapter<MyTripListRecyclerViewAdapter.ViewHolder> {

    private final List<Car> mValues;

    private ImageLoader mImageLoader;
    private Context con;

    public MyTripListRecyclerViewAdapter(List<Car> items, Context con) {
        mValues = items;
        this.con = con;
        mImageLoader = MySingleton.getInstance(con).getImageLoader();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mytrip_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);


        holder.car_location.setText(Utils.dest_str[holder.mItem.getLocation() - 1]);
        holder.date.setText(holder.mItem.getStart_date().replace("-", ".") + " - " + holder.mItem.getEnd_date().replace("-", "."));
        holder.name.setText(holder.mItem.getDriver_name());
        holder.car_det.setText(holder.mItem.getCar_model() + "," + holder.mItem.getYear());

        holder.status.setText(Utils.payment_status[holder.mItem.getPayment_status()]);
        switch (holder.mItem.getPayment_status()) {
            case 0:
                holder.status.setBackgroundColor(con.getResources().getColor(R.color.status_orange));
                break;
            case 1:
                holder.status.setBackgroundColor(con.getResources().getColor(R.color.status_green));
                break;
        }
        try {
            JSONArray image = new JSONArray(holder.mItem.getCar_pic_urls());
            holder.image.setImageUrl(image.getString(0), mImageLoader);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.driver.setImageUrl(holder.mItem.getDriver_pic_url(), mImageLoader);
        holder.driver.setDefaultImageResId(R.drawable.nodriver);
        holder.image.setDefaultImageResId(R.drawable.nocar);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView car_location;
        public final TextView status;
        public final TextView name;
        public final TextView date;
        public final TextView car_det;
        public final NetworkImageView image;
        public final CircleImageView driver;


        public Car mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            car_location = (TextView) view.findViewById(R.id.trip_item_location);
            status = (TextView) view.findViewById(R.id.trip_item_status);
            name = (TextView) view.findViewById(R.id.trip_item_name);
            date = (TextView) view.findViewById(R.id.trip_item_date);
            car_det = (TextView) view.findViewById(R.id.trip_item_car_det);
            image = (NetworkImageView) view.findViewById(R.id.trip_item_image);
            driver = (CircleImageView) view.findViewById(R.id.trip_item_driver);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
