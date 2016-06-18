package com.logitrips.userapp.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.logitrips.userapp.R;
import com.logitrips.userapp.adapter.FavRecyclerViewAdapter;

import com.logitrips.userapp.detail.CarDetailAc;
import com.logitrips.userapp.model.Car;
import com.logitrips.userapp.util.CircleImageView;
import com.logitrips.userapp.util.CustomRequest;
import com.logitrips.userapp.util.MySingleton;
import com.logitrips.userapp.util.RecyclerItemClickListener;
import com.logitrips.userapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavFragment extends Fragment {


    private static final String ARG_PARAM1 = "user_id";

    private String mParam1;
    private static final String ARG_PARAM2 = "auth";


    private String mParam2;
    private List<Car> currentList;
    private FavRecyclerViewAdapter mAdapter;
    private ActionBar actionBar;


    public FavFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FavFragment newInstance(String param1,String param2) {
        FavFragment fragment = new FavFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_fav_list, container, false);

        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.menu_favourite_orange);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle(R.string.favourite);

        MenuUtils.inflateMenu(view,getActivity());

        currentList=new ArrayList<Car>();
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.favlist
            );

            mAdapter=new FavRecyclerViewAdapter(currentList,context);
            recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), CarDetailAc.class);

                intent.putExtra("Car", currentList.get(position));
//                Bundle b = new Bundle();
//                b.putString("pick_date", pick_date_tv.getText() + "");
//                b.putString("end_date", drop_date_tv.getText() + "");
//                intent.putExtras(b);
                startActivity(intent);
            }
        }));
        getData(mParam1,mParam2);
        return view;
    }

    private void getData(String mParam1, final String mParam2) {
        if (!Utils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {
            CustomRequest loginReq = new CustomRequest(Request.Method.GET,
                    getString(R.string.main_url) + "/mobile/getfavouritecars?user_id=" + mParam1, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("fav data",response.toString());
                    try {
                        switch (response.getInt("response")) {
                            case 200:

                                getCars(response.getJSONArray("data"));
                                break;

                            case 100:
                                Toast.makeText(getActivity(), R.string.error_request, Toast.LENGTH_SHORT).show();
                                break;
                            case 300:
                                Toast.makeText(getActivity(), R.string.no_available_cars, Toast.LENGTH_SHORT).show();
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
    private void getCars(JSONArray array) throws JSONException {

        if(array.length()==0){
                Toast.makeText(getActivity(),R.string.no_available_cars,Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < array.length(); i++) {
            Car car = new Car();
            JSONObject item = array.getJSONObject(i);
            car.setCar_id(item.getInt("car_id"));
            car.setCar_model(item.getString("car_model"));
            car.setYear(item.getInt("car_year"));
            car.setCar_class(item.getString("car_class"));
            car.setCar_rating(item.getDouble("car_rating"));
            JSONArray car_image_ar = item.getJSONArray("car_pic_urls");

            car.setCar_pic_urls(car_image_ar.toString());
            car.setLocation(item.getInt("location"));
            if (!item.isNull("hourly_price"))
                car.setHourly_price(item.getDouble("hourly_price"));
            car.setDaily_price(item.getDouble("daily_price"));
            car.setDay2_price(item.getDouble("day2_price"));
            car.setDriver_id(item.getInt("driver_id"));
            car.setDriver_name(item.getString("driver_name"));
            car.setDriver_pic_url(item.getString("driver_pic_url"));
            car.setDriver_knowledge(item.getString("driver_knowledge"));
            car.setDriver_smoking(item.getInt("driver_smoking"));
            JSONArray driver_lang_ar = item.getJSONArray("driver_languages");
            car.setDriver_langStr(driver_lang_ar.toString());
            car.setDriver_year(item.getInt("driver_year"));
            currentList.add(car);

        }
        mAdapter.notifyDataSetChanged();

    }

}
