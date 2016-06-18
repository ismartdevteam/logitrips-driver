package com.logitrips.userapp.menu;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.logitrips.userapp.R;
import com.logitrips.userapp.adapter.MessagesRecyclerViewAdapter;
import com.logitrips.userapp.detail.MessageDetailAc;
import com.logitrips.userapp.model.LogiMessage;
import com.logitrips.userapp.util.CustomRequest;
import com.logitrips.userapp.util.MySingleton;
import com.logitrips.userapp.util.RecyclerItemClickListener;
import com.logitrips.userapp.util.Utils;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageFragment extends Fragment {

    private static final String ARG_PARAM1 = "user_id";

    private String mParam1;
    private static final String ARG_PARAM2 = "auth";


    private String mParam2;
    List<LogiMessage> logiMessages = new ArrayList<LogiMessage>();
    private MessagesRecyclerViewAdapter mAdapter;

    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        Log.d("Message",param1+"-"+param2);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ActionBar actionBar;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessageFragment() {
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
        View view = inflater.inflate(R.layout.fragment_message, container, false);

//        SinchClient sinchClient = Sinch.getSinchClientBuilder().context(getActivity())
//                .applicationKey("479bc85c-a6f1-4c5d-bf80-547d1850d24e")
//                .applicationSecret("DY18bx2rDEGQOMKUxzj4MQ==")
//                .environmentHost("sandbox.sinch.com")
//                .userId(ARG_PARAM1)
//                .build();


        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.menu_message_orange);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle(R.string.messages);

        MenuUtils.inflateMenu(view, getActivity());


        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.messenger_list);

        mAdapter = new MessagesRecyclerViewAdapter(logiMessages, context);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), MessageDetailAc.class);
                        Bundle b = new Bundle();
                        LogiMessage message = logiMessages.get(position);
                        b.putString("user_id", mParam1);

                        b.putString("driver_id", message.getDriver_id() + "");
                        b.putString("driver_name", message.getDriver_name());
                        b.putString("auth", mParam2);
                        b.putString("profile_pic", message.getProfile_pic());
                        intent.putExtras(b);
                        startActivity(intent);

                    }
                })
        );
        getData(mParam1, mParam2);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void getData(String mParam1, final String mParam2) {
        if (!Utils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {
            CustomRequest loginReq = new CustomRequest(Request.Method.GET,
                    getString(R.string.main_url) + "/mobile/getchatlist?user_id=" + mParam1, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("fav data", response.toString());
                    try {
                        switch (response.getInt("response")) {
                            case 200:

                                getMessages(response.getJSONArray("data"));
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

    private void getMessages(JSONArray array) throws JSONException {

        if (array.length() == 0) {
            Toast.makeText(getActivity(), R.string.no_available_cars, Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < array.length(); i++) {
            LogiMessage message = new LogiMessage();
            JSONObject item = array.getJSONObject(i);
            message.setDriver_id(item.getInt("driver_id"));
            message.setDriver_email(item.getString("driver_email"));
            message.setDriver_name(item.getString("driver_name"));
            message.setProfile_pic(item.getString("profile_pic"));
            message.setLast_message(item.getString("last_message"));

            logiMessages.add(message);

        }

        mAdapter.notifyDataSetChanged();

    }

}
