package com.logitrips.driver.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.logitrips.driver.R;
import com.logitrips.driver.model.Chat;
import com.logitrips.driver.util.CircleImageView;
import com.logitrips.driver.util.MySingleton;

import java.util.List;


public class ChatAdapter extends ArrayAdapter<Chat> {
    private final Context mContext;
    private final int user_id;
    private final ImageLoader mImageLoader;
    private final String profile_pic;

    public ChatAdapter(Context context, List<Chat> mListItems, int user_id, String profile_pic) {
        super(context, 0, 0, mListItems);
        this.mContext = context;
        this.user_id = user_id;
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

            hol.lin=(LinearLayout)v.findViewById(R.id.chat_message_lin);
            hol.main=(LinearLayout)v.findViewById(R.id.chat_main_lin);
            hol.driver_image = (CircleImageView) v.findViewById(R.id.chat_det_driver_img);
            hol.text = (TextView) v.findViewById(R.id.chat_det_msg);
            hol.date = (TextView) v.findViewById(R.id.chat_det_date);
            v.setTag(hol);
        } else
            hol = (Holder) v.getTag();
        hol.text.setText(item.getMessage() + "");
        hol.date.setText(item.getDate_sent() + "");
        Log.i("is receive:"+item.is_recv()," message:"+item.getMessage());
        if (item.is_recv()==1) {
            hol.driver_image.setVisibility(View.VISIBLE);
            hol.lin.setBackground(mContext.getResources().getDrawable(R.drawable.chat_driver));
            hol.text.setTextColor(Color.WHITE);
            hol.date.setTextColor(mContext.getResources().getColor(R.color.chat_text_orange));
            hol.driver_image.setImageUrl(profile_pic, mImageLoader);
            hol.driver_image.setDefaultImageResId(R.drawable.nodriver);
            hol.main.setGravity(Gravity.LEFT);
        }
        else{
            hol.driver_image.setVisibility(View.GONE);
            hol.main.setGravity(Gravity.RIGHT);
            hol.lin.setBackground(mContext.getResources().getDrawable(R.drawable.chat_user));
            hol.text.setTextColor(Color.BLACK);
            hol.date.setTextColor(mContext.getResources().getColor(R.color.chat_text_gray));
        }

        return v;
    }

    class Holder {
        CircleImageView driver_image;
        TextView text;
        TextView date;
        LinearLayout lin;
        LinearLayout main;
    }
}
