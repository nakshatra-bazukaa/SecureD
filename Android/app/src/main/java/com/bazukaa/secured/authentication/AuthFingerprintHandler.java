package com.bazukaa.secured.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import com.bazukaa.secured.ui.PasswordActivity;

public class AuthFingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;

    public AuthFingerprintHandler(Context context) {
        this.context = context;
    }
    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        context.startActivity(new Intent(((Activity)context), PasswordActivity.class));
    }
}
