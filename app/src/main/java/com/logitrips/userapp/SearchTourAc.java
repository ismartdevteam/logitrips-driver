package com.logitrips.userapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.logitrips.userapp.detail.BookingDetail;
import com.logitrips.userapp.menu.MenuUtils;
import com.logitrips.userapp.util.Utils;

import java.util.Calendar;


public class SearchTourAc extends Fragment implements View.OnClickListener {
    private Spinner destination_sp;
    private Button find_btn;
    private ActionBar actionBar;

    public static SearchTourAc newInstance() {
        SearchTourAc fragment = new SearchTourAc();

        return fragment;
    }

    public SearchTourAc() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search_tour, container, false);

        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.menu_home_orange);
        actionBar.setDisplayShowTitleEnabled(true);

        MenuUtils.inflateMenu(view, getActivity());
        initView(view);
        return view;
    }

    private void initView(View v) {

        find_btn = (Button) v.findViewById(R.id.search_find_btn);
        find_btn.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.YEAR) + "-"
                + (c.get(Calendar.MONTH) + 1) + "-" +
                c.get(Calendar.DAY_OF_MONTH);
        destination_sp = (Spinner) v.findViewById(R.id.search_destination_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_white, Utils.dest_str); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        destination_sp.setAdapter(spinnerArrayAdapter);
        destination_sp.setSelection(0);

    }

    @Override
    public void onClick(View v) {
        if (v == find_btn) {
            Bundle b = new Bundle();
            b.putInt("dest", destination_sp.getSelectedItemPosition());
            Intent intent = new Intent(getActivity(), BookingDetail.class);
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}
