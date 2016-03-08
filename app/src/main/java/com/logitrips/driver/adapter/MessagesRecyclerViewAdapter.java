package com.logitrips.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.logitrips.driver.R;
import com.logitrips.driver.model.LogiMessage;
import com.logitrips.driver.util.CircleImageView;
import com.logitrips.driver.util.MySingleton;

import java.util.List;


public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder> {

    private final List<LogiMessage> mValues;

    private ImageLoader mImageLoader;

    public MessagesRecyclerViewAdapter(List<LogiMessage> items, Context con) {
        mValues = items;

        mImageLoader = MySingleton.getInstance(con).getImageLoader();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messenger_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder hol, int position) {
        hol.mItem = mValues.get(position);


        hol.driver_name.setText( hol.mItem .getDriver_name());
        hol.date.setText(" ");
        hol.last_message.setText(hol.mItem.getLast_message());
        hol.driver_image.setImageUrl(hol.mItem .getProfile_pic(),mImageLoader);
        hol.driver_image.setDefaultImageResId(R.drawable.nodriver);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircleImageView driver_image;
        public final  TextView driver_name;
        public final   TextView date;
        public final   TextView last_message;


        public LogiMessage mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            driver_image = (CircleImageView) view.findViewById(R.id.messages_driver_img);
            driver_name = (TextView) view.findViewById(R.id.messages_driver_name);
            date = (TextView) view.findViewById(R.id.messages_date);
            last_message=(TextView)view.findViewById(R.id.messages_last);


        }


    }
}
