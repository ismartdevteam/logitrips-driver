package com.logitrips.userapp.menu;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.logitrips.userapp.CarImageFrag;
import com.logitrips.userapp.R;
import com.logitrips.userapp.TripListFragment;

import com.logitrips.userapp.model.Car;
import com.logitrips.userapp.util.CustomRequest;
import com.logitrips.userapp.util.MySingleton;
import com.logitrips.userapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyTripFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user_id";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private static final String ARG_PARAM2 = "auth";


    // TODO: Rename and change types of parameters
    private String mParam2;


    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ActionBar actionBar;
    private String responseStr;

    // TODO: Rename and change types of parameters
    public static MyTripFragment newInstance(String param1,String param2) {
        MyTripFragment fragment = new MyTripFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyTripFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.menu_mytrips_orange);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle(R.string.mytrips);
        View view = inflater.inflate(R.layout.fragment_mytrip, container, false);
        mPager=(ViewPager)view.findViewById(R.id.mytrip_pager);
        getData(mParam1,mParam2);
        MenuUtils.inflateMenu(view, getActivity());

        return view;
    }

    private void getData(String mParam1, final String mParam2) {
        if (!Utils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {
            CustomRequest loginReq = new CustomRequest(Request.Method.GET,
                    getString(R.string.main_url) + "/mobile/gettripsbyuser?user_id=" + mParam1, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("mytrips response",response.toString());
                    try {
                        switch (response.getInt("response")) {
                            case 200:
                                 responseStr=response.getJSONArray("data").toString();
                                makeList();
                                break;

                            case 100:
                                Toast.makeText(getActivity(), R.string.error_request, Toast.LENGTH_SHORT).show();
                                break;

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", mParam2);
                    return params;
                }
            };
            MySingleton.getInstance(getActivity()).addToRequestQueue(loginReq);
        }

    }

    private void makeList() {


        mPagerAdapter = new ScreenSlidePagerAdapter(((ActionBarActivity) getActivity()).getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

                    return TripListFragment.newInstance(responseStr,position);

        }

        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return getString(R.string.current);

                case 1:
                    return getString(R.string.past);

                default:
                    return "";

            }
        }


    }

}
