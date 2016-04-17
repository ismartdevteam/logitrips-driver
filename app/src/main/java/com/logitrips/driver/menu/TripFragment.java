package com.logitrips.driver.menu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.logitrips.driver.R;
import com.logitrips.driver.detail.OffDays;
import com.logitrips.driver.util.CustomRequest;
import com.logitrips.driver.util.MySingleton;
import com.logitrips.driver.util.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class TripFragment extends Fragment {

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
    public static TripFragment newInstance(String param1, String param2) {
        TripFragment fragment = new TripFragment();
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
    public TripFragment() {
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

        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        mPager = (ViewPager) view.findViewById(R.id.mytrip_pager);
        getData(mParam1, mParam2);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionBar.setHomeAsUpIndicator(R.drawable.menu_message_orange);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle(R.string.trips);
        return view;
    }

    private void getData(String mParam1, final String mParam2) {
        Log.e("url", getString(R.string.main_url) + "/mobiledriver/gettripsbydriver?driver_id=" + mParam1);
        if (!Utils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {
            CustomRequest loginReq = new CustomRequest(Request.Method.GET,
                    getString(R.string.main_url) + "/mobiledriver/gettripsbydriver?driver_id=" + mParam1, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("mytrips response", response.toString());
                    try {
                        switch (response.getInt("response")) {
                            case 200:
                                responseStr = response.getJSONArray("data").toString();
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


        mPagerAdapter = new ScreenSlidePagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            return TripListFragment.newInstance(responseStr, position);

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.current);

                case 1:
                    return getString(R.string.past);

                default:
                    return "";

            }
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_off_days) {
            Intent off = new Intent(getActivity(), OffDays.class);
            Bundle b = new Bundle();
            b.putString("driver_id", mParam1);
            off.putExtras(b);
            startActivity(off);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
