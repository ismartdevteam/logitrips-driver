package com.logitrips.driver;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.logitrips.driver.detail.LegalFragment;
import com.logitrips.driver.menu.AccountFragment;
import com.logitrips.driver.menu.MessageFragment;
import com.logitrips.driver.menu.TripFragment;
import com.logitrips.driver.util.CircleImageView;
import com.logitrips.driver.util.MySingleton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences("user", 1);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initHeader(navigationView);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    private void initHeader(NavigationView v) {
        View headerVw = v.getHeaderView(0);
        CircleImageView image = (CircleImageView) headerVw.findViewById(R.id.nav_header_driver_img);
        TextView name = (TextView) headerVw.findViewById(R.id.nav_header_name);
        image.setImageUrl(sp.getString("profile_pic_url", ""), MySingleton.getInstance(this).getImageLoader());
        image.setDefaultImageResId(R.drawable.nodriver);
        name.setText(sp.getString("name", ""));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (id == R.id.nav_trips) {
            TripFragment tripFragment = TripFragment.newInstance(sp.getInt("user_id", 0) + "", sp.getString("auth_key", ""));
            transaction.replace(R.id.main_frame, tripFragment);
            transaction.commit();
        } else if (id == R.id.nav_messages) {
            MessageFragment messageFragment = MessageFragment.newInstance(sp.getInt("user_id", 0) + "", sp.getString("auth_key", ""));
            transaction.replace(R.id.main_frame, messageFragment);
            transaction.commit();
        } else if (id == R.id.nav_account) {
            AccountFragment accountFragment = AccountFragment.newInstance(sp.getInt("user_id", 0) + "", sp.getString("auth_key", ""));
            transaction.replace(R.id.main_frame, accountFragment);
            transaction.commit();
        } else if (id == R.id.nav_legal) {
            LegalFragment legalFragment = LegalFragment.newInstance();
            transaction.replace(R.id.main_frame, legalFragment);
            transaction.commit();
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this);
            alertDialogBuilder.setTitle(R.string.log_out);
            alertDialogBuilder
                    .setMessage(R.string.really_want_logout)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sp.edit().clear().commit();
                            finish();
                            startActivity(new Intent(MainActivity.this, LoginAc.class));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
