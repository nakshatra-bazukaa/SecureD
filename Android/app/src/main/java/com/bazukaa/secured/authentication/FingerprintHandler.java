package com.bazukaa.secured.authentication;

import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.bazukaa.secured.R;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerprintHandler(Context context) {
        this.context = context;
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
    }

    private void update(String s, boolean b){
        TextView tvLabel = ((Activity)context).findViewById(R.id.act_finger_print_auth_tv_label);
        ImageView fingerprintImg = ((Activity)context).findViewById(R.id.act_finger_print_auth_img_finger);

        tvLabel.setText(s);

        if(b == false){
            tvLabel.setTextColor(ContextCompat.getColor(context, R.color.colorError));
        }else{
            tvLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccepted));
            fingerprintImg.setImageResource(R.mipmap.done);
        }

    }
}
