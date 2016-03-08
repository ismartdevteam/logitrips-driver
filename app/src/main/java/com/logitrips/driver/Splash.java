package com.logitrips.driver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        try {
            PackageInfo info = getPackageManager()
                    .getPackageInfo("com.logitrips.userapp",
                            PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                SharedPreferences sp =getSharedPreferences("user",1);
                if(!sp.getBoolean("isLogin",false)){
                    startActivity(new Intent(Splash.this, LoginAc.class));
                }

                else{
                    startActivity(new Intent(Splash.this, MainActivity.class));
                }

            }
        }, 4000);

    }


}
