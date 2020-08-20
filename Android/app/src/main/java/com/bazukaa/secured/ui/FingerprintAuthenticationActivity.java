package com.bazukaa.secured.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bazukaa.secured.R;

import butterknife.ButterKnife;

public class FingerprintAuthenticationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_authentication);
        ButterKnife.bind(this);
    }
}
