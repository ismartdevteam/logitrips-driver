package com.logitrips.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.logitrips.driver.R;
import com.logitrips.driver.model.Trip;
import com.logitrips.driver.util.CircleImageView;
import com.logitrips.driver.util.MySingleton;
import com.logitrips.driver.util.Utils;


import java.util.List;


public class TripListRecyclerViewAdapter extends RecyclerView.Adapter<TripListRecyclerViewAdapter.ViewHolder> {

    private final List<Trip> mValues;

    private ImageLoader mImageLoader;
    private Context con;

    public TripListRecyclerViewAdapter(List<Trip> items, Context con) {
        mValues = items;
        this.con = con;
        mImageLoader = MySingleton.getInstance(con).getImageLoader();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);


        holder.name.setText(holder.mItem.getUsername());
        holder.trip_date.setText(holder.mItem.getStart_date() + " to " + holder.mItem.getEnd_date());
        holder.trip_book.setText(holder.mItem.getBooking_date()+" ");
        holder.status.setText(Utils.trip_status[holder.mItem.getTrip_status()]);
        holder.status.setBackgroundDrawable(con.getResources().getDrawable(Utils.trip_status_col[holder.mItem.getTrip_status()]));
        holder.driver.setImageUrl(holder.mItem.getUser_pic_url(), mImageLoader);
        holder.driver.setDefaultImageResId(R.drawable.nodriver);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView status;
        public final TextView name;
        public final TextView trip_date;
        public final TextView trip_book;
        public final CircleImageView driver;


        public Trip mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            status = (TextView) view.findViewById(R.id.trip_status);
            name = (TextView) view.findViewById(R.id.trip_user_name);
            trip_date = (TextView) view.findViewById(R.id.trip_dates);
            trip_book = (TextView) view.findViewById(R.id.trip_booking);
            driver = (CircleImageView) view.findViewById(R.id.trip_user_image);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
