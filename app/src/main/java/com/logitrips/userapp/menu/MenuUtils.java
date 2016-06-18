package com.logitrips.userapp.menu;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.logitrips.userapp.LoginAc;
import com.logitrips.userapp.R;

/**
 * Created by Ulziiburen on 12/21/15.
 */
public class MenuUtils {

    public static void inflateMenu(View view ,final Activity ac){
        final SharedPreferences sp = ac.getSharedPreferences("user",1);
       final FragmentTransaction ft=ac.getFragmentManager().beginTransaction();
        ImageView home_btn=(ImageView)view.findViewById(R.id.menu_home_btn);
        final Bundle b =ac.getIntent().getExtras();
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = HomeFragment.newInstance(b.getString("json"));
                ft.replace(R.id.frame, homeFragment);
                ft.addToBackStack("home");
                ft.commit();
            }
        });

        ImageView my_trip_btn=(ImageView)view.findViewById(R.id.menu_trip_btn);

        my_trip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sp.getBoolean("isLogin",false)){
                    Intent intent=new Intent(ac,LoginAc.class);
                    intent.putExtras(b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ac.startActivity(intent);
                }

                else{
                    MyTripFragment accfrag = MyTripFragment.newInstance(sp.getInt("user_id",0)+"",sp.getString("auth_key", ""));

                    ft.replace(R.id.frame, accfrag);
                    ft.addToBackStack("mytrip");
                    ft.commit();
                }

            }
        });
        ImageView fav_btn=(ImageView)view.findViewById(R.id.menu_fav_btn);

        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sp.getBoolean("isLogin",false)){
                    Intent intent=new Intent(ac,LoginAc.class);
                    intent.putExtras(b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ac.startActivity(intent);
                }

                else{
                    FavFragment favFrag = FavFragment.newInstance(sp.getInt("user_id",0)+"",sp.getString("auth_key",""));
                    ft.replace(R.id.frame, favFrag);
                    ft.addToBackStack("favmenu");
                    ft.commit();
                }
            }
        });

        ImageView message_btn=(ImageView)view.findViewById(R.id.menu_message_btn);

        message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sp.getBoolean("isLogin",false)){
                    Intent intent=new Intent(ac,LoginAc.class);

                    intent.putExtras(b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ac.startActivity(intent);
                }

                else{
                    Log.e("user id and auth",sp.getInt("user_id",0)+"-"+sp.getString("auth_key",""));
                    MessageFragment messageFragment = MessageFragment.newInstance(sp.getInt("user_id",0)+"",sp.getString("auth_key",""));
                    ft.replace(R.id.frame, messageFragment);
                    ft.addToBackStack("messageFragment");
                    ft.commit();
                }

            }
        });
        ImageView acc_btn=(ImageView)view.findViewById(R.id.menu_account_btn);

        acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!sp.getBoolean("isLogin",false)){
                    Intent intent=new Intent(ac,LoginAc.class);
                    intent.putExtras(b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ac.startActivity(intent);
                }

                else{
                    AccountFragment accfrag = AccountFragment.newInstance("1", "1");

                    ft.replace(R.id.frame, accfrag);
                    ft.addToBackStack("account");
                    ft.commit();
                }
            }
        });

    }
}
