package com.logitrips.userapp;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.logitrips.userapp.menu.AccountFragment;
import com.logitrips.userapp.menu.HomeFragment;
import com.logitrips.userapp.util.DateDialog;
import com.logitrips.userapp.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import info.hoang8f.android.segmented.SegmentedGroup;


public class MenuAc extends ActionBarActivity {
    private Bundle b;
    private ActionBar actionBar;
    private String jsonObject;


    public static FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ft = getFragmentManager().beginTransaction();
        b = getIntent().getExtras();

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        if (b != null) {
            jsonObject = b.getString("json");
            HomeFragment homeFragment = HomeFragment.newInstance(jsonObject);
            ft.add(R.id.frame, homeFragment);
            ft.commit();
        } else {
            SearchTourAc searchFrag = SearchTourAc.newInstance();
            ft.add(R.id.frame, searchFrag);
            ft.commit();
        }
    }

}
