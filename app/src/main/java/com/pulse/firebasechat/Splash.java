package com.pulse.firebasechat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Splash extends AppCompatActivity {
    private ImageView imageView;
    private SharedPreferences userPreferences;
    private Boolean isLoggedOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.img_splash);
        userPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isLoggedOn = userPreferences.getBoolean("isLoggedOn",false);
        Glide.with(this)
                .asGif()
                .load(R.drawable.splash)
                .into(imageView);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(isLoggedOn) {
                    Intent homeSplash = new Intent(Splash.this, MainActivity.class);
                    startActivity(homeSplash);
                    Splash.this.finish();
                } else {
                    Intent homeSplash = new Intent(Splash.this, LoginActivity.class);
                    startActivity(homeSplash);
                    Splash.this.finish();
                }

            }
        }, 2000);
    }
}
