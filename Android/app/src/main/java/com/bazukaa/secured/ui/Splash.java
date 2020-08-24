package com.bazukaa.secured.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bazukaa.secured.R;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            Intent splashIntent = new Intent(Splash.this, PasswordActivity.class);
            startActivity(splashIntent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}
