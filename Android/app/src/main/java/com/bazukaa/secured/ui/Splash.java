package com.bazukaa.secured.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.bazukaa.secured.R;

public class Splash extends AppCompatActivity {

    // To set app mode
    public static final boolean DARK_MODE = true;
    public static final boolean LIGHT_MODE = false;

    // Shared preferences
    public static final String SHARED_PREFERENCE = "sharedPrefs";
    public static final String APP_MODE = "app mode dark/light";

    // Variables for Shared Preferences
    private Boolean appMode;

    private static int SPLASH_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        setAppMode();
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            Intent splashIntent = new Intent(Splash.this, PasswordActivity.class);
            startActivity(splashIntent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        }, SPLASH_TIME_OUT);
    }

    // To load data from shared preferences
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        appMode = sharedPreferences.getBoolean(APP_MODE, false);
    }

    // To set the app mode
    public void setAppMode(){
        if(appMode == DARK_MODE){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
