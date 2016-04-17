package com.logitrips.driver.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logitrips.driver.R;
import com.logitrips.driver.adapter.TripListRecyclerViewAdapter;
import com.logitrips.driver.detail.TripDetailAc;
import com.logitrips.driver.model.Trip;
import com.logitrips.driver.model.BookingDetails;
import com.logitrips.driver.util.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TripListFragment extends Fragment {


    private List<Trip> lists;

    private String json;
    private int status;

    public TripListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TripListFragment newInstance(String json, int status) {


        TripListFragment fragment = new TripListFragment();
        Bundle args = new Bundle();
        args.putString("json", json);
        args.putInt("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            json = getArguments().getString("json");
            status = getArguments().getInt("status");
        }
    }

    private void setList(JSONArray array) throws JSONException {


        lists = new ArrayList<Trip>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < array.length(); i++) {
            Trip trip = new Trip();
            JSONObject item = array.getJSONObject(i);
            trip.setTrip_id(item.getInt("trip_id"));
            trip.setUsername(item.getString("user_name"));
            trip.setUser_country(item.getString("user_country"));
            trip.setUser_pic_url(item.getString("user_pic_url"));
            trip.setDestination(item.getString("destination"));
            trip.setCar_pic_url(item.getString("car_pic_url"));
            trip.setStart_date(item.getString("start_date"));
            trip.setEnd_date(item.getString("end_date"));
            trip.setTrip_status(item.getInt("trip_status"));
            if (!item.isNull("total_fee"))
                trip.setTotal_fee(item.getString("total_fee"));
            trip.setTime_pick(item.getString("time_pick"));
            trip.setDuration(item.getString("duration"));
            trip.setLocation_drop(item.getString("location_drop"));
            trip.setLocation_pick(item.getString("location_pick"));
            trip.setTime_drop(item.getString("time_drop"));
            trip.setBooking_date(item.getString("booking_date"));
            JSONArray booking_detail = item.getJSONArray("booking_detail");

            trip.setDetails(booking_detail.toString());
            Date date = new Date();

            try {
                if (status == 0) {
                    if (date.before(dateFormatter.parse(trip.getEnd_date()))) {
                        lists.add(trip);
                    }
                } else {
                    if (!date.before(dateFormatter.parse(trip.getEnd_date()))) {
                        lists.add(trip);
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_triplist_list, container, false);


        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mytrip_list);


        try {
            setList(new JSONArray(json));

            recyclerView.setAdapter(new TripListRecyclerViewAdapter(lists, getActivity()));

            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), TripDetailAc.class);
                    intent.putExtra("Trip",lists.get(position));
                    startActivity(intent);
                }
            }));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


}
