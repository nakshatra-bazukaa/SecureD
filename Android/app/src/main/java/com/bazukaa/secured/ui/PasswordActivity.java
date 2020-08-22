package com.bazukaa.secured.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bazukaa.secured.R;
import com.bazukaa.secured.adapters.PasswordAdapter;
import com.bazukaa.secured.models.PasswordDetails;
import com.bazukaa.secured.viewmodel.PasswordDetailsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PasswordActivity extends AppCompatActivity {

    public static final int ADD_PASSWORD_REQUEST = 1;
    public static final int PICK_IMAGE_REQUEST = 100;

    private PasswordDetailsViewModel passwordDetailsViewModel;

    private Uri imgFilePath;
    private Bitmap imgToStore;

    private Toolbar toolbar;
    @BindView(R.id.act_pwd_rv)
    RecyclerView passwordDetailsRecyclerView;
    @BindView(R.id.act_pwd_fab_add)
    FloatingActionButton addButton;

    CircleImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);

        // Setting up toolbar
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Profile dialog
        CircleImageView civ = findViewById(R.id.profile_image);
        civ.setOnClickListener(v -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(PasswordActivity.this, R.style.AlertDialogTheme);
            View view = LayoutInflater.from(PasswordActivity.this).inflate(R.layout.profile_dialog, findViewById(R.id.profile_dialog_container));
            Button okBtn = view.findViewById(R.id.dialog_profile_btn_ok);
            Button cancelBtn = view.findViewById(R.id.dialog_profile_btn_cancel);
            LinearLayout tapToEdit = view.findViewById(R.id.dialog_profile_ll_tap_to_edit);
            alert.setView(view);
            final AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(false);

            tapToEdit.setOnClickListener(v13 -> {
                Intent imgIntent = new Intent();
                imgIntent.setType("image/*");

                imgIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imgIntent, PICK_IMAGE_REQUEST);
                profileImg = view.findViewById(R.id.dialog_profile_pic_civ_profile);
            });

            okBtn.setOnClickListener(v1 -> alertDialog.dismiss());

            cancelBtn.setOnClickListener(v12 -> alertDialog.dismiss());

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
            public void onPwdTvClicked(int position) {
                PasswordDetails passwordDetails = adapter.getPasswordDetailsFromPosition(position);
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Password copied to clipboard", passwordDetails.getPassword());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(PasswordActivity.this, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // To create a new password
    @OnClick(R.id.act_pwd_fab_add)
    public void onFabClicked(){
        Intent intent = new Intent(PasswordActivity.this, MakePasswordActivity.class);
        startActivityForResult(intent, ADD_PASSWORD_REQUEST);
    }

    // Function to setup and open settings dialog
    public void openSettings(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(PasswordActivity.this);
        View view = getLayoutInflater().inflate(R.layout.setting_dialog, null);
        Button dismissBtn = view.findViewById(R.id.dialog_setting_btn_dismiss);
        Button dltAllPwdBtn = view.findViewById(R.id.dialog_setting_btn_delete_all);
        Button addFingerUnlockBtn = view.findViewById(R.id.dialog_setting_btn_add_finger_unlock);
        Button seeSrcCodeBtn = view.findViewById(R.id.dialog_setting_btn_see_source_code);
        Button giveFeedbackBtn = view.findViewById(R.id.dialog_setting_btn_feedback);
        alert.setView(view);
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
                alertDialog1.dismiss();
            });

            cancelButton.setOnClickListener(v1 -> alertDialog1.dismiss());

            alertDialog1.show();
        });
        // Add fingerprint auth
        addFingerUnlockBtn.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), FingerprintAuthenticationActivity.class));
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgFilePath = data.getData();

            try {
                imgToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imgFilePath);
                profileImg.setImageBitmap(imgToStore);
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }
    }
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
