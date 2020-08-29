package com.bazukaa.secured.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bazukaa.secured.R;
import com.bazukaa.secured.adapters.PasswordAdapter;
import com.bazukaa.secured.models.PasswordDetails;
import com.bazukaa.secured.viewmodel.PasswordDetailsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PasswordActivity extends AppCompatActivity {

    // Intent results constants
    public static final int ADD_PASSWORD_REQUEST = 1;
    public static final int PICK_IMAGE_REQUEST = 100;

    // To rotate the image from gallery
    public static final float leftDegrees = -90;
    public static final float rightDegrees = 90;

    // To set app mode
    public static final boolean DARK_MODE = true;
    public static final boolean LIGHT_MODE = false;

    // Shared preferences
    public static final String SHARED_PREFERENCE = "sharedPrefs";
    public static final String PATH = "path";
    public static final String APP_MODE = "app mode dark/light";
    public static final String AVATAR_NAME = "avatar name";

    // Viewmodel
    private PasswordDetailsViewModel passwordDetailsViewModel;

    // Variables for profile image
    private Uri imgFilePath;
    private Bitmap imgToStore = null;
    private CircleImageView dialogProfileImg;

    // Variables for Shared Preferences
    private String path;
    private Boolean appMode;
    private String avatarName;

    // Variables for different views
    private Toolbar toolbar;
    private LinearLayout rotateBtnLl;

    @BindView(R.id.act_pwd_rv)
    RecyclerView passwordDetailsRecyclerView;
    @BindView(R.id.act_pwd_fab_add)
    FloatingActionButton addButton;
    @BindView(R.id.act_pwd_toolbar_civ_profile_image)
    CircleImageView toolbarProfileImg;
    @BindView(R.id.act_pwd_toolbar_tv_profile_name)
    TextView toolbarProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);

        loadData();
        setAvatarName();
        loadFromStorageForToolbarCiv(path);


        // Setting up toolbar
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Profile image dialog
        toolbarProfileImg.setOnClickListener(v -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(PasswordActivity.this);
            View view = getLayoutInflater().inflate(R.layout.profile_dialog, null);
            alert.setView(view);

            Button okBtn = view.findViewById(R.id.dialog_profile_btn_ok);
            Button cancelBtn = view.findViewById(R.id.dialog_profile_btn_cancel);
            Button rotateLeft = view.findViewById(R.id.dialog_profile_btn_rotate_left);
            Button rotateRight = view.findViewById(R.id.dialog_profile_btn_rotate_right);
            rotateBtnLl = view.findViewById(R.id.dialog_profile_ll_btn_rotate);
            LinearLayout tapToEdit = view.findViewById(R.id.dialog_profile_ll_tap_to_edit);
            dialogProfileImg = view.findViewById(R.id.dialog_profile_pic_civ_profile);
            try {
                dialogProfileImg.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(new File(path, "profile.jpg"))));
            } catch (FileNotFoundException e) { e.printStackTrace(); }

            final AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(false);

            // TO pick an image from the gallery
            tapToEdit.setOnClickListener(v13 -> {
                Intent imgIntent = new Intent();
                imgIntent.setType("image/*");
                imgIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imgIntent, PICK_IMAGE_REQUEST);
            });
            // To rotate the present bitmap in left
            rotateLeft.setOnClickListener(v14 -> {
                Matrix matrix = new Matrix();
                matrix.setRotate(leftDegrees);
                imgToStore = Bitmap.createBitmap(imgToStore, 0, 0, imgToStore.getWidth(), imgToStore.getHeight(), matrix, true);
                dialogProfileImg.setImageBitmap(imgToStore);
            });
            // To rotate the present bitmap in right
            rotateRight.setOnClickListener(v15 -> {
                Matrix matrix = new Matrix();
                matrix.setRotate(rightDegrees);
                imgToStore = Bitmap.createBitmap(imgToStore, 0, 0, imgToStore.getWidth(), imgToStore.getHeight(), matrix, true);
                dialogProfileImg.setImageBitmap(imgToStore);
            });
            // To save the picked image
            okBtn.setOnClickListener(v1 -> {
                if(rotateBtnLl.getVisibility() == View.VISIBLE){
                    toolbarProfileImg.setImageBitmap(imgToStore);
                    Toast.makeText(this, "Pic saved", Toast.LENGTH_SHORT).show();
                    new ProfileDialogOkClickedAsyncTask().execute(imgToStore);
                }
                alertDialog.dismiss();
            });
            // To cancel the process
            cancelBtn.setOnClickListener(v12 -> alertDialog.dismiss());
            alertDialog.show();
        });
        // Profile name dialog
        toolbarProfileName.setOnClickListener(v -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(PasswordActivity.this);
            View view = getLayoutInflater().inflate(R.layout.name_dialog, null);
            alert.setView(view);

            EditText enterName = view.findViewById(R.id.name_dialog_et_avatar_name);
            Button saveBtn = view.findViewById(R.id.name_dialog__btn_save);
            Button cancelBtn = view.findViewById(R.id.name_dialog__btn_cancel);

            final AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(false);

            // Save btn clicked
            saveBtn.setOnClickListener(v16 -> {
                String name = enterName.getText().toString();
                toolbarProfileName.setText("Hello " + name);
                saveAvatarName(name);
                alertDialog.dismiss();
            });
            // Cancel btn clicked
            cancelBtn.setOnClickListener(v17 -> alertDialog.dismiss());

            alertDialog.show();
        });
        // Setting up recyclerview
        final PasswordAdapter adapter = new PasswordAdapter();
        passwordDetailsRecyclerView.setAdapter(adapter);
        passwordDetailsViewModel = ViewModelProviders.of(this).get(PasswordDetailsViewModel.class);
        passwordDetailsViewModel.getPasswordDetailsList().observe(this, new Observer<List<PasswordDetails>>() {
            @Override
            public void onChanged(List<PasswordDetails> passwordDetails) {
                adapter.setPasswords(passwordDetails);
            }
        });
        // To handle clicks on recycler view
        adapter.setOnItemClickListener(new PasswordAdapter.OnItemClickListener() {
            // To delete a password
            @Override
            public void onDeleteButtonClick(int position) {
                PasswordDetails passwordDetails = adapter.getPasswordDetailsFromPosition(position);

                final AlertDialog.Builder alert = new AlertDialog.Builder(PasswordActivity.this);
                View view = getLayoutInflater().inflate(R.layout.confirm_delete_dialog, null);
                Button deleteButton = view.findViewById(R.id.confirm_delete_dialog_btn_delete);
                Button cancelButton = view.findViewById(R.id.confirm_delete_dialog_btn_cancel);
                alert.setView(view);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                deleteButton.setOnClickListener(v -> {
                    passwordDetailsViewModel.delete(passwordDetails);
                    passwordDetailsRecyclerView.removeViewAt(position);
                    adapter.notifyItemRemoved(position);
                    alertDialog.dismiss();
                });

                cancelButton.setOnClickListener(v -> alertDialog.dismiss());

                alertDialog.show();
            }
            // To copy pwd to clip board
            @Override
            public void onPwdTvClick(int position) {
                PasswordDetails passwordDetails = adapter.getPasswordDetailsFromPosition(position);
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Password copied to clipboard", passwordDetails.getPassword());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(PasswordActivity.this, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
            }
            // Long press to reveal password
            @Override
            public void onPwdTvLongClick(int position) {
                PasswordDetails passwordDetails = adapter.getPasswordDetailsFromPosition(position);
                TextView pwdTv = findViewById(R.id.card_pwd_ev_tv_pwd);
                pwdTv.setText(passwordDetails.getPassword());
            }
        });
    }
    // To create a new password
    @OnClick(R.id.act_pwd_fab_add)
    public void onFabClicked(){
        Intent intent = new Intent(PasswordActivity.this, MakePasswordActivity.class);
        startActivityForResult(intent, ADD_PASSWORD_REQUEST);
        overridePendingTransition(R.anim.overlay_in, R.anim.still);
    }
    // Function to setup and open settings dialog
    public void openSettings(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(PasswordActivity.this);
        View view = getLayoutInflater().inflate(R.layout.setting_dialog, null);
        Button dismissBtn = view.findViewById(R.id.dialog_setting_btn_dismiss);
        TextView switchModeTv = view.findViewById(R.id.dialog_setting_tv_switch);
        Button dltAllPwdBtn = view.findViewById(R.id.dialog_setting_btn_delete_all);
        Button addFingerUnlockBtn = view.findViewById(R.id.dialog_setting_btn_add_finger_unlock);
        Button seeSrcCodeBtn = view.findViewById(R.id.dialog_setting_btn_see_source_code);
        Button giveFeedbackBtn = view.findViewById(R.id.dialog_setting_btn_feedback);
        SwitchMaterial switchDarkMode = view.findViewById(R.id.dialog_setting_switch_mode);
        alert.setView(view);

        // Night/Light mode setup
        if(appMode == DARK_MODE){
            switchModeTv.setText("Switch to Light Mode");
            switchDarkMode.setChecked(true);
        }else{
            switchModeTv.setText("Switch to Night Mode");
            switchDarkMode.setChecked(false);
        }

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        // Dismiss button click
        dismissBtn.setOnClickListener(v -> alertDialog.dismiss());
        // Delete all passwords button click
        dltAllPwdBtn.setOnClickListener(v -> {
            final AlertDialog.Builder alert1 = new AlertDialog.Builder(PasswordActivity.this);
            View view1 = getLayoutInflater().inflate(R.layout.confirm_delete_all_dialog, null);
            Button deleteButton = view1.findViewById(R.id.confirm_delete_all_dialog_btn_delete);
            Button cancelButton = view1.findViewById(R.id.confirm_delete_all_dialog_btn_cancel);
            alert1.setView(view1);
            final AlertDialog alertDialog1 = alert1.create();
            alertDialog1.setCanceledOnTouchOutside(false);

            deleteButton.setOnClickListener(v12 -> {
                passwordDetailsViewModel.deleteAllPasswords();
                Toast.makeText(this, "All Passwords deleted successfully", Toast.LENGTH_SHORT).show();
                alertDialog1.dismiss();
            });

            cancelButton.setOnClickListener(v1 -> alertDialog1.dismiss());

            alertDialog1.show();
        });
        // Add fingerprint auth
        addFingerUnlockBtn.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), FingerprintAuthenticationSetupActivity.class));
        });
        // See source code
        seeSrcCodeBtn.setOnClickListener(v -> {
            Intent seeSrcCodeIntent = new Intent(Intent.ACTION_VIEW);
            seeSrcCodeIntent.setData(Uri.parse("https://github.com/nakshatra-bazukaa/news-app-2"));
            startActivity(seeSrcCodeIntent);
        });
        // Give feedback button click
        giveFeedbackBtn.setOnClickListener(v -> {
            Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
            sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"secured.bazukaa@gmail.com"});
            sendEmailIntent.setType("message/rfc883");
            startActivity(Intent.createChooser(sendEmailIntent, "Choose an email client"));
        });
        alertDialog.show();
        // To switch to dark mode
        switchDarkMode.setOnClickListener(v -> {
            if(switchDarkMode.isChecked() == true){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveAppModeData(DARK_MODE);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveAppModeData(LIGHT_MODE);
            }
        });
    }
    // To save profile img to internal storage
    private String saveProfileImgToStorage(Bitmap bitmapImg){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory,"profile.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImg.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    // To save profile picture to shared preferences
    public void saveProfileImgData(String path){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PATH, path);
        editor.apply();
    }
    // To save app mode to shared preferences
    public void saveAppModeData(boolean currentAppMode){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_MODE, currentAppMode);
        editor.apply();
    }
    // Save avatar name to shared preferences
    public void saveAvatarName(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AVATAR_NAME, name);
        editor.apply();
    }
    // To load data from shared preferences
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        path = sharedPreferences.getString(PATH, null);
        appMode = sharedPreferences.getBoolean(APP_MODE, false);
        avatarName = sharedPreferences.getString(AVATAR_NAME, "User");
    }
    // To load profile img to toolbar
    private void loadFromStorageForToolbarCiv(String path){
        try {
            toolbarProfileImg.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(new File(path, "profile.jpg"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    // Set avatar name in toolbar
    private void setAvatarName(){
        toolbarProfileName.setText("Hello " + avatarName);
    }
    // Intent results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // To add a new password
        if(requestCode == ADD_PASSWORD_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(MakePasswordActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(MakePasswordActivity.EXTRA_DESC);
            String pwd = data.getStringExtra(MakePasswordActivity.EXTRA_PWD);
            long timeStamp = data.getLongExtra(MakePasswordActivity.EXTRA_TIMESTAMP, 10000);

            PasswordDetails passwordDetails = new PasswordDetails(title, desc, pwd, timeStamp);
            passwordDetailsViewModel.insert(passwordDetails);

            Toast.makeText(this, "Password Generated Successfully", Toast.LENGTH_SHORT).show();
        }else if(requestCode == ADD_PASSWORD_REQUEST){
            Toast.makeText(this, "Password Not Generated", Toast.LENGTH_SHORT).show();
        }
        // To add a new profile image
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgFilePath = data.getData();
            try {
                imgToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imgFilePath);
                dialogProfileImg.setImageBitmap(imgToStore);
                rotateBtnLl.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == PICK_IMAGE_REQUEST){
            Toast.makeText(this, "Image not picked", Toast.LENGTH_SHORT).show();
        }
    }
    // Async task class to save the profile image
    private class ProfileDialogOkClickedAsyncTask extends AsyncTask<Bitmap, Void, Void>{
        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            if(bitmaps[0] != null){
                String newImgPath = saveProfileImgToStorage(bitmaps[0]);
                saveProfileImgData(newImgPath);
            }
            return null;
        }
    }
    // Menu setup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_password, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_act_pwd_item_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
