package com.logitrips.driver.detail;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.logitrips.driver.R;
import com.logitrips.driver.util.CustomRequest;
import com.logitrips.driver.util.MySingleton;
import com.logitrips.driver.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LegalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView legal_text;

    public LegalFragment() {
        // Required empty public constructor
    }


    public static LegalFragment newInstance() {
        LegalFragment fragment = new LegalFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_legal, container, false);
        legal_text = (TextView) v.findViewById(R.id.legal_text);
        getData();
        return v;
    }

    private void getData() {
        if (!Utils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {
            CustomRequest loginReq = new CustomRequest(Request.Method.GET,
                    getString(R.string.main_url) + "/mobiledriver/getsettings", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("legal response", response.toString());
                    try {
                        legal_text.setText(response.getString("legal"));


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

            });
            MySingleton.getInstance(getActivity()).addToRequestQueue(loginReq);
        }

    }


}
