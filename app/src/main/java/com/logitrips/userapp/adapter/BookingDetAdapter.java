package com.logitrips.userapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.logitrips.userapp.R;
import com.logitrips.userapp.detail.AddDayAc;
import com.logitrips.userapp.detail.BookingDetail;
import com.logitrips.userapp.model.Booking;
import com.logitrips.userapp.model.Car;
import com.logitrips.userapp.util.CircleImageView;
import com.logitrips.userapp.util.MySingleton;
import com.logitrips.userapp.util.Utils;

import java.util.List;


public class BookingDetAdapter extends RecyclerView.Adapter<BookingDetAdapter.ViewHolder> {

    private final List<Booking> mValues;
    private static final int FOOTER_VIEW = 1;
    private BookingDetail context;

    public BookingDetAdapter(List<Booking> items, BookingDetail con) {
        mValues = items;
        this.context = con;
    }

    public class FooterViewHolder extends ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddDayAc.class);
                    context.startActivityForResult(intent, context.ADD_REQ);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == FOOTER_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_footer, parent, false);

            FooterViewHolder vh = new FooterViewHolder(v);

            return vh;
        }

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);

        NormalViewHolder vh = new NormalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder hol, final int position) {
        try {
            if (hol instanceof NormalViewHolder) {
                NormalViewHolder vh = (NormalViewHolder) hol;
                vh.mItem = mValues.get(position);

                vh.number.setText("Booking " + (position + 1));
                vh.dateStr.setText(vh.mItem.getDate());
                vh.hours.setText(vh.mItem.getHours()+"");
                vh.startTime.setText(vh.mItem.getStart_time()+"");
                vh.pickLoc.setText(Utils.pick_up_location[vh.mItem.getPick_loc()]);
                vh.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeAt(position);
                    }
                });
            } else if (hol instanceof FooterViewHolder) {
                FooterViewHolder vh = (FooterViewHolder) hol;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class NormalViewHolder extends ViewHolder {

        public NormalViewHolder(View hol) {
            super(hol);

        }
    }

    public void removeAt(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mValues.size());
    }


    @Override
    public int getItemCount() {
        if (mValues == null) {
            return 0;
        }

        if (mValues.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return mValues.size() + 1;
    }

// Now define getItemViewType of your own.

    @Override
    public int getItemViewType(int position) {
        if (position == mValues.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView number;
        public final TextView dateStr;
        public final TextView hours;
        public final TextView startTime;
        public final TextView pickLoc;
        public final ImageView delete;

        public Booking mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            number = (TextView) view.findViewById(R.id.booking_det_number);
            dateStr = (TextView) view.findViewById(R.id.booking_det_date);
            hours = (TextView) view.findViewById(R.id.booking_det_hours);
            startTime = (TextView) view.findViewById(R.id.booking_det_start_time);
            pickLoc = (TextView) view.findViewById(R.id.booking_det_pick_loc);
            delete = (ImageView) view.findViewById(R.id.booking_det_delete);


        }

    }
}
