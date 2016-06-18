package com.logitrips.userapp.menu;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.logitrips.userapp.detail.CarDetailAc;
import com.logitrips.userapp.R;
import com.logitrips.userapp.adapter.CarAdapter;
import com.logitrips.userapp.model.Car;
import com.logitrips.userapp.model.DatabaseHelper;
import com.logitrips.userapp.util.CustomRequest;
import com.logitrips.userapp.util.DateDialog;
import com.logitrips.userapp.util.MySingleton;
import com.logitrips.userapp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "pick_date";
    private static final String TAG = "HomeFragment";
    private static final String ARG_PARAM1 = "json";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String classType = "any";
    private int langSel = 0;
    private int smokeStatus = -1;
    List<Car> cars = new ArrayList<Car>();
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    private ProgressBar home_list_prog;
    private SimpleDateFormat df;
    private DatabaseHelper helper;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private CarAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    ActionBar actionBar;
    private View action_date_view;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
//            getAvailableCar(mParam1, mParam2, mParam3, classType);
        }
        if (id == R.id.action_filter) {

            final Dialog dialog = new Dialog(getActivity());

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.filter_dialog);
            final Spinner langSp = (Spinner) dialog.findViewById(R.id.filter_dialog_lang_spinner);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Utils.lang_arr);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            langSp.setAdapter(spinnerArrayAdapter);
            langSp.setSelection(langSel);

            SegmentedGroup segmentedGroup = (SegmentedGroup) dialog.findViewById(R.id.filter_class_type_group);
            if (classType.equals("any"))
                segmentedGroup.check(R.id.filter_class_any_btn);
            if (classType.equals("mpv"))
                segmentedGroup.check(R.id.filter_class_mpv_btn);
            if (classType.equals("suv"))
                segmentedGroup.check(R.id.filter_class_suv_btn);

            segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Log.e("bla", checkedId + "");
                    switch (checkedId) {
                        case R.id.filter_class_any_btn:
                            classType = "any";
                            break;
                        case R.id.filter_class_mpv_btn:
                            classType = "mpv";
                            break;
                        case R.id.filter_class_suv_btn:
                            classType = "suv";
                            break;

                    }
                }
            });
            SegmentedGroup smokeGroup = (SegmentedGroup) dialog.findViewById(R.id.filter_smoke_group);
            Log.e("param4", smokeStatus + "");
            if (smokeStatus < 0)
                smokeGroup.check(R.id.filter_smoke_any_btn);
            if (smokeStatus == 0)
                smokeGroup.check(R.id.filter_smoke_no_btn);
            if (smokeStatus > 0)
                smokeGroup.check(R.id.filter_smoke_yes_btn);

            smokeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Log.e("bla", checkedId + "");
                    switch (checkedId) {
                        case R.id.filter_smoke_any_btn:
                            smokeStatus = -1;
                            break;
                        case R.id.filter_smoke_no_btn:
                            smokeStatus = 0;
                            break;
                        case R.id.filter_smoke_yes_btn:
                            smokeStatus = 1;
                            break;

                    }
                }
            });

            Button apply = (Button) dialog.findViewById(R.id.filter_apply_btn);
            apply.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    langSel = langSp.getSelectedItemPosition();
//                    getAvailableCar(mParam1, mParam2, mParam3, classType);
                    try {
                        filterList(langSel, classType, smokeStatus);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();

                }
            });

            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void filterList(int lang, String classType, int smokeStatus) throws SQLException {
        Log.d("filter", lang + "-" + classType + "-" + smokeStatus + " ");
        QueryBuilder<Car, Integer> builder = helper.getCarDao().queryBuilder();
        Where<Car, Integer> stringWhere = builder.where();
        boolean YNwhere = false;

        if (lang > 0) {
            stringWhere.like("driver_langStr", "%" + Utils.lang_arr[lang] + "%");
            YNwhere = true;
        }
        if (!classType.equals("any")) {
            if (YNwhere)
                stringWhere.and();
            else
                YNwhere = true;
            stringWhere.eq("car_class", classType);
        }
        if (smokeStatus > -1) {
            if (YNwhere)
                stringWhere.and();
            stringWhere.eq("driver_smoking", smokeStatus);
        }

        PreparedQuery<Car> preparedQuery = builder.prepare();
        Log.e("query", builder.prepareStatementString());
        cars.clear();
        cars = helper.getCarDao().query(preparedQuery);
        Log.d(TAG, "filterList: " + cars.size());
        if (cars.size() == 0)
            Toast.makeText(getActivity(), R.string.no_available_cars, Toast.LENGTH_LONG).show();
        mListView.setAdapter(null);
        mAdapter = new CarAdapter(getActivity(), cars);
        mListView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        helper = DatabaseHelper.getHelper(getActivity());
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        home_list_prog = (ProgressBar) view.findViewById(R.id.home_list_prog);

        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.menu_home_orange);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle("Available cars");

        MenuUtils.inflateMenu(view, getActivity());
        mAdapter = new CarAdapter(getActivity(), cars);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), CarDetailAc.class);

                intent.putExtra("Car", cars.get(position));
                Bundle b = new Bundle();
                b.putString("json",mParam1);
                intent.putExtras(b);
                startActivity(intent);

            }
        });
        df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            getAvailableCar(mParam1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }




    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    private void getAvailableCar(final String json) throws JSONException {
        if (!Utils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {
            home_list_prog.setVisibility(View.VISIBLE);
            if (cars.size() > 0) {
                cars.clear();
                mAdapter.notifyDataSetChanged();
            }

            Log.e("json", json);
//
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getActivity().getString(R.string.main_url) + "/mobile/getavailablecars", new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {
                    Log.e("register res", res.toString());
                    JSONObject response = null;
                    try {
                        response = new JSONObject(res);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    home_list_prog.setVisibility(View.GONE);
                    try {
                        switch (response.getInt("response")) {
                            case 200:
                                getCars(response.getJSONArray("data"));
                                break;
                            case 300:
                                Toast.makeText(getActivity(), R.string.no_available_cars, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    home_list_prog.setVisibility(View.GONE);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return json == null ? null : json.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                json, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = new String(response.data);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        }

    }

    private void getCars(JSONArray array) throws JSONException {
        if (array.length() == 0) {
            setEmptyText(getString(R.string.no_available_cars));
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
            cars.add(car);

        }
        mAdapter.notifyDataSetChanged();
        helper.insertListCar(cars);

    }
}
