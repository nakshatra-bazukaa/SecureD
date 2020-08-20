package com.bazukaa.secured.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bazukaa.secured.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FingerprintAuthenticationActivity extends AppCompatActivity {

    @BindView(R.id.act_finger_print_auth_tv_title)
    TextView tvTitle;
    @BindView(R.id.act_finger_print_auth_img_finger)
    ImageView fingerprintImg;
    @BindView(R.id.act_finger_print_auth_tv_label)
    TextView tvLabel;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_authentication);
        ButterKnife.bind(this);

        checkRequirements();

    }
    // To check whether the app is ready to use fingerprint auth
    private void checkRequirements(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            if(!fingerprintManager.isHardwareDetected()){
                tvLabel.setText("Fingerprint scanner not detected in the Device");
            }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
                tvLabel.setText("Permission not granted to use Fingerprint Scanner");
            }else if(!keyguardManager.isKeyguardSecure()){
                tvLabel.setText("Add Lock to your Phone in Settings");
            }else if(!fingerprintManager.hasEnrolledFingerprints()){
                tvLabel.setText("You should add at least one Fingerprint to use this feature");
            }else{
                tvLabel.setText("Place your Finger on Scanner to Access the App");
            }
        }
    }
}
