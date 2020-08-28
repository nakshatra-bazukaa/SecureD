package com.bazukaa.secured.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bazukaa.secured.R;
import com.bazukaa.secured.ui.PasswordActivity;

public class AuthFingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    private View bottomSheetView;
    private TextView tvLabel;

    public AuthFingerprintHandler(Context context, View bottomSheetView) {
        this.context = context;
        this.bottomSheetView = bottomSheetView;
    }
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
        context.startActivity(new Intent(((Activity)context), PasswordActivity.class));
        ((Activity)context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        ((Activity)context).finish();
    }
    private void update(String s, boolean b){
        tvLabel = bottomSheetView.findViewById(R.id.bottom_sheet_tv_fingerprint_msg);
        tvLabel.setText(s);

        if(b == false){
            tvLabel.setTextColor(ContextCompat.getColor(context, R.color.colorError));
        }else{
            tvLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccepted));
        }
    }
}
