package com.bazukaa.secured.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.bazukaa.secured.R;
import com.bazukaa.secured.ui.Splash;

public class SetupFingerprintHandler extends FingerprintManager.AuthenticationCallback {

    // Shared preferences
    public static final String SHARED_PREFERENCE = "sharedPrefs";
    public static final String FINGERPRINT_AUTH = "fingerprint auth";

    // Variables for Shared Preferences
    private boolean authEnabled;

    // Variables for different views
    private TextView tvLabel;
    private ImageView fingerprintImg;
    private Button addFingerprintAuth;

    private Context context;

    public SetupFingerprintHandler(Context context) { this.context = context; }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("There was an Auth Error" + errString, false);
    }
    @Override
    public void onAuthenticationFailed() {
        this.update("Auth Failed", false);
    }
    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error" + helpString, false);
    }
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("You can now access the app", true);
        addFingerprintAuth = ((Activity)context).findViewById(R.id.act_finger_print_auth_btn_add_fingerprint);
        loadData();
        if(authEnabled)
            addFingerprintAuth.setText("Remove Fingerprint Auth");
        addFingerprintAuth.setVisibility(View.VISIBLE);
        addFingerprintAuth.setOnClickListener(v -> {
            if(authEnabled)
                saveFingerprintAuthData(false);
            else
                saveFingerprintAuthData(true);
            context.startActivity(new Intent(((Activity)context), Splash.class));
        });
    }

    private void update(String s, boolean b){
        tvLabel = ((Activity)context).findViewById(R.id.act_finger_print_auth_tv_label);
        fingerprintImg = ((Activity)context).findViewById(R.id.act_finger_print_auth_img_finger);

        tvLabel.setText(s);

        if(b == false){
            tvLabel.setTextColor(ContextCompat.getColor(context, R.color.colorError));
        }else{
            tvLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccepted));
            fingerprintImg.setImageResource(R.mipmap.done);
        }
    }
    // To save auth to shared preferences
    public void saveFingerprintAuthData(boolean newAuthState){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FINGERPRINT_AUTH, newAuthState);
        editor.apply();
    }
    // To load data from shared preferences
    public void loadData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        authEnabled = sharedPreferences.getBoolean(FINGERPRINT_AUTH, false);
    }
}
