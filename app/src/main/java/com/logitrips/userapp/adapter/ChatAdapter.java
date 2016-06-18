package com.logitrips.userapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.logitrips.userapp.R;
import com.logitrips.userapp.model.Chat;
import com.logitrips.userapp.util.CircleImageView;
import com.logitrips.userapp.util.MySingleton;
import com.logitrips.userapp.util.Utils;

import java.util.List;


public class ChatAdapter extends ArrayAdapter<Chat> {
    private final Context mContext;
    private final ImageLoader mImageLoader;
    private final String profile_pic;

    public ChatAdapter(Context context, List<Chat> mListItems, String profile_pic) {
        super(context, 0, 0, mListItems);
        this.mContext = context;
        this.profile_pic = profile_pic;
        mImageLoader = MySingleton.getInstance(context).getImageLoader();
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Chat item = getItem(position);
        Holder hol = null;
        if (v == null) {
            hol = new Holder();
            v = ((Activity) mContext).getLayoutInflater().inflate(
                    R.layout.chat_driver, parent, false);

            hol.lin = (LinearLayout) v.findViewById(R.id.chat_message_lin);
            hol.main = (LinearLayout) v.findViewById(R.id.chat_main_lin);
            hol.driver_image = (CircleImageView) v.findViewById(R.id.chat_det_driver_img);
            hol.text = (TextView) v.findViewById(R.id.chat_det_msg);
            hol.date = (TextView) v.findViewById(R.id.chat_det_date);
            hol.location = (ImageView) v.findViewById(R.id.chat_det_location);
            v.setTag(hol);
        } else
            hol = (Holder) v.getTag();

        hol.text.setText(item.getMessage() + "");
        boolean isLoc = false;
        hol.date.setText(Utils.getTimeAgo(item.getDate_sent()));
        if (!TextUtils.isEmpty(item.getLocation())) {

            isLoc = true;
        }
        if (item.is_recv() == 1) {
            hol.driver_image.setVisibility(View.VISIBLE);
            hol.lin.setBackground(mContext.getResources().getDrawable(R.drawable.chat_driver));
            hol.text.setTextColor(Color.WHITE);
            hol.date.setTextColor(mContext.getResources().getColor(R.color.chat_text_orange));
            hol.driver_image.setImageUrl(profile_pic, mImageLoader);
            hol.driver_image.setDefaultImageResId(R.drawable.nodriver);
            hol.main.setGravity(Gravity.LEFT);
        } else {
            hol.driver_image.setVisibility(View.GONE);
            hol.main.setGravity(Gravity.RIGHT);
            hol.lin.setBackground(mContext.getResources().getDrawable(R.drawable.chat_user));
            hol.text.setTextColor(Color.BLACK);
            hol.date.setTextColor(mContext.getResources().getColor(R.color.chat_text_gray));
        }
        if (isLoc) {
            hol.text.setVisibility(View.GONE);
            hol.location.setVisibility(View.VISIBLE);
            hol.location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] loc = item.getLocation().split(",");
                    Utils.openMapIntent(mContext, loc[0], loc[1]);
                }
            });
        } else {
            hol.location.setVisibility(View.GONE);
            hol.text.setVisibility(View.VISIBLE);
        }

        return v;
    }

    class Holder {
        CircleImageView driver_image;
        ImageView location;
        TextView text;
        TextView date;
        LinearLayout lin;
        LinearLayout main;
    }
}
