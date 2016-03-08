package com.logitrips.driver.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.logitrips.driver.MainActivity;
import com.logitrips.driver.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ulziiburen on 12/17/15.
 */
public class Services {
    public static void register(final Activity ac, final int type, final String name, final String email, final String password, final String phone, final String country, final String profile_pic_url) {

        CustomRequest loginReq = new CustomRequest(Request.Method.POST,
                ac.getString(R.string.main_url) + "/mobile/register", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("register res", response.toString());
                try {
                    if (response.getInt("response") == 200 || response.getInt("response") == 300) {
                        SharedPreferences user = ac.getSharedPreferences("user", 1);
                        SharedPreferences.Editor edit = user.edit();
                        edit.putBoolean("isLogin", true);
                        JSONObject userObj = response.getJSONObject("data");
                        edit.putInt("user_id", userObj.getInt("user_id"));
                        edit.putString("phone", userObj.getString("phone"));
                        edit.putString("name", userObj.getString("name"));
                        edit.putString("profile_pic_url", userObj.getString("profile_pic_url"));
                        edit.putString("auth_key", userObj.getString("auth_key"));
                        edit.putString("email", userObj.getString("email"));
                        edit.putString("country", userObj.getString("country"));
                        edit.commit();
                        ac.finish();
                        Intent intent = new Intent(ac, MainActivity.class);
                        ac.startActivity(intent);
                    } else {
                        Toast.makeText(ac.getApplicationContext(), R.string.error_login_social, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.i("error", error.getMessage() + "");

                Toast.makeText(ac.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("register_type", type + "");
                params.put("email", email + "");
                params.put("country", country + "");
                params.put("profile_pic_url", profile_pic_url + "");
                params.put("phone", phone + "");
                params.put("name", name + "");
                params.put("password", password + "");

                return params;
            }
        };

        MySingleton.getInstance(ac.getApplicationContext()).addToRequestQueue(loginReq);


    }

    public static void login(final Activity ac, final String email, final String password) {
        if (!Utils.isNetworkAvailable(ac.getApplicationContext())) {
            Toast.makeText(ac.getApplicationContext(), R.string.no_net, Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(ac,
                "", ac.getString(R.string.loading));
        CustomRequest loginReq = new CustomRequest(Request.Method.POST,
                ac.getString(R.string.main_url) + "/mobiledriver/login", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("register res", response.toString());
                try {
                    switch (response.getInt("response")) {
                        case 200:
                            SharedPreferences user = ac.getSharedPreferences("user", 1);
                            SharedPreferences.Editor edit = user.edit();
                            edit.putBoolean("isLogin", true);
                            JSONObject userObj = response.getJSONObject("data");
                            edit.putInt("user_id", userObj.getInt("driver_id"));
                            edit.putString("phone", userObj.getString("phone"));
                            edit.putString("name", userObj.getString("name"));
                            edit.putString("profile_pic_url", userObj.getString("profile_pic_url"));
                            edit.putString("auth_key", userObj.getString("auth_key"));
                            edit.putString("phone_emergency", userObj.getString("phone_emergency"));
                            edit.putString("address", userObj.getString("address"));
                            edit.putString("city", userObj.getString("city"));
                            edit.putString("registration_id", userObj.getString("registration_id"));
                            edit.commit();
                            ac.finish();
                            Intent intent = new Intent(ac, MainActivity.class);
                            ac.startActivity(intent);
                            break;
                        case 300:
                            Toast.makeText(ac.getApplicationContext(), R.string.user_not_found, Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Toast.makeText(ac.getApplicationContext(), R.string.password_mismatch, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ac.getApplicationContext(), R.string.error_login_social, Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.i("error", error.getMessage() + "");
                progressDialog.dismiss();
                Toast.makeText(ac.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email + "");
                params.put("password", password + "");
                return params;
            }
        };

        MySingleton.getInstance(ac.getApplicationContext()).addToRequestQueue(loginReq);


    }

    public static void createOffday(final AppCompatActivity ac, final String startDate, final String endDate, final String reason, final String driver_id) {
        if (!Utils.isNetworkAvailable(ac.getApplicationContext())) {
            Toast.makeText(ac.getApplicationContext(), R.string.no_net, Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(ac,
                "", ac.getString(R.string.loading));
        CustomRequest request = new CustomRequest(Request.Method.POST,
                ac.getString(R.string.main_url) + "/mobiledriver/createoffday", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("create off day res", response.toString());
                try {
                    switch (response.getInt("response")) {
                        case 200:
                            Toast.makeText(ac.getApplicationContext(), R.string.create_off_successfully, Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            Toast.makeText(ac.getApplicationContext(), R.string.error_request, Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.i("error", error.getMessage() + "");
                progressDialog.dismiss();
                Toast.makeText(ac.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("driver_id", driver_id + "");
                params.put("date_start", startDate + "");
                params.put("date_end", endDate + "");
                params.put("reason", reason + "");
                return params;
            }
        };

        MySingleton.getInstance(ac.getApplicationContext()).addToRequestQueue(request);


    }

    public static void signUp(final Activity ac, final String email, final String password, final String fname, final String lname, final String phone, final String country) {
        if (!Utils.isNetworkAvailable(ac.getApplicationContext())) {
            Toast.makeText(ac.getApplicationContext(), R.string.no_net, Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(ac,
                "", ac.getString(R.string.loading));
        CustomRequest loginReq = new CustomRequest(Request.Method.POST,
                ac.getString(R.string.main_url) + "/mobile/register", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("register res", response.toString());
                try {
                    switch (response.getInt("response")) {
                        case 200:
                            SharedPreferences user = ac.getSharedPreferences("user", 1);
                            SharedPreferences.Editor edit = user.edit();
                            edit.putBoolean("isLogin", true);
                            JSONObject userObj = response.getJSONObject("data");
                            edit.putInt("user_id", userObj.getInt("user_id"));
                            edit.putString("phone", userObj.getString("phone"));
                            edit.putString("name", userObj.getString("name"));
                            edit.putString("profile_pic_url", userObj.getString("profile_pic_url"));
                            edit.putString("auth_key", userObj.getString("auth_key"));
                            edit.putString("email", userObj.getString("email"));
                            edit.putString("country", userObj.getString("country"));
                            edit.commit();
                            ac.finish();
                            Intent intent = new Intent(ac, MainActivity.class);
                            ac.startActivity(intent);
                            break;
                        case 300:
                            Toast.makeText(ac.getApplicationContext(), R.string.user_exist, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ac.getApplicationContext(), R.string.error_login_social, Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.i("error", error.getMessage() + "");
                progressDialog.dismiss();
                Toast.makeText(ac.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("register_type", 1 + "");
                params.put("email", email + "");
                params.put("country", country + "");
                params.put("profile_pic_url", "");
                params.put("phone", phone + "");
                params.put("name", lname + " " + fname);
                params.put("password", password + "");
                return params;
            }
        };
        MySingleton.getInstance(ac.getApplicationContext()).addToRequestQueue(loginReq);
    }


}
