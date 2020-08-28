package com.bazukaa.secured.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bazukaa.secured.R;
import com.bazukaa.secured.authentication.AuthFingerprintHandler;
import com.bazukaa.secured.authentication.SetupFingerprintHandler;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class Splash extends AppCompatActivity {

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private static final String KEY_NAME = "ANDROID KEY";

    // To set app mode
    public static final boolean DARK_MODE = true;
    public static final boolean LIGHT_MODE = false;

    // Shared preferences
    public static final String SHARED_PREFERENCE = "sharedPrefs";
    public static final String APP_MODE = "app mode dark/light";
    public static final String FINGERPRINT_AUTH = "fingerprint auth";

    // Variables for Shared Preferences
    private Boolean appMode;
    private boolean authEnabled;

    private static int SPLASH_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        setAppMode();
        setContentView(R.layout.activity_splash);

        if(authEnabled){
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Splash.this, R.style.BottomSheetDialogTheme);
            View bottomSheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, (LinearLayout)findViewById(R.id.bottomSheetContainer));
            bottomSheetDialog.setContentView(bottomSheetView);
            TextView tvLabel = bottomSheetView.findViewById(R.id.bottom_sheet_tv_fingerprint_msg);
            bottomSheetDialog.show();
            try {
                Field behaviourField = bottomSheetDialog.getClass().getDeclaredField("behavior");
                behaviourField.setAccessible(true);
                final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviourField.get(bottomSheetDialog);
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if(newState == BottomSheetBehavior.STATE_DRAGGING){
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }
                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) { }
                });
            } catch (NoSuchFieldException |IllegalAccessException e) { e.printStackTrace(); }
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
                    generateKey();
                    if(cipherInit()){
                        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        AuthFingerprintHandler fingerprintHandler = new AuthFingerprintHandler(this, bottomSheetView);
                        fingerprintHandler.startAuth(fingerprintManager, cryptoObject);
                    }
                }
            }else{
                Toast.makeText(this, "Your android version is less than Android M", Toast.LENGTH_LONG).show();
            }
        }else{
            new Handler().postDelayed(() -> {
                Intent splashIntent = new Intent(Splash.this, PasswordActivity.class);
                startActivity(splashIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();
            }, SPLASH_TIME_OUT);
        }
    }

    // To load data from shared preferences
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        appMode = sharedPreferences.getBoolean(APP_MODE, false);
        authEnabled = sharedPreferences.getBoolean(FINGERPRINT_AUTH, false);
    }

    // To set the app mode
    public void setAppMode(){
        if(appMode == DARK_MODE){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e) { e.printStackTrace(); }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) { throw new RuntimeException("Failed to get Cipher", e); }

        try {
            keyStore.load(null);
            cipher.init(Cipher.ENCRYPT_MODE, keyStore.getKey(KEY_NAME, null));
            return true;
        } catch (KeyPermanentlyInvalidatedException e) { return false; }
        catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) { throw new RuntimeException("Failed to init Cipher", e); }
    }
}
